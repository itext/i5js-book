/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part1.chapter03;

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
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfWriter;

public class MovieColumns3 extends MovieColumns1 {

    /** The resulting PDF file. */
    public static final String RESULT = "results/part1/chapter03/movie_columns3.pdf";
    
    /**
     * Creates a PDF with information about the movies
     * @param    filename the name of the PDF file that will be created.
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
        List<Movie> movies = PojoFactory.getMovies(connection);
        ColumnText ct = new ColumnText(writer.getDirectContent());
        int column = 0;
        ct.setSimpleColumn(
            COLUMNS[column][0], COLUMNS[column][1],
            COLUMNS[column][2], COLUMNS[column][3]);
        int status = ColumnText.START_COLUMN;
        Phrase p;
        float y;
        for (Movie movie : movies) {
            y = ct.getYLine();
            p = createMovieInformation(movie);
            ct.addText(p);
            status = ct.go(true);
            if (ColumnText.hasMoreText(status)) {
                column = Math.abs(column - 1);
                if (column == 0)
                    document.newPage();
                ct.setSimpleColumn(
                    COLUMNS[column][0], COLUMNS[column][1],
                    COLUMNS[column][2], COLUMNS[column][3]);
                y = COLUMNS[column][3];
            }
            ct.setYLine(y);
            ct.setText(p);
            status = ct.go(false);
        }
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
        new MovieColumns3().createPdf(RESULT);
    }
}
