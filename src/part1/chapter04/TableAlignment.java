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
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class TableAlignment extends MyFirstTable {

    public static final String RESULT = "results/part1/chapter04/table_alignment.pdf";
    
    public static void main(String[] args) throws IOException, DocumentException {
        new TableAlignment().createPdf(RESULT);
    }
    
    /**
     * Creates a PDF with information about the movies
     * @param    filename the name of the PDF file that will be created.
     * @throws    DocumentException 
     * @throws    IOException 
     * @throws    SQLException
     */
    public void createPdf(String filename)
        throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        document.open();
        PdfPTable table = createFirstTable();
        table.setWidthPercentage(50);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        document.add(table);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);
        document.add(table);
        table.setHorizontalAlignment(Element.ALIGN_RIGHT);
        document.add(table);
        document.close();
    }
}
