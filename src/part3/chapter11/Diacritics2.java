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


public class Diacritics2 {

    /** The resulting PDF file. */
    public static final String RESULT
        = "results/part3/chapter11/diacritics2.pdf";
    /** A movie title. */
    public static final String MOVIE
        = "Tomten \u00a8ar far till alla barnen";
    /** Fonts */
    public static final String[] FONTS = {
        "c:/windows/fonts/arial.ttf",
        "c:/windows/fonts/cour.ttf"
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
        document.add(new Paragraph("Movie title: In Bed With Santa (Sweden)"));
        document.add(new Paragraph("directed by Kjell Sundvall"));
        BaseFont bf = BaseFont.createFont(FONTS[0], BaseFont.CP1252, BaseFont.EMBEDDED);
        Font font = new Font(bf, 12);
        bf.setCharAdvance('\u00a8', -100);
        document.add(new Paragraph(MOVIE, font));
        bf = BaseFont.createFont(FONTS[1], BaseFont.CP1252, BaseFont.EMBEDDED);
        bf.setCharAdvance('\u00a8', 0);
        font = new Font(bf, 12);
        document.add(new Paragraph(MOVIE, font));
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
        new Diacritics2().createPdf(RESULT);
    }
}
