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
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;

public class ItemUploadReader implements ItemStreamReader<ItemUploadRequestDTO> {

	private BufferedReader reader;

	private final Resource resource;

	private CsvParser parser;

	private Iterator<Record> iterator;

	private long linhaAtual = 0;

	public ItemUploadReader(Resource resource) {
		this.resource = resource;
	}

	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		try {
			this.reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));
			CsvParserSettings settings = new CsvParserSettings();
			settings.setHeaderExtractionEnabled(true);
			settings.getFormat().setLineSeparator("\n");
			this.parser = new CsvParser(settings);

			List<Record> all = parser.parseAllRecords(reader);
			this.iterator = all.iterator();
			this.linhaAtual = 0;
		}
		catch (IOException e) {
			throw new ItemStreamException("Erro ao abrir arquivo CSV", e);
		}
	}

	@Override
	public ItemUploadRequestDTO read() throws Exception {
		if (iterator != null && iterator.hasNext()) {
			Record rec = iterator.next();
			linhaAtual++;
			return ItemUploadRequestDTO.builder()
				.nome(rec.getString("Nome"))
				.localizacao(rec.getString("Localização"))
				.codigo(rec.getString("Número do patrimônio"))
				.categoria(rec.getString("Categoria"))
				.condicao(rec.getString("Condição"))
				.tipoEntrada(rec.getString("Modalidade Aquisição"))
				.dataCadastro(rec.getString("Data de Cadastramento"))
				.numeroLinha(linhaAtual + 1)
				.build();
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
