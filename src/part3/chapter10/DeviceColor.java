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
import com.itextpdf.text.pdf.PdfWriter;

public class DeviceColor {

    /** The resulting PDF file. */
    public static final String RESULT
        = "results/part3/chapter10/device_color.pdf";
    
    /**
     * Draws a colored rectangle.
     * @param canvas the canvas to draw on
     * @param color the Color
     * @param x the X coordinate
     * @param y the Y coordinate
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     */
    public void colorRectangle(PdfContentByte canvas,
        BaseColor color, float x, float y, float width, float height) {
        canvas.saveState();
        canvas.setColorFill(color);
        canvas.rectangle(x, y, width, height);
        canvas.fillStroke();
        canvas.restoreState();
    }

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
        // RGB Colors
        colorRectangle(canvas, new BaseColor(0x00, 0x00, 0x00), 36, 770, 36, 36);
        colorRectangle(canvas, new BaseColor(0x00, 0x00, 0xFF), 90, 770, 36, 36);
        colorRectangle(canvas, new BaseColor(0x00, 0xFF, 0x00), 144, 770, 36, 36);
        colorRectangle(canvas, new BaseColor(0xFF, 0x00, 0x00), 198, 770, 36, 36);
        colorRectangle(canvas, new BaseColor(0f, 1f, 1f), 252, 770, 36, 36);
        colorRectangle(canvas, new BaseColor(1f, 0f, 1f), 306, 770, 36, 36);
        colorRectangle(canvas, new BaseColor(1f, 1f, 0f), 360, 770, 36, 36);
        colorRectangle(canvas, BaseColor.BLACK, 416, 770, 36, 36);
        colorRectangle(canvas, BaseColor.LIGHT_GRAY, 470, 770, 36, 36);
        // CMYK Colors
        colorRectangle(canvas, new CMYKColor(0x00, 0x00, 0x00, 0x00), 36, 716, 36, 36);
        colorRectangle(canvas, new CMYKColor(0x00, 0x00, 0xFF, 0x00), 90, 716, 36, 36);
        colorRectangle(canvas, new CMYKColor(0x00, 0x00, 0xFF, 0xFF), 144, 716, 36, 36);
        colorRectangle(canvas, new CMYKColor(0x00, 0xFF, 0x00, 0x00), 198, 716, 36, 36);
        colorRectangle(canvas, new CMYKColor(0f, 1f, 0f, 0.5f), 252, 716, 36, 36);
        colorRectangle(canvas, new CMYKColor(1f, 0f, 0f, 0f), 306, 716, 36, 36);
        colorRectangle(canvas, new CMYKColor(1f, 0f, 0f, 0.5f), 360, 716, 36, 36);
        colorRectangle(canvas, new CMYKColor(1f, 0f, 0f, 1f), 416, 716, 36, 36);
        colorRectangle(canvas, new CMYKColor(0f, 0f, 0f, 1f), 470, 716, 36, 36);
        // Gray color
        colorRectangle(canvas, new GrayColor(0x20), 36, 662, 36, 36);
        colorRectangle(canvas, new GrayColor(0x40), 90, 662, 36, 36);
        colorRectangle(canvas, new GrayColor(0x60), 144, 662, 36, 36);
        colorRectangle(canvas, new GrayColor(0.5f), 198, 662, 36, 36);
        colorRectangle(canvas, new GrayColor(0.625f), 252, 662, 36, 36);
        colorRectangle(canvas, new GrayColor(0.75f), 306, 662, 36, 36);
        colorRectangle(canvas, new GrayColor(0.825f), 360, 662, 36, 36);
        colorRectangle(canvas, GrayColor.GRAYBLACK, 416, 662, 36, 36);
        colorRectangle(canvas, GrayColor.GRAYWHITE, 470, 662, 36, 36);
        // Alternative ways to color the rectangle
        canvas.setRGBColorFill(0x00, 0x80, 0x80);
        canvas.rectangle(36, 608, 36, 36);
        canvas.fillStroke();
        canvas.setRGBColorFillF(0.5f, 0.25f, 0.60f);
        canvas.rectangle(90, 608, 36, 36);
        canvas.fillStroke();
        canvas.setGrayFill(0.5f);
        canvas.rectangle(144, 608, 36, 36);
        canvas.fillStroke();
        canvas.setCMYKColorFill(0xFF, 0xFF, 0x00, 0x80);
        canvas.rectangle(198, 608, 36, 36);
        canvas.fillStroke();
        canvas.setCMYKColorFillF(0f, 1f, 1f, 0.5f);
        canvas.rectangle(252, 608, 36, 36);
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
        new DeviceColor().createPdf(RESULT);
    }
}
