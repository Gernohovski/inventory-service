package br.com.fatec.mogi.inventory_service.coreService.batch;

import br.com.fatec.mogi.inventory_service.InventoryServiceApplication;
import br.com.fatec.mogi.inventory_service.coreService.config.ItemUploadJobConfig;
import br.com.fatec.mogi.inventory_service.coreService.domain.model.Item;
import br.com.fatec.mogi.inventory_service.coreService.reader.ItemUploadReader;
import br.com.fatec.mogi.inventory_service.coreService.repository.ItemRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = InventoryServiceApplication.class)
@SpringBatchTest
@ActiveProfiles("test")
public class ItemUploadJobTest {

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private ItemUploadJobConfig jobConfig;

	@Autowired
	private ItemRepository itemRepository;

	private File tempCsvFile;

	@BeforeEach
	void setUp() {
		itemRepository.deleteAll();
	}

	@AfterEach
	void tearDown() {
		if (tempCsvFile != null && tempCsvFile.exists()) {
			tempCsvFile.delete();
		}
	}

	@Test
	@DisplayName("Deve processar CSV válido com sucesso")
	void deveProcessarCsvValidoComSucesso() throws Exception {
		tempCsvFile = criarCsvValido();
		Resource resource = new org.springframework.core.io.FileSystemResource(tempCsvFile);
		ItemUploadReader reader = new ItemUploadReader(resource);

		var step = jobConfig.itemUploadStep(reader);
		Job job = jobConfig.createJob(step);
		JobParameters params = new JobParametersBuilder().addString("fileName", "test-valid.csv")
			.addLong("timestamp", System.currentTimeMillis())
			.toJobParameters();

		JobExecution execution = jobLauncher.run(job, params);

		assertEquals("COMPLETED", execution.getStatus().toString());

		long totalLidos = execution.getExecutionContext().getLong("totalLidos");
		long totalSalvos = execution.getExecutionContext().getLong("totalSalvos");
		long totalErros = execution.getExecutionContext().getLong("totalErros");

		assertEquals(3, totalLidos);
		assertEquals(3, totalSalvos);
		assertEquals(0, totalErros);

		List<Item> itens = itemRepository.findAll();
		assertEquals(3, itens.size());

		assertTrue(itemRepository.existsByCodigoItem("PAT-001"));
		assertTrue(itemRepository.existsByCodigoItem("PAT-002"));
		assertTrue(itemRepository.existsByCodigoItem("PAT-003"));

		Item item1 = itemRepository.findByCodigoItem("PAT-001").orElseThrow();
		assertEquals("Cadeira Giratória", item1.getNomeItem());
		assertEquals("CADEIRA", item1.getCategoriaItem().getNome());
		assertEquals("LAB01", item1.getLocalizacao().getNomeSala());
		assertEquals("NOVO", item1.getStatusItem().getNome());
		assertEquals("COMPRA", item1.getTipoEntrada().getNome());
		assertNotNull(item1.getDataCadastro());
		assertNotNull(item1.getDataAlteracao());
	}

	@Test
	@DisplayName("Deve pular linhas com código inválido")
	void devePularLinhasComCodigoInvalido() throws Exception {
		tempCsvFile = criarCsvComCodigoInvalido();
		Resource resource = new org.springframework.core.io.FileSystemResource(tempCsvFile);
		ItemUploadReader reader = new ItemUploadReader(resource);

		var step = jobConfig.itemUploadStep(reader);
		Job job = jobConfig.createJob(step);
		JobParameters params = new JobParametersBuilder().addString("fileName", "test-invalid-code.csv")
			.addLong("timestamp", System.currentTimeMillis())
			.toJobParameters();

		JobExecution execution = jobLauncher.run(job, params);

		assertEquals("COMPLETED", execution.getStatus().toString());

		long totalLidos = execution.getExecutionContext().getLong("totalLidos");
		long totalSalvos = execution.getExecutionContext().getLong("totalSalvos");
		long totalErros = execution.getExecutionContext().getLong("totalErros");

		assertEquals(3, totalLidos);
		assertEquals(2, totalSalvos);
		assertEquals(0, totalErros);

		List<Item> itens = itemRepository.findAll();
		assertEquals(2, itens.size());

		assertTrue(itemRepository.existsByCodigoItem("PAT-004"));
		assertTrue(itemRepository.existsByCodigoItem("PAT-005"));
		assertFalse(itemRepository.existsByCodigoItem(""));
	}

	@Test
	@DisplayName("Deve pular linhas com categoria não mapeada")
	void devePularLinhasComCategoriaNaoMapeada() throws Exception {
		tempCsvFile = criarCsvComCategoriaNaoMapeada();
		Resource resource = new org.springframework.core.io.FileSystemResource(tempCsvFile);
		ItemUploadReader reader = new ItemUploadReader(resource);

		var step = jobConfig.itemUploadStep(reader);
		Job job = jobConfig.createJob(step);
		JobParameters params = new JobParametersBuilder().addString("fileName", "test-invalid-category.csv")
			.addLong("timestamp", System.currentTimeMillis())
			.toJobParameters();

		JobExecution execution = jobLauncher.run(job, params);

		assertEquals("COMPLETED", execution.getStatus().toString());

		long totalLidos = execution.getExecutionContext().getLong("totalLidos");
		long totalSalvos = execution.getExecutionContext().getLong("totalSalvos");

		assertEquals(2, totalLidos);
		assertEquals(1, totalSalvos);

		List<Item> itens = itemRepository.findAll();
		assertEquals(1, itens.size());

		assertTrue(itemRepository.existsByCodigoItem("PAT-006"));
	}

	@Test
	@DisplayName("Deve pular linhas com localização não mapeada")
	void devePularLinhasComLocalizacaoNaoMapeada() throws Exception {
		tempCsvFile = criarCsvComLocalizacaoNaoMapeada();
		Resource resource = new org.springframework.core.io.FileSystemResource(tempCsvFile);
		ItemUploadReader reader = new ItemUploadReader(resource);

		var step = jobConfig.itemUploadStep(reader);
		Job job = jobConfig.createJob(step);
		JobParameters params = new JobParametersBuilder().addString("fileName", "test-invalid-location.csv")
			.addLong("timestamp", System.currentTimeMillis())
			.toJobParameters();

		JobExecution execution = jobLauncher.run(job, params);

		assertEquals("COMPLETED", execution.getStatus().toString());

		long totalLidos = execution.getExecutionContext().getLong("totalLidos");
		long totalSalvos = execution.getExecutionContext().getLong("totalSalvos");

		assertEquals(2, totalLidos);
		assertEquals(1, totalSalvos);

		List<Item> itens = itemRepository.findAll();
		assertEquals(1, itens.size());

		assertTrue(itemRepository.existsByCodigoItem("PAT-008"));
	}

	@Test
	@DisplayName("Deve pular linhas com código duplicado")
	void devePularLinhasComCodigoDuplicado() throws Exception {
		Item itemExistente = new Item();
		itemExistente.setCodigoItem("PAT-DUP");
		itemExistente.setNomeItem("Item Existente");
		itemRepository.save(itemExistente);

		tempCsvFile = criarCsvComCodigoDuplicado();
		Resource resource = new org.springframework.core.io.FileSystemResource(tempCsvFile);
		ItemUploadReader reader = new ItemUploadReader(resource);

		var step = jobConfig.itemUploadStep(reader);
		Job job = jobConfig.createJob(step);
		JobParameters params = new JobParametersBuilder().addString("fileName", "test-duplicate-code.csv")
			.addLong("timestamp", System.currentTimeMillis())
			.toJobParameters();

		JobExecution execution = jobLauncher.run(job, params);

		assertEquals("COMPLETED", execution.getStatus().toString());

		long totalLidos = execution.getExecutionContext().getLong("totalLidos");
		long totalSalvos = execution.getExecutionContext().getLong("totalSalvos");

		assertEquals(2, totalLidos);
		assertEquals(1, totalSalvos);

		List<Item> itens = itemRepository.findAll();
		assertEquals(2, itens.size());
	}

	@Test
	@DisplayName("Deve processar CSV com data de cadastro vazia usando data atual")
	void deveProcessarCsvComDataVazia() throws Exception {
		tempCsvFile = criarCsvComDataVazia();
		Resource resource = new org.springframework.core.io.FileSystemResource(tempCsvFile);
		ItemUploadReader reader = new ItemUploadReader(resource);

		var step = jobConfig.itemUploadStep(reader);
		Job job = jobConfig.createJob(step);
		JobParameters params = new JobParametersBuilder().addString("fileName", "test-empty-date.csv")
			.addLong("timestamp", System.currentTimeMillis())
			.toJobParameters();

		JobExecution execution = jobLauncher.run(job, params);

		assertEquals("COMPLETED", execution.getStatus().toString());

		long totalSalvos = execution.getExecutionContext().getLong("totalSalvos");
		assertEquals(1, totalSalvos);

		Item item = itemRepository.findByCodigoItem("PAT-010").orElseThrow();
		assertNotNull(item.getDataCadastro());
		assertNotNull(item.getDataAlteracao());
	}

	@Test
	@DisplayName("Deve pular linhas com data inválida")
	void devePularLinhasComDataInvalida() throws Exception {
		tempCsvFile = criarCsvComDataInvalida();
		Resource resource = new org.springframework.core.io.FileSystemResource(tempCsvFile);
		ItemUploadReader reader = new ItemUploadReader(resource);

		var step = jobConfig.itemUploadStep(reader);
		Job job = jobConfig.createJob(step);
		JobParameters params = new JobParametersBuilder().addString("fileName", "test-invalid-date.csv")
			.addLong("timestamp", System.currentTimeMillis())
			.toJobParameters();

		JobExecution execution = jobLauncher.run(job, params);

		assertEquals("COMPLETED", execution.getStatus().toString());

		long totalLidos = execution.getExecutionContext().getLong("totalLidos");
		long totalSalvos = execution.getExecutionContext().getLong("totalSalvos");

		assertEquals(1, totalLidos);
		assertEquals(0, totalSalvos);

		assertFalse(itemRepository.existsByCodigoItem("PAT-011"));
	}

	@Test
	@DisplayName("Deve processar CSV vazio sem erros")
	void deveProcessarCsvVazio() throws Exception {
		tempCsvFile = criarCsvVazio();
		Resource resource = new org.springframework.core.io.FileSystemResource(tempCsvFile);
		ItemUploadReader reader = new ItemUploadReader(resource);

		var step = jobConfig.itemUploadStep(reader);
		Job job = jobConfig.createJob(step);
		JobParameters params = new JobParametersBuilder().addString("fileName", "test-empty.csv")
			.addLong("timestamp", System.currentTimeMillis())
			.toJobParameters();

		JobExecution execution = jobLauncher.run(job, params);

		assertEquals("COMPLETED", execution.getStatus().toString());

		long totalLidos = execution.getExecutionContext().getLong("totalLidos");
		long totalSalvos = execution.getExecutionContext().getLong("totalSalvos");

		assertEquals(0, totalLidos);
		assertEquals(0, totalSalvos);

		List<Item> itens = itemRepository.findAll();
		assertEquals(0, itens.size());
	}

	private File criarCsvValido() throws IOException {
		File file = File.createTempFile("test-valid-", ".csv");
		try (FileWriter writer = new FileWriter(file)) {
			writer.write(
					"Nome,Localização,Número do patrimônio,Categoria,Condição,Modalidade Aquisição,Data de Cadastramento\n");
			writer.write("Cadeira Giratória,LAB01,PAT-001,CADEIRA,NOVO,COMPRA,2024-01-15\n");
			writer.write("Mesa de Escritório,LAB02,PAT-002,MESA,USADO,DOACAO,2024-02-20\n");
			writer.write("Computador Desktop,LAB03,PAT-003,COMPUTADOR,NOVO,COMPRA,2024-03-10\n");
		}
		return file;
	}

	private File criarCsvComCodigoInvalido() throws IOException {
		File file = File.createTempFile("test-invalid-code-", ".csv");
		try (FileWriter writer = new FileWriter(file)) {
			writer.write(
					"Nome,Localização,Número do patrimônio,Categoria,Condição,Modalidade Aquisição,Data de Cadastramento\n");
			writer.write("Cadeira,LAB01,PAT-004,CADEIRA,NOVO,COMPRA,2024-01-15\n");
			writer.write("Mesa,LAB02,,MESA,USADO,DOACAO,2024-02-20\n");
			writer.write("Computador,LAB03,PAT-005,COMPUTADOR,NOVO,COMPRA,2024-03-10\n");
		}
		return file;
	}

	private File criarCsvComCategoriaNaoMapeada() throws IOException {
		File file = File.createTempFile("test-invalid-category-", ".csv");
		try (FileWriter writer = new FileWriter(file)) {
			writer.write(
					"Nome,Localização,Número do patrimônio,Categoria,Condição,Modalidade Aquisição,Data de Cadastramento\n");
			writer.write("Item Teste,LAB01,PAT-006,CADEIRA,NOVO,COMPRA,2024-01-15\n");
			writer.write("Item Inválido,LAB02,PAT-007,CATEGORIA_INEXISTENTE,USADO,DOACAO,2024-02-20\n");
		}
		return file;
	}

	private File criarCsvComLocalizacaoNaoMapeada() throws IOException {
		File file = File.createTempFile("test-invalid-location-", ".csv");
		try (FileWriter writer = new FileWriter(file)) {
			writer.write(
					"Nome,Localização,Número do patrimônio,Categoria,Condição,Modalidade Aquisição,Data de Cadastramento\n");
			writer.write("Item Inválido,SALA_INEXISTENTE,PAT-007,CADEIRA,NOVO,COMPRA,2024-01-15\n");
			writer.write("Item Válido,LAB01,PAT-008,MESA,USADO,DOACAO,2024-02-20\n");
		}
		return file;
	}

	private File criarCsvComCodigoDuplicado() throws IOException {
		File file = File.createTempFile("test-duplicate-code-", ".csv");
		try (FileWriter writer = new FileWriter(file)) {
			writer.write(
					"Nome,Localização,Número do patrimônio,Categoria,Condição,Modalidade Aquisição,Data de Cadastramento\n");
			writer.write("Item Duplicado,LAB01,PAT-DUP,CADEIRA,NOVO,COMPRA,2024-01-15\n");
			writer.write("Item Novo,LAB02,PAT-009,MESA,USADO,DOACAO,2024-02-20\n");
		}
		return file;
	}

	private File criarCsvComDataVazia() throws IOException {
		File file = File.createTempFile("test-empty-date-", ".csv");
		try (FileWriter writer = new FileWriter(file)) {
			writer.write(
					"Nome,Localização,Número do patrimônio,Categoria,Condição,Modalidade Aquisição,Data de Cadastramento\n");
			writer.write("Item Sem Data,LAB01,PAT-010,CADEIRA,NOVO,COMPRA,\n");
		}
		return file;
	}

	private File criarCsvComDataInvalida() throws IOException {
		File file = File.createTempFile("test-invalid-date-", ".csv");
		try (FileWriter writer = new FileWriter(file)) {
			writer.write(
					"Nome,Localização,Número do patrimônio,Categoria,Condição,Modalidade Aquisição,Data de Cadastramento\n");
			writer.write("Item Data Inválida,LAB01,PAT-011,CADEIRA,NOVO,COMPRA,DATA-INVALIDA\n");
		}
		return file;
	}

	private File criarCsvVazio() throws IOException {
		File file = File.createTempFile("test-empty-", ".csv");
		try (FileWriter writer = new FileWriter(file)) {
			writer.write(
					"Nome,Localização,Número do patrimônio,Categoria,Condição,Modalidade Aquisição,Data de Cadastramento\n");
		}
		return file;
	}

}
