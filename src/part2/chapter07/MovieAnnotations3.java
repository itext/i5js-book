/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part2.chapter07;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;
import com.lowagie.filmfestival.PojoToElementFactory;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.PdfWriter;

public class MovieAnnotations3 {

    /** The resulting PDF. */
    public static final String RESULT = "results/part2/chapter07/movie_annotations_3.pdf";
    /** Path to the resources. */
    public static final String RESOURCE = "resources/posters/%s.jpg";
    
    public static void main(String[] args) throws IOException, DocumentException, SQLException {
        MovieAnnotations3 example = new MovieAnnotations3();
        example.createPdf(RESULT);
    }
    
    public void createPdf(String filename) throws IOException, DocumentException, SQLException {
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document,
                new FileOutputStream(filename));
        document.open();
        Phrase phrase;
        Chunk chunk;
        PdfAnnotation annotation;
        for (Movie movie : PojoFactory.getMovies(connection)) {
            phrase = new Phrase(movie.getMovieTitle());
            chunk = new Chunk("\u00a0\u00a0");
            annotation = PdfAnnotation.createFileAttachment(writer, null,
                    movie.getMovieTitle(), null,
                    String.format(RESOURCE, movie.getImdb()),
                    String.format("img_%s.jpg", movie.getImdb()));
            annotation.put(PdfName.NAME, new PdfString("Paperclip"));
            chunk.setAnnotation(annotation);
            phrase.add(chunk);
            document.add(phrase);
            document.add(PojoToElementFactory.getDirectorList(movie));
            document.add(PojoToElementFactory.getCountryList(movie));
        }
        document.close();
        connection.close();
    }
}
