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
import com.itextpdf.text.pdf.draw.LineSeparator;

public class DirectorOverview1 {
    
    /** The resulting PDF file. */
    public static final String RESULT
        = "results/part1/chapter02/director_overview_1.pdf";

    /**
     * Creates a PDF with information about the movies
     * @param    filename the name of the PDF file that will be created.
     * @throws    DocumentException 
     * @throws    IOException 
     * @throws    SQLException
     */
    public void createPdf(String filename)
        throws IOException, DocumentException, SQLException {
        // step 1
    	Document document = new Document();
        // step 2
    	PdfWriter.getInstance(document, new FileOutputStream(filename));
        // step 3
    	document.open();
        // step 4
    	// database connection and statement
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        Statement stm = connection.createStatement();
        ResultSet rs = stm.executeQuery(
            "SELECT DISTINCT d.id, d.name, d.given_name, count(*) AS c "
            + "FROM film_director d, film_movie_director md "
            + "WHERE d.id = md.director_id "
            + "GROUP BY d.id, d.name, d.given_name ORDER BY name");
        Director director;
        // creating separators
        LineSeparator line
            = new LineSeparator(1, 100, null, Element.ALIGN_CENTER, -2);
        Paragraph stars = new Paragraph(20);
        stars.add(new Chunk(StarSeparator.LINE));
        stars.setSpacingAfter(30);
        // looping over the directors
        while (rs.next()) {
            // get the director object and use it in a Paragraph
            director = PojoFactory.getDirector(rs);
            Paragraph p = new Paragraph(
                PojoToElementFactory.getDirectorPhrase(director));
            // if there are more than 2 movies for this director
            // an arrow is added to the left
            if (rs.getInt("c") > 2)
                p.add(PositionedArrow.LEFT);
            p.add(line);
            // add the paragraph with the arrow to the document
            document.add(p);
            
            // Get the movies of the directory, ordered by year
            TreeSet<Movie> movies = new TreeSet<Movie>(
                new MovieComparator(MovieComparator.BY_YEAR));
            movies.addAll(PojoFactory.getMovies(connection, rs.getInt("id")));
            // loop over the movies
            for (Movie movie : movies) {
                p = new Paragraph(movie.getMovieTitle());
                p.add(": ");
                p.add(new Chunk(String.valueOf(movie.getYear())));
                if (movie.getYear() > 1999)
                    p.add(PositionedArrow.RIGHT);
                document.add(p);
            }
            // add a star separator after the director info is added
            document.add(stars);
        }
        // step 5
        document.close();
        connection.close();
    }
    
    /**
     * Main method.
     *
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException 
     * @throws SQLException
     */
    public static void main(String[] args)
        throws IOException, DocumentException, SQLException {
        new DirectorOverview1().createPdf(RESULT);
    }
}