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
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfWriter;

public class Diacritics1 {

    /** The resulting PDF file. */
    public static final String RESULT
        = "results/part3/chapter11/diacritics1.pdf";
    /** A movie title. */
    public static final String MOVIE
        = "\u0e1f\u0e49\u0e32\u0e17\u0e30\u0e25\u0e32\u0e22\u0e42\u0e08\u0e23";
    /** Movie poster */
    public static final String POSTER
        = "resources/posters/0269217.jpg";
    /** Fonts */
    public static final String[] FONTS = {
        "c:/windows/fonts/angsa.ttf",
        "c:/windows/fonts/arialuni.ttf"
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
        Document document = new Document();
        // step 2
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        // step 3
        document.open();
        // step 4
        BaseFont bf;
        Font font;
        Image img = Image.getInstance(POSTER);
        img.scalePercent(50);
        img.setBorderWidth(18f);
        img.setBorder(Image.BOX);
        img.setBorderColor(GrayColor.GRAYWHITE);
        img.setAlignment(Element.ALIGN_LEFT | Image.TEXTWRAP);
        document.add(img);
        document.add(new Paragraph(
            "Movie title: Tears of the Black Tiger (Thailand)"));
        document.add(new Paragraph("directed by Wisit Sasanatieng"));
        for (int i = 0; i < 2; i++) {
            bf = BaseFont.createFont(FONTS[i], BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            document.add(new Paragraph("Font: " + bf.getPostscriptFontName()));
            font = new Font(bf, 20);
            document.add(new Paragraph(MOVIE, font));
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
        new Diacritics1().createPdf(RESULT);
    }
}
