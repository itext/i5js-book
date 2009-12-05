/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part1.chapter03;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

public class GraphicsStateStack {
    
    /** The resulting PDF. */
    public static final String RESULT = "results/part1/chapter03/graphics_state.pdf";
    
    public static void main(String[] args) {
        Document document = new Document(new Rectangle(200, 120));
        try {
            PdfWriter writer = PdfWriter.getInstance(document,
                new FileOutputStream(RESULT));
            document.open();
            PdfContentByte canvas = writer.getDirectContent();
            canvas.setRGBColorFill(0xFF, 0x45, 0x00);
            canvas.rectangle(10, 10, 60, 60);
            canvas.fill();
            canvas.saveState();
            canvas.setLineWidth(3);
            canvas.setRGBColorFill(0x8B, 0x00, 0x00);
            canvas.rectangle(40, 20, 60, 60);
            canvas.fillStroke();
            canvas.saveState();
            canvas.concatCTM(1, 0, 0.1f, 1, 0, 0);
            canvas.setRGBColorStroke(0xFF, 0x45, 0x00);
            canvas.setRGBColorFill(0xFF, 0xD7, 0x00);
            canvas.rectangle(70, 30, 60, 60);
            canvas.fillStroke();
            canvas.restoreState();
            canvas.rectangle(100, 40, 60, 60);
            canvas.stroke();
            canvas.restoreState();
            canvas.rectangle(130, 50, 60, 60);
            canvas.fillStroke();
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        document.close();
    }
}
