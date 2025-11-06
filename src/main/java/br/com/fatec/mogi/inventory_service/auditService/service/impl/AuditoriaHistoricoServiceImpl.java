package br.com.fatec.mogi.inventory_service.auditService.service.impl;

import br.com.fatec.mogi.inventory_service.auditService.domain.model.Auditoria;
import br.com.fatec.mogi.inventory_service.auditService.domain.model.AuditoriaHistorico;
import br.com.fatec.mogi.inventory_service.auditService.domain.model.ItemAuditado;
import br.com.fatec.mogi.inventory_service.auditService.domain.model.ItemAuditadoHistorico;
import br.com.fatec.mogi.inventory_service.auditService.repository.AuditoriaHistoricoRepository;
import br.com.fatec.mogi.inventory_service.auditService.repository.ItemAuditadoHistoricoRepository;
import br.com.fatec.mogi.inventory_service.auditService.service.AuditoriaHistoricoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditoriaHistoricoServiceImpl implements AuditoriaHistoricoService {

	private final AuditoriaHistoricoRepository auditoriaHistoricoRepository;

	private final ItemAuditadoHistoricoRepository itemAuditadoHistoricoRepository;

	@Override
	public String gerarCodigoAuditoria() {
		int anoAtual = Year.now().getValue();
		Long contador = auditoriaHistoricoRepository.countByAno(anoAtual);
		long proximoNumero = (contador != null ? contador : 0L) + 1;
		return String.format("AUD-%d-%04d", anoAtual, proximoNumero);
	}

	@Override
	@Transactional
	public AuditoriaHistorico arquivarAuditoria(Auditoria auditoria) {
		long totalItens = auditoria.getItensAuditados().size();
		AuditoriaHistorico historico = AuditoriaHistorico.builder()
			.codigoAuditoria(auditoria.getCodigoAuditoria())
			.dataInicio(auditoria.getDataInicio())
			.dataFim(auditoria.getDataFim())
			.usuarioResponsavelId(auditoria.getUsuarioResponsavel().getId())
			.usuarioResponsavelNome(auditoria.getUsuarioResponsavel().getNome())
			.totalItens(totalItens)
			.build();
		AuditoriaHistorico historicoSalvo = auditoriaHistoricoRepository.save(historico);
		List<ItemAuditadoHistorico> itensHistorico = new ArrayList<>();
		for (ItemAuditado itemAuditado : auditoria.getItensAuditados()) {
			var item = itemAuditado.getItem();
			ItemAuditadoHistorico itemHistorico = ItemAuditadoHistorico.builder()
				.auditoriaHistorico(historicoSalvo)
				.itemId(item.getId())
				.itemCodigo(item.getCodigoItem())
				.itemNome(item.getNomeItem())
				.itemCategoria(item.getCategoriaItem() != null ? item.getCategoriaItem().getNome() : null)
				.itemLocalizacao(item.getLocalizacao() != null ? item.getLocalizacao().getNomeSala() : null)
				.itemStatus(item.getStatusItem() != null ? item.getStatusItem().getNome() : null)
				.itemNumeroSerie(item.getNumeroSerie())
				.observacao(itemAuditado.getObservacao())
				.dataVerificacao(itemAuditado.getDataVerificacao())
				.conformidade(itemAuditado.getConformidade())
				.usuarioNome(itemAuditado.getUsuarioResponsavel().getNome())
				.build();
			itensHistorico.add(itemHistorico);
		}
		itemAuditadoHistoricoRepository.saveAll(itensHistorico);
		historicoSalvo.setItensAuditadosHistorico(itensHistorico);
		return historicoSalvo;
	}

}
