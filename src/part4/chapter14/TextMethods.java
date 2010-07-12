/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */
package part4.chapter14;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

public class TextMethods {

    /** The resulting PDF. */
    public static final String RESULT = "results/part4/chapter14/text_methods.pdf";

    /**
     * Creates a PDF document.
     * @param filename the path to the new PDF document
     * @throws DocumentException 
     * @throws IOException
     */
    public void createPdf(String filename) throws IOException, DocumentException {
        // step 1
        Document document = new Document();
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document,
                new FileOutputStream(filename));
        // step 3
        document.open();
        // step 4
        // draw helper lines
        PdfContentByte cb = writer.getDirectContent();
        cb.setLineWidth(0f);
        cb.moveTo(150, 600);
        cb.lineTo(150, 800);
        cb.moveTo(50, 760);
        cb.lineTo(250, 760);
        cb.moveTo(50, 700);
        cb.lineTo(250, 700);
        cb.moveTo(50, 640);
        cb.lineTo(250, 640);
        cb.stroke();
        // draw text
        String text = "AWAY again ";
        BaseFont bf = BaseFont.createFont();
        cb.beginText();
        cb.setFontAndSize(bf, 12);
        cb.setTextMatrix(50, 800);
        cb.showText(text);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, text + " Center", 150, 760, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_RIGHT, text + " Right", 150, 700, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, text + " Left", 150, 640, 0);
        cb.showTextAlignedKerned(PdfContentByte.ALIGN_LEFT, text + " Left", 150, 628, 0);
        cb.setTextMatrix(0, 1, -1, 0, 300, 600);
        cb.showText("Position 300,600, rotated 90 degrees.");
        for (int i = 0; i < 360; i += 30) {
            cb.showTextAligned(PdfContentByte.ALIGN_LEFT, text, 400, 700, i);
        }
        cb.endText();
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
        new TextMethods().createPdf(RESULT);
    }

}
