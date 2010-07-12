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
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;
import com.lowagie.filmfestival.Screening;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfBorderArray;
import com.itextpdf.text.pdf.PdfBorderDictionary;
import com.itextpdf.text.pdf.PdfDashPattern;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.BaseColor;

import part1.chapter03.MovieTemplates;

public class TimetableAnnotations3 extends TimetableAnnotations1 {
    /** The resulting PDF file. */
    public static final String RESULT = "results/part2/chapter07/timetable_tickets.pdf";
    /** Path to IMDB. */
    public static final String IMDB = "http://imdb.com/title/tt%s/";

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
        // Loop over the days and screenings
        int page = 1;
        Rectangle rect;
        float top;
        PdfAnnotation annotation;
        Movie movie;
        for (Date day : PojoFactory.getDays(connection)) {
            for (Screening screening : PojoFactory.getScreenings(connection, day)) {
                rect = getPosition(screening);
                movie = screening.getMovie();
                // Annotation for press previews
                if (screening.isPress()) {
                    annotation = PdfAnnotation.createStamp(stamper.getWriter(),
                        rect, "Press only", "NotForPublicRelease");
                    annotation.setColor(BaseColor.BLACK);
                    annotation.setFlags(PdfAnnotation.FLAGS_PRINT);
                }
                // Annotation for screenings that are sold out
                else if (isSoldOut(screening)) {
                    top = reader.getPageSizeWithRotation(page).getTop();
                    annotation = PdfAnnotation.createLine(
                        stamper.getWriter(), rect, "SOLD OUT",
                        top - rect.getTop(), rect.getRight(),
                        top - rect.getBottom(), rect.getLeft());
                    annotation.setTitle(movie.getMovieTitle());
                    annotation.setColor(BaseColor.WHITE);
                    annotation.setFlags(PdfAnnotation.FLAGS_PRINT);
                    annotation.setBorderStyle(
                        new PdfBorderDictionary(5, PdfBorderDictionary.STYLE_SOLID));
                }
                // Annotation for screenings with tickets available
                else {
                    annotation = PdfAnnotation.createSquareCircle(
                        stamper.getWriter(), rect, "Tickets available", true);
                    annotation.setTitle(movie.getMovieTitle());
                    annotation.setColor(BaseColor.BLUE);
                    annotation.setFlags(PdfAnnotation.FLAGS_PRINT);
                    annotation.setBorder(new PdfBorderArray(0, 0, 2, new PdfDashPattern()));
                }
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
     * Checks if the screening has been sold out.
     * @param screening a Screening POJO
     * @return true if the screening has been sold out.
     */
    public boolean isSoldOut(Screening screening) {
        if (screening.getMovie().getMovieTitle().startsWith("L"))
            return true;
        return false;
    }

    /**
     * Main method.
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException
     */
    public static void main(String[] args) 
        throws IOException, DocumentException, SQLException {
        MovieTemplates.main(args);
        new TimetableAnnotations3().manipulatePdf(
            MovieTemplates.RESULT, RESULT);
    }
}
