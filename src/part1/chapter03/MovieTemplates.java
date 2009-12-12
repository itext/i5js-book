/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part1.chapter03;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.PojoFactory;
import com.lowagie.filmfestival.Screening;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Draws a time table to the direct content using lines and simple shapes,
 * adding blocks representing a movies.
 */
public class MovieTemplates extends MovieCalendar {

    /** The resulting PDF. */
    public static final String RESULT
        = "results/part1/chapter03/movie_templates.pdf";

    /**
     * Creates a PDF document.
     * @param filename the path to the new PDF document
     * @throws    DocumentException 
     * @throws    IOException
     */
    public void createPdf(String filename)
        throws IOException, DocumentException {
    	// step 1
        Document document = new Document(PageSize.A4.rotate());
        // step 2
        PdfWriter writer
            = PdfWriter.getInstance(document, new FileOutputStream(filename));
        // step 3
        document.open();
        // step 4
        PdfContentByte over = writer.getDirectContent();
        PdfContentByte under = writer.getDirectContentUnder();
        try {
            DatabaseConnection connection = new HsqldbConnection("filmfestival");
            locations = PojoFactory.getLocations(connection);
            PdfTemplate t_under = under.createTemplate(
                    PageSize.A4.getHeight(), PageSize.A4.getWidth());
            drawTimeTable(t_under);
            PdfTemplate t_over = over.createTemplate(
                    PageSize.A4.getHeight(), PageSize.A4.getWidth());
            drawTimeSlots(t_over);
            drawInfo(t_over);
            List<Date> days = PojoFactory.getDays(connection);
            List<Screening> screenings;
            int d = 1;
            for (Date day : days) {
                over.addTemplate(t_over, 0, 0);
                under.addTemplate(t_under, 0, 0);
                drawDateInfo(day, d++, over);
                screenings = PojoFactory.getScreenings(connection, day);
                for (Screening screening : screenings) {
                    drawBlock(screening, under, over);
                    drawMovieInfo(screening, over);
                }
                document.newPage();
            }
            connection.close();
        }
        catch(SQLException sqle) {
            sqle.printStackTrace();
            document.add(new Paragraph("Database error: " + sqle.getMessage()));
        }
        // step 5
        document.close();
    }
    
    /**
     * Constructs a MovieTemplates object.
     * @throws DocumentException
     * @throws IOException
     */
    public MovieTemplates() throws DocumentException, IOException {
        super();
    }
    
    /**
     * Main method creating the PDF.
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException 
     */
    public static void main(String[] args)
        throws IOException, DocumentException {
        new MovieTemplates().createPdf(RESULT);
    }
}
