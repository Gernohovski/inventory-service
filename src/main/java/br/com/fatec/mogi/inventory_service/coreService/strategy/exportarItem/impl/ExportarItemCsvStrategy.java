package br.com.fatec.mogi.inventory_service.coreService.strategy.exportarItem.impl;

import br.com.fatec.mogi.inventory_service.coreService.domain.annotations.TipoExportacao;
import br.com.fatec.mogi.inventory_service.coreService.domain.exception.ErroExportarItensException;
import br.com.fatec.mogi.inventory_service.coreService.domain.exception.NenhumItemEncontradoException;
import br.com.fatec.mogi.inventory_service.coreService.domain.model.Item;
import br.com.fatec.mogi.inventory_service.coreService.repository.ItemRepository;
import br.com.fatec.mogi.inventory_service.coreService.strategy.exportarItem.ExportarItemStrategy;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@TipoExportacao("csv")
@Component
@RequiredArgsConstructor
public class ExportarItemCsvStrategy implements ExportarItemStrategy {

	private final ItemRepository itemRepository;

	@Override
	public ResponseEntity<byte[]> exportar(List<Long> itensId) {
		try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
			byteArrayOutputStream.write(0xEF);
			byteArrayOutputStream.write(0xBB);
			byteArrayOutputStream.write(0xBF);

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			CsvWriterSettings configuracoes = new CsvWriterSettings();
			configuracoes.setHeaderWritingEnabled(true);
			CsvWriter writer = new CsvWriter(new OutputStreamWriter(byteArrayOutputStream, StandardCharsets.UTF_8),
					configuracoes);
			writer.writeHeaders("Nº Patrimônio", "Nome", "Última vez auditado", "Localização", "Categoria", "Status");
			List<Item> itens;
			if (!itensId.isEmpty()) {
				itens = itemRepository.findAllById(itensId);
				if (itens.isEmpty()) {
					throw new NenhumItemEncontradoException();
				}
			}
			else {
				itens = itemRepository.findAll();
			}
			for (Item item : itens) {
				writer.writeRow(item.getCodigoItem(), item.getNomeItem(),
						Optional.ofNullable(item.getUltimaVezAuditado()).map(formatter::format).orElse(""),
						item.getLocalizacao().getNomeSala(), item.getCategoriaItem().getNome(),
						item.getStatusItem().getNome());
			}
			writer.close();
			return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=itens.csv")
				.contentType(MediaType.parseMediaType("text/csv; charset=UTF-8"))
				.body(byteArrayOutputStream.toByteArray());
		}
		catch (Exception e) {
			throw new ErroExportarItensException();
		}
	}

}
