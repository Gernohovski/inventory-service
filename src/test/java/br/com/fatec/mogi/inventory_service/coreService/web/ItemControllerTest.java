package br.com.fatec.mogi.inventory_service.coreService.web;

import br.com.fatec.mogi.inventory_service.authService.service.AutorizacaoService;
import br.com.fatec.mogi.inventory_service.coreService.repository.ItemRepository;
import br.com.fatec.mogi.inventory_service.coreService.web.request.CadastrarItemRequestDTO;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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
}
