/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part2.chapter07;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;

import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.PojoFactory;
import com.lowagie.filmfestival.Screening;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import part1.chapter03.MovieTemplates;

public class TimetableAnnotations2 extends TimetableAnnotations1 {

    /** The resulting PDF. */
    public static final String RESULT
        = "results/part2/chapter07/timetable_links.pdf";
    /** Path to IMDB. */
    public static final String IMDB
        = "http://imdb.com/title/tt%s/";

    /**
     * Manipulates a PDF file src with the file dest as result
     * @param src the original PDF
     * @param dest the resulting PDF
     * @throws IOException
     * @throws DocumentException
     * @throws SQLException
     */
    public void manipulatePdf(String src, String dest)
        throws SQLException, IOException, DocumentException {
    	// Create a database connection
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        locations = PojoFactory.getLocations(connection);
        // Create a reader
        PdfReader reader = new PdfReader(src);
        // Create a stamper
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        // Add annotations for every screening
        int page = 1;
        Rectangle rect;
        PdfAnnotation annotation;
        for (Date day : PojoFactory.getDays(connection)) {
            for (Screening screening : PojoFactory.getScreenings(connection, day)) {
                rect = getPosition(screening);
                annotation = PdfAnnotation.createLink(
                    stamper.getWriter(), rect, PdfAnnotation.HIGHLIGHT_INVERT,
                    new PdfAction(String.format(IMDB, screening.getMovie().getImdb())));
                stamper.addAnnotation(annotation, page);
            }
            page++;
        }
        // Close the stamper
        stamper.close();
        // Close the database connection
        connection.close();
    }
    /**
     * Main method.
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException
     * @throws SQLException
     */
    public static void main(String[] args)
        throws IOException, DocumentException, SQLException {
        MovieTemplates.main(args);
        new TimetableAnnotations2().manipulatePdf(
            MovieTemplates.RESULT, RESULT);
    }
}
