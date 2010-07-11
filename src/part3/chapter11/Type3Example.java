/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */
package part3.chapter11;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.Type3Font;

public class Type3Example {
    
    /** The resulting PDF file. */
    public static final String RESULT
        = "results/part3/chapter11/type3_example.pdf";

    /**
     * Creates a PDF document.
     * @param filename the path to the new PDF document
     * @throws DocumentException 
     * @throws IOException 
     * @throws    DocumentException 
     * @throws    IOException
     */
    public void createPdf(String filename) throws IOException, DocumentException {
        // step 1
        Document document = new Document();
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
        // step 3
        document.open();
        // step 4
        Type3Font t3 = new Type3Font(writer, true);
        PdfContentByte d = t3.defineGlyph('D', 600, 0, 0, 600, 700);
        d.setColorStroke(new BaseColor(0xFF, 0x00, 0x00));
        d.setColorFill(new GrayColor(0.7f));
        d.setLineWidth(100);
        d.moveTo(5, 5);
        d.lineTo(300, 695);
        d.lineTo(595, 5);
        d.closePathFillStroke();
        PdfContentByte s = t3.defineGlyph('S', 600, 0, 0, 600, 700);
        s.setColorStroke(new BaseColor(0x00, 0x80, 0x80));
        s.setLineWidth(100);
        s.moveTo(595,5);
        s.lineTo(5, 5);
        s.lineTo(300, 350);
        s.lineTo(5, 695);
        s.lineTo(595, 695);
        s.stroke();
        Font f = new Font(t3, 12);
        Paragraph p = new Paragraph();
        p.add("This is a String with a Type3 font that contains a fancy Delta (");
        p.add(new Chunk("D", f));
        p.add(") and a custom Sigma (");
        p.add(new Chunk("S", f));
        p.add(").");
        document.add(p);
        // step 5: we close the document
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
        new Type3Example().createPdf(RESULT);
    }
}
