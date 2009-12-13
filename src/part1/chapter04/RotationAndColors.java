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
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.BaseColor;

public class RotationAndColors {
    /** The resulting PDF file. */
    public static final String RESULT
        = "results/part1/chapter04/rotation_colors.pdf";

    /**
     * Main method.
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException
     */
    public static void main(String[] args)
        throws DocumentException, IOException {
        // step 1
        Document document = new Document(PageSize.A4.rotate());
        // step 2
        PdfWriter.getInstance(document, new FileOutputStream(RESULT));
        // step 3
        document.open();
        // step 4
        PdfPTable table = new PdfPTable(4);
        table.setWidths(new int[]{ 1, 3, 3, 3 });
        table.setWidthPercentage(100);
        PdfPCell cell;
        // row 1, cell 1
        cell = new PdfPCell(new Phrase("COLOR"));
        cell.setRotation(90);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        table.addCell(cell);
        // row 1, cell 2
        cell = new PdfPCell(new Phrase("red / no borders"));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setBackgroundColor(BaseColor.RED);
        table.addCell(cell);
        // row 1, cell 3
        cell = new PdfPCell(new Phrase("green / black bottom border"));
        cell.setBorder(Rectangle.BOTTOM);
        cell.setBorderColorBottom(BaseColor.BLACK);
        cell.setBorderWidthBottom(10f);
        cell.setBackgroundColor(BaseColor.GREEN);
        table.addCell(cell);
        // row 1, cell 4
        cell = new PdfPCell(new Phrase(
                "cyan / blue top border + padding"));
        cell.setBorder(Rectangle.TOP);
        cell.setUseBorderPadding(true);
        cell.setBorderWidthTop(5f);
        cell.setBorderColorTop(BaseColor.BLUE);
        cell.setBackgroundColor(BaseColor.CYAN);
        table.addCell(cell);
        // row 2, cell 1
        cell = new PdfPCell(new Phrase("GRAY"));
        cell.setRotation(90);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);
        // row 2, cell 2
        cell = new PdfPCell(new Phrase("0.6"));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setGrayFill(0.6f);
        table.addCell(cell);
        // row 2, cell 3
        cell = new PdfPCell(new Phrase("0.75"));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setGrayFill(0.75f);
        table.addCell(cell);
        // row 2, cell 4
        cell = new PdfPCell(new Phrase("0.9"));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setGrayFill(0.9f);
        table.addCell(cell);
        // row 3, cell 1
        cell = new PdfPCell(new Phrase("BORDERS"));
        cell.setRotation(90);
        cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        table.addCell(cell);
        // row 3, cell 2
        cell = new PdfPCell(new Phrase("different borders"));
        cell.setBorderWidthLeft(16f);
        cell.setBorderWidthBottom(12f);
        cell.setBorderWidthRight(8f);
        cell.setBorderWidthTop(4f);
        cell.setBorderColorLeft(BaseColor.RED);
        cell.setBorderColorBottom(BaseColor.ORANGE);
        cell.setBorderColorRight(BaseColor.YELLOW);
        cell.setBorderColorTop(BaseColor.GREEN);
        table.addCell(cell);
        // row 3, cell 3
        cell = new PdfPCell(new Phrase("with correct padding"));
        cell.setUseBorderPadding(true);
        cell.setBorderWidthLeft(16f);
        cell.setBorderWidthBottom(12f);
        cell.setBorderWidthRight(8f);
        cell.setBorderWidthTop(4f);
        cell.setBorderColorLeft(BaseColor.RED);
        cell.setBorderColorBottom(BaseColor.ORANGE);
        cell.setBorderColorRight(BaseColor.YELLOW);
        cell.setBorderColorTop(BaseColor.GREEN);
        table.addCell(cell);
        // row 3, cell 4
        cell = new PdfPCell(new Phrase("red border"));
        cell.setBorderWidth(8f);
        cell.setBorderColor(BaseColor.RED);
        table.addCell(cell);
        document.add(table);
        // step 5
        document.close();
    }
}