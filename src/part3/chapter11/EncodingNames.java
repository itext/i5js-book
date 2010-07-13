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
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

public class EncodingNames {

    /** The resulting PDF file. */
    public static final String RESULT
        = "results/part3/chapter11/font_encodings.pdf";
    /** The path to the font. */
    public static final String[] FONT = {
        "c:/windows/fonts/ARBLI__.TTF",
        "resources/fonts/Puritan2.otf",
        "c:/windows/fonts/arialbd.ttf"
    };
    

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
        Document document = new Document(new Rectangle(350, 842));
        // step 2
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        // step 3
        document.open();
        // step 4
        showEncodings(document, FONT[0]);
        showEncodings(document, FONT[1]);
        document.newPage();
        showEncodings(document, FONT[2]);
        // step 5
        document.close();
    }
    
    /**
     * Writes the available encodings of a font to the document.
     * @param document the document to which the encodings have to be written
     * @param font     the font
     * @throws DocumentException
     * @throws IOException
     */
    public void showEncodings(Document document, String font) throws DocumentException, IOException {
        BaseFont bf = BaseFont.createFont(font, BaseFont.WINANSI, BaseFont.EMBEDDED);
        document.add(new Paragraph("PostScript name: " + bf.getPostscriptFontName()));
        document.add(new Paragraph("Available code pages:"));
        String[] encoding = bf.getCodePagesSupported();
        for (int i = 0; i < encoding.length; i++) {
            document.add(new Paragraph("encoding[" + i + "] = " + encoding[i]));
        }
        document.add(Chunk.NEWLINE);
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
        new EncodingNames().createPdf(RESULT);
    }
}