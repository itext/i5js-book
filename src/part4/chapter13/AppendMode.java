/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part4.chapter13;

import java.io.FileOutputStream;
import java.io.IOException;

import part1.chapter01.HelloWorld;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class AppendMode {
    /** The resulting PDF. */
    public static final String RESULT
        = "results/part4/chapter13/appended.pdf";

    /**
     * Manipulates a PDF file src with the file dest as result
     * @param src the original PDF
     * @param dest the resulting PDF
     * @throws IOException 
     * @throws DocumentException
     */
    public void manipulatePdf(String src, String dest) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        PdfStamper stamper =
            new PdfStamper(reader, new FileOutputStream(dest), '\0', true);
        PdfContentByte cb = stamper.getUnderContent(1);
        cb.beginText();
        cb.setFontAndSize(BaseFont.createFont(), 12);
        cb.showTextAligned(Element.ALIGN_LEFT, "Hello People!", 36, 770, 0);
        cb.endText();
        stamper.close();
    }
    
    /**
     * Main method.
     *
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException
     */
    public static void main(String[] args) throws IOException, DocumentException {
        new HelloWorld().createPdf(HelloWorld.RESULT);
        new AppendMode().manipulatePdf(HelloWorld.RESULT, RESULT);
    }
}