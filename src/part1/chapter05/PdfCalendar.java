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

    public static final String RESULT = "results/part1/chapter05/calendar.pdf";

    public PdfPTableEvent tableBackground;
    public PdfPCellEvent cellBackground;
    public PdfPCellEvent roundRectangle;
    public PdfPCellEvent whiteRectangle;
    
    class TableBackground implements PdfPTableEvent {
        
        public void tableLayout(PdfPTable table, float[][] width, float[] height,
                int headerRows, int rowStart, PdfContentByte[] canvas) {
            PdfContentByte background = canvas[PdfPTable.BASECANVAS];
            background.saveState();
            background.setCMYKColorFill(0x00, 0x00, 0xFF, 0x0F);
            background.roundRectangle(width[0][0], height[height.length - 1] - 2, width[0][1] - width[0][0] + 6, height[0] - height[height.length - 1] - 4, 4);
            background.fill();
            background.restoreState();
        }

    }
    
    class CellBackground implements PdfPCellEvent {
        
        public void cellLayout(PdfPCell cell, Rectangle rect,
                PdfContentByte[] canvas) {
            PdfContentByte cb = canvas[PdfPTable.BACKGROUNDCANVAS];
            cb.roundRectangle(rect.getLeft() + 1.5f, rect.getBottom() + 1.5f, rect.getWidth() - 3,
                    rect.getHeight() - 3, 4);
            cb.setCMYKColorFill(0x00, 0x00, 0x00, 0x00);
            cb.fill();
        }
    }
    
    class RoundRectangle implements PdfPCellEvent {
        
        protected int[] color;

        public RoundRectangle(int[] color) {
            this.color = color;
        }
        
        public void cellLayout(PdfPCell cell, Rectangle rect,
                PdfContentByte[] canvas) {
            PdfContentByte cb = canvas[PdfPTable.LINECANVAS];
            cb.roundRectangle(rect.getLeft() + 1.5f, rect.getBottom() + 1.5f, rect.getWidth() - 3,
                    rect.getHeight() - 3, 4);
            cb.setLineWidth(1.5f);
            cb.setCMYKColorStrokeF(color[0], color[1], color[2], color[3]);
            cb.stroke();
        }
    }

    
    public static void main(String[] args) throws IOException, DocumentException {
        Locale locale = new Locale(LANGUAGE);
        new PdfCalendar().createPdf(RESULT, locale, YEAR);
    }

    public PdfCalendar() throws DocumentException, IOException {
        super();
        tableBackground = new TableBackground();
        cellBackground = new CellBackground();
        roundRectangle = new RoundRectangle(new int[]{ 0xFF, 0x00, 0xFF, 0x00 });
        whiteRectangle = new RoundRectangle(new int[]{ 0x00, 0x00, 0x00, 0x00 });
    }

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
            table.setTableEvent(tableBackground);
            table.setTotalWidth(504);
            table.setLockedWidth(true);
            table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
            table.getDefaultCell().setCellEvent(whiteRectangle);
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
            table.writeSelectedRows(0, -1, 169, table.getTotalHeight() + 20, canvas);
            document.newPage();
        }
        
        document.close();
    }

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

    public PdfPCell getDayCell(Calendar calendar, Locale locale) {
        PdfPCell cell = new PdfPCell();
        cell.setCellEvent(cellBackground);
        if (isSunday(calendar) || isSpecialDay(calendar))
            cell.setCellEvent(roundRectangle);
        cell.setPadding(3);
        cell.setBorder(PdfPCell.NO_BORDER);
        Chunk chunk = new Chunk(String.format(locale, "%1$ta", calendar), small);
        chunk.setTextRise(8);
        Paragraph p = new Paragraph(chunk);
        p.add(new Chunk(new VerticalPositionMark()));
        p.add(new Chunk(String.format(locale, "%1$te", calendar), normal));
        cell.addElement(p);
        return cell;
    }
    
    public boolean isSpecialDay(Calendar calendar) {
        if (specialDays.containsKey(String.format("%1$tm%1$td", calendar)))
            return true;
        return false;
    }
}
