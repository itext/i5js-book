/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */
package part4.chapter14;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.FontMapper;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

public class Text2ToPdf1 {
    /** The resulting PDF. */
    public static final String RESULT = "results/part4/chapter14/text21.pdf";

    /**
     * Creates a PDF document.
     * @param filename the path to the new PDF document
     * @throws DocumentException 
     * @throws IOException
     */
    public void createPdf(String filename) throws IOException, DocumentException {
    	// step 1
        Document document = new Document(new Rectangle(300, 150));
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
        // step 3
        document.open();
        // step 4
        PdfContentByte canvas = writer.getDirectContent();
        // Create a custom font mapper that forces the use of arial unicode
        FontMapper arialuni = new FontMapper() {
            public BaseFont awtToPdf(Font font) {
                try {
                    return BaseFont.createFont(
                            "c:/windows/fonts/arialuni.ttf",
                            BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            public Font pdfToAwt(BaseFont font, int size) {
                return null;
            }
            
        };
        // Create a Graphics2D object
        Graphics2D g2 = canvas.createGraphics(300, 150, arialuni);
        // Draw text to the Graphics2D
        TextExample2 text = new TextExample2();
        text.setSize(new Dimension(300, 150));
        text.paint(g2);
        g2.dispose();
        // step 5
        document.close();
    }
    /**
     * Main method.
     *
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException
     */
    public static void main(String[] args) throws IOException, DocumentException {
        new Text2ToPdf1().createPdf(RESULT);
    }
}
