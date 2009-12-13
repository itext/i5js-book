/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part1.chapter04;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Properties;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;

public class PdfCalendar {

    /** The resulting PDF file. */
    public static final String RESULT = "results/part1/chapter04/calendar.pdf";
    /** The year for which we want to create a calendar */
    public static final int YEAR = 2011;
    /** The language code for the calendar */
    public static final String LANGUAGE = "en";
    /** Path to the resources. */
    public static final String RESOURCE = "resources/calendar/%tm.jpg";
    /** Path to the resources. */
    public static final String SPECIAL = "resources/calendar/%d.txt";
    /** Path to the resources. */
    public static final String CONTENT = "resources/calendar/content.txt";

    /** Collection with special days */
    public static Properties specialDays = new Properties();
    /** Collection with the description of the images */
    public static Properties content = new Properties();

    /** A font that is used in the calendar */
    protected Font normal;
    /** A font that is used in the calendar */
    protected Font small;
    /** A font that is used in the calendar */
    protected Font bold;

    /**
     * Main method.
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException
     */
    public static void main(String[] args)
        throws IOException, DocumentException {
        Locale locale = new Locale(LANGUAGE);
        new PdfCalendar().createPdf(RESULT, locale, YEAR);
    }
    
    /**
     * Initializes the fonts and collections.
     * @throws DocumentException
     * @throws IOException
     */
    public PdfCalendar() throws DocumentException, IOException {
        // fonts
    	BaseFont bf_normal
            = BaseFont.createFont("c://windows/fonts/arial.ttf",
                BaseFont.WINANSI, BaseFont.EMBEDDED);
        normal = new Font(bf_normal, 16);
        small = new Font(bf_normal, 8);
        BaseFont bf_bold
            = BaseFont.createFont("c://windows/fonts/arialbd.ttf",
                BaseFont.WINANSI,BaseFont.EMBEDDED);
        bold = new Font(bf_bold, 14);
        // collections
        specialDays.load(new FileInputStream(String.format(SPECIAL, YEAR)));
        content.load(new FileInputStream(CONTENT));
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
            table.setTotalWidth(504);
            // add the name of the month
            table.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
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
            table.writeSelectedRows(0, -1, 169, table.getTotalHeight() + 18, canvas);
            document.newPage();
        }
        // step 5
        document.close();
    }

    /**
     * Draws the image of the month to the calendar.
     * @param canvas the direct content layer
     * @param calendar the month (to know which picture to use)
     * @throws DocumentException
     * @throws IOException
     */
    public void drawImageAndText(PdfContentByte canvas, Calendar calendar) throws DocumentException, IOException {
        // get the image
    	Image img = Image.getInstance(String.format(RESOURCE, calendar));
        img.scaleToFit(PageSize.A4.getHeight(), PageSize.A4.getWidth());
        img.setAbsolutePosition(
                (PageSize.A4.getHeight() - img.getScaledWidth()) / 2,
                (PageSize.A4.getWidth() - img.getScaledHeight()) / 2);
        canvas.addImage(img);
        // add metadata
        canvas.saveState();
        canvas.setCMYKColorFill(0x00, 0x00, 0x00, 0x80);
        Phrase p = new Phrase(String.format(
                "%s - \u00a9 Katharine Osborne",
                content.getProperty(String.format("%tm.jpg", calendar))), small);
        ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, p, 5, 5, 0);
        p = new Phrase("Calendar generated using iText - example for the book iText in Action 2nd Edition", small);
        ColumnText.showTextAligned(canvas, Element.ALIGN_RIGHT, p, 839, 5, 0);
        canvas.restoreState();
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
        cell.setBackgroundColor(BaseColor.WHITE);
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
        cell.setPadding(3);
        // set the background color, based on the type of day
        if (isSunday(calendar))
            cell.setBackgroundColor(BaseColor.GRAY);
        else if (isSpecialDay(calendar))
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        else
            cell.setBackgroundColor(BaseColor.WHITE);
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
     * Returns true for Sundays.
     * @param calendar a date
     * @return true for Sundays
     */
    public boolean isSunday(Calendar calendar) {
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            return true;
        }
        return false;
    }
    
    /**
     * Returns true if the date was found in a list with special days (holidays).
     * @param calendar a date
     * @return true for holidays
     */
    public boolean isSpecialDay(Calendar calendar) {
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
            return true;
        if (specialDays.containsKey(String.format("%1$tm%1$td", calendar)))
            return true;
        return false;
    }
}
