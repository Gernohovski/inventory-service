package br.com.fatec.mogi.inventory_service.coreService.strategy.exportarItem;

import br.com.fatec.mogi.inventory_service.coreService.domain.annotations.TipoExportacao;
import br.com.fatec.mogi.inventory_service.coreService.domain.exception.TipoExportacaoInvalidoException;
import jakarta.annotation.PostConstruct;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ExportarItemNavigation {

	private final ApplicationContext applicationContext;

	private final List<ExportarItemStrategy> strategies;

	public ExportarItemNavigation(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
		this.strategies = new ArrayList<>();
	}

	@PostConstruct
	public void init() {
		strategies.addAll(applicationContext.getBeansOfType(ExportarItemStrategy.class).values());
	}

	public ResponseEntity<byte[]> processarEstrategia(List<Long> itensId, String tipo) {
		var strategySelecionada = strategies.stream()
			.filter(strategy -> strategy.getClass().getAnnotation(TipoExportacao.class).value().equals(tipo))
			.findFirst();
		if (strategySelecionada.isEmpty()) {
			throw new TipoExportacaoInvalidoException();
		}
		return strategySelecionada.get().exportar(itensId);
	}

}
