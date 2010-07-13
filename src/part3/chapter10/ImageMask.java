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
import com.itextpdf.text.pdf.PdfWriter;

public class ImageMask {

    /** The resulting PDF file. */
    public static final String RESULT1
        = "results/part3/chapter10/hardmask.pdf";
    /** The resulting PDF file. */
    public static final String RESULT2
        = "results/part3/chapter10/softmask.pdf";
    /** One of the resources. */
    public static final String RESOURCE
        = "resources/img/bruno.jpg";

    /**
     * Creates a PDF document.
     * @param filename the path to the new PDF document
     * @throws    DocumentException 
     * @throws    IOException
     */
    public void createPdf(String filename, Image mask) throws IOException, DocumentException {
        // step 1
        Document document = new Document();
        // step 2
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        // step 3
        document.open();
        // step 4
        Image img = Image.getInstance(RESOURCE);
        img.setImageMask(mask);
        document.add(img);
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
        byte circledata[] = { (byte) 0x3c, (byte) 0x7e, (byte) 0xff,
                (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0x7e,
                (byte) 0x3c };
        Image mask = Image.getInstance(8, 8, 1, 1, circledata);
        mask.makeMask();
        mask.setInverted(true);
        new ImageMask().createPdf(RESULT1, mask);
        byte gradient[] = new byte[256];
        for (int i = 0; i < 256; i++)
            gradient[i] = (byte) i;
        mask = Image.getInstance(256, 1, 1, 8, gradient);
        mask.makeMask();
        new ImageMask().createPdf(RESULT2, mask);
    }
}
