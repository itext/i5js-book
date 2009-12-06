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
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;

public class DirectorOverview3 {

    /** The resulting PDF file. */
    public static final String RESULT = "results/part1/chapter02/director_overview_3.pdf";

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

        Chunk CONNECT = new Chunk(new LineSeparator(0.5f, 95, BaseColor.BLUE, Element.ALIGN_CENTER, 3.5f));
        LineSeparator UNDERLINE = new LineSeparator(1, 100, null, Element.ALIGN_CENTER, -2);
        
        Chunk tab1 = new Chunk(new VerticalPositionMark(), 200, true);
        Chunk tab2 = new Chunk(new VerticalPositionMark(), 350, true);
        Chunk tab3 = new Chunk(new DottedLineSeparator(), 450, true);
        
        while (rs.next()) {
            
            director = PojoFactory.getDirector(rs);
            Paragraph p = new Paragraph(PojoToElementFactory.getDirectorPhrase(director));
            p.add(CONNECT);
            p.add(String.format("movies: %d", rs.getInt("c")));
            p.add(UNDERLINE);
            document.add(p);
            
            TreeSet<Movie> movies = new TreeSet<Movie>(new MovieComparator(MovieComparator.BY_YEAR));
            movies.addAll(PojoFactory.getMovies(connection, rs.getInt("id")));
            for (Movie movie : movies) {
                p = new Paragraph(movie.getMovieTitle());
                p.add(new Chunk(tab1));
                if (movie.getOriginalTitle() != null)
                    p.add(new Chunk(movie.getOriginalTitle()));
                p.add(new Chunk(tab2));
                p.add(new Chunk(String.format("%d minutes", movie.getDuration())));
                p.add(new Chunk(tab3));
                p.add(new Chunk(String.valueOf(movie.getYear())));
                document.add(p);
            }
            document.add(Chunk.NEWLINE);
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
        new DirectorOverview3().createPdf(RESULT);
    }
}