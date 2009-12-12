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
import java.sql.SQLException;
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

    public static final String RESULT = "results/part1/chapter04/calendar.pdf";
    public static final int YEAR = 2011;
    public static final String LANGUAGE = "en";
    public static final String RESOURCE = "resources/calendar/%tm.jpg";
    public static final String SPECIAL = "resources/calendar/%d.txt";
    public static final String CONTENT = "resources/calendar/content.txt";

    public static Properties specialDays = new Properties();
    public static Properties content = new Properties();

    public Font normal;
    public Font small;
    public Font bold;
    
    public static void main(String[] args) throws IOException, DocumentException {
        Locale locale = new Locale(LANGUAGE);
        new PdfCalendar().createPdf(RESULT, locale, YEAR);
    }
    
    public PdfCalendar() throws DocumentException, IOException {
        BaseFont bf_normal = BaseFont.createFont("c://windows/fonts/arial.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED);
        normal = new Font(bf_normal, 16);
        small = new Font(bf_normal, 8);
        BaseFont bf_bold = BaseFont.createFont("c://windows/fonts/arialbd.ttf", BaseFont.WINANSI,BaseFont.EMBEDDED);
        bold = new Font(bf_bold, 14);
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
        
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(RESULT));
        document.open();
        
        PdfPTable table;
        Calendar calendar;
        PdfContentByte canvas = writer.getDirectContent();
        for (int month = 0; month < 12; month++) {
            calendar = new GregorianCalendar(year, month, 1);
            drawImageAndText(canvas, calendar);
            table = new PdfPTable(7);
            table.setTotalWidth(504);
            table.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
            table.addCell(getMonthCell(calendar, locale));
            int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            int day = 1;
            int position = 2;
            while (position != calendar.get(Calendar.DAY_OF_WEEK)) {
                position = (position % 7) + 1;
                table.addCell("");
            }
            while (day <= daysInMonth) {
                calendar = new GregorianCalendar(year, month, day++);
                table.addCell(getDayCell(calendar, locale));
            }
            table.completeRow();
            table.writeSelectedRows(0, -1, 169, table.getTotalHeight() + 18, canvas);
            document.newPage();
        }
        
        document.close();
    }

    public void drawImageAndText(PdfContentByte canvas, Calendar calendar) throws DocumentException, IOException {
        Image img = Image.getInstance(String.format(RESOURCE, calendar));
        img.scaleToFit(PageSize.A4.getHeight(), PageSize.A4.getWidth());
        img.setAbsolutePosition(
                (PageSize.A4.getHeight() - img.getScaledWidth()) / 2,
                (PageSize.A4.getWidth() - img.getScaledHeight()) / 2);
        canvas.addImage(img);
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

    public PdfPCell getDayCell(Calendar calendar, Locale locale) {
        PdfPCell cell = new PdfPCell();
        cell.setPadding(3);
        if (isSunday(calendar))
            cell.setBackgroundColor(BaseColor.GRAY);
        else if (isSpecialDay(calendar))
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        else
            cell.setBackgroundColor(BaseColor.WHITE);
        Chunk chunk = new Chunk(String.format(locale, "%1$ta", calendar), small);
        chunk.setTextRise(8);
        Paragraph p = new Paragraph(chunk);
        p.add(new Chunk(new VerticalPositionMark()));
        p.add(new Chunk(String.format(locale, "%1$te", calendar), normal));
        cell.addElement(p);
        return cell;
    }
    
    public boolean isSunday(Calendar calendar) {
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            return true;
        }
        return false;
    }
    
    public boolean isSpecialDay(Calendar calendar) {
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
            return true;
        if (specialDays.containsKey(String.format("%1$tm%1$td", calendar)))
            return true;
        return false;
    }
}
