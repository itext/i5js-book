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
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.ZapfDingbatsList;
import com.itextpdf.text.pdf.PdfWriter;

public class MovieLists4 {

    /** The resulting PDF file. */
    public static final String RESULT = "results/part1/chapter02/movie_lists_4.pdf";
    
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
        // Create the database connection and statement
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        Statement stm = connection.createStatement();
        ResultSet rs = stm.executeQuery(
            "SELECT DISTINCT mc.country_id, c.country, count(*) AS c "
            + "FROM film_country c, film_movie_country mc "
            + "WHERE c.id = mc.country_id "
            + "GROUP BY mc.country_id, country ORDER BY c DESC");
        // Create a list for the countries
        List list = new List(List.ORDERED);
        list.setFirst(9);
        // Loop over the countries
        while (rs.next()) {
            // Create a list item for a country
            ListItem item = new ListItem(String.format("%s: %d movies",
                rs.getString("country"), rs.getInt("c")));
            // Create a list for the movies
            List movielist = new List();
            movielist.setListSymbol(new Chunk("Movie: ", FilmFonts.BOLD));
            for(Movie movie :
                PojoFactory.getMovies(connection, rs.getString("country_id"))) {
                ListItem movieitem = new ListItem(movie.getMovieTitle());
                // Create a list for the directors
                List directorlist = new ZapfDingbatsList(42);
                for (Director director : movie.getDirectors()) {
                    directorlist.add(String.format("%s, %s",
                        director.getName(), director.getGivenName()));
                }
                movieitem.add(directorlist);
                movielist.add(movieitem);
            }
            item.add(movielist);
            list.add(item);
        }
        document.add(list);
        // Close the statement and database connection
        stm.close();
        connection.close();
        // step 5
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
        new MovieLists4().createPdf(RESULT);
    }
}
