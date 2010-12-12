/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part2.chapter08;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.util.List;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.XfaForm;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Country;
import com.lowagie.filmfestival.Director;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;

public class XfaMovies {

    /** The original PDF. */
    public static final String RESOURCE = "resources/pdfs/xfa_movies.pdf";
    /** Information about the form in xfa_movies.pdf */
    public static final String RESULTTXT = "results/part2/chapter08/movies_xfa.txt";
    /** The XML data that is going to be used to fill out the XFA form. */
    public static final String XMLDATA = "results/part2/chapter08/movies.xml";
    /** The resulting PDF. */
    public static final String RESULT = "results/part2/chapter08/xfa_filled_in.pdf";

    /**
     * Manipulates a PDF file src with the file dest as result
     * @param src the original PDF
     * @param xml the XML data that needs to be added to the XFA form
     * @param dest the resulting PDF
     * @throws IOException
     * @throws DocumentException
     */
    public void manipulatePdf(String src, String xml, String dest)
        throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        PdfStamper stamper = new PdfStamper(reader,
                new FileOutputStream(dest));
        AcroFields form = stamper.getAcroFields();
        XfaForm xfa = form.getXfa();
        xfa.fillXfaForm(new FileInputStream(xml));
        stamper.close();
    }
    
    /**
     * Creates an XML file containing data about movies.
     * @param dest the path to the resulting XML file
     * @throws IOException
     * @throws SQLException
     */
    public void createXML(String dest) throws IOException, SQLException {
        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(dest), "UTF-8");
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        List<Movie> movies = PojoFactory.getMovies(connection);
        out.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
        out.write("<movies>\n");
        for (Movie movie : movies) {
            out.write(getXml(movie));
        }
        out.write("</movies>");
        out.flush();
        out.close();
        connection.close();
    }
    
    /**
     * Creates an XML snippet containing information about a movie.
     * @param movie the Movie pojo
     * @return an XML snippet 
     */
    public String getXml(Movie movie) {
        StringBuffer buf = new StringBuffer();
        buf.append("<movie duration=\"");
        buf.append(movie.getDuration());
        buf.append("\" imdb=\"");
        buf.append(movie.getImdb());
        buf.append("\" year=\"");
        buf.append(movie.getYear());
        buf.append("\">");
        buf.append("<title>");
        buf.append(movie.getMovieTitle());
        buf.append("</title>");
        if (movie.getOriginalTitle() != null) {
            buf.append("<original>");
            buf.append(movie.getOriginalTitle());
            buf.append("</original>");
        }
        buf.append("<directors>");
        for (Director director : movie.getDirectors()) {
            buf.append("<director>");
            buf.append(director.getName());
            buf.append(", ");
            buf.append(director.getGivenName());
            buf.append("</director>");
        }
        buf.append("</directors>");
        buf.append("<countries>");
        for (Country country : movie.getCountries()) {
            buf.append("<country>");
            buf.append(country.getCountry());
            buf.append("</country>");
        }
        buf.append("</countries>");
        buf.append("</movie>\n");
        return buf.toString();
    }

    /**
     * Main method
     * @param args no arguments needed
     * @throws IOException
     * @throws DocumentException
     */
    public static void main(String[] args) throws IOException, SQLException, DocumentException {
        new XfaMovie().readFieldnames(RESOURCE, RESULTTXT);
        XfaMovies xfa = new XfaMovies();
        xfa.createXML(XMLDATA);
        xfa.manipulatePdf(RESOURCE, XMLDATA, RESULT);
    }
}
