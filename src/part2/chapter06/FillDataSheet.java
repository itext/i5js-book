/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part2.chapter06;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Director;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;
import com.lowagie.filmfestival.Screening;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class FillDataSheet {

    public static final String DATASHEET = "resources/pdfs/datasheet.pdf";
    public static final String RESULT = "results/part2/chapter06/imdb%s.pdf";
    
    public static void main(String[] args) throws SQLException, IOException, DocumentException {
        DatabaseConnection connection = new HsqldbConnection("filmfestival");

        List<Movie> movies = PojoFactory.getMovies(connection);
        PdfReader reader;
        PdfStamper stamper;
        for (Movie movie : movies) {
            if (movie.getYear() < 2007)
                continue;
            reader = new PdfReader(DATASHEET);
            stamper = new PdfStamper(reader,
                    new FileOutputStream(String.format(RESULT, movie.getImdb())));
            fill(stamper.getAcroFields(), movie);
            if (movie.getYear() == 2007)
                stamper.setFormFlattening(true);
            stamper.close();
        }
        connection.close();
    }
    
    public static void fill(AcroFields form, Movie movie) throws IOException, DocumentException {
        form.setField("title", movie.getMovieTitle());
        form.setField("director", getDirectors(movie));
        form.setField("year", String.valueOf(movie.getYear()));
        form.setField("duration", String.valueOf(movie.getDuration()));
        form.setField("category", movie.getEntry().getCategory().getKeyword());
        for (Screening screening : movie.getEntry().getScreenings()) {
            form.setField(screening.getLocation().replace('.', '_'), "Yes");
        }
    }
    
    public static String getDirectors(Movie movie) {
        List<Director> directors = movie.getDirectors();
        StringBuffer buf = new StringBuffer();
        for (Director director : directors) {
            buf.append(director.getGivenName());
            buf.append(' ');
            buf.append(director.getName());
            buf.append(',');
            buf.append(' ');
        }
        int i = buf.length();
        if (i > 0)
            buf.delete(i - 2, i);
        return buf.toString();
    }
}
