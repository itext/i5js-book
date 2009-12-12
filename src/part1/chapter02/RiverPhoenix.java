/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part1.chapter02;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.filmfestival.FilmFonts;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class RiverPhoenix {
    /** Path to the resulting PDF */
    public static final String RESULT = "results/part1/chapter02/river_phoenix.pdf";

    /**
     * Main method.
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException, DocumentException {
        new RiverPhoenix().createPdf(RESULT);
    }
    
    /**
     * Creates a PDF with information about the movies
     * @param    filename the name of the PDF file that will be created.
     * @throws    DocumentException 
     * @throws    IOException
     */
    public void createPdf(String filename)
        throws IOException, DocumentException {
    	// step 1
        Document document = new Document();
        // step 2
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        // step 3
        document.open();
        // step 4
        document.add(new Paragraph("Movies featuring River Phoenix", FilmFonts.BOLD));
        document.add(createParagraph(
            "My favorite movie featuring River Phoenix was ", "0092005"));
        document.add(createParagraph(
            "River Phoenix was nominated for an academy award for his role in ", "0096018"));
        document.add(createParagraph(
            "River Phoenix played the young Indiana Jones in ", "0097576"));
        document.add(createParagraph(
            "His best role was probably in ", "0102494"));
        // step 5
        document.close();
    }
    
    /**
     * Creates a paragraph with some text about a movie with River Phoenix,
     * and a poster of the corresponding movie.
     * @param text the text about the movie
     * @param imdb the IMDB code referring to the poster
     * @throws DocumentException
     * @throws IOException
     */
    public Paragraph createParagraph(String text, String imdb)
        throws DocumentException, IOException {
        Paragraph p = new Paragraph(text);
        Image img = Image.getInstance(
                String.format("resources/posters/%s.jpg", imdb));
        img.scaleToFit(1000, 72);
        img.setRotationDegrees(-30);
        p.add(new Chunk(img, 0, -15, true));
        return p;
    }
}
