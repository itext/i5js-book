/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */
package part3.chapter11;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

public class UnicodeExample extends EncodingExample {

    /** The resulting PDF file. */
    public static final String RESULT
        = "results/part3/chapter11/unicode_example.pdf";
    
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
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        // step 3: we open the document
        document.open();
        // step 4
        BaseFont bf;
        for (int i = 0; i < 4; i++) {
            bf = BaseFont.createFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            document.add(new Paragraph("Font: " + bf.getPostscriptFontName()
                    + " with encoding: " + bf.getEncoding()));
            document.add(new Paragraph(MOVIES[i][1]));
            document.add(new Paragraph(MOVIES[i][2]));
            document.add(new Paragraph(MOVIES[i][3], new Font(bf, 12)));
            document.add(Chunk.NEWLINE);
        }
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
    public static void main(String[] args)
        throws IOException, DocumentException {
        new UnicodeExample().createPdf(RESULT);
    }
}
