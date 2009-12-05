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

    public static final String RESULT = "results/part2/chapter07/timetable_links.pdf";
    /** Path to IMDB. */
    public static final String IMDB = "http://imdb.com/title/tt%s/";
    
    public static void main(String[] args) throws IOException, DocumentException, SQLException {
        MovieTemplates.main(args);
        new TimetableAnnotations2().manipulatePdf(MovieTemplates.RESULT, RESULT);
    }

    public void manipulatePdf(String src, String dest) throws SQLException, IOException, DocumentException {
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        locations = PojoFactory.getLocations(connection);
        
        PdfReader reader = new PdfReader(src);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
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
        stamper.close();
        connection.close();
    }
}
