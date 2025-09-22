package br.com.fatec.mogi.inventory_service.coreService.web;

import br.com.fatec.mogi.inventory_service.InventoryServiceApplication;
import br.com.fatec.mogi.inventory_service.authService.service.AutorizacaoService;
import br.com.fatec.mogi.inventory_service.coreService.repository.ItemRepository;
import br.com.fatec.mogi.inventory_service.coreService.web.request.AtualizarItemRequestDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.request.CadastrarItemRequestDTO;
import br.com.fatec.mogi.inventory_service.coreService.web.response.ItemResponseDTO;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = InventoryServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ItemControllerTest {

	@LocalServerPort
	private int porta;

	@Autowired
	private ItemRepository itemRepository;

	@MockitoBean
	private AutorizacaoService autorizacaoService;

	@Test
	@DisplayName("Deve cadastrar item com sucesso")
	void deveCadastrarItemComSucesso() {
		Mockito.doNothing().when(autorizacaoService).autorizar(ArgumentMatchers.any(), ArgumentMatchers.any());

		var dto = CadastrarItemRequestDTO.builder()
			.nomeItem("Mouse")
			.descricaoCurta("Periférico")
			.descricaoDetalhada("Mouse óptico")
			.numeroSerie("MS-001")
			.codigoItem("COD-CTRL-1")
			.notaFiscal("NF-010")
			.categoriaItemId(1L)
			.localizacaoId(1L)
			.statusItemId(1L)
			.tipoEntradaId(1L)
			.build();

		RestAssured.given()
			.port(porta)
			.contentType(ContentType.JSON)
			.header("X-ACCESS-TOKEN", "token")
			.body(dto)
			.when()
			.post("/core-service/v1/itens")
			.then()
			.statusCode(201);

		assertTrue(itemRepository.existsByCodigoItem("COD-CTRL-1"));
	}

	@Test
	@DisplayName("Deve retornar erro ao cadastrar item com código duplicado")
	void deveRetornarErroCodigoDuplicado() {
		Mockito.doNothing().when(autorizacaoService).autorizar(ArgumentMatchers.any(), ArgumentMatchers.any());

		var dto = CadastrarItemRequestDTO.builder()
			.nomeItem("Teclado")
			.descricaoCurta("Periférico")
			.descricaoDetalhada("Teclado mecânico")
			.numeroSerie("TC-001")
			.codigoItem("COD-DUP-CTRL")
			.notaFiscal("NF-011")
			.categoriaItemId(1L)
			.localizacaoId(1L)
			.statusItemId(1L)
			.tipoEntradaId(1L)
			.build();

		RestAssured.given()
			.port(porta)
			.contentType(ContentType.JSON)
			.header("X-ACCESS-TOKEN", "token")
			.body(dto)
			.when()
			.post("/core-service/v1/itens")
			.then()
			.statusCode(201);

		RestAssured.given()
			.port(porta)
			.contentType(ContentType.JSON)
			.header("X-ACCESS-TOKEN", "token")
			.body(dto)
			.when()
			.post("/core-service/v1/itens")
			.then()
			.statusCode(400);
	}

	@Test
	@DisplayName("Deve retornar erro ao cadastrar item com entidades inválidas")
	void deveRetornarErroEntidadesInvalidas() {
		Mockito.doNothing().when(autorizacaoService).autorizar(ArgumentMatchers.any(), ArgumentMatchers.any());

		var dto = CadastrarItemRequestDTO.builder()
			.nomeItem("Monitor")
			.descricaoCurta("Periférico")
			.descricaoDetalhada("Monitor 24 pol")
			.numeroSerie("MN-001")
			.codigoItem("COD-INV-1")
			.notaFiscal("NF-012")
			.categoriaItemId(999L)
			.localizacaoId(1L)
			.statusItemId(1L)
			.tipoEntradaId(1L)
			.build();

		RestAssured.given()
			.port(porta)
			.contentType(ContentType.JSON)
			.header("X-ACCESS-TOKEN", "token")
			.body(dto)
			.when()
			.post("/core-service/v1/itens")
			.then()
			.statusCode(400);
	}

	@Test
	@DisplayName("Deve listar itens sem filtros")
	void deveListarSemFiltros() {
		Mockito.doNothing().when(autorizacaoService).autorizar(ArgumentMatchers.any(), ArgumentMatchers.any());
		var dto1 = CadastrarItemRequestDTO.builder()
			.nomeItem("Monitor 27")
			.descricaoCurta("Monitor")
			.descricaoDetalhada("QHD")
			.numeroSerie("MN-777")
			.codigoItem("COD-LF-1")
			.notaFiscal("NF-777")
			.categoriaItemId(1L)
			.localizacaoId(1L)
			.statusItemId(1L)
			.tipoEntradaId(1L)
			.build();
		RestAssured.given()
			.port(porta)
			.contentType(ContentType.JSON)
			.header("X-ACCESS-TOKEN", "token")
			.body(dto1)
			.post("/core-service/v1/itens")
			.then()
			.statusCode(201);
		var total = RestAssured.given()
			.port(porta)
			.header("X-ACCESS-TOKEN", "token")
			.get("/core-service/v1/itens")
			.then()
			.statusCode(200)
			.extract()
			.path("totalElements");
		assertTrue(((Number) total).longValue() >= 1L);
	}

	@Test
	@DisplayName("Deve filtrar por nome parcialmente")
	void deveFiltrarPorNomeParcialmente() {
		Mockito.doNothing().when(autorizacaoService).autorizar(ArgumentMatchers.any(), ArgumentMatchers.any());
		var resposta = RestAssured.given()
			.port(porta)
			.header("X-ACCESS-TOKEN", "token")
			.param("nomeItem", "tec")
			.get("/core-service/v1/itens")
			.then()
			.statusCode(200)
			.extract()
			.body()
			.asString();
		assertTrue(resposta.toUpperCase().contains("TEC"));
	}

	@Test
	@DisplayName("Deve filtrar combinando categoria e status")
	void deveFiltrarCombinandoCategoriaStatus() {
		Mockito.doNothing().when(autorizacaoService).autorizar(ArgumentMatchers.any(), ArgumentMatchers.any());
		var total = RestAssured.given()
			.port(porta)
			.header("X-ACCESS-TOKEN", "token")
			.param("categoriaItemId", 1)
			.param("statusItemId", 1)
			.get("/core-service/v1/itens")
			.then()
			.statusCode(200)
			.extract()
			.path("totalElements");
		assertTrue(((Number) total).longValue() >= 0L);
	}

	@Test
	@DisplayName("Deve filtrar por intervalo de datas")
	void deveFiltrarPorIntervaloDatas() {
		Mockito.doNothing().when(autorizacaoService).autorizar(ArgumentMatchers.any(), ArgumentMatchers.any());
		var agora = java.time.LocalDateTime.now();
		var total = RestAssured.given()
			.port(porta)
			.header("X-ACCESS-TOKEN", "token")
			.param("dataCadastroInicio", agora.minusMinutes(10).toString())
			.param("dataCadastroFim", agora.plusMinutes(10).toString())
			.get("/core-service/v1/itens")
			.then()
			.statusCode(200)
			.extract()
			.path("totalElements");
		assertTrue(((Number) total).longValue() >= 0L);
	}

	@Test
	@DisplayName("Deve paginar itens com size e page")
	void devePaginar() {
		Mockito.doNothing().when(autorizacaoService).autorizar(ArgumentMatchers.any(), ArgumentMatchers.any());
		var totalPagina = RestAssured.given()
			.port(porta)
			.header("X-ACCESS-TOKEN", "token")
			.param("page", 0)
			.param("size", 1)
			.get("/core-service/v1/itens")
			.then()
			.statusCode(200)
			.extract()
			.path("content.size()");
		assertTrue(((Number) totalPagina).intValue() <= 1);
	}

	@Test
	@DisplayName("Deve atualizar item com sucesso")
	void deveAtualizarItemComSucesso() {
		Mockito.doNothing().when(autorizacaoService).autorizar(ArgumentMatchers.any(), ArgumentMatchers.any());

		var dtoOriginal = CadastrarItemRequestDTO.builder()
			.nomeItem("Item Original")
			.descricaoCurta("Descrição original")
			.descricaoDetalhada("Descrição detalhada original")
			.numeroSerie("SN-ORIGINAL")
			.codigoItem("COD-UPDATE-1")
			.notaFiscal("NF-ORIGINAL")
			.categoriaItemId(1L)
			.localizacaoId(1L)
			.statusItemId(1L)
			.tipoEntradaId(1L)
			.build();

		var itemCriado = RestAssured.given()
			.port(porta)
			.contentType(ContentType.JSON)
			.header("X-ACCESS-TOKEN", "token")
			.body(dtoOriginal)
			.when()
			.post("/core-service/v1/itens")
			.then()
			.statusCode(201)
			.extract()
			.body()
			.as(ItemResponseDTO.class);

		Long itemId =  itemCriado.getId();

		var dtoAtualizacao = AtualizarItemRequestDTO.builder()
			.nomeItem("Item Atualizado")
			.descricaoCurta("Descrição atualizada")
			.descricaoDetalhada("Descrição detalhada atualizada")
			.numeroSerie("SN-ATUALIZADO")
			.notaFiscal("NF-ATUALIZADA")
			.categoriaItemId(1L)
			.localizacaoId(2L)
			.statusItemId(2L)
			.tipoEntradaId(2L)
			.build();

		var itemAtualizado = RestAssured.given()
			.port(porta)
			.contentType(ContentType.JSON)
			.header("X-ACCESS-TOKEN", "token")
			.body(dtoAtualizacao)
			.when()
			.put("/core-service/v1/itens/" + itemId)
			.then()
			.statusCode(200)
			.extract()
			.body()
			.as(ItemResponseDTO.class);

		assertTrue(itemAtualizado.getNomeItem().equals("Item Atualizado"));
		assertTrue(itemAtualizado.getNumeroSerie().equals("SN-ATUALIZADO"));
	}

	@Test
	@DisplayName("Deve atualizar apenas campos fornecidos (atualização parcial)")
	void deveAtualizarApenasCamposFornecidos() {
		Mockito.doNothing().when(autorizacaoService).autorizar(ArgumentMatchers.any(), ArgumentMatchers.any());
		var dtoOriginal = CadastrarItemRequestDTO.builder()
			.nomeItem("Item Parcial")
			.descricaoCurta("Descrição original")
			.descricaoDetalhada("Detalhes originais")
			.numeroSerie("SN-PARCIAL")
			.codigoItem("COD-PARCIAL-1")
			.notaFiscal("NF-PARCIAL")
			.categoriaItemId(1L)
			.localizacaoId(1L)
			.statusItemId(1L)
			.tipoEntradaId(1L)
			.build();

		var itemCriado = RestAssured.given()
			.port(porta)
			.contentType(ContentType.JSON)
			.header("X-ACCESS-TOKEN", "token")
			.body(dtoOriginal)
			.post("/core-service/v1/itens")
			.then()
			.statusCode(201)
			.extract()
			.body()
			.as(ItemResponseDTO.class);

		Long itemId = itemCriado.getId();

		var dtoAtualizacaoParcial = AtualizarItemRequestDTO.builder()
			.nomeItem("Nome Atualizado")
			.localizacaoId(2L)
			.build();

		var itemAtualizado = RestAssured.given()
			.port(porta)
			.contentType(ContentType.JSON)
			.header("X-ACCESS-TOKEN", "token")
			.body(dtoAtualizacaoParcial)
			.put("/core-service/v1/itens/" + itemId)
			.then()
			.statusCode(200)
			.extract()
			.body()
			.as(ItemResponseDTO.class);

		assertTrue(itemAtualizado.getNomeItem().equals("Nome Atualizado"));
		assertTrue(itemAtualizado.getLocalizacao().getId().longValue() == 2L);

		assertTrue(itemAtualizado.getNumeroSerie().equals("SN-PARCIAL"));
	}

	@Test
	@DisplayName("Deve retornar erro ao tentar atualizar item inexistente")
	void deveRetornarErroItemInexistente() {
		Mockito.doNothing().when(autorizacaoService).autorizar(ArgumentMatchers.any(), ArgumentMatchers.any());

		var dtoAtualizacao = AtualizarItemRequestDTO.builder()
			.nomeItem("Item Inexistente")
			.build();

		RestAssured.given()
			.port(porta)
			.contentType(ContentType.JSON)
			.header("X-ACCESS-TOKEN", "token")
			.body(dtoAtualizacao)
			.when()
			.put("/core-service/v1/itens/99999")
			.then()
			.statusCode(400);
	}

	@Test
	@DisplayName("Deve retornar erro ao atualizar com código já existente")
	void deveRetornarErroCodigoJaExistente() {
		Mockito.doNothing().when(autorizacaoService).autorizar(ArgumentMatchers.any(), ArgumentMatchers.any());

		var dto1 = CadastrarItemRequestDTO.builder()
			.nomeItem("Item 1")
			.codigoItem("COD-EXISTENTE-1")
			.categoriaItemId(1L)
			.localizacaoId(1L)
			.statusItemId(1L)
			.tipoEntradaId(1L)
			.build();

		RestAssured.given()
			.port(porta)
			.contentType(ContentType.JSON)
			.header("X-ACCESS-TOKEN", "token")
			.body(dto1)
			.post("/core-service/v1/itens")
			.then()
			.statusCode(201);

		var dto2 = CadastrarItemRequestDTO.builder()
			.nomeItem("Item 2")
			.codigoItem("COD-EXISTENTE-2")
			.categoriaItemId(1L)
			.localizacaoId(1L)
			.statusItemId(1L)
			.tipoEntradaId(1L)
			.build();

		var itemCriado2 = RestAssured.given()
			.port(porta)
			.contentType(ContentType.JSON)
			.header("X-ACCESS-TOKEN", "token")
			.body(dto2)
			.post("/core-service/v1/itens")
			.then()
			.statusCode(201)
			.extract()
			.body()
			.as(ItemResponseDTO.class);

		Long item2Id = itemCriado2.getId();

		var dtoAtualizacao = AtualizarItemRequestDTO.builder()
			.codigoItem("COD-EXISTENTE-1")
			.build();

		RestAssured.given()
			.port(porta)
			.contentType(ContentType.JSON)
			.header("X-ACCESS-TOKEN", "token")
			.body(dtoAtualizacao)
			.put("/core-service/v1/itens/" + item2Id)
			.then()
			.statusCode(400);
	}

	@Test
	@DisplayName("Deve retornar erro ao atualizar com entidades relacionadas inexistentes")
	void deveRetornarErroEntidadesInexistentes() {
		Mockito.doNothing().when(autorizacaoService).autorizar(ArgumentMatchers.any(), ArgumentMatchers.any());

		var dtoOriginal = CadastrarItemRequestDTO.builder()
			.nomeItem("Item Teste Entidades")
			.codigoItem("COD-ENT-TEST")
			.categoriaItemId(1L)
			.localizacaoId(1L)
			.statusItemId(1L)
			.tipoEntradaId(1L)
			.build();

		var itemCriado = RestAssured.given()
			.port(porta)
			.contentType(ContentType.JSON)
			.header("X-ACCESS-TOKEN", "token")
			.body(dtoOriginal)
			.post("/core-service/v1/itens")
			.then()
			.statusCode(201)
			.extract()
			.body()
			.as(ItemResponseDTO.class);

		Long itemId = itemCriado.getId();

		var dtoCategoria = AtualizarItemRequestDTO.builder()
			.categoriaItemId(999L)
			.build();

		RestAssured.given()
			.port(porta)
			.contentType(ContentType.JSON)
			.header("X-ACCESS-TOKEN", "token")
			.body(dtoCategoria)
			.put("/core-service/v1/itens/" + itemId)
			.then()
			.statusCode(400);

		var dtoLocalizacao = AtualizarItemRequestDTO.builder()
			.localizacaoId(999L)
			.build();

		RestAssured.given()
			.port(porta)
			.contentType(ContentType.JSON)
			.header("X-ACCESS-TOKEN", "token")
			.body(dtoLocalizacao)
			.put("/core-service/v1/itens/" + itemId)
			.then()
			.statusCode(400);

		var dtoStatus = AtualizarItemRequestDTO.builder()
			.statusItemId(999L)
			.build();

		RestAssured.given()
			.port(porta)
			.contentType(ContentType.JSON)
			.header("X-ACCESS-TOKEN", "token")
			.body(dtoStatus)
			.put("/core-service/v1/itens/" + itemId)
			.then()
			.statusCode(400);

		var dtoTipoEntrada = AtualizarItemRequestDTO.builder()
			.tipoEntradaId(999L)
			.build();

		RestAssured.given()
			.port(porta)
			.contentType(ContentType.JSON)
			.header("X-ACCESS-TOKEN", "token")
			.body(dtoTipoEntrada)
			.put("/core-service/v1/itens/" + itemId)
			.then()
			.statusCode(400);
	}

}
