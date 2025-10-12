package br.com.fatec.mogi.inventory_service.coreService.strategy.validarItem;

import br.com.fatec.mogi.inventory_service.coreService.web.request.ItemUploadRequestDTO;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Priority;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class ValidarItemNavigation {

	private final ApplicationContext applicationContext;

	private final List<ValidarItemStrategy> strategies;

	public ValidarItemNavigation(ApplicationContext applicationContext, List<ValidarItemStrategy> strategies) {
		this.applicationContext = applicationContext;
		this.strategies = new ArrayList<>();
	}

	@PostConstruct
	public void init() {
		applicationContext.getBeansOfType(ValidarItemStrategy.class)
			.values()
			.stream()
			.sorted(Comparator.comparingInt(strategie -> strategie.getClass().getAnnotation(Priority.class).value()))
			.forEach(strategies::add);
	}

	public ValidarItemContexto executar(ItemUploadRequestDTO dto, ValidarItemContexto contexto) {
		return processarEstrategias(dto, contexto);
	}

	private ValidarItemContexto processarEstrategias(ItemUploadRequestDTO dto, ValidarItemContexto contexto) {
		for (var strategy : strategies) {
			if (contexto.isEncerrarFluxo()) {
				return contexto;
			}
			processarEstrategia(strategy, dto, contexto);
		}
		return contexto;
	}

	public void processarEstrategia(ValidarItemStrategy validarItemStrategy, ItemUploadRequestDTO dto,
			ValidarItemContexto contexto) {
		validarItemStrategy.executar(dto, contexto);
	}

}
