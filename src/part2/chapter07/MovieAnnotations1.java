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
import com.itextpdf.text.Annotation;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfWriter;

public class MovieAnnotations1 {

    /** The resulting PDF. */
    public static final String RESULT = "results/part2/chapter07/movie_annotations_1.pdf";
    public static final String INFO = "Movie produced in %s; run length: %s";
    
    public static void main(String[] args) throws IOException, DocumentException, SQLException {
        MovieAnnotations1 example = new MovieAnnotations1();
        example.createPdf(RESULT);
    }

    /**
     * Creates a PDF document.
     * @param filename the path to the new PDF document
     * @throws    DocumentException 
     * @throws    IOException
     * @throws    SQLExcception
     */
    public void createPdf(String filename) throws IOException, DocumentException, SQLException {
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        Document document = new Document();
        PdfWriter.getInstance(document,
                new FileOutputStream(filename));
        document.open();
        for (Movie movie : PojoFactory.getMovies(connection)) {
            document.add(new Phrase(movie.getMovieTitle()));
            document.add(new Annotation(movie.getTitle(), String.format(INFO, movie.getYear(), movie.getDuration())));
            document.add(PojoToElementFactory.getDirectorList(movie));
            document.add(PojoToElementFactory.getCountryList(movie));
        }
        document.close();
        connection.close();
    }
}
