/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part1.chapter05;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

public class Hero3 extends Hero1 {

    /** The resulting PDF file. */
    public static final String RESULT = "results/part1/chapter05/hero3.pdf";

    /**
     * Creates a PDF document.
     * @param filename the path to the new PDF document
     * @throws    DocumentException 
     * @throws    IOException
     */
    public void createPdf(String filename) throws IOException, DocumentException {
    	// step 1
        Document document = new Document(PageSize.A4);
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
        Rectangle art = new Rectangle(50, 50, 545, 792);
        writer.setBoxSize("art", art);
        // step 3
        document.open();
        // step 4
        PdfContentByte content = writer.getDirectContent();
        PdfTemplate template = createTemplate(content, PageSize.A4, 1);
        content.addTemplate(template, 0, 0);
        // step 5
        document.close();
    }

    /**
     * Main method.
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException
     */
    public static void main(String[] args)
        throws IOException, DocumentException {
        new Hero3().createPdf(RESULT);
    }
}
