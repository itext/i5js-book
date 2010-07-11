/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part4.chapter15;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.TextMarginFinder;

public class ShowTextMargins {

    /** The original document. */
    public static final String PREFACE = "resources/pdfs/preface.pdf";
    /** The new document to which we've added a border rectangle. */
    public static final String RESULT = "results/part4/chapter15/margins.pdf";
    
    /**
     * Parses a PDF and ads a rectangle showing the text margin.
     * @param src the source PDF
     * @param dest the resulting PDF
     */
    public void addMarginRectangle(String src, String dest)
        throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        PdfReaderContentParser parser = new PdfReaderContentParser(reader);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(RESULT));
        TextMarginFinder finder;
        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
            finder = parser.processContent(i, new TextMarginFinder());
            PdfContentByte cb = stamper.getOverContent(i);
            cb.rectangle(finder.getLlx(), finder.getLly(),
                finder.getWidth(), finder.getHeight());
            cb.stroke();
        }
        stamper.close();
    }

    /**
     * Main method.
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException
     */
    public static void main(String[] args) throws IOException, DocumentException {
        new ShowTextMargins().addMarginRectangle(PREFACE, RESULT);
    }
}
