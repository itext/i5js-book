/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part2.chapter06;

import java.io.ByteArrayOutputStream;
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
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

public class TwoPasses {

    /** The resulting PDF file. */
    public static final String RESULT = "results/part2/chapter06/page_x_of_y.pdf";
    
    public static void main(String[] args) throws SQLException, DocumentException, IOException {
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 36, 36, 54, 36);
        PdfWriter.getInstance(document, baos);
        document.open();
        
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
                document.add(new Paragraph(String.format("Year: %d; run length: %d minutes", movie.getYear(), movie.getDuration()), FilmFonts.NORMAL));
                document.add(PojoToElementFactory.getDirectorList(movie));
            }
            document.newPage();
        }

        document.close();
        connection.close();
        
        PdfReader reader = new PdfReader(baos.toByteArray());
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(RESULT));
        int n = reader.getNumberOfPages();
        for (int i = 1; i <= n; i++) {
            getHeaderTable(i, n).writeSelectedRows(
                    0, -1, 34, 803, stamper.getOverContent(i));
        }
        stamper.close();
    }
    
    public static PdfPTable getHeaderTable(int x, int y) {
        PdfPTable table = new PdfPTable(2);
        table.setTotalWidth(527);
        table.setLockedWidth(true);
        table.getDefaultCell().setFixedHeight(20);
        table.getDefaultCell().setBorder(Rectangle.BOTTOM);
        table.addCell("FOOBAR FILMFESTIVAL");
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(String.format("Page %d of %d", x, y));
        return table;
    }
}
