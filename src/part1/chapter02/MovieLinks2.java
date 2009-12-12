/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part1.chapter02;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.FilmFonts;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class MovieLinks2 {

    /** The resulting PDF file. */
    public static final String RESULT = "results/part1/chapter02/movie_links_2.pdf";
    
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
        new MovieLinks1().createPdf(MovieLinks1.RESULT);
        new MovieLinks2().createPdf(RESULT);
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
        // Create a local destination at the top of the page
        Paragraph p = new Paragraph();
        Chunk top = new Chunk("Country List", FilmFonts.BOLD);
        top.setLocalDestination("top");
        p.add(top);
        document.add(p);
        // create an external link
        Chunk imdb = new Chunk("Internet Movie Database", FilmFonts.ITALIC);
        imdb.setAnchor(new URL("http://www.imdb.com/"));
        p = new Paragraph("Click on a country, and you'll get a list of movies, "
            + "containing links to the ");
        p.add(imdb);
        p.add(".");
        document.add(p);
        // Create a remote goto
        p = new Paragraph("This list can be found in a ");
        Chunk page1 = new Chunk("separate document");
        page1.setRemoteGoto("movie_links_1.pdf", 1);
        p.add(page1);
        p.add(".");
        document.add(p);
        document.add(Chunk.NEWLINE);
        // Create a database connection and statement
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        Statement stm = connection.createStatement();
        ResultSet rs = stm.executeQuery(
            "SELECT DISTINCT mc.country_id, c.country, count(*) AS c "
            + "FROM film_country c, film_movie_country mc "
            + "WHERE c.id = mc.country_id "
            + "GROUP BY mc.country_id, country ORDER BY c DESC");
        // loop over the results
        while (rs.next()) {
        	// add country with remote goto
            Paragraph country = new Paragraph(rs.getString("country"));
            country.add(": ");
            Chunk link = new Chunk(String.format("%d movies", rs.getInt("c")));
            link.setRemoteGoto("movie_links_1.pdf", rs.getString("country_id"));
            country.add(link);
            document.add(country);
        }
        document.add(Chunk.NEWLINE);
        // Create local goto to top
        p = new Paragraph("Go to ");
        top = new Chunk("top");
        top.setLocalGoto("top");
        p.add(top);
        p.add(".");
        document.add(p);
        // step 5
        document.close();
        connection.close();
    }
}
