/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part1.chapter05;

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
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPTableEvent;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.RGBColor;

public class AlternatingBackground implements PdfPTableEvent {

    public static final String RESULT = "results/part1/chapter05/alternating.pdf";

    public static void main(String[] args) throws SQLException, DocumentException, IOException {
        new AlternatingBackground().createPdf(RESULT);
    }
    
    public void createPdf(String filename) throws SQLException, DocumentException, IOException {
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        document.open();
        List<Date> days = PojoFactory.getDays(connection);
        PdfPTableEvent event = new AlternatingBackground();
        for (Date day : days) {
            PdfPTable table = getTable(connection, day);
            table.setTableEvent(event);
            document.add(table);
            document.newPage();
        }
        document.close();
        connection.close();
    }
    
    public PdfPTable getTable(DatabaseConnection connection, Date day) throws SQLException, DocumentException, IOException {
        PdfPTable table = new PdfPTable(new float[] { 2, 1, 2, 5, 1 });
        table.setWidthPercentage(100f);
        table.getDefaultCell().setPadding(3);
        table.getDefaultCell().setUseAscender(true);
        table.getDefaultCell().setUseDescender(true);
        table.getDefaultCell().setColspan(5);
        table.getDefaultCell().setBackgroundColor(RGBColor.RED);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(day.toString());
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        table.getDefaultCell().setColspan(1);
        table.getDefaultCell().setBackgroundColor(RGBColor.ORANGE);
        for (int i = 0; i < 2; i++) {
            table.addCell("Location");
            table.addCell("Time");
            table.addCell("Run Length");
            table.addCell("Title");
            table.addCell("Year");
        }
        table.getDefaultCell().setBackgroundColor(null);
        table.setHeaderRows(3);
        table.setFooterRows(1);
        List<Screening> screenings = PojoFactory.getScreenings(connection, day);
        Movie movie;
        for (Screening screening : screenings) {
            movie = screening.getMovie();
            table.addCell(screening.getLocation());
            table.addCell(String.format("%1$tH:%1$tM", screening.getTime()));
            table.addCell(String.format("%d '", movie.getDuration()));
            table.addCell(movie.getMovieTitle());
            table.addCell(String.valueOf(movie.getYear()));
        }
        return table;
    }
    
    public void tableLayout(PdfPTable table, float[][] widths, float[] heights, int headerRows, int rowStart, PdfContentByte[] canvases) {
        int columns;
        Rectangle rect;
        int footer = widths.length - table.getFooterRows();
        int header = table.getHeaderRows() - table.getFooterRows() + 1;
        for (int row = header; row < footer; row += 2) {
            columns = widths[row].length - 1;
            rect = new Rectangle(widths[row][0], heights[row],
                        widths[row][columns], heights[row + 1]);
            rect.setBackgroundColor(RGBColor.YELLOW);
            rect.setBorder(Rectangle.NO_BORDER);
            canvases[PdfPTable.BASECANVAS].rectangle(rect);
        }
    }

}
