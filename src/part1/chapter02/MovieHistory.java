/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part1.chapter02;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;
import java.util.TreeSet;

import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.MovieComparator;
import com.lowagie.filmfestival.PojoFactory;
import com.lowagie.filmfestival.PojoToElementFactory;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfWriter;

public class MovieHistory {

    /** The resulting PDF file. */
    public static final String RESULT = "results/part1/chapter02/movie_history.pdf";
    
    /** The different epochs. */
    public static final String[] EPOCH =
        { "Forties", "Fifties", "Sixties", "Seventies", "Eighties", "Nineties", "Twenty-first Century" };
    /** The fonts for the title. */
    public static final Font[] FONT = new Font[4];
    static {
        FONT[0] = new Font(Font.HELVETICA, 24);
        FONT[1] = new Font(Font.HELVETICA, 18);
        FONT[2] = new Font(Font.HELVETICA, 14);
        FONT[3] = new Font(Font.HELVETICA, 12, Font.BOLD);
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
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(RESULT));
        document.open();
        
        Set<Movie> movies = 
            new TreeSet<Movie>(new MovieComparator(MovieComparator.BY_YEAR));
        movies.addAll(PojoFactory.getMovies(connection));
        int epoch = -1;
        int currentYear = 0;
        Paragraph title = null;
        Chapter chapter = null;
        Section section = null;
        Section subsection = null;
        for (Movie movie : movies) {
            if (epoch < (movie.getYear() - 1940) / 10) {
                epoch = (movie.getYear() - 1940) / 10;
                if (chapter != null) {
                    document.add(chapter);
                }
                title = new Paragraph(EPOCH[epoch], FONT[0]);
                chapter = new Chapter(title, epoch + 1);
            }
            if (currentYear < movie.getYear()) {
                currentYear = movie.getYear();
                title = new Paragraph(String.format("The year %d", movie.getYear()), FONT[1]);
                section = chapter.addSection(title);
                section.setBookmarkTitle(String.valueOf(movie.getYear()));
                section.setIndentation(30);
                section.setBookmarkOpen(false);
                section.setNumberStyle(Section.NUMBERSTYLE_DOTTED_WITHOUT_FINAL_DOT);
                section.add(new Paragraph(String.format("Movies from the year %d:", movie.getYear())));
            }
            title = new Paragraph(movie.getMovieTitle(), FONT[2]);
            subsection = section.addSection(title);
            subsection.setIndentationLeft(20);
            subsection.setNumberDepth(1);
            subsection.add(new Paragraph("Duration: " + movie.getDuration(), FONT[3]));
            subsection.add(new Paragraph("Director(s):", FONT[3]));
            subsection.add(PojoToElementFactory.getDirectorList(movie));
            subsection.add(new Paragraph("Countries:", FONT[3]));
            subsection.add(PojoToElementFactory.getCountryList(movie));
        }
        document.add(chapter);
        
        document.close();
        connection.close();
    }
}
