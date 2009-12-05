/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part1.chapter04;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class CellHeights {
    public static final String RESULT = "results/part1/chapter04/cell_heights.pdf";

    public static void main(String[] args) throws DocumentException, IOException {
        Document document = new Document(PageSize.A5.rotate());
        PdfWriter.getInstance(document, new FileOutputStream(RESULT));
        document.open();
        PdfPTable table = new PdfPTable(2);
        Phrase p = new Phrase(
                "Dr. iText or: How I Learned to Stop Worrying and Love PDF.");
        PdfPCell cell = new PdfPCell(p);
        table.addCell("wrap");
        cell.setNoWrap(false);
        table.addCell(cell);
        table.addCell("no wrap");
        cell.setNoWrap(true);
        table.addCell(cell);
        p = new Phrase(
            "Dr. iText or:\nHow I Learned to Stop Worrying\nand Love PDF.");
        cell = new PdfPCell(p);
        table.addCell("fixed height (more than sufficient)");
        cell.setFixedHeight(72f);
        table.addCell(cell);
        table.addCell("fixed height (not sufficient)");
        cell.setFixedHeight(36f);
        table.addCell(cell);
        table.addCell("minimum height");
        cell = new PdfPCell(new Phrase("Dr. iText"));
        cell.setMinimumHeight(36f);
        table.addCell(cell);
        table.setExtendLastRow(true);
        table.addCell("extend last row");
        table.addCell(cell);
        document.add(table);

        document.close();
    }
}