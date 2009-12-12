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
import com.lowagie.filmfestival.Director;
import com.lowagie.filmfestival.FilmFonts;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.pdf.PdfWriter;

public class MovieLists1 {

    /** The resulting PDF file. */
    public static final String RESULT = "results/part1/chapter02/movie_lists_1.pdf";
    
    /**
     * Creates a PDF with information about the movies
     * @param    filename the name of the PDF file that will be created.
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
        // Create a new list
        List list = new List(List.ORDERED);
        // loop over the countries
        while (rs.next()) {
        	// create a list item for the country
            ListItem item = new ListItem(
                String.format("%s: %d movies",
                    rs.getString("country"), rs.getInt("c")),
                FilmFonts.BOLDITALIC);
            // create a movie list for each country
            List movielist = new List(List.ORDERED, List.ALPHABETICAL);
            movielist.setLowercase(List.LOWERCASE);
            for(Movie movie :
                PojoFactory.getMovies(connection, rs.getString("country_id"))) {
                ListItem movieitem = new ListItem(movie.getMovieTitle());
                List directorlist = new List(List.UNORDERED);
                for (Director director : movie.getDirectors()) {
                    directorlist.add(
                        String.format("%s, %s",
                            director.getName(), director.getGivenName()));
                }
                movieitem.add(directorlist);
                movielist.add(movieitem);
            }
            item.add(movielist);
            list.add(item);
        }
        document.add(list);
        // close the statement and the database connection
        stm.close();
        connection.close();
        // step 4
        document.close();
    }
    
    /**
     * Main method.
     *
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException 
     * @throws SQLException
     */
    public static void main(String[] args) throws IOException, DocumentException, SQLException {
        new MovieLists1().createPdf(RESULT);
    }
}
