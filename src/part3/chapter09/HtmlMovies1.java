/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part3.chapter09;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Country;
import com.lowagie.filmfestival.Director;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.html.simpleparser.StyleSheet;
import com.itextpdf.text.pdf.PdfWriter;

public class HtmlMovies1 {

    /** The resulting HTML file. */
    public static final String HTML = "results/part3/chapter09/movies_1.html";
    /** The resulting PDF file. */
    public static final String PDF = "results/part3/chapter09/html_movies_1.pdf";
    
    /** The StyleSheet. */
    protected StyleSheet styles = null;
    /** Extra properties. */
    protected HashMap<String,Object> providers = null;

    /**
     * Main method.
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException 
     * @throws SQLException
     */
    public static void main(String[] args)
        throws IOException, DocumentException, SQLException {
        new HtmlMovies1().createHtmlAndPdf(HTML, PDF);
    }
    
    /**
     * Creates a list with movies in HTML and PDF simultaneously.
     * @param html a path to the resulting HTML file
     * @param pdf a path to the resulting PDF file
     * @throws SQLException
     * @throws IOException
     * @throws DocumentException
     */
    public void createHtmlAndPdf(String html, String pdf)
        throws SQLException, IOException, DocumentException {
        // Create a database connection
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        // Create a stream to produce HTML
        PrintStream out = new PrintStream(new FileOutputStream(html));
        out.println("<html>\n<body>");
        // step 1
        Document document = new Document();
        // step 2
        PdfWriter.getInstance(document, new FileOutputStream(pdf));
        // step 3
        document.open();
        // step 4
        List<Movie> movies = PojoFactory.getMovies(connection);
        String snippet;
        for (Movie movie : movies) {
            // create the snippet
            snippet = createHtmlSnippet(movie);
            // use the snippet for the HTML page
            out.println(snippet);
            // use the snippet for the PDF document
            List<Element> objects
                = HTMLWorker.parseToList(
                    new StringReader(snippet), styles, providers);
            for (Element element : objects)
                document.add(element);
        }
        // step 5
        document.close();
        // flush and close the HTML stream
        out.print("</body>\n</html>");
        out.flush();
        out.close();
        // close the database connection
        connection.close();
    }
    
    /**
     * Sets the styles for the HTML to PDF conversion
     * @param styles a StyleSheet object
     */
    public void setStyles(StyleSheet styles) {
        this.styles = styles;
    }

    /**
     * Set some extra properties.
     * @param providers the properties map
     */
    public void setProviders(HashMap<String, Object> providers) {
        this.providers = providers;
    }

    /**
     * Creates an HTML snippet with info about a movie.
     * @param movie the movie for which we want to create HTML
     * @return a String with HTML code
     */
    public String createHtmlSnippet(Movie movie) {
        StringBuffer buf = new StringBuffer("\t<span class=\"title\">");
        buf.append(movie.getMovieTitle());
        buf.append("</span><br />\n");
        buf.append("\t<ul>\n");
        for (Country country : movie.getCountries()) {
            buf.append("\t\t<li class=\"country\">");
            buf.append(country.getCountry());
            buf.append("</li>\n");
        }
        buf.append("\t</ul>\n");
        buf.append("\tYear: <i>");
        buf.append(movie.getYear());
        buf.append(" minutes</i><br />\n");
        buf.append("\tDuration: <i>");
        buf.append(movie.getDuration());
        buf.append(" minutes</i><br />\n");
        buf.append("\t<ul>\n");
        for (Director director : movie.getDirectors()) {
            buf.append("\t\t<li><span class=\"director\">");
            buf.append(director.getName());
            buf.append(", ");
            buf.append(director.getGivenName());
            buf.append("</span></li>\n");
        }
        buf.append("\t</ul>\n");
        return buf.toString();
    }
}
