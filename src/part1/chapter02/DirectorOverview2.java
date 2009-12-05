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
import java.util.TreeSet;

import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Director;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.MovieComparator;
import com.lowagie.filmfestival.PojoFactory;
import com.lowagie.filmfestival.PojoToElementFactory;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;

public class DirectorOverview2 {

    /** The resulting PDF file. */
    public static final String RESULT = "results/part1/chapter02/director_overview_2.pdf";

    /**
     * Creates a PDF with information about the movies
     * @param    filename the name of the PDF file that will be created.
     * @throws    DocumentException 
     * @throws    IOException 
     * @throws    SQLException
     */
    public void createPdf(String filename)
        throws IOException, DocumentException, SQLException {

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        document.open();

        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        Statement stm = connection.createStatement();
        ResultSet rs = stm.executeQuery(
            "SELECT DISTINCT d.id, d.name, d.given_name, count(*) AS c "
            + "FROM film_director d, film_movie_director md WHERE d.id = md.director_id "
            + "GROUP BY d.id, d.name, d.given_name ORDER BY c DESC");
        Director director;
        while (rs.next()) {
            
            director = PojoFactory.getDirector(rs);
            Paragraph p = new Paragraph(PojoToElementFactory.getDirectorPhrase(director));
            p.add(new Chunk(new DottedLineSeparator()));
            p.add(String.format("movies: %d", rs.getInt("c")));
            document.add(p);
            
            List list = new List(List.ORDERED);
            list.setIndentationLeft(36);
            list.setIndentationRight(36);
            TreeSet<Movie> movies = new TreeSet<Movie>(new MovieComparator(MovieComparator.BY_YEAR));
            movies.addAll(PojoFactory.getMovies(connection, rs.getInt("id")));
            ListItem movieitem;
            for (Movie movie : movies) {
                movieitem = new ListItem(movie.getMovieTitle());
                movieitem.add(new Chunk(new VerticalPositionMark()));
                movieitem.add(new Chunk(String.valueOf(movie.getYear())));
                if (movie.getYear() > 1999) {
                    movieitem.add(PositionedArrow.RIGHT);
                }
                list.add(movieitem);
            }
            document.add(list);
        }
        
        document.close();
    }
    
    /**
     * Main method.
     * Reads information from a database and writes it to a PDF document
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException 
     * @throws SQLException
     */
    public static void main(String[] args) throws IOException, DocumentException, SQLException {
        new DirectorOverview2().createPdf(RESULT);
    }
}
