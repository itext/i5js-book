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

public class TTCExample {

    /** The resulting PDF file. */
    public static final String RESULT
        = "results/part3/chapter11/ttc_example.pdf";
    /** The path to the font. */
    public static final String FONT = "c:/windows/fonts/msgothic.ttc";
    
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
        Font font;
        String[] names = BaseFont.enumerateTTCNames(FONT);
        for (int i = 0; i < names.length; i++) {
            bf = BaseFont.createFont(String.format("%s,%s", FONT, i),
                BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            font = new Font(bf, 12);
            document.add(new Paragraph("font " + i + ": " + names[i], font));
            document.add(new Paragraph("Rash\u00f4mon", font));
            document.add(new Paragraph("Directed by Akira Kurosawa", font));
            document.add(new Paragraph("\u7f85\u751f\u9580", font));
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
    public static void main(String[] args) throws IOException, DocumentException {
        new TTCExample().createPdf(RESULT);
    }
}
