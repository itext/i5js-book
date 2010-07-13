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

public class RawImage {

    /** The resulting PDF file. */
    public static final String RESULT
        = "results/part3/chapter10/gradient.pdf";
    
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
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        // step 3
        document.open();
        // step 4
        // Image in color space DeviceGray
        byte gradient[] = new byte[256];
        for (int i = 0; i < 256; i++)
            gradient[i] = (byte) i;
        Image img1 = Image.getInstance(256, 1, 1, 8, gradient);
        img1.scaleAbsolute(256, 50);
        document.add(img1);
        // Image in color space RGB
        byte cgradient[] = new byte[256 * 3];
        for (int i = 0; i < 256; i++) {
            cgradient[i * 3] = (byte) (255 - i);
            cgradient[i * 3 + 1] = (byte) (255 - i);
            cgradient[i * 3 + 2] = (byte) i;
        }
        Image img2 = Image.getInstance(256, 1, 3, 8, cgradient);
        img2.scaleAbsolute(256, 50);
        document.add(img2);
        Image img3 = Image.getInstance(16, 16, 3, 8, cgradient);
        img3.scaleAbsolute(64, 64);
        document.add(img3);
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
        new RawImage().createPdf(RESULT);
    }
}
