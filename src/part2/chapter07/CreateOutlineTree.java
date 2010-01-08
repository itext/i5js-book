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
import java.util.HashMap;
import java.util.List;

import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;
import com.lowagie.filmfestival.PojoToElementFactory;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfDestination;
import com.itextpdf.text.pdf.PdfOutline;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.SimpleBookmark;

public class CreateOutlineTree {

    /** The resulting PDF. */
    public static final String RESULT
        = "results/part2/chapter07/outline_tree.pdf";
    /** An XML representing the outline tree */
    public static final String RESULTXML
        = "results/part2/chapter07/outline_tree.xml";
    /** Pattern of the IMDB urls */
    public static final String RESOURCE
        = "http://imdb.com/title/tt%s/";
    /** JavaScript snippet. */
    public static final String INFO
        = "app.alert('Movie produced in %s; run length: %s');";

    /**
     * Creates a PDF document.
     * @param filename the path to the new PDF document
     * @throws    DocumentException 
     * @throws    IOException
     * @throws    SQLException
     */
    public void createPdf(String filename)
        throws IOException, DocumentException, SQLException {
    	// Create a database connection
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        // step 1
        Document document = new Document();
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document,
                new FileOutputStream(filename));
        writer.setViewerPreferences(PdfWriter.PageModeUseOutlines);
        // step 3
        document.open();
        // step 4
        PdfOutline root = writer.getRootOutline();
        PdfOutline movieBookmark;
        PdfOutline link;
        String title;
        List<Movie> movies = PojoFactory.getMovies(connection);
        for (Movie movie : movies) {
            title = movie.getMovieTitle();
            if ("3-Iron".equals(title))
                title = "\ube48\uc9d1";
            movieBookmark = new PdfOutline(root, 
                new PdfDestination(
                    PdfDestination.FITH, writer.getVerticalPosition(true)),
                title, true);
            movieBookmark.setStyle(Font.BOLD);
            link = new PdfOutline(movieBookmark,
                new PdfAction(String.format(RESOURCE, movie.getImdb())),
                "link to IMDB");
            link.setColor(BaseColor.BLUE);
            new PdfOutline(movieBookmark,
                PdfAction.javaScript(
                    String.format(INFO, movie.getYear(), movie.getDuration()), writer),
                    "instant info");
            document.add(new Paragraph(movie.getMovieTitle()));
            document.add(PojoToElementFactory.getDirectorList(movie));
            document.add(PojoToElementFactory.getCountryList(movie));
        }
        // step 5
        document.close();
        // Close the database connection
        connection.close();
    }
    
    /**
     * Creates an XML file with the bookmarks of a PDF file.
     * @param src the path to the PDF file with the bookmarks
     * @param dest the path to the XML file
     * @throws IOException
     */
    public void createXml(String src, String dest) throws IOException {
        PdfReader reader = new PdfReader(src);
        List<HashMap<String, Object>> list = SimpleBookmark.getBookmark(reader);
        SimpleBookmark.exportToXML(list,
                new FileOutputStream(dest), "ISO8859-1", true);
    }
    
    /**
     * Main method.
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException
     * @throws SQLException
     */
    public static void main(String[] args) throws IOException, DocumentException, SQLException {
        CreateOutlineTree example = new CreateOutlineTree();
        example.createPdf(RESULT);
        example.createXml(RESULT, RESULTXML);
    }
}
