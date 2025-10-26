package br.com.fatec.mogi.inventory_service.coreService.strategy.exportarItem.impl;

import br.com.fatec.mogi.inventory_service.coreService.domain.annotations.TipoExportacao;
import br.com.fatec.mogi.inventory_service.coreService.domain.exception.ErroExportarItensException;
import br.com.fatec.mogi.inventory_service.coreService.domain.exception.NenhumItemEncontradoException;
import br.com.fatec.mogi.inventory_service.coreService.domain.model.Item;
import br.com.fatec.mogi.inventory_service.coreService.repository.ItemRepository;
import br.com.fatec.mogi.inventory_service.coreService.strategy.exportarItem.ExportarItemStrategy;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

@TipoExportacao("pdf")
@Component
@RequiredArgsConstructor
public class ExportarItemPdfStrategy implements ExportarItemStrategy {

	private final ItemRepository itemRepository;

	@Override
	public ResponseEntity<byte[]> exportar(List<Long> itensId) {
		try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			PdfWriter pdfWriter = new PdfWriter(byteArrayOutputStream);
			PdfDocument pdfDocument = new PdfDocument(pdfWriter);
			Document document = new Document(pdfDocument);
			var logoFile = new ClassPathResource("static/logo.png");
			Image logo = new Image(ImageDataFactory.create(logoFile.getInputStream().readAllBytes())).scaleToFit(100,
					100);
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
			Table header = new Table(new float[] { 1, 4 });
			header.addCell(new Cell().add(logo).setBorder(null));
			header.addCell(
					new Cell()
						.add(new Paragraph("Relatório de Itens").setFontSize(16)
							.setBold()
							.setTextAlignment(TextAlignment.CENTER))
						.setBorder(null));
			document.add(header);
			document.add(new Paragraph("\n"));
			float[] colunas = { 2, 4, 3, 3, 3, 2 };
			Table tabela = new Table(colunas);
			tabela.addHeaderCell(new Cell().add(new Paragraph("Código")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
			tabela.addHeaderCell(new Cell().add(new Paragraph("Nome")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
			tabela.addHeaderCell(
					new Cell().add(new Paragraph("Data de entrada")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
			tabela.addHeaderCell(
					new Cell().add(new Paragraph("Localização")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
			tabela.addHeaderCell(
					new Cell().add(new Paragraph("Categoria")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
			tabela.addHeaderCell(new Cell().add(new Paragraph("Status")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
			for (Item item : itens) {
				tabela.addCell(new Cell().add(new Paragraph(item.getCodigoItem())));
				tabela.addCell(new Cell().add(new Paragraph(item.getNomeItem())));
				tabela.addCell(new Cell().add(new Paragraph(item.getDataCadastro().format(formatter))));
				tabela.addCell(new Cell().add(new Paragraph(item.getLocalizacao().getNomeSala())));
				tabela.addCell(new Cell().add(new Paragraph(item.getCategoriaItem().getNome())));
				tabela.addCell(new Cell().add(new Paragraph(item.getStatusItem().getNome())));
			}
			document.add(tabela);
			document.close();
			return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=itens.pdf")
				.contentType(MediaType.APPLICATION_PDF)
				.body(byteArrayOutputStream.toByteArray());
		}
		catch (Exception e) {
			throw new ErroExportarItensException();
		}
	}

}
