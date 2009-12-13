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
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class ColumnWidths {

    /** The resulting PDF file. */
    public static final String RESULT = "results/part1/chapter04/column_widths.pdf";
    
    /**
     * Creates a PDF with five tables.
     * @param    filename the name of the PDF file that will be created.
     * @throws    DocumentException 
     * @throws    IOException
     */
    public void createPdf(String filename)
        throws IOException, DocumentException {
    	// step 1
        Document document = new Document();
        // step 2
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        // step 3
        document.open();
        // step 4
        PdfPTable table = createTable1();
        document.add(table);
        table = createTable2();
        table.setSpacingBefore(5);
        table.setSpacingAfter(5);
        document.add(table);
        table = createTable3();
        document.add(table);
        table = createTable4();
        table.setSpacingBefore(5);
        table.setSpacingAfter(5);
        document.add(table);
        table = createTable5();
        document.add(table);
        // step 5
        document.close();
    }
    
    /**
     * Creates a table; widths are set with setWidths().
     * @return a PdfPTable
     * @throws DocumentException
     */
    public static PdfPTable createTable1() throws DocumentException {
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(288 / 5.23f);
        table.setWidths(new int[]{2, 1, 1});
        PdfPCell cell;
        cell = new PdfPCell(new Phrase("Table 1"));
        cell.setColspan(3);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Cell with rowspan 2"));
        cell.setRowspan(2);
        table.addCell(cell);
        table.addCell("row 1; cell 1");
        table.addCell("row 1; cell 2");
        table.addCell("row 2; cell 1");
        table.addCell("row 2; cell 2");
        return table;
    }
    
    /**
     * Creates a table; widths are set with setWidths().
     * @return a PdfPTable
     * @throws DocumentException
     */
    public static PdfPTable createTable2() throws DocumentException {
        PdfPTable table = new PdfPTable(3);
        table.setTotalWidth(288);
        table.setLockedWidth(true);
        table.setWidths(new float[]{2, 1, 1});
        PdfPCell cell;
        cell = new PdfPCell(new Phrase("Table 2"));
        cell.setColspan(3);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Cell with rowspan 2"));
        cell.setRowspan(2);
        table.addCell(cell);
        table.addCell("row 1; cell 1");
        table.addCell("row 1; cell 2");
        table.addCell("row 2; cell 1");
        table.addCell("row 2; cell 2");
        return table;
    }
    
    /**
     * Creates a table; widths are set in the constructor.
     * @return a PdfPTable
     * @throws DocumentException
     */
    public static PdfPTable createTable3() throws DocumentException {
        PdfPTable table = new PdfPTable(new float[]{ 2, 1, 1 });
        table.setWidthPercentage(55.067f);
        PdfPCell cell;
        cell = new PdfPCell(new Phrase("Table 3"));
        cell.setColspan(3);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Cell with rowspan 2"));
        cell.setRowspan(2);
        table.addCell(cell);
        table.addCell("row 1; cell 1");
        table.addCell("row 1; cell 2");
        table.addCell("row 2; cell 1");
        table.addCell("row 2; cell 2");
        return table;
    }
    
    /**
     * Creates a table; widths are set with special setWidthPercentage() method.
     * @return a PdfPTable
     * @throws DocumentException
     */
    public static PdfPTable createTable4() throws DocumentException {
        PdfPTable table = new PdfPTable(3);
        Rectangle rect = new Rectangle(523, 770);
        table.setWidthPercentage(new float[]{ 144, 72, 72 }, rect);
        PdfPCell cell;
        cell = new PdfPCell(new Phrase("Table 4"));
        cell.setColspan(3);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Cell with rowspan 2"));
        cell.setRowspan(2);
        table.addCell(cell);
        table.addCell("row 1; cell 1");
        table.addCell("row 1; cell 2");
        table.addCell("row 2; cell 1");
        table.addCell("row 2; cell 2");
        return table;
    }
    
    /**
     * Creates a table; widths are set with setTotalWidth().
     * @return a PdfPTable
     * @throws DocumentException
     */
    public static PdfPTable createTable5() throws DocumentException {
        PdfPTable table = new PdfPTable(3);
        table.setTotalWidth(new float[]{ 144, 72, 72 });
        table.setLockedWidth(true);
        PdfPCell cell;
        cell = new PdfPCell(new Phrase("Table 5"));
        cell.setColspan(3);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Cell with rowspan 2"));
        cell.setRowspan(2);
        table.addCell(cell);
        table.addCell("row 1; cell 1");
        table.addCell("row 1; cell 2");
        table.addCell("row 2; cell 1");
        table.addCell("row 2; cell 2");
        return table;
    }

    /**
     * Main method.
     * @param args no arguments needed
     * @throws DocumentException 
     * @throws IOException
     */
    public static void main(String[] args) throws IOException, DocumentException {
        new ColumnWidths().createPdf(RESULT);
    }
}
