/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part4.chapter13;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;
import java.util.TreeSet;

import part2.chapter07.LinkActions;

import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Section;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfIndirectReference;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.MovieComparator;
import com.lowagie.filmfestival.PojoFactory;
import com.lowagie.filmfestival.PojoToElementFactory;

public class Bookmarks2NamedDestinations {

    /** The resulting PDF file. */
    public static final String RESULT1 = "results/part4/chapter13/bookmarks.pdf";
    /** The resulting PDF file. */
    public static final String RESULT2 = "results/part4/chapter13/named_destinations.pdf";
    /** The resulting PDF file. */
    public static final String RESULT3 = "results/part4/chapter13/named_destinations.xml";
    
    /** The different epochs. */
    public static final String[] EPOCH =
        { "Forties", "Fifties", "Sixties", "Seventies", "Eighties",
          "Nineties", "Twenty-first Century" };
    /** The fonts for the title. */
    public static final Font[] FONT = new Font[4];
    static {
        FONT[0] = new Font(FontFamily.HELVETICA, 24);
        FONT[1] = new Font(FontFamily.HELVETICA, 18);
        FONT[2] = new Font(FontFamily.HELVETICA, 14);
        FONT[3] = new Font(FontFamily.HELVETICA, 12, Font.BOLD);
    }
    
    /**
     * Creates a PDF document.
     * @param filename the path to the new PDF document
     * @throws DocumentException 
     * @throws IOException 
     * @throws    DocumentException 
     * @throws    IOException
     * @throws SQLException 
     */
    public void createPdf(String filename) throws IOException, DocumentException, SQLException {
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        // step 1
        Document document = new Document();
        // step 2
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        // step 3
        document.open();
        // step 4
        Set<Movie> movies = 
            new TreeSet<Movie>(new MovieComparator(MovieComparator.BY_YEAR));
        movies.addAll(PojoFactory.getMovies(connection));
        int epoch = -1;
        int currentYear = 0;
        Paragraph title = null;
        Chapter chapter = null;
        Section section = null;

        for (Movie movie : movies) {
            // add the chapter if we're in a new epoch
            if (epoch < (movie.getYear() - 1940) / 10) {
                epoch = (movie.getYear() - 1940) / 10;
                if (chapter != null) {
                    document.add(chapter);
                }
                title = new Paragraph(EPOCH[epoch], FONT[0]);
                chapter = new Chapter(title, epoch + 1);
                chapter.setBookmarkTitle(EPOCH[epoch]);
            }
            // switch to a new year
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
            section.add(title);
            section.add(new Paragraph("Duration: " + movie.getDuration(), FONT[3]));
            section.add(new Paragraph("Director(s):", FONT[3]));
            section.add(PojoToElementFactory.getDirectorList(movie));
            section.add(new Paragraph("Countries:", FONT[3]));
            section.add(PojoToElementFactory.getCountryList(movie));
        }
        document.add(chapter);
        // step 5
        document.close();
        connection.close();
    }
    /**
     * Manipulates a PDF file src with the file dest as result
     * @param src the original PDF
     * @param dest the resulting PDF
     * @throws IOException
     * @throws DocumentException 
     */
    public void manipulatePdf(String src, String dest) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        PdfDictionary root = reader.getCatalog();
        PdfDictionary outlines = root.getAsDict(PdfName.OUTLINES);
        if (outlines == null)
            return;
        PdfArray dests = new PdfArray();
        addKids(dests, outlines.getAsDict(PdfName.FIRST));
        if (dests.size() == 0)
            return;
        PdfIndirectReference ref = reader.addPdfObject(dests);
        PdfDictionary nametree = new PdfDictionary();
        nametree.put(PdfName.NAMES, ref);
        PdfDictionary names = new PdfDictionary();
        names.put(PdfName.DESTS, nametree);
        root.put(PdfName.NAMES, names);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        stamper.close();
    }
    
    /**
     * Adds Outline dictionaries to an array of destinations.
     * @param dests
     * @param outline
     */
    public static void addKids(PdfArray dests, PdfDictionary outline) {
        while (outline != null) {
            dests.add(outline.getAsString(PdfName.TITLE));
            dests.add(outline.getAsArray(PdfName.DEST));
            addKids(dests, outline.getAsDict(PdfName.FIRST));
            outline = outline.getAsDict(PdfName.NEXT);
        }
    }
    
    /**
     * Main method.
     *
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException
     * @throws SQLException 
     */
    public static void main(String[] args) throws IOException, DocumentException, SQLException {
        Bookmarks2NamedDestinations example = new Bookmarks2NamedDestinations();
        example.createPdf(RESULT1);
        example.manipulatePdf(RESULT1, RESULT2);
        new LinkActions().createXml(RESULT2, RESULT3);
    }
}
