/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part2.chapter07;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import part1.chapter02.MovieLinks1;

import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.FilmFonts;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.SimpleNamedDestination;

public class LinkActions {

    /** The resulting PDF file. */
    public static final String RESULT1 = "results/part2/chapter07/movie_links_1.pdf";
    /** The resulting PDF file. */
    public static final String RESULT2 = "results/part2/chapter07/movie_links_2.pdf";
    /** The resulting XML file. */
    public static final String RESULT3 = "results/part2/chapter07/destinations.xml";
    
    /**
     * Main method.
     * Reads information from a database and writes it to a PDF document
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException 
     * @throws SQLException
     */
    public static void main(String[] args) throws IOException, DocumentException, SQLException {
        new MovieLinks1().createPdf(RESULT1);
        LinkActions actions = new LinkActions();
        actions.createPdf(RESULT2);
        actions.createXml(RESULT1, RESULT3);
    }

    /**
     * Creates a PDF document.
     * @param filename the path to the new PDF document
     * @throws    DocumentException 
     * @throws    IOException
     * @throws    SQLException
     */
    protected void createPdf(String filename) throws IOException, DocumentException, SQLException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        document.open();
        
        Paragraph p = new Paragraph();
        Chunk top = new Chunk("Country List", FilmFonts.BOLD);
        top.setLocalDestination("top");
        p.add(top);
        document.add(p);
        
        Chunk imdb = new Chunk("Internet Movie Database", FilmFonts.ITALIC);
        imdb.setAction(new PdfAction(new URL("http://www.imdb.com/")));
        p = new Paragraph("Click on a country, and you'll get a list of movies, containing links to the ");
        p.add(imdb);
        p.add(".");
        document.add(p);
        p = new Paragraph("This list can be found in a ");
        Chunk page1 = new Chunk("separate document");
        page1.setAction(new PdfAction("movie_links_1.pdf", 1));
        p.add(page1);
        p.add(".");
        document.add(p);
        document.add(Chunk.NEWLINE);
        
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        Statement stm = connection.createStatement();
        ResultSet rs = stm.executeQuery(
            "SELECT DISTINCT mc.country_id, c.country, count(*) AS c "
            + "FROM film_country c, film_movie_country mc WHERE c.id = mc.country_id "
            + "GROUP BY mc.country_id, country ORDER BY c DESC");
        while (rs.next()) {
            Paragraph country = new Paragraph(rs.getString("country"));
            country.add(": ");
            Chunk link = new Chunk(String.format("%d movies", rs.getInt("c")));
            link.setAction(PdfAction.gotoRemotePage("movie_links_1.pdf", rs.getString("country_id"), false, true));
            country.add(link);
            document.add(country);
        }
        document.add(Chunk.NEWLINE);
        
        p = new Paragraph("Go to ");
        top = new Chunk("top");
        top.setAction(PdfAction.gotoLocalPage("top", false));
        p.add(top);
        p.add(".");
        document.add(p);
        
        document.close();
        connection.close();
    }
    
    @SuppressWarnings("unchecked")
    public void createXml(String src, String dest) throws IOException {
        PdfReader reader = new PdfReader(src);
        HashMap<String,String> map = SimpleNamedDestination.getNamedDestination(reader, false);
        SimpleNamedDestination.exportToXML(map, new FileOutputStream(RESULT3),
                "ISO8859-1", true);
    }

}
