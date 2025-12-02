package br.com.fatec.mogi.inventory_service.coreService.strategy.exportarItem.impl;

import br.com.fatec.mogi.inventory_service.coreService.domain.annotations.TipoExportacao;
import br.com.fatec.mogi.inventory_service.coreService.domain.exception.ErroExportarItensException;
import br.com.fatec.mogi.inventory_service.coreService.domain.exception.NenhumItemEncontradoException;
import br.com.fatec.mogi.inventory_service.coreService.domain.model.Item;
import br.com.fatec.mogi.inventory_service.coreService.repository.ItemRepository;
import br.com.fatec.mogi.inventory_service.coreService.strategy.exportarItem.ExportarItemStrategy;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@TipoExportacao("pdf")
@Component
@RequiredArgsConstructor
public class ExportarItemPdfStrategy implements ExportarItemStrategy {

	private final ItemRepository itemRepository;

	@Override
	public ResponseEntity<byte[]> exportar(List<Long> itensId) {
		try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			String dataGeracao = java.time.LocalDate.now().format(formatter);

			PdfWriter pdfWriter = new PdfWriter(byteArrayOutputStream);
			PdfDocument pdfDocument = new PdfDocument(pdfWriter);
			pdfDocument.setDefaultPageSize(PageSize.A4.rotate());

			Document document = new Document(pdfDocument);
			document.setMargins(50, 36, 50, 36);

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

			int totalPages = 1;
			pdfDocument.addEventHandler(PdfDocumentEvent.END_PAGE, new FooterEventHandler(totalPages));

			Table headerTable = new Table(UnitValue.createPercentArray(new float[] { 1, 1 })).useAllAvailableWidth();

			Table logoTextCell = new Table(1);
			logoTextCell.addCell(
					new Cell().add(new Paragraph("FATEC Mogi das Cruzes").setFontSize(16).setBold()).setBorder(null).setPadding(0));

			headerTable.addCell(new Cell().add(logoTextCell).setBorder(null).setTextAlignment(TextAlignment.LEFT));

			headerTable.addCell(new Cell().add(new Paragraph("Data de geração: " + dataGeracao).setFontSize(10))
				.setBorder(null)
				.setTextAlignment(TextAlignment.RIGHT)
				.setVerticalAlignment(VerticalAlignment.TOP));

			document.add(headerTable);
			document.add(new Paragraph("\n").setMarginTop(-10));

			Paragraph titulo = new Paragraph("Relatório de Itens").setFontSize(16)
				.setBold()
				.setTextAlignment(TextAlignment.CENTER)
				.setMarginBottom(15);
			document.add(titulo);

			DeviceRgb headerColor = new DeviceRgb(139, 0, 0);

			float[] colunas = { 2f, 3f, 2.5f, 1.5f, 2.5f, 1.5f };
			Table tabela = new Table(UnitValue.createPercentArray(colunas)).useAllAvailableWidth();

			tabela.addHeaderCell(createHeaderCell("Nº Patrimônio", headerColor));
			tabela.addHeaderCell(createHeaderCell("Nome", headerColor));
			tabela.addHeaderCell(createHeaderCell("Data da última auditoria", headerColor));
			tabela.addHeaderCell(createHeaderCell("Localização", headerColor));
			tabela.addHeaderCell(createHeaderCell("Categoria", headerColor));
			tabela.addHeaderCell(createHeaderCell("Status", headerColor));

			for (Item item : itens) {
				tabela.addCell(createDataCell(item.getCodigoItem()));
				tabela.addCell(createDataCell(Optional.ofNullable(item.getNomeItem()).orElse("-")));
				tabela.addCell(createDataCell(
						Optional.ofNullable(item.getUltimaVezAuditado()).map(formatter::format).orElse("-")));
				tabela.addCell(createDataCell(item.getLocalizacao().getNomeSala()));
				tabela.addCell(createDataCell(item.getCategoriaItem().getNome()));
				tabela.addCell(createDataCell(item.getStatusItem().getNome()));
			}

			document.add(tabela);
			document.close();

			return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=relatorio-itens.pdf")
				.contentType(MediaType.APPLICATION_PDF)
				.body(byteArrayOutputStream.toByteArray());
		}
		catch (Exception e) {
			throw new ErroExportarItensException();
		}
	}

	private Cell createHeaderCell(String text, DeviceRgb color) {
		return new Cell().add(new Paragraph(text).setFontSize(10).setBold().setFontColor(ColorConstants.WHITE))
			.setBackgroundColor(color)
			.setTextAlignment(TextAlignment.CENTER)
			.setVerticalAlignment(VerticalAlignment.MIDDLE)
			.setPadding(5);
	}

	private Cell createDataCell(String text) {
		return new Cell().add(new Paragraph(text != null ? text : "").setFontSize(9))
			.setPadding(5)
			.setVerticalAlignment(VerticalAlignment.MIDDLE);
	}

	private static class FooterEventHandler implements IEventHandler {

		private final int totalPages;

		public FooterEventHandler(int totalPages) {
			this.totalPages = totalPages;
		}

		@Override
		public void handleEvent(Event event) {
			PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
			PdfDocument pdfDoc = docEvent.getDocument();
			PdfPage page = docEvent.getPage();
			int pageNumber = pdfDoc.getPageNumber(page);

			PdfCanvas canvas = new PdfCanvas(page);
			Canvas canvasLayout = new Canvas(canvas, page.getPageSize());

			Paragraph footer = new Paragraph("Fatec Mogi das Cruzes ©2025").setFontSize(8)
				.setTextAlignment(TextAlignment.CENTER);

			Paragraph pageInfo = new Paragraph("Página " + pageNumber + " de " + totalPages).setFontSize(8)
				.setTextAlignment(TextAlignment.CENTER);

			float x = page.getPageSize().getWidth() / 2;
			float y = 20;

			canvasLayout.showTextAligned(footer, x, y, TextAlignment.CENTER);
			canvasLayout.showTextAligned(pageInfo, x, y - 12, TextAlignment.CENTER);

			canvasLayout.close();
		}

	}

}