package com.matteusmoreno.moto_manager.finance;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.*;
import com.matteusmoreno.moto_manager.finance.response.FinanceDetailsResponse;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;

@Component
public class PdfReportGenerator {

    public void generateReportPdf(FinanceDetailsResponse financeDetails, String outputPath) throws IOException, DocumentException {
        Document document = new Document(PageSize.A4.rotate(), 20, 20, 30, 30); // Adicionando margens
        try (FileOutputStream fos = new FileOutputStream(outputPath)) {
            PdfWriter.getInstance(document, fos);
            document.open();

            // Título
            Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD, Color.BLACK);
            Paragraph title = new Paragraph(financeDetails.reportName(), titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // Data do Relatório
            Font dateFont = new Font(Font.HELVETICA, 12, Font.NORMAL, Color.BLACK);
            Paragraph date = new Paragraph("Date: " + financeDetails.reportDate(), dateFont);
            date.setAlignment(Element.ALIGN_CENTER);
            document.add(date);

            // Espaço entre seções
            document.add(Chunk.NEWLINE);

            // Tabela de Receivables
            document.add(new Paragraph("Receivables", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
            PdfPTable receivablesTable = new PdfPTable(11); // Removida a coluna ID, agora 11 colunas
            receivablesTable.setWidthPercentage(100); // Tabela ocupa toda a largura

            // Largura das colunas ajustada para melhor uso do espaço
            float[] columnWidths = {2f, 2.5f, 2f, 3f, 3f, 3f, 6f, 2f, 2f, 2f, 2f};
            receivablesTable.setWidths(columnWidths);

            // Cabeçalho
            addTableHeader(receivablesTable, new String[]{
                    "Service Order", "Motorcycle Model", "Motorcycle Plate", "Customer", "Seller",
                    "Mechanic", "Description", "Value", "Issue Date", "Payment Date", "Status"
            });

            // Linhas de dados
            boolean alternateColor = false;
            for (var r : financeDetails.receivables()) {
                addTableRow(receivablesTable, new String[]{
                        r.serviceOrderId().toString(), r.motorcycleModel(), r.motorcyclePlate(),
                        r.customerName(), r.sellerName(), r.mechanicName(), r.description(),
                        "R$" + r.value().toString(), r.issueDate().toString(),
                        r.paymentDate() != null ? r.paymentDate().toString() : "N/A", r.status()
                }, alternateColor);
                alternateColor = !alternateColor;
            }

            document.add(receivablesTable);

            // Espaço entre seções
            document.add(Chunk.NEWLINE);

            // Tabela de Payables
            document.add(new Paragraph("Payables", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
            PdfPTable payablesTable = new PdfPTable(8); // Removida a coluna ID, agora 8 colunas
            payablesTable.setWidthPercentage(100);

            // Largura ajustada
            float[] payablesColumnWidths = {4f, 3f, 4f, 2f, 2f, 2f, 2f, 2f};
            payablesTable.setWidths(payablesColumnWidths);

            // Cabeçalho
            addTableHeader(payablesTable, new String[]{
                    "Supplier Name", "Supplier CNPJ", "Description", "Value", "Issue Date", "Due Date", "Payment Date", "Status"
            });

            // Linhas de dados
            alternateColor = false;
            for (var p : financeDetails.payables()) {
                addTableRow(payablesTable, new String[]{
                        p.supplierName(), p.supplierCnpj(), p.description(),
                        "R$" + p.value().toString(), p.issueDate().toString(),
                        p.dueDate().toString(), p.paymentDate() != null ? p.paymentDate().toString() : "N/A", p.status()
                }, alternateColor);
                alternateColor = !alternateColor;
            }

            document.add(payablesTable);

            // Resumo
            document.add(Chunk.NEWLINE);
            Font summaryFont = new Font(Font.HELVETICA, 12, Font.BOLD, Color.BLACK);
            document.add(new Paragraph("Summary", summaryFont));
            Font contentFont = new Font(Font.HELVETICA, 12, Font.NORMAL, Color.BLACK);
            document.add(new Paragraph("Total Receivables: R$" + financeDetails.totalReceivables(), contentFont));
            document.add(new Paragraph("Total Payables: R$" + financeDetails.totalPayables(), contentFont));
            document.add(new Paragraph("Profit/Loss: R$" + financeDetails.profitOrLoss(), contentFont));

            document.close();
        }
    }

    private void addTableHeader(PdfPTable table, String[] headers) {
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
            cell.setBackgroundColor(Color.LIGHT_GRAY);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorderWidth(0.5f); // Borda mais fina para visual mais leve
            table.addCell(cell);
        }
    }

    private void addTableRow(PdfPTable table, String[] values, boolean alternateColor) {
        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
        Color rowColor = alternateColor ? new Color(240, 240, 240) : Color.WHITE;

        for (String value : values) {
            PdfPCell cell = new PdfPCell(new Phrase(value, cellFont));
            cell.setBorderWidth(0.5f);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setMinimumHeight(25); // Altura mínima da célula
            cell.setBackgroundColor(rowColor); // Cor alternada nas linhas
            table.addCell(cell);
        }
    }
}
