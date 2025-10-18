package br.com.fatec.mogi.inventory_service.coreService.service.impl;

import br.com.fatec.mogi.inventory_service.common.web.response.CustomPageResponseDTO;
import br.com.fatec.mogi.inventory_service.coreService.domain.exception.LocalizacaoNaoEncontradaException;
import br.com.fatec.mogi.inventory_service.coreService.domain.exception.LocalizacaoNaoPodeSerExcluidaException;
import br.com.fatec.mogi.inventory_service.coreService.domain.mapper.LocalizacaoMapper;
import br.com.fatec.mogi.inventory_service.coreService.domain.model.Localizacao;
import br.com.fatec.mogi.inventory_service.coreService.repository.ItemRepository;
import br.com.fatec.mogi.inventory_service.coreService.repository.LocalizacaoRepository;
import br.com.fatec.mogi.inventory_service.coreService.service.LocalizacaoService;
import br.com.fatec.mogi.inventory_service.coreService.web.request.AtualizarLocalizacaoRequestDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.request.CadastrarLocalizacaoRequestDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.response.BuscarLocalizacaoResponseDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.response.LocalizacaoResponseDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocalizacaoServiceImpl implements LocalizacaoService {

	private final LocalizacaoRepository localizacaoRepository;

	private final ItemRepository itemRepository;

	private final LocalizacaoMapper localizacaoMapper;

	@Override
	public BuscarLocalizacaoResponseDTO buscar() {
		return new BuscarLocalizacaoResponseDTO(localizacaoRepository.findAll());
	}

	@Override
	public void cadastrar(CadastrarLocalizacaoRequestDTO dto) {
		Localizacao localizacao = Localizacao.builder().andar(dto.getAndar()).nomeSala(dto.getNomeSala()).build();
		localizacaoRepository.save(localizacao);
	}

	@Override
	public CustomPageResponseDTO<LocalizacaoResponseDTO> buscarPaginado(Pageable pageable) {
		var localizacoes = localizacaoRepository.findPaginado(pageable);
		return CustomPageResponseDTO.<LocalizacaoResponseDTO>builder()
			.page(localizacoes.getNumber())
			.size(localizacoes.getSize())
			.totalElements(localizacoes.getTotalElements())
			.totalPages(localizacoes.getTotalPages())
			.content(localizacoes.getContent())
			.build();
	}

	@Override
	@Transactional
	public void atualizar(AtualizarLocalizacaoRequestDTO dto, Long id) {
		var localizacao = localizacaoRepository.findById(id).orElseThrow(LocalizacaoNaoEncontradaException::new);
		localizacaoMapper.update(dto, localizacao);
		localizacaoRepository.save(localizacao);
	}

	@Override
	public void deletar(Long id) {
		localizacaoRepository.findById(id).orElseThrow(LocalizacaoNaoEncontradaException::new);
		var localizacaoVinculada = itemRepository.existsByLocalizacaoId(id);
		if (localizacaoVinculada) {
			throw new LocalizacaoNaoPodeSerExcluidaException();
		}
		localizacaoRepository.deleteById(id);
	}

}
