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

public class TransparentImage {

    /** The resulting PDF file. */
    public static final String RESULT
        = "results/part3/chapter10/transparent_image.pdf";
    /** One of the resources. */
    public static final String RESOURCE1
        = "resources/img/bruno.jpg";
    /** One of the resources. */
    public static final String RESOURCE2
        = "resources/img/info.png";
    /** One of the resources. */
    public static final String RESOURCE3
        = "resources/img/1t3xt.gif";
    /** One of the resources. */
    public static final String RESOURCE4
        = "resources/img/logo.gif";


    /**
     * Creates a PDF document.
     * @param filename the path to the new PDF document
     * @throws    DocumentException 
     * @throws    IOException
     */
    public void createPdf(String filename) throws IOException, DocumentException {
        Image img1 = Image.getInstance(RESOURCE1);
        // step 1
        Document document = new Document(img1);
        // step 2
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        // step 3
        document.open();
        // step 4
        img1.setAbsolutePosition(0, 0);
        document.add(img1);
        Image img2 = Image.getInstance(RESOURCE2);
        img2.setAbsolutePosition(0, 260);
        document.add(img2);
        Image img3 = Image.getInstance(RESOURCE3);
        img3.setTransparency(new int[]{ 0x00, 0x10 });
        img3.setAbsolutePosition(0, 0);
        document.add(img3);
        Image img4 = Image.getInstance(RESOURCE4);
        img4.setTransparency(new int[]{ 0xF0, 0xFF });
        img4.setAbsolutePosition(50, 50);
        document.add(img4);

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
        new TransparentImage().createPdf(RESULT);
    }
}
