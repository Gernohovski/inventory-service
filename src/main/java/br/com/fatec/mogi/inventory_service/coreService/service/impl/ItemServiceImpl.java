package br.com.fatec.mogi.inventory_service.coreService.service.impl;

import br.com.fatec.mogi.inventory_service.auditService.repository.AuditoriaRepository;
import br.com.fatec.mogi.inventory_service.auditService.repository.ItemAuditadoRepository;
import br.com.fatec.mogi.inventory_service.common.web.response.CustomPageResponseDTO;
import br.com.fatec.mogi.inventory_service.coreService.config.ItemUploadJobConfig;
import br.com.fatec.mogi.inventory_service.coreService.domain.exception.*;
import br.com.fatec.mogi.inventory_service.coreService.domain.mapper.ItemMapper;
import br.com.fatec.mogi.inventory_service.coreService.domain.model.Item;
import br.com.fatec.mogi.inventory_service.coreService.listener.ItemUploadSkipListener;
import br.com.fatec.mogi.inventory_service.coreService.reader.ItemUploadReader;
import br.com.fatec.mogi.inventory_service.coreService.repository.*;
import br.com.fatec.mogi.inventory_service.coreService.service.ItemService;
import br.com.fatec.mogi.inventory_service.coreService.strategy.exportarItem.ExportarItemNavigation;
import br.com.fatec.mogi.inventory_service.coreService.web.request.AtualizarItemRequestDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.request.CadastrarItemRequestDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.request.ConsultarItemRequestDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.request.ExportarItensRequestDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.response.ItemResponseDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.response.ItemUploadResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

	private final ItemRepository itemRepository;

	private final CategoriaItemRepository categoriaItemRepository;

	private final TipoEntradaRepository tipoEntradaRepository;

	private final StatusItemRepository statusItemRepository;

	private final LocalizacaoRepository localizacaoRepository;

	private final ItemMapper itemMapper;

	private final JobLauncher jobLauncher;

	private final ItemUploadJobConfig jobConfig;

	private final ExportarItemNavigation exportarItemNavigation;

	private final ItemUploadSkipListener skipListener;

	private final AuditoriaRepository auditoriaRepository;

	private final ItemAuditadoRepository itemAuditadoRepository;

	@Override
	public ItemResponseDTO cadastrarItem(CadastrarItemRequestDTO dto) {
		if (itemRepository.existsByCodigoItem(dto.getCodigoItem())) {
			throw new ItemJaCadastradoException();
		}
		if (auditoriaRepository.findAtiva().isPresent()) {
			throw new NaoPossivelCadastrarItemException();
		}
		categoriaItemRepository.findById(dto.getCategoriaItemId()).orElseThrow(CategoriaNaoEncontradaException::new);
		tipoEntradaRepository.findById(dto.getTipoEntradaId()).orElseThrow(TipoEntradaNaoEncontradaException::new);
		statusItemRepository.findById(dto.getStatusItemId()).orElseThrow(StatusItemNaoEncontradoException::new);
		localizacaoRepository.findById(dto.getLocalizacaoId()).orElseThrow(LocalizacaoNaoEncontradaException::new);
		Item item = itemMapper.from(dto);
		item.setDataAlteracao(LocalDateTime.now());
		item.setDataCadastro(LocalDateTime.now());
		return itemMapper.from(itemRepository.save(item));
	}

	@Override
	public CustomPageResponseDTO<ItemResponseDTO> filtrarItems(ConsultarItemRequestDTO dto, Pageable pageable) {
		var pagina = itemRepository.filtrar(dto.getDataCadastroInicio(), dto.getDataCadastroFim(),
				dto.getCategoriaItemId(), dto.getLocalizacaoId(), dto.getStatusItemId(), dto.getNomeItem(),
				dto.getCodigoItem(), dto.getNumeroSerie(), dto.getNotaFiscal(), dto.getTermoPesquisa(), pageable);

		var content = pagina.getContent().stream().map(itemMapper::from).toList();

		return CustomPageResponseDTO.<ItemResponseDTO>builder()
			.content(content)
			.size(pagina.getSize())
			.page(pagina.getNumber())
			.totalElements(pagina.getTotalElements())
			.totalPages(pagina.getTotalPages())
			.build();
	}

	@Override
	public ItemResponseDTO atualizar(AtualizarItemRequestDTO dto, Long id) {
		var item = itemRepository.findById(id).orElseThrow(ItemNaoEncontradoException::new);
		var codigoItemSalvo = item.getCodigoItem();
		if (dto.getCategoriaItemId() != null) {
			var categoria = categoriaItemRepository.findById(dto.getCategoriaItemId())
				.orElseThrow(CategoriaNaoEncontradaException::new);
			item.setCategoriaItem(categoria);
		}
		if (dto.getTipoEntradaId() != null) {
			var tipoEntrada = tipoEntradaRepository.findById(dto.getTipoEntradaId())
				.orElseThrow(TipoEntradaNaoEncontradaException::new);
			item.setTipoEntrada(tipoEntrada);
		}
		if (dto.getStatusItemId() != null) {
			var status = statusItemRepository.findById(dto.getStatusItemId())
				.orElseThrow(StatusItemNaoEncontradoException::new);
			item.setStatusItem(status);
		}
		if (dto.getLocalizacaoId() != null) {
			var localizacao = localizacaoRepository.findById(dto.getLocalizacaoId())
				.orElseThrow(LocalizacaoNaoEncontradaException::new);
			item.setLocalizacao(localizacao);
		}
		itemMapper.update(dto, item);
		if (!Objects.equals(item.getCodigoItem(), codigoItemSalvo)) {
			if (itemRepository.existsByCodigoItem(item.getCodigoItem())) {
				throw new ItemJaCadastradoException();
			}
		}
		item.setDataAlteracao(LocalDateTime.now());
		return itemMapper.from(itemRepository.save(item));
	}

	@Override
	public void deletar(Long id) {
		itemRepository.findById(id).orElseThrow(ItemNaoEncontradoException::new);
		if (itemAuditadoRepository.existsByItemId(id)) {
			throw new ItemSendoAuditadoException();
		}
		itemRepository.deleteById(id);
	}

	@Override
	public ItemUploadResponseDTO upload(MultipartFile file) throws Exception {
		String originalFilename = file.getOriginalFilename();
		if (originalFilename == null || !originalFilename.toLowerCase().endsWith(".csv")) {
			throw new ArquivoNaoSuportadoException();
		}

		File tempFile = null;
		try {
			tempFile = File.createTempFile("upload-", ".csv");
			try (FileOutputStream fos = new FileOutputStream(tempFile)) {
				fos.write(file.getBytes());
			}

			Resource resource = new FileSystemResource(tempFile);
			ItemUploadReader reader = new ItemUploadReader(resource);
			var step = jobConfig.itemUploadStep(reader);
			Job job = jobConfig.createJob(step);
			JobParameters params = new JobParametersBuilder().addString("fileName", originalFilename)
				.addLong("timestamp", System.currentTimeMillis())
				.addString("tempFilePath", tempFile.getAbsolutePath())
				.toJobParameters();

			skipListener.limparErros();
			JobExecution jobExecution = jobLauncher.run(job, params);

			ItemUploadResponseDTO itemUploadResponseDTO = ItemUploadResponseDTO.builder()
				.processadosComSucesso(
						jobExecution.getStepExecutions().stream().mapToLong(StepExecution::getWriteCount).sum())
				.processadosComErro(
						jobExecution.getStepExecutions().stream().mapToLong(StepExecution::getSkipCount).sum())
				.erros(skipListener.getErros())
				.build();

			return itemUploadResponseDTO;
		}
		finally {
			if (tempFile != null) {
				final File fileToDelete = tempFile;
				new Thread(() -> {
					try {
						Thread.sleep(60000);
						if (fileToDelete.exists()) {
							fileToDelete.delete();
						}
					}
					catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}).start();
			}
		}
	}

	@Override
	public ResponseEntity<byte[]> exportar(ExportarItensRequestDTO dto, String tipo) {
		return exportarItemNavigation.processarEstrategia(dto.getItensId(), tipo);
	}

	@Override
	public ResponseEntity<byte[]> exportarTodos(String tipo) {
		return exportarItemNavigation.processarEstrategia(List.of(), tipo);
	}

}
