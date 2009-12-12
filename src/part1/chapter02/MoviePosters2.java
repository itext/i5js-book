/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part1.chapter02;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class MoviePosters2 {
    /** Path to the resulting PDF */
    public static final String RESULT
        = "results/part1/chapter02/movie_posters_2.pdf";
    /** Path to the resources. */
    public static final String RESOURCE = "resources/posters/%s.jpg";
    
    /**
     * Main method.
     *
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException 
     * @throws SQLException
     */
    public static void main(String[] args)
        throws IOException, DocumentException, SQLException {
        new MoviePosters2().createPdf(RESULT);
    }
    
    /**
     * Creates a PDF with information about the movies
     * @param    filename the name of the PDF file that will be created.
     * @throws    DocumentException 
     * @throws    IOException 
     * @throws    SQLException
     */
    public void createPdf(String filename)
        throws IOException, DocumentException, SQLException {
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        
        // step 1
        Document document = new Document();
        // step 2
        PdfWriter.getInstance(document,
                new FileOutputStream(filename)).setStrictImageSequence(true);
        // step 3
        document.open();
        // step 4
        List<Movie> movies = PojoFactory.getMovies(connection);
        for (Movie movie : movies) {
            document.add(new Paragraph(movie.getMovieTitle()));
            // Add an image
            document.add(
                Image.getInstance(String.format(RESOURCE, movie.getImdb())));
        }
        // step 5
        document.close();
        
        connection.close();
    }
}
