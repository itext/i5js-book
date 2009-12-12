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
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfWriter;

public class MovieColumns2 extends MovieColumns1 {

    /** The resulting PDF file. */
    public static final String RESULT
        = "results/part1/chapter03/movie_columns2.pdf";
    
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
        ct.setAlignment(Element.ALIGN_JUSTIFIED);
        ct.setExtraParagraphSpace(6);
        ct.setLeading(14);
        ct.setIndent(10);
        ct.setRightIndent(3);
        ct.setSpaceCharRatio(PdfWriter.NO_SPACE_CHAR_RATIO);
        int column = 0;
        int status = ColumnText.START_COLUMN;
        ct.setSimpleColumn(
            COLUMNS[column][0], COLUMNS[column][1],
            COLUMNS[column][2], COLUMNS[column][3]);
        for (Movie movie : movies) {
            ct.addText(createMovieInformation(movie));
            status = ct.go();
            if (ColumnText.hasMoreText(status)) {
                column = Math.abs(column - 1);
                if (column == 0)
                    document.newPage();
                ct.setSimpleColumn(
                    COLUMNS[column][0], COLUMNS[column][1],
                    COLUMNS[column][2], COLUMNS[column][3]);
                ct.setYLine(COLUMNS[column][3]);
                status = ct.go();
            }
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
        new MovieColumns2().createPdf(RESULT);
    }
}
