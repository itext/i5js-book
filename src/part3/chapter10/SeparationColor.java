/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part3.chapter10;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfSpotColor;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.SpotColor;

public class SeparationColor extends DeviceColor {
    
    /** The resulting PDF file. */
    public static final String RESULT
        = "results/part3/chapter10/separation_color.pdf";

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
        PdfContentByte canvas = writer.getDirectContent();
        PdfSpotColor psc_g = new PdfSpotColor(
            "iTextSpotColorGray", new GrayColor(0.9f));
        PdfSpotColor psc_rgb = new PdfSpotColor(
            "iTextSpotColorRGB", new BaseColor(0x64, 0x95, 0xed));
        PdfSpotColor psc_cmyk = new PdfSpotColor(
            "iTextSpotColorCMYK", new CMYKColor(0.3f, .9f, .3f, .1f));

        colorRectangle(canvas, new SpotColor(psc_g, 0.5f), 36, 770, 36, 36);
        colorRectangle(canvas, new SpotColor(psc_rgb, 0.1f), 90, 770, 36, 36);
        colorRectangle(canvas, new SpotColor(psc_rgb, 0.2f), 144, 770, 36, 36); 
        colorRectangle(canvas, new SpotColor(psc_rgb, 0.3f), 198, 770, 36, 36); 
        colorRectangle(canvas, new SpotColor(psc_rgb, 0.4f), 252, 770, 36, 36); 
        colorRectangle(canvas, new SpotColor(psc_rgb, 0.5f), 306, 770, 36, 36); 
        colorRectangle(canvas, new SpotColor(psc_rgb, 0.6f), 360, 770, 36, 36); 
        colorRectangle(canvas, new SpotColor(psc_rgb, 0.7f), 416, 770, 36, 36); 
        colorRectangle(canvas, new SpotColor(psc_cmyk, 0.25f), 470, 770, 36, 36);
        
        canvas.setColorFill(psc_g, 0.5f);
        canvas.rectangle(36, 716, 36, 36);
        canvas.fillStroke();
        canvas.setColorFill(psc_g, 0.9f);
        canvas.rectangle(90, 716, 36, 36);
        canvas.fillStroke();
        canvas.setColorFill(psc_rgb, 0.5f);
        canvas.rectangle(144, 716, 36, 36);
        canvas.fillStroke();
        canvas.setColorFill(psc_rgb, 0.9f);
        canvas.rectangle(198, 716, 36, 36);
        canvas.fillStroke();
        canvas.setColorFill(psc_cmyk, 0.5f);
        canvas.rectangle(252, 716, 36, 36);
        canvas.fillStroke();
        canvas.setColorFill(psc_cmyk, 0.9f);
        canvas.rectangle(306, 716, 36, 36);
        canvas.fillStroke();

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
     * @throws DocumentException 
     * @throws IOException
     */
    public static void main(String[] args) throws IOException, DocumentException {
        new SeparationColor().createPdf(RESULT);
    }
}
