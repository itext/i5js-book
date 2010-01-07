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
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PatternColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPatternPainter;
import com.itextpdf.text.pdf.PdfWriter;

public class TilingPatternColor extends DeviceColor {

    /** The resulting PDF file. */
    public static final String RESULT
        = "results/part3/chapter10/tiling_pattern.pdf";

    /** An image that will be used for a pattern color. */
    public static final String RESOURCE
        = "resources/img/info.png";

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
        PdfPatternPainter square
            = canvas.createPattern(15, 15);
        square.setColorFill(new BaseColor(0xFF, 0xFF, 0x00));
        square.setColorStroke(new BaseColor(0xFF, 0x00, 0x00));
        square.rectangle(5, 5, 5, 5);
        square.fillStroke();
        
        PdfPatternPainter ellipse
            = canvas.createPattern(15, 10, 20, 25);
        ellipse.setColorFill(new BaseColor(0xFF, 0xFF, 0x00));
        ellipse.setColorStroke(new BaseColor(0xFF, 0x00, 0x00));
        ellipse.ellipse(2f, 2f, 13f, 8f);
        ellipse.fillStroke();
        
        PdfPatternPainter circle
            = canvas.createPattern(15, 15, 10, 20, BaseColor.BLUE);
        circle.circle(7.5f, 7.5f, 2.5f);
        circle.fill();
        
        PdfPatternPainter line
            = canvas.createPattern(5, 10, null);
        line.setLineWidth(1);
        line.moveTo(3, -1);
        line.lineTo(3, 11);
        line.stroke();
        
        Image img = Image.getInstance(RESOURCE);
        img.scaleAbsolute(20, 20);
        img.setAbsolutePosition(0, 0);
        PdfPatternPainter img_pattern
            = canvas.createPattern(20, 20, 20, 20);
        img_pattern.addImage(img);
        img_pattern.setPatternMatrix(-0.5f, 0f, 0f, 0.5f, 0f, 0f);
        
        colorRectangle(canvas, new PatternColor(square), 36, 696, 126, 126);
        colorRectangle(canvas, new PatternColor(ellipse), 180, 696, 126, 126);
        colorRectangle(canvas, new PatternColor(circle), 324, 696, 126, 126);
        colorRectangle(canvas, new PatternColor(line), 36, 552, 126, 126);
        colorRectangle(canvas, new PatternColor(img_pattern), 36, 408, 126, 126);

        canvas.setPatternFill(line, BaseColor.RED);
        canvas.ellipse(180, 552, 306, 678);
        canvas.fillStroke();
        canvas.setPatternFill(circle, BaseColor.GREEN);
        canvas.ellipse(324, 552, 450, 678);
        canvas.fillStroke();
        
        canvas.setPatternFill(img_pattern);
        canvas.ellipse(180, 408, 450, 534);
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
     */
    public static void main(String[] args) throws IOException, DocumentException {
        new TilingPatternColor().createPdf(RESULT);
    }
}
