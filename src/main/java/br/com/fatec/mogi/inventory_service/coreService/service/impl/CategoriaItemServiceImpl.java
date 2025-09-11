package br.com.fatec.mogi.inventory_service.coreService.service.impl;

import br.com.fatec.mogi.inventory_service.authService.service.AutorizacaoService;
import br.com.fatec.mogi.inventory_service.authService.web.dto.request.AutorizarUsuarioRequestDTO;
import br.com.fatec.mogi.inventory_service.common.domain.enums.FuncionalidadesEnum;
import br.com.fatec.mogi.inventory_service.coreService.domain.exception.CategoriaJaCadastradaException;
import br.com.fatec.mogi.inventory_service.coreService.domain.model.CategoriaItem;
import br.com.fatec.mogi.inventory_service.coreService.repository.CategoriaItemRepository;
import br.com.fatec.mogi.inventory_service.coreService.service.CategoriaItemService;
import br.com.fatec.mogi.inventory_service.coreService.web.request.CadastrarCategoriaItemRequestDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.response.BuscarCategoriasResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoriaItemServiceImpl implements CategoriaItemService {

	private final CategoriaItemRepository categoriaItemRepository;

	private final AutorizacaoService autorizacaoService;

	@Override
	public BuscarCategoriasResponseDTO buscar(String accessToken) {
		AutorizarUsuarioRequestDTO autorizarUsuarioRequestDTO = AutorizarUsuarioRequestDTO.builder()
			.funcionalidade(FuncionalidadesEnum.LISTAR_CATEGORIA.toString())
			.build();
		autorizacaoService.autorizar(autorizarUsuarioRequestDTO, accessToken);
		return new BuscarCategoriasResponseDTO(categoriaItemRepository.findAll());
	}

	@Override
	public void cadastrar(CadastrarCategoriaItemRequestDTO dto, String accessToken) {
		AutorizarUsuarioRequestDTO autorizarUsuarioRequestDTO = AutorizarUsuarioRequestDTO.builder()
			.funcionalidade(FuncionalidadesEnum.CADASTRO_CATEGORIA.toString())
			.build();
		autorizacaoService.autorizar(autorizarUsuarioRequestDTO, accessToken);
		if (categoriaItemRepository.existsByNome(dto.getNome())) {
			throw new CategoriaJaCadastradaException(dto.getNome());
		}
		CategoriaItem categoriaSalvar = CategoriaItem.builder().nome(dto.getNome()).build();
		categoriaItemRepository.save(categoriaSalvar);
	}

}
