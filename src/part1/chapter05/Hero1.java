/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part1.chapter05;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

public class Hero1 {

    /** The resulting PDF file. */
    public static final String RESULT = "results/part1/chapter05/hero1.pdf";
    /** Path to the resources. */
    public static final String RESOURCE = "resources/txt/hero.txt";

    /**
     * Creates a PDF document.
     * @param filename the path to the new PDF document
     * @throws    DocumentException 
     * @throws    IOException
     */
    public void createPdf(String filename)
        throws IOException, DocumentException {
    	// step 1
        Rectangle rect = new Rectangle(-1192, -1685, 1192, 1685);
        Document document = new Document(rect);
        // step 2
        PdfWriter writer =
            PdfWriter.getInstance(document, new FileOutputStream(filename));
        // step 3
        document.open();
        // step 4
        PdfContentByte content = writer.getDirectContent();
        PdfTemplate template = createTemplate(content, rect, 4);
        content.addTemplate(template, -1192, -1685);
        content.moveTo(-595, 0);
        content.lineTo(595, 0);
        content.moveTo(0, -842);
        content.lineTo(0, 842);
        content.stroke();
        // step 5
        document.close();
    }
    
    /**
     * Creates a template based on a stream of PDF syntax.
     * @param content The direct content
     * @param rect The dimension of the templare
     * @param factor A magnification factor
     * @return A PdfTemplate
     * @throws IOException
     */
    public PdfTemplate createTemplate(PdfContentByte content, Rectangle rect, int factor)
        throws IOException {
        PdfTemplate template = content.createTemplate(rect.getWidth(), rect.getHeight());
        template.concatCTM(factor, 0, 0, factor, 0, 0);
        FileReader reader = new FileReader(RESOURCE);
        int c;
        while ((c = reader.read()) > -1) {
            template.setLiteral((char)c);
        }
        return template;
    }

    /**
     * Main method.
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException
     */
    public static void main(String[] args)
        throws IOException, DocumentException {
        new Hero1().createPdf(RESULT);
    }
}
