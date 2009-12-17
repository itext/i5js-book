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
import java.util.List;

import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;
import com.lowagie.filmfestival.Screening;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import part1.chapter03.MovieTemplates;

public class TimetableAnnotations1 {

    /** The resulting PDF. */
    public static final String RESULT
        = "results/part2/chapter07/timetable_help.pdf";
    /** A pattern for an info string. */
    public static final String INFO
        = "Movie produced in %s; run length: %s";

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
        // Add the annotations
        int page = 1;
        Rectangle rect;
        PdfAnnotation annotation;
        Movie movie;
        for (Date day : PojoFactory.getDays(connection)) {
            for (Screening screening : PojoFactory.getScreenings(connection, day)) {
                movie = screening.getMovie();
                rect = getPosition(screening);
                annotation = PdfAnnotation.createText(
                    stamper.getWriter(), rect, movie.getMovieTitle(),
                    String.format(INFO, movie.getYear(), movie.getDuration()),
                    false, "Help");
                annotation.setColor(WebColors.getRGBColor(
                    "#" + movie.getEntry().getCategory().getColor()));
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
     * Calculates the position of a rectangle corresponding with a screening.
     * @param    screening    a screening POJO, contains a movie
     * @return    a Rectangle
     */
    protected Rectangle getPosition(Screening screening) {
        float llx, lly, urx, ury;
        long minutesAfter930 = (screening.getTime().getTime() - TIME930) / 60000l;
        llx = OFFSET_LEFT + (MINUTE * minutesAfter930);
        int location = locations.indexOf(screening.getLocation()) + 1;
        lly = OFFSET_BOTTOM + (LOCATIONS - location) * HEIGHT_LOCATION;
        urx = llx + MINUTE * screening.getMovie().getDuration();
        ury = lly + HEIGHT_LOCATION;
        Rectangle rect = new Rectangle(llx, lly, urx, ury);
        return rect;
    }

    /** A list containing all the locations. */
    protected List<String> locations;
    
    /** The number of locations on our time table. */
    public static final int LOCATIONS = 9;
    /** The number of time slots on our time table. */
    public static final int TIMESLOTS = 32;
    /** The "offset time" for our calendar sheets. */
    public static final long TIME930 = 30600000l;
    /** The offset to the left of our time table. */
    public static final float OFFSET_LEFT = 76;
    /** The width of our time table. */
    public static final float WIDTH = 740;
    /** The width of a time slot. */
    public static final float WIDTH_TIMESLOT = WIDTH / TIMESLOTS;
    /** The width of one minute. */
    public static final float MINUTE = WIDTH_TIMESLOT / 30f;
    /** The offset from the bottom of our time table. */
    public static final float OFFSET_BOTTOM = 36;
    /** The height of our time table */
    public static final float HEIGHT = 504;
    /** The height of a bar showing the movies at one specific location. */
    public static final float HEIGHT_LOCATION = HEIGHT / LOCATIONS;

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
        new TimetableAnnotations1().manipulatePdf(
            MovieTemplates.RESULT, RESULT);
    }
}
