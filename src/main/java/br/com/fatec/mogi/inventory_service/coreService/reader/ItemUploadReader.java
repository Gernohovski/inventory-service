package br.com.fatec.mogi.inventory_service.coreService.reader;

import br.com.fatec.mogi.inventory_service.coreService.web.request.ItemUploadRequestDTO;
import com.univocity.parsers.common.record.Record;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ItemUploadReader implements ItemStreamReader<ItemUploadRequestDTO> {

	private BufferedReader reader;

	private final Resource resource;

	private CsvParser parser;

	private Iterator<Record> iterator;

	private long linhaAtual = 0;

	private static final Map<String, List<String>> HEADER_ALIASES = Map.of("Nome",
			List.of("nome", "Name", "Descrição", "Descricao", "Descrição do Bem", "Item", "Descricao do Bem"),
			"Localização",
			List.of("Localização", "Localizacao", "localizacao", "localização", "Local", "Sala", "Location"),
			"Número do patrimônio",
			List.of("Número do patrimônio", "Numero do patrimonio", "Patrimônio", "Patrimonio", "Código", "Codigo",
					"numero", "codigo", "Nº Patrimonial", "Número do Patrimônio"),
			"Categoria", List.of("Categoria", "Grupo", "grupo", "categoria", "Grupo Patrimonial", "Grupo patrimonial"),
			"Condição", List.of("Condição", "Condicao", "Estado", "Status", "condicao", "status"),
			"Modalidade Aquisição",
			List.of("Modalidade Aquisição", "Modalidade Aquisicao", "Tipo Entrada", "Aquisição", "Aquisicao",
					"Modalidade"),
			"Data de Cadastramento", List.of("Data de Cadastramento", "Data Cadastramento", "Data", "Data Cadastro",
					"data", "Data de cadastramento", "Data de Incorporação", "Data de Incorporacao"));

	public ItemUploadReader(Resource resource) {
		this.resource = resource;
	}

	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		try {
			Charset charset = tentarDetectarCharset();
			this.reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), charset));
			CsvParserSettings settings = new CsvParserSettings();
			settings.setHeaderExtractionEnabled(true);
			settings.getFormat().setLineSeparator("\n");
			settings.detectFormatAutomatically(',', ';');
			this.parser = new CsvParser(settings);

			List<Record> all = parser.parseAllRecords(reader);
			this.iterator = all.iterator();
			this.linhaAtual = 0;
		}
		catch (IOException e) {
			throw new ItemStreamException("Erro ao abrir arquivo CSV", e);
		}
	}

	private Charset tentarDetectarCharset() throws IOException {
		try (BufferedReader testeReader = new BufferedReader(
				new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
			CsvParserSettings settings = new CsvParserSettings();
			settings.setHeaderExtractionEnabled(true);
			settings.getFormat().setLineSeparator("\n");
			settings.detectFormatAutomatically(',', ';');
			CsvParser testeParser = new CsvParser(settings);
			testeParser.parseAllRecords(testeReader);

			String[] headers = testeParser.getRecordMetadata().headers();
			if (headers != null && Arrays.toString(headers).contains("�")) {
				return StandardCharsets.ISO_8859_1;
			}
		}
		return StandardCharsets.UTF_8;
	}

	@Override
	public ItemUploadRequestDTO read() {
		if (iterator != null && iterator.hasNext()) {
			Record rec = iterator.next();
			linhaAtual++;
			return ItemUploadRequestDTO.builder()
				.nome(getValueByAlias(rec, "Nome"))
				.localizacao(getValueByAlias(rec, "Localização"))
				.codigo(getValueByAlias(rec, "Número do patrimônio"))
				.categoria(getValueByAlias(rec, "Categoria"))
				.condicao(getValueByAlias(rec, "Condição"))
				.tipoEntrada(getValueByAlias(rec, "Modalidade Aquisição"))
				.dataCadastro(getValueByAlias(rec, "Data de Cadastramento"))
				.numeroLinha(linhaAtual)
				.build();
		}
		return null;
	}

	private String getValueByAlias(Record rec, String headerPrincipal) {
		List<String> aliases = HEADER_ALIASES.get(headerPrincipal);
		if (aliases == null) {
			return null;
		}
		for (String alias : aliases) {
			try {
				String value = rec.getString(alias);
				if (value != null && !value.trim().isEmpty()) {
					return value;
				}
			}
			catch (Exception e) {
				// Header não encontrado, tenta próximo alias
			}
		}
		return null;
	}

	@Override
	public void update(ExecutionContext executionContext) throws ItemStreamException {
		executionContext.putLong("linhaAtual", linhaAtual);
	}

	@Override
	public void close() throws ItemStreamException {
		if (reader != null) {
			try {
				reader.close();
			}
			catch (IOException e) {
				throw new ItemStreamException("Erro ao fechar reader", e);
			}
		}
	}

}
