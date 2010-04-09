/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part3.chapter10;

import java.awt.Color;
import java.awt.Toolkit;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfWriter;

public class TransparentAwt {

    /** The resulting PDF file. */
    public static final String RESULT
        = "results/part3/chapter10/transparent_hitchcock.pdf";
    /** One of the resources. */
    public static final String RESOURCE
        = "resources/img/hitchcock.gif";

    /**
     * Creates a PDF document.
     * @param filename the path to the new PDF document
     * @throws    DocumentException 
     * @throws    IOException
     */
    public void createPdf(String filename) throws IOException, DocumentException {
        // step 1
        Rectangle r = new Rectangle(PageSize.A4);
        r.setBackgroundColor(new GrayColor(0.8f));
        Document document = new Document(r);
        // step 2
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        // step 3
        document.open();
        // step 4
        java.awt.Image awtImage = Toolkit.getDefaultToolkit().createImage(RESOURCE);
        document.add(new Paragraph("Hitchcock in Red."));
        Image img1 = Image.getInstance(awtImage, null);
        document.add(img1);
        document.add(new Paragraph("Hitchcock in Black and White."));
        Image img2 = Image.getInstance(awtImage, null, true);
        document.add(img2);
        document.newPage();
        document.add(new Paragraph("Hitchcock in Red and Yellow."));
        Image img3 = Image.getInstance(awtImage, new Color(0xFF, 0xFF, 0x00));
        document.add(img3);
        document.add(new Paragraph("Hitchcock in Black and White."));
        Image img4 = Image.getInstance(awtImage, new Color(0xFF, 0xFF, 0x00), true);
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
        new TransparentAwt().createPdf(RESULT);
    }
}
