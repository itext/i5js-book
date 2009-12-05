/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part1.chapter05;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

public class Hero2 extends Hero1 {

    public static final String RESULT = "results/part1/chapter05/hero2.pdf";
    
    public static void main(String[] args) throws IOException, DocumentException {
        new Hero2().createPdf(RESULT);
    }
    
    public void createPdf(String filename) throws IOException, DocumentException {
        float w = PageSize.A4.getWidth();
        float h = PageSize.A4.getHeight();
        Rectangle rect = new Rectangle(-2*w, -2*h, 2*w, 2*h);
        Rectangle crop = new Rectangle(-2 * w, h, -w, 2 * h);
        Document document = new Document(rect);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
        writer.setCropBoxSize(crop);
        document.open();
        
        PdfContentByte content = writer.getDirectContent();
        PdfTemplate template = createTemplate(content, rect, 4);
        
        float adjust;
        while(true) {
            content.addTemplate(template, -2*w, -2*h);
            adjust = crop.getRight() + w;
            if (adjust > 2 * w) {
                adjust = crop.getBottom() - h;
                if (adjust < - 2 * h)
                    break;
                crop = new Rectangle(-2*w, adjust, -w, crop.getBottom());
            }
            else {
                crop = new Rectangle(crop.getRight(), crop.getBottom(), adjust, crop.getTop());
            }
            writer.setCropBoxSize(crop);
            document.newPage();
        }
        document.close();
    }
}
