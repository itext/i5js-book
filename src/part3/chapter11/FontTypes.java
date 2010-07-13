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
import com.itextpdf.text.pdf.draw.LineSeparator;

public class FontTypes {

    /** The resulting PDF file. */
    public static String RESULT
        = "results/part3/chapter11/font_types.pdf";
    /** Some text. */
    public static String TEXT
        = "quick brown fox jumps over the lazy dog\nQUICK BROWN FOX JUMPS OVER THE LAZY DOG";
    
    /** Paths to and encodings of fonts we're going to use in this example */
    public static String[][] FONTS = {
        {BaseFont.HELVETICA, BaseFont.WINANSI},
        {"resources/fonts/cmr10.afm", BaseFont.WINANSI},
        {"resources/fonts/cmr10.pfm", BaseFont.WINANSI},
        {"c:/windows/fonts/ARBLI__.TTF", BaseFont.WINANSI},
        {"c:/windows/fonts/arial.ttf", BaseFont.WINANSI},
        {"c:/windows/fonts/arial.ttf", BaseFont.IDENTITY_H},
        {"resources/fonts/Puritan2.otf", BaseFont.WINANSI},
        {"c:/windows/fonts/msgothic.ttc,0", BaseFont.IDENTITY_H},
        {"KozMinPro-Regular", "UniJIS-UCS2-H"}
    };

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
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        // step 3
        document.open();
        // step 4
        BaseFont bf;
        Font font;
        for (int i = 0; i < FONTS.length; i++) {
            bf = BaseFont.createFont(FONTS[i][0], FONTS[i][1], BaseFont.EMBEDDED);
            document.add(new Paragraph(
                    String.format("Font file: %s with encoding %s", FONTS[i][0], FONTS[i][1])));
            document.add(new Paragraph(
                    String.format("iText class: %s", bf.getClass().getName())));
            font = new Font(bf, 12);
            document.add(new Paragraph(TEXT, font));
            document.add(new LineSeparator(0.5f, 100, null, 0, -5));
        }
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
        new FontTypes().createPdf(RESULT);
    }
}
