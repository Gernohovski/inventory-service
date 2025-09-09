package br.com.fatec.mogi.inventory_service.coreService.repository;

import br.com.fatec.mogi.inventory_service.coreService.domain.model.Localizacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocalizacaoRepository extends JpaRepository<Localizacao, Long> {

}
