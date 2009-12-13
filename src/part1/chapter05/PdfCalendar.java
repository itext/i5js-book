/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part1.chapter05;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPTableEvent;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;

public class PdfCalendar extends part1.chapter04.PdfCalendar {

    /** The resulting PDF. */
    public static final String RESULT = "results/part1/chapter05/calendar.pdf";

    /** A table event. */
    public PdfPTableEvent tableBackground;
    /** A cell event. */
    public PdfPCellEvent cellBackground;
    /** A cell event. */
    public PdfPCellEvent roundRectangle;
    /** A cell event. */
    public PdfPCellEvent whiteRectangle;
    
    /**
     * Inner class with a table event that draws a background with rounded corners.
     */
    class TableBackground implements PdfPTableEvent {
        
        public void tableLayout(PdfPTable table, float[][] width, float[] height,
                int headerRows, int rowStart, PdfContentByte[] canvas) {
            PdfContentByte background = canvas[PdfPTable.BASECANVAS];
            background.saveState();
            background.setCMYKColorFill(0x00, 0x00, 0xFF, 0x0F);
            background.roundRectangle(
                width[0][0], height[height.length - 1] - 2,
                width[0][1] - width[0][0] + 6, height[0] - height[height.length - 1] - 4, 4);
            background.fill();
            background.restoreState();
        }

    }
    
    /**
     * Inner class with a cell event that draws a background with rounded corners.
     */
    class CellBackground implements PdfPCellEvent {
        
        public void cellLayout(PdfPCell cell, Rectangle rect,
                PdfContentByte[] canvas) {
            PdfContentByte cb = canvas[PdfPTable.BACKGROUNDCANVAS];
            cb.roundRectangle(
                rect.getLeft() + 1.5f, rect.getBottom() + 1.5f, rect.getWidth() - 3,
                rect.getHeight() - 3, 4);
            cb.setCMYKColorFill(0x00, 0x00, 0x00, 0x00);
            cb.fill();
        }
    }

    /**
     * Inner class with a cell event that draws a border with rounded corners.
     */
    class RoundRectangle implements PdfPCellEvent {
        /** the border color described as CMYK values. */
        protected int[] color;
        /** Constructs the event using a certain color. */
        public RoundRectangle(int[] color) {
            this.color = color;
        }
        
        public void cellLayout(PdfPCell cell, Rectangle rect,
                PdfContentByte[] canvas) {
            PdfContentByte cb = canvas[PdfPTable.LINECANVAS];
            cb.roundRectangle(
                rect.getLeft() + 1.5f, rect.getBottom() + 1.5f, rect.getWidth() - 3,
                rect.getHeight() - 3, 4);
            cb.setLineWidth(1.5f);
            cb.setCMYKColorStrokeF(color[0], color[1], color[2], color[3]);
            cb.stroke();
        }
    }
    
    /**
     * Main method creating the PDF.
     * @param    args    no arguments needed
     * @throws IOException 
     * @throws DocumentException 
     */
    public static void main(String[] args) throws IOException, DocumentException {
        Locale locale = new Locale(LANGUAGE);
        new PdfCalendar().createPdf(RESULT, locale, YEAR);
    }

    /**
     * Initializes the fonts and collections.
     * @throws DocumentException
     * @throws IOException
     */
    public PdfCalendar() throws DocumentException, IOException {
        super();
        tableBackground = new TableBackground();
        cellBackground = new CellBackground();
        roundRectangle = new RoundRectangle(new int[]{ 0xFF, 0x00, 0xFF, 0x00 });
        whiteRectangle = new RoundRectangle(new int[]{ 0x00, 0x00, 0x00, 0x00 });
    }

    /**
     * Creates a PDF document.
     * @param filename the path to the new PDF document
     * @param locale Locale in case you want to create
     * a Calendar in another language
     * @param year the year for which you want to make a calendar
     * @throws    DocumentException 
     * @throws    IOException
     */
    public void createPdf(String filename, Locale locale, int year) throws IOException, DocumentException {
        // step 1
        Document document = new Document(PageSize.A4.rotate());
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(RESULT));
        // step 3
        document.open();
        // step 4
        PdfPTable table;
        Calendar calendar;
        PdfContentByte canvas = writer.getDirectContent();
        // Loop over the months
        for (int month = 0; month < 12; month++) {
            calendar = new GregorianCalendar(year, month, 1);
            // draw the background
            drawImageAndText(canvas, calendar);
            // create a table with 7 columns
            table = new PdfPTable(7);
            table.setTableEvent(tableBackground);
            table.setTotalWidth(504);
            // add the name of the month
            table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
            table.getDefaultCell().setCellEvent(whiteRectangle);
            table.addCell(getMonthCell(calendar, locale));
            int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            int day = 1;
            int position = 2;
            // add empty cells
            while (position != calendar.get(Calendar.DAY_OF_WEEK)) {
                position = (position % 7) + 1;
                table.addCell("");
            }
            // add cells for each day
            while (day <= daysInMonth) {
                calendar = new GregorianCalendar(year, month, day++);
                table.addCell(getDayCell(calendar, locale));
            }
            // complete the table
            table.completeRow();
            // write the table to an absolute position
            table.writeSelectedRows(0, -1, 169, table.getTotalHeight() + 20, canvas);
            document.newPage();
        }
        // step 5
        document.close();
    }
    
    /**
     * Creates a PdfPCell with the name of the month
     * @param calendar a date
     * @param locale a locale
     * @return a PdfPCell with rowspan 7, containing the name of the month
     */
    public PdfPCell getMonthCell(Calendar calendar, Locale locale) {
        PdfPCell cell = new PdfPCell();
        cell.setColspan(7);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setUseDescender(true);
        Paragraph p = new Paragraph(String.format(locale, "%1$tB %1$tY", calendar), bold);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        return cell;
    }

    /**
     * Creates a PdfPCell for a specific day
     * @param calendar a date
     * @param locale a locale
     * @return a PdfPCell
     */
    public PdfPCell getDayCell(Calendar calendar, Locale locale) {
        PdfPCell cell = new PdfPCell();
        // set the event to draw the background
        cell.setCellEvent(cellBackground);
        // set the event to draw a special border
        if (isSunday(calendar) || isSpecialDay(calendar))
            cell.setCellEvent(roundRectangle);
        cell.setPadding(3);
        cell.setBorder(PdfPCell.NO_BORDER);
        // set the content in the language of the locale
        Chunk chunk = new Chunk(String.format(locale, "%1$ta", calendar), small);
        chunk.setTextRise(8);
        // a paragraph with the day
        Paragraph p = new Paragraph(chunk);
        // a separator
        p.add(new Chunk(new VerticalPositionMark()));
        // and the number of the day
        p.add(new Chunk(String.format(locale, "%1$te", calendar), normal));
        cell.addElement(p);
        return cell;
    }
    
    /**
     * Returns true if the date was found in a list with special days (holidays).
     * @param calendar a date
     * @return true for holidays
     */
    public boolean isSpecialDay(Calendar calendar) {
        if (specialDays.containsKey(String.format("%1$tm%1$td", calendar)))
            return true;
        return false;
    }
}
