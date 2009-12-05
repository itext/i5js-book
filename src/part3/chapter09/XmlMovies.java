/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part3.chapter09;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Country;
import com.lowagie.filmfestival.Director;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;

public class XmlMovies {
    
    public static final String XML = "results/part3/chapter09/movies.xml";
    public static final String PDF = "results/part3/chapter09/movies_xml.pdf";
    
    public static void main(String[] args) throws SQLException, IOException, DocumentException, ParserConfigurationException, SAXException {
        XmlMovies movies = new XmlMovies();
        movies.createXml(XML);
        movies.createPdf(XML, PDF);
    }
    
    public void createXml(String xml) throws SQLException, IOException {
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        PrintStream out = new PrintStream(new FileOutputStream(xml), true, "UTF-8");
        out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        out.println("<movies>");
        List<Movie> movies = PojoFactory.getMovies(connection);
        for (Movie movie : movies)
            out.println(createXmlSnippet(movie));
        out.print("</movies>");
    }

    public void createPdf(String xml, String pdf) throws IOException, DocumentException, ParserConfigurationException, SAXException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(pdf));
        document.open();
        SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
        parser.parse(new InputSource(new FileInputStream(xml)), new XmlHandler(document));
        document.close();
    }
    
    public String createXmlSnippet(Movie movie) {
        StringBuffer buf = new StringBuffer("<movie imdb=\"");
        buf.append(movie.getImdb());
        buf.append("\" year=\"");
        buf.append(movie.getYear());
        buf.append("\" duration=\"");
        buf.append(movie.getDuration());
        buf.append("\">\n\t<title>");
        buf.append(movie.getMovieTitle());
        buf.append("</title>\n");
        if (movie.getOriginalTitle() != null) {
            buf.append("\t<original>");
            buf.append(movie.getOriginalTitle());
            buf.append("</original>\n");
        }
        buf.append("\t<directors>\n");
        for (Director director : movie.getDirectors()) {
            buf.append("\t\t<director>");
            buf.append(director.getName());
            buf.append(", ");
            buf.append(director.getGivenName());
            buf.append("</director>\n");
        }
        buf.append("\t</directors>\n\t<countries>\n");
        for (Country country : movie.getCountries()) {
            buf.append("\t\t<country>");
            buf.append(country.getCountry());
            buf.append("</country>\n");
        }
        buf.append("\t</countries>\n</movie>");
        return buf.toString();
    }
}
