/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part3.chapter10;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

public class ClippingPath {

    /** The resulting PDF file. */
    public static final String RESULT
        = "results/part3/chapter10/clipping_path.pdf";
    /** One of the resources. */
    public static final String RESOURCE
        = "resources/img/bruno_ingeborg.jpg";
    
    /**
     * Creates a PDF document.
     * @param filename the path to the new PDF document
     * @throws    DocumentException 
     * @throws    IOException
     */
    public void createPdf(String filename) throws IOException, DocumentException {
        // step 1
        Document document = new Document();
        // step 2
        PdfWriter writer
          = PdfWriter.getInstance(document, new FileOutputStream(filename));
        // step 3
        document.open();
        // step 4
        Image img = Image.getInstance(RESOURCE);
        float w = img.getScaledWidth();
        float h = img.getScaledHeight();
        PdfTemplate t = writer.getDirectContent().createTemplate(850, 600);
        t.ellipse(0, 0, 850, 600);
        t.clip();
        t.newPath();
        t.addImage(img, w, 0, 0, h, 0, -600);
        Image clipped = Image.getInstance(t);
        clipped.scalePercent(50);
        document.add(clipped);
        // step 5
        document.close();
    }
    
    /**
     * Main method.
     *
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException 
     * @throws DocumentException 
     * @throws IOException
     */
    public static void main(String[] args) throws IOException, DocumentException {
        new ClippingPath().createPdf(RESULT);
    }
}
