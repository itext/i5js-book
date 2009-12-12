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


    public static final String RESULT = "results/part1/chapter04/zhang.pdf";
    
    public static void main(String[] args) throws IOException, DocumentException, SQLException {
        new Zhang().createPdf(RESULT);
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
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
        document.open();
        List<Movie> movies = PojoFactory.getMovies(connection, 3);
        PdfPTable table = new PdfPTable(new float[] { 1, 5, 5, 1});
        for (Movie movie : movies) {
            table.addCell(String.valueOf(movie.getYear()));
            table.addCell(movie.getMovieTitle());
            table.addCell(movie.getOriginalTitle());
            table.addCell(String.valueOf(movie.getDuration()));
        }
        table.setTotalWidth(600);
        PdfContentByte canvas = writer.getDirectContent();
        table.writeSelectedRows(0, 2, 0, -1, 236, 806, canvas);
        document.newPage();
        table.writeSelectedRows(2, -1, 0, -1, 36, 806, canvas);
        document.close();
        connection.close();
    }
}
