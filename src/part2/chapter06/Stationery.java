/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part2.chapter06;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;
import java.util.TreeSet;

import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.FilmFonts;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.MovieComparator;
import com.lowagie.filmfestival.PojoFactory;
import com.lowagie.filmfestival.PojoToElementFactory;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

public class Stationery extends PdfPageEventHelper {

    /** The original PDF. */
    public static final String STATIONERY
        = "results/part2/chapter06/stationery.pdf";
    /** The resulting PDF. */
    public static final String RESULT
        = "results/part2/chapter06/text_on_stationery.pdf";
    
    /**
     * Main method.
     * @param args no arguments needed
     * @throws DocumentException 
     * @throws IOException
     * @throws SQLException
     */
    public static void main(String[] args)
        throws IOException, DocumentException, SQLException {
        Stationery stationary = new Stationery();
        createStationary(STATIONERY);
        stationary.createPdf(RESULT);
    }

    /**
     * Creates a PDF document.
     * @param filename the path to the new PDF document
     * @throws    DocumentException 
     * @throws    IOException
     * @throws    SQLException
     */
    public void createPdf(String filename)
        throws SQLException, IOException, DocumentException {
    	// Create the database connection
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        // step 1
        Document document = new Document(PageSize.A4, 36, 36, 72, 36);
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
        useStationary(writer);
        // step 3
        document.open();
        // step 4
        Statement stm = connection.createStatement();
        ResultSet rs = stm.executeQuery(
            "SELECT country, id FROM film_country ORDER BY country");
        while (rs.next()) {
            document.add(new Paragraph(rs.getString("country"), FilmFonts.BOLD));
            document.add(Chunk.NEWLINE);
            Set<Movie> movies = 
                new TreeSet<Movie>(new MovieComparator(MovieComparator.BY_YEAR));
            movies.addAll(PojoFactory.getMovies(connection, rs.getString("id")));
            for(Movie movie : movies) {
                document.add(new Paragraph(movie.getMovieTitle(), FilmFonts.BOLD));
                if (movie.getOriginalTitle() != null)
                    document.add(new Paragraph(movie.getOriginalTitle(), FilmFonts.ITALIC));
                document.add(new Paragraph(
                    String.format("Year: %d; run length: %d minutes",
                    movie.getYear(), movie.getDuration()), FilmFonts.NORMAL));
                document.add(PojoToElementFactory.getDirectorList(movie));
            }
            document.newPage();
        }
        // step 5
        document.close();
        // Close the database connection
        connection.close();
    }
    
    /** Imported page with the stationery. */
    protected PdfImportedPage page;

    /**
     * Initialize the imported page.
     * @param writer The PdfWriter
     * @throws IOException
     */
    public void useStationary(PdfWriter writer) throws IOException {
        writer.setPageEvent(this);
        PdfReader reader = new PdfReader(STATIONERY);
        page = writer.getImportedPage(reader, 1);
    }
    
    /**
     * @see com.itextpdf.text.pdf.PdfPageEventHelper#onEndPage(
     *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
     */
    public void onEndPage(PdfWriter writer, Document document) {
        writer.getDirectContentUnder().addTemplate(page, 0, 0);
    }

    /**
     * Creates a PDF document.
     * @param filename the path to the new PDF document
     * @throws    DocumentException 
     * @throws    IOException
     * @throws    SQLException
     */
    public static void createStationary(String filename)
        throws IOException, DocumentException {
        // step 1
    	Document document = new Document();
    	// step 2
        PdfWriter writer
            = PdfWriter.getInstance(document, new FileOutputStream(filename));
        // step 3
        document.open();
        // step 4
        PdfPTable table = new PdfPTable(1);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(new Phrase("FOOBAR FILM FESTIVAL", FilmFonts.BOLD));
        document.add(table);
        Font font = new Font(FontFamily.HELVETICA, 52, Font.BOLD, new GrayColor(0.75f));
        ColumnText.showTextAligned(writer.getDirectContentUnder(),
                Element.ALIGN_CENTER, new Phrase("FOOBAR FILM FESTIVAL", font),
                297.5f, 421, 45);
        // step 5
        document.close();
    }
}
