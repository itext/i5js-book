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
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

public class StampStationery {

    public static final String ORIGINAL = "results/part2/chapter06/original.pdf";
    /** The resulting PDF. */
    public static final String RESULT = "results/part2/chapter06/stamped_stationary.pdf";
    
    public static void main(String[] args) throws IOException, DocumentException, SQLException {
        Stationery.createStationary(Stationery.STATIONERY);
        StampStationery stationary = new StampStationery();
        stationary.createPdf(ORIGINAL);
        stationary.manipulatePdf(ORIGINAL, Stationery.STATIONERY, RESULT);
    }
    
    public void manipulatePdf(String src, String stationery, String dest) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        PdfReader s_reader = new PdfReader(stationery);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        PdfImportedPage page = stamper.getImportedPage(s_reader, 1);
        int n = reader.getNumberOfPages();
        PdfContentByte background;
        for (int i = 1; i <= n; i++) {
            background = stamper.getUnderContent(i);
            background.addTemplate(page, 0, 0);
        }
        stamper.close();
    }
    
    public void createPdf(String filename) throws SQLException, IOException, DocumentException {
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        Document document = new Document(PageSize.A4, 36, 36, 72, 36);
        PdfWriter.getInstance(document, new FileOutputStream(filename));
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
    }
}
