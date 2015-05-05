/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part1.chapter03;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.BaseColor;

/**
 * Draws a time table to the direct content using lines and simple shapes.
 */
public class MovieTimeTable {

    /** The resulting PDF. */
    public static final String RESULT = "results/part1/chapter03/time_table.pdf";
    
    /**
     * Creates a PDF file containing a time table for our filmfestival.
     * @param    filename    the name of the PDF file
     * @throws DocumentException
     * @throws IOException 
     */
    public void createPdf(String filename)
        throws DocumentException, IOException {
    	// step 1
        Document document = new Document(PageSize.A4.rotate());
        // step 2
        PdfWriter writer
            = PdfWriter.getInstance(document, new FileOutputStream(filename));
        // step 3
        document.open();
        // step 4
        drawTimeTable(writer.getDirectContentUnder());
        drawTimeSlots(writer.getDirectContent());
        // step 5
        document.close();
    }

    /** The number of locations on our time table. */
    public static final int LOCATIONS = 9;
    /** The number of time slots on our time table. */
    public static final int TIMESLOTS = 32;
    
    /** The offset to the left of our time table. */
    public static final float OFFSET_LEFT = 76;
    /** The width of our time table. */
    public static final float WIDTH = 740;
    /** The offset from the bottom of our time table. */
    public static final float OFFSET_BOTTOM = 36;
    /** The height of our time table */
    public static final float HEIGHT = 504;
    
    /** The offset of the location bar next to our time table. */
    public static final float OFFSET_LOCATION = 26;
    /** The width of the location bar next to our time table. */
    public static final float WIDTH_LOCATION = 48;
    
    /** The height of a bar showing the movies at one specific location. */
    public static final float HEIGHT_LOCATION = HEIGHT / LOCATIONS;
    /** The width of a time slot. */
    public static final float WIDTH_TIMESLOT = WIDTH / TIMESLOTS;
    
    /**
     * Draws the time table for a day at the film festival.
     * @param directcontent a canvas to which the time table has to be drawn.
     */
    protected void drawTimeTable(PdfContentByte directcontent) {        
        directcontent.saveState();
        
        directcontent.setLineWidth(1.2f);
        float llx, lly, urx, ury;
        
        llx = OFFSET_LEFT;
        lly = OFFSET_BOTTOM;
        urx = OFFSET_LEFT + WIDTH;
        ury = OFFSET_BOTTOM + HEIGHT;
        directcontent.moveTo(llx, lly);
        directcontent.lineTo(urx, lly);
        directcontent.lineTo(urx, ury);
        directcontent.lineTo(llx, ury);
        directcontent.closePath();
        directcontent.stroke();
        
        llx = OFFSET_LOCATION;
        lly = OFFSET_BOTTOM;
        urx = OFFSET_LOCATION + WIDTH_LOCATION;
        ury = OFFSET_BOTTOM + HEIGHT;
        directcontent.moveTo(llx, lly);
        directcontent.lineTo(urx, lly);
        directcontent.lineTo(urx, ury);
        directcontent.lineTo(llx, ury);
        directcontent.closePathStroke();
        
        directcontent.setLineWidth(1);
        directcontent.moveTo(OFFSET_LOCATION + WIDTH_LOCATION / 2, OFFSET_BOTTOM);
        directcontent.lineTo(OFFSET_LOCATION + WIDTH_LOCATION / 2, OFFSET_BOTTOM + HEIGHT);
        float y;
        for (int i = 1; i < LOCATIONS; i++) {
            y = OFFSET_BOTTOM + (i * HEIGHT_LOCATION);
            if (i == 2 || i == 6) {
                directcontent.moveTo(OFFSET_LOCATION, y);
                directcontent.lineTo(OFFSET_LOCATION + WIDTH_LOCATION, y);
            }
            else {
                directcontent.moveTo(OFFSET_LOCATION + WIDTH_LOCATION / 2, y);
                directcontent.lineTo(OFFSET_LOCATION + WIDTH_LOCATION, y);
            }
            directcontent.moveTo(OFFSET_LEFT, y);
            directcontent.lineTo(OFFSET_LEFT + WIDTH, y);
        }
        directcontent.stroke();
        
        directcontent.restoreState();
    }
    
    /**
     * Draws the time slots for a day at the film festival.
     * @param directcontent the canvas to which the time table has to be drawn.
     */
    protected void drawTimeSlots(PdfContentByte directcontent) {
        directcontent.saveState();
        float x;
        for (int i = 1; i < TIMESLOTS; i++) {
            x = OFFSET_LEFT + (i * WIDTH_TIMESLOT);
            directcontent.moveTo(x, OFFSET_BOTTOM);
            directcontent.lineTo(x, OFFSET_BOTTOM + HEIGHT);
        }
        directcontent.setLineWidth(0.3f);
        directcontent.setColorStroke(BaseColor.GRAY);
        directcontent.setLineDash(3, 1);
        directcontent.stroke();
        directcontent.restoreState();
    }
    
    /**
     * Main method creating the PDF.
     * @param    args    no arguments needed
     * @throws IOException 
     * @throws DocumentException 
     */
    public static void main(String[] args)
        throws DocumentException, IOException {
        new MovieTimeTable().createPdf(RESULT);
    }
}
