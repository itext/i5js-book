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
import com.itextpdf.text.pdf.PdfWriter;

public class MovieAnnotations2 {

    /** The resulting PDF. */
    public static final String RESULT = "results/part2/chapter07/movie_annotations_2.pdf";
    public static final String INFO = "Movie produced in %s; run length: %s";
    
    public static void main(String[] args) throws IOException, DocumentException, SQLException {
        MovieAnnotations2 example = new MovieAnnotations2();
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
        for (Movie movie : PojoFactory.getMovies(connection)) {
            phrase = new Phrase(movie.getMovieTitle());
            chunk = new Chunk("\u00a0");
            chunk.setAnnotation(PdfAnnotation.createText(writer, null, movie.getMovieTitle(),
                    String.format(INFO, movie.getYear(), movie.getDuration()), false, "Comment"));
            phrase.add(chunk);
            document.add(phrase);
            document.add(PojoToElementFactory.getDirectorList(movie));
            document.add(PojoToElementFactory.getCountryList(movie));
        }
        document.close();
        connection.close();
    }
}
