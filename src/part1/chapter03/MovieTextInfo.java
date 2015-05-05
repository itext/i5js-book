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
import java.sql.Date;
import java.util.List;

import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.PojoFactory;
import com.lowagie.filmfestival.Screening;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.BaseColor;

/**
 * Draws a time table to the direct content using lines and simple shapes,
 * adding blocks representing a movies.
 */
public class MovieTextInfo extends MovieTimeBlocks {

    /** The resulting PDF. */
    public static final String RESULT = "results/part1/chapter03/calendar.pdf";

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
            List<Date> days = PojoFactory.getDays(connection);
            List<Screening> screenings;
            int d = 1;
            for (Date day : days) {
                drawTimeTable(under);
                drawTimeSlots(over);
                drawInfo(over);
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
    
    /** The base font that will be used to write info on the calendar sheet. */
    protected BaseFont bf;
    
    /** A phrase containing a white letter "P" (for Press) */
    protected Phrase press;

    /** The different time slots. */
    public static String[] TIME =
        { "09:30", "10:00", "10:30", "11:00", "11:30", "12:00",
          "00:30", "01:00", "01:30", "02:00", "02:30", "03:00",
          "03:30", "04:00", "04:30", "05:00", "05:30", "06:00",
          "06:30", "07:00", "07:30", "08:00", "08:30", "09:00",
          "09:30", "10:00", "10:30", "11:00", "11:30", "12:00",
          "00:30", "01:00" };
    
    /**
     * Draws some text on every calendar sheet.
     * 
     */
    protected void drawInfo(PdfContentByte directcontent) {
        directcontent.beginText();
        directcontent.setFontAndSize(bf, 18);
        float x, y;
        x = (OFFSET_LEFT + WIDTH + OFFSET_LOCATION) / 2;
        y = OFFSET_BOTTOM + HEIGHT + 24;
        directcontent.showTextAligned(Element.ALIGN_CENTER,
            "FOOBAR FILM FESTIVAL", x, y, 0);
        x = OFFSET_LOCATION + WIDTH_LOCATION / 2f - 6;
        y = OFFSET_BOTTOM + HEIGHT_LOCATION;
        directcontent.showTextAligned(Element.ALIGN_CENTER,
            "The Majestic", x, y, 90);
        y = OFFSET_BOTTOM + HEIGHT_LOCATION * 4f;
        directcontent.showTextAligned(Element.ALIGN_CENTER,
            "Googolplex", x, y, 90);
        y = OFFSET_BOTTOM + HEIGHT_LOCATION * 7.5f;
        directcontent.showTextAligned(Element.ALIGN_CENTER,
            "Cinema Paradiso", x, y, 90);
        directcontent.setFontAndSize(bf, 12);
        x = OFFSET_LOCATION + WIDTH_LOCATION - 6;
        for (int i = 0; i < LOCATIONS; i++) {
            y = OFFSET_BOTTOM + ((8.5f - i) * HEIGHT_LOCATION);
            directcontent.showTextAligned(Element.ALIGN_CENTER,
                locations.get(i), x, y, 90);
        }
        directcontent.setFontAndSize(bf, 6);
        y = OFFSET_BOTTOM + HEIGHT + 1;
        for (int i = 0; i < TIMESLOTS; i++) {
            x = OFFSET_LEFT + (i * WIDTH_TIMESLOT);
            directcontent.showTextAligned(Element.ALIGN_LEFT,
                TIME[i], x, y, 45);
        }
        directcontent.endText();
    }
    /**
     * Draws some text on every calendar sheet.
     * 
     */
    protected void drawDateInfo(Date day, int d, PdfContentByte directcontent) {
        directcontent.beginText();
        directcontent.setFontAndSize(bf, 18);
        float x, y;
        x = OFFSET_LOCATION;
        y = OFFSET_BOTTOM + HEIGHT + 24;
        directcontent.showTextAligned(Element.ALIGN_LEFT,
                "Day " + d, x, y, 0);
        x = OFFSET_LEFT + WIDTH;
        directcontent.showTextAligned(Element.ALIGN_RIGHT,
                day.toString(), x, y, 0);
        directcontent.endText();
    }
    
    /**
     * Draws the info about the movie.
     * @throws DocumentException 
     */
    protected void drawMovieInfo(Screening screening, PdfContentByte directcontent)
        throws DocumentException {
        if (screening.isPress()) {
            Rectangle rect = getPosition(screening);
            ColumnText.showTextAligned(directcontent, Element.ALIGN_CENTER,
                    press, (rect.getLeft() + rect.getRight()) / 2,
                    rect.getBottom() + rect.getHeight() / 4, 0);
        }
    }
    
    /**
     * Constructor for the MovieCalendar class; initializes the base font object.
     * @throws IOException 
     * @throws DocumentException 
     */
    public MovieTextInfo() throws DocumentException, IOException {
        bf = BaseFont.createFont();
        Font f = new Font(bf, HEIGHT_LOCATION / 2);
        f.setColor(BaseColor.WHITE);
        press = new Phrase("P", f);
    }
    
    /**
     * Main method creating the PDF.
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException 
     */
    public static void main(String[] args)
        throws IOException, DocumentException {
        new MovieTextInfo().createPdf(RESULT);
    }
}
