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
    
    public static final String RESOURCE = "resources/pdfs/xfa_movies.pdf";
    public static final String RESULTTXT = "results/part2/chapter08/movies_xfa.txt";
    public static final String XMLDATA = "results/part2/chapter08/movies.xml";
    public static final String RESULTXFA = "results/part2/chapter08/movies_xfa.xml";
    public static final String RESULT = "results/part2/chapter08/xfa_filled_in.pdf";

    public static void main(String[] args) throws IOException, SQLException, DocumentException {
        new XfaMovie().readFieldnames(RESOURCE, RESULTTXT);
        XfaMovies xfa = new XfaMovies();
        xfa.createXML(XMLDATA);
        xfa.manipulatePdf(RESOURCE, XMLDATA, RESULT);
    }
    
    public void createXML(String dest) throws IOException, SQLException {
        // getting new data from a "datasets" XML snippet
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

    public void manipulatePdf(String src, String xml, String dest) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        PdfStamper stamper = new PdfStamper(reader,
                new FileOutputStream(dest));
        AcroFields form = stamper.getAcroFields();
        XfaForm xfa = form.getXfa();
        xfa.fillXfaForm(new FileInputStream(XMLDATA));
        stamper.close();
    }
    
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
}
