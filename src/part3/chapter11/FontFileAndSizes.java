/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */
package part3.chapter11;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

public class FontFileAndSizes {

    /** The names of the resulting PDF files. */
    public static final String[] RESULT = {
        "results/part3/chapter11/font_not_embedded.pdf",
        "results/part3/chapter11/font_embedded.pdf",
        "results/part3/chapter11/font_embedded_less_glyphs.pdf",
        "results/part3/chapter11/font_compressed.pdf",
        "results/part3/chapter11/font_full.pdf"
    };
    /** The path to the font. */
    public static final String FONT = "c:/windows/fonts/arial.ttf";
    /** Some text. */
    public static String TEXT
        = "quick brown fox jumps over the lazy dog";
    /** Some text. */
    public static String OOOO
        = "ooooo ooooo ooo ooooo oooo ooo oooo ooo";
    
    /**
     * Creates a PDF document.
     * @param filename the path to the new PDF document
     * @throws DocumentException 
     * @throws IOException 
     * @throws    DocumentException 
     * @throws    IOException
     */
    public void createPdf(String filename, BaseFont bf, String text)
        throws IOException, DocumentException {
        // step 1
        Document document = new Document();
        // step 2
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        // step 3
        document.open();
        // step 4
        document.add(new Paragraph(text, new Font(bf, 12)));
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
        FontFileAndSizes ffs = new FontFileAndSizes();
        BaseFont bf;
        bf = BaseFont.createFont(FONT, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
        ffs.createPdf(RESULT[0], bf, TEXT);
        bf = BaseFont.createFont(FONT, BaseFont.WINANSI, BaseFont.EMBEDDED);
        ffs.createPdf(RESULT[1], bf, TEXT);
        ffs.createPdf(RESULT[2], bf, OOOO);
        bf = BaseFont.createFont(FONT, BaseFont.WINANSI, BaseFont.EMBEDDED);
        bf.setCompressionLevel(9);
        ffs.createPdf(RESULT[3], bf, TEXT);
        bf = BaseFont.createFont(FONT, BaseFont.WINANSI, BaseFont.EMBEDDED);
        bf.setSubset(false);
        ffs.createPdf(RESULT[4], bf, TEXT);
    }
}
