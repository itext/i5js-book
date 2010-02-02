/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part1.chapter05;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class MovieHistory1 {
    
    /** The resulting PDF file. */
    public static final String RESULT
        = "results/part1/chapter05/movie_history1.pdf";

    /**
     * Inner class to keep track of the TOC
     * and to draw lines under ever chapter and section.
     */
    class ChapterSectionTOC extends PdfPageEventHelper {
        /** List with the titles. */
        List<Paragraph> titles = new ArrayList<Paragraph>();
        
        public void onChapter(PdfWriter writer, Document document,
                float position, Paragraph title) {
            titles.add(new Paragraph(title.getContent(), FONT[4]));
        }

        public void onChapterEnd(PdfWriter writer, Document document,
                float position) {
            drawLine(writer.getDirectContent(),
                    document.left(), document.right(), position - 5);
        }

        public void onSection(PdfWriter writer, Document document,
                float position, int depth, Paragraph title) {
            title = new Paragraph(title.getContent(), FONT[4]);
            title.setIndentationLeft(18 * depth);
            titles.add(title);
        }

        public void onSectionEnd(PdfWriter writer, Document document,
                float position) {
            drawLine(writer.getDirectContent(),
                    document.left(), document.right(), position - 3);
        }
        
        public void drawLine(PdfContentByte cb, float x1, float x2, float y) {
            cb.moveTo(x1, y);
            cb.lineTo(x2, y);
            cb.stroke();
        }
    }
    
    /** The different epochs. */
    public static final String[] EPOCH =
        { "Forties", "Fifties", "Sixties", "Seventies", "Eighties",
    	  "Nineties", "Twenty-first Century" };
    /** The fonts for the title. */
    public static final Font[] FONT = new Font[5];
    static {
        FONT[0] = new Font(FontFamily.HELVETICA, 24);
        FONT[1] = new Font(FontFamily.HELVETICA, 18);
        FONT[2] = new Font(FontFamily.HELVETICA, 14);
        FONT[3] = new Font(FontFamily.HELVETICA, 12, Font.BOLD);
        FONT[4] = new Font(FontFamily.HELVETICA, 10);
    }

    /**
     * Creates a PDF document.
     * @param filename the path to the new PDF document
     * @throws    DocumentException 
     * @throws    IOException
     * @throws    SQLException
     */
    public void createPdf(String filename)
        throws IOException, DocumentException, SQLException {
    	// Create a database connection
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        // step 1
        Document document = new Document();
        // step 2
        PdfWriter writer
            = PdfWriter.getInstance(document, new FileOutputStream(RESULT));
        // IMPORTANT: set linear page mode!
        writer.setLinearPageMode();
        ChapterSectionTOC event = new ChapterSectionTOC();
        writer.setPageEvent(event);
        // step 3
        document.open();
        // step 4
        // add the chapters
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
                title = new Paragraph(
                    String.format("The year %d", movie.getYear()), FONT[1]);
                section = chapter.addSection(title);
                section.setBookmarkTitle(String.valueOf(movie.getYear()));
                section.setIndentation(30);
                section.setBookmarkOpen(false);
                section.setNumberStyle(Section.NUMBERSTYLE_DOTTED_WITHOUT_FINAL_DOT);
                section.add(new Paragraph(
                    String.format("Movies from the year %d:", movie.getYear())));
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
        // add the TOC starting on the next page
        document.newPage();
        int toc = writer.getPageNumber();
        for (Paragraph p : event.titles) {
            document.add(p);
        }
        // always go to a new page before reordering pages.
        document.newPage();
        // get the total number of pages that needs to be reordered
        int total = writer.reorderPages(null);
        // change the order
        int[] order = new int[total];
        for (int i = 0; i < total; i++) {
            order[i] = i + toc;
            if (order[i] > total)
                order[i] -= total;
        }
        // apply the new order
        writer.reorderPages(order);
        // step 5
        document.close();
        // Close the database connection
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
        new MovieHistory1().createPdf(RESULT);
    }
}
