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
    
    public static void main(String[] args) throws IOException, DocumentException, SQLException {
        new HtmlMovies1().createHtmlAndPdf(HTML, PDF);
    }
    
    @SuppressWarnings("unchecked")
    public void createHtmlAndPdf(String html, String pdf) throws SQLException, IOException, DocumentException {
        DatabaseConnection connection = new HsqldbConnection("filmfestival");

        PrintStream out = new PrintStream(new FileOutputStream(html));
        out.println("<html>\n<body>");
        
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(pdf));
        document.open();
        List<Movie> movies = PojoFactory.getMovies(connection);
        String snippet;
        for (Movie movie : movies) {
            snippet = createHtmlSnippet(movie);
            out.println(snippet);
            List<Element> objects = HTMLWorker.parseToList(new StringReader(snippet), styles, providers);
            for (Element element : objects)
                document.add(element);
        }
        document.close();
        
        out.print("</body>\n<html>");
        connection.close();
    }
    
    public void setStyles(StyleSheet styles) {
        this.styles = styles;
    }

    public void setProviders(HashMap<String, Object> providers) {
        this.providers = providers;
    }

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
