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
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class NestedTable {

    /** The resulting PDF file. */
    public static final String RESULT
        = "results/part1/chapter04/nested_table.pdf";

    /**
     * Main method.
     * @param args no arguments needed
     * @throws DocumentException 
     * @throws IOException
     */
    public static void main(String[] args)
        throws DocumentException, IOException {
    	// step 1
        Document document = new Document();
        // step 2
        PdfWriter.getInstance(document, new FileOutputStream(RESULT));
        // step 3
        document.open();
        // step 4
        PdfPTable table = new PdfPTable(4);
        PdfPTable nested1 = new PdfPTable(2);
        nested1.addCell("1.1");
        nested1.addCell("1.2");
        PdfPTable nested2 = new PdfPTable(1);
        nested2.addCell("12.1");
        nested2.addCell("12.2");
        for (int k = 0; k < 16; ++k) {
            if (k == 1) {
                table.addCell(nested1);
            } else if (k == 12) {
                table.addCell(new PdfPCell(nested2));
            } else {
                table.addCell("cell " + k);
            }
        }
        document.add(table);
        // step 5
        document.close();
    }
}