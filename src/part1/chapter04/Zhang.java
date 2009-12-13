/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part1.chapter04;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class Zhang {

    /** The resulting PDF file. */
    public static final String RESULT = "results/part1/chapter04/zhang.pdf";

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
            = PdfWriter.getInstance(document, new FileOutputStream(filename));
        // step 3
        document.open();
        // step 4
        // Create a table and fill it with movies
        List<Movie> movies = PojoFactory.getMovies(connection, 3);
        PdfPTable table = new PdfPTable(new float[] { 1, 5, 5, 1});
        for (Movie movie : movies) {
            table.addCell(String.valueOf(movie.getYear()));
            table.addCell(movie.getMovieTitle());
            table.addCell(movie.getOriginalTitle());
            table.addCell(String.valueOf(movie.getDuration()));
        }
        // set the total width of the table
        table.setTotalWidth(600);
        PdfContentByte canvas = writer.getDirectContent();
        // draw the first three columns on one page
        table.writeSelectedRows(0, 2, 0, -1, 236, 806, canvas);
        document.newPage();
        // draw the next three columns on the next page
        table.writeSelectedRows(2, -1, 0, -1, 36, 806, canvas);
        // step 5
        document.close();
        // close the database connection
        connection.close();
    }

    /**
     * Main method.
     * @param args no arguments needed
     * @throws DocumentException 
     * @throws IOException
     * @throws SQLException
     */
    public static void main(String[] args) throws IOException, DocumentException, SQLException {
        new Zhang().createPdf(RESULT);
    }
}
