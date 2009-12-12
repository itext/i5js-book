/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part1.chapter02;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.FilmFonts;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;
import com.itextpdf.text.Anchor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class MovieLinks1 {

    /** The resulting PDF file. */
    public static final String RESULT = "results/part1/chapter02/movie_links_1.pdf";
    
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
        new MovieLinks1().createPdf(RESULT);
    }

    /**
     * Creates a PDF document.
     * @param filename the path to the new PDF document
     * @throws    DocumentException 
     * @throws    IOException
     * @throws    SQLException
     */
    public void createPdf(String filename)
        throws IOException, DocumentException, SQLException {
    	// step 1
        Document document = new Document();
        // step 2
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        // step 3
        document.open();
        // step 4
        // Create database connection and statement
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        Statement stm = connection.createStatement();
        ResultSet rs = stm.executeQuery(
            "SELECT DISTINCT mc.country_id, c.country, count(*) AS c "
            + "FROM film_country c, film_movie_country mc "
            + "WHERE c.id = mc.country_id "
            + "GROUP BY mc.country_id, country ORDER BY c DESC");
        Anchor imdb;
        // loop over the countries
        while (rs.next()) {
            Paragraph country = new Paragraph();
            // the name of the country will be a destination
            Anchor dest = new Anchor(rs.getString("country"), FilmFonts.BOLD);
            dest.setName(rs.getString("country_id"));
            country.add(dest);
            country.add(String.format(": %d movies", rs.getInt("c")));
            document.add(country);
            // loop over the movies
            for(Movie movie :
                PojoFactory.getMovies(connection, rs.getString("country_id"))) {
            	// the movie title will be an external link
                imdb = new Anchor(movie.getMovieTitle());
                imdb.setReference(
                    String.format("http://www.imdb.com/title/tt%s/", movie.getImdb()));
                document.add(imdb);
                document.add(Chunk.NEWLINE);
            }
            document.newPage();
        }
        // Create an internal link to the first page
        Anchor toUS = new Anchor("Go back to the first page.");
        toUS.setReference("#US");
        document.add(toUS);
        
        document.close();
        connection.close();
    }
}
