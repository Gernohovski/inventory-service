package br.com.fatec.mogi.inventory_service.coreService.strategy;

import br.com.fatec.mogi.inventory_service.coreService.domain.model.Item;
import br.com.fatec.mogi.inventory_service.coreService.web.response.UploadErrorResponseDTO;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Data
public class ValidarItemContexto {

	private boolean encerrarFluxo;

	private List<UploadErrorResponseDTO> erros;

	@Setter(AccessLevel.NONE)
	private List<String> strategiesExecutadas;

	private Item item;

	public ValidarItemContexto() {
		this.strategiesExecutadas = new ArrayList<>();
		this.erros = new ArrayList<>();
		this.item = new Item();
	}

	public ValidarItemContexto(boolean encerrarFluxo) {
		this.encerrarFluxo = encerrarFluxo;
		this.strategiesExecutadas = new ArrayList<>();
		this.erros = new ArrayList<>();
	}

	public void adicionarStrategieExecutada(ValidarItemStrategy validarItemStrategy) {
		this.strategiesExecutadas.add(validarItemStrategy.getClass().getName());
	}

	public void adicionarErro(String mensagem, String linha) {
		this.erros.add(UploadErrorResponseDTO.builder().linha(linha).mensagem(mensagem).build());
	}

}
