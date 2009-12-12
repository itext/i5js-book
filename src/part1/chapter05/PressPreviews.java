/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part1.chapter05;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;
import com.lowagie.filmfestival.Screening;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPTableEvent;
import com.itextpdf.text.pdf.PdfWriter;

public class PressPreviews implements PdfPCellEvent, PdfPTableEvent {

    public void tableLayout(PdfPTable table, float[][] width, float[] height,
            int headerRows, int rowStart, PdfContentByte[] canvas) {
        float widths[] = width[0];
        float x1 = widths[0];
        float x2 = widths[widths.length - 1];
        float y1 = height[0];
        float y2 = height[height.length - 1];
        PdfContentByte cb = canvas[PdfPTable.LINECANVAS];
        cb.rectangle(x1, y1, x2 - x1, y2 - y1);
        cb.stroke();
        cb.resetRGBColorStroke();
    }

    /**
     * @see com.lowagie.text.pdf.PdfPCellEvent#cellLayout(com.lowagie.text.pdf.PdfPCell,
     *      com.lowagie.text.Rectangle, com.lowagie.text.pdf.PdfContentByte[])
     */
    public void cellLayout(PdfPCell cell, Rectangle position,
            PdfContentByte[] canvases) {
        float x1 = position.getLeft() + 2;
        float x2 = position.getRight() - 2;
        float y1 = position.getTop() - 2;
        float y2 = position.getBottom() + 2;
        PdfContentByte canvas = canvases[PdfPTable.LINECANVAS];
        canvas.rectangle(x1, y1, x2 - x1, y2 - y1);
        canvas.stroke();
        canvas.resetRGBColorStroke();
    }
    
    public static final String RESULT = "results/part1/chapter05/press_previews.pdf";

    public static void main(String[] args) throws SQLException, DocumentException, IOException {
        new PressPreviews().createPdf(RESULT);
    }

    /**
     * Creates a PDF document.
     * @param filename the path to the new PDF document
     * @throws    DocumentException 
     * @throws    IOException
     * @throws    SQLException
     */
    public void createPdf(String filename) throws SQLException, DocumentException, IOException {
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        document.open();
        document.add(getTable(connection));
        document.close();
        connection.close();
    }
    
    public PdfPTable getTable(DatabaseConnection connection) throws SQLException, DocumentException, IOException {
        PdfPTable table = new PdfPTable(new float[] { 1, 2, 2, 5, 1 });
        table.setTableEvent(new PressPreviews());
        table.setWidthPercentage(100f);
        table.getDefaultCell().setPadding(5);
        table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
        table.getDefaultCell().setCellEvent(new PressPreviews());
        for (int i = 0; i < 2; i++) {
            table.addCell("Location");
            table.addCell("Date/Time");
            table.addCell("Run Length");
            table.addCell("Title");
            table.addCell("Year");
        }
        table.getDefaultCell().setBackgroundColor(null);
        table.setHeaderRows(2);
        table.setFooterRows(1);
        List<Screening> screenings = PojoFactory.getPressPreviews(connection);
        Movie movie;
        for (Screening screening : screenings) {
            movie = screening.getMovie();
            table.addCell(screening.getLocation());
            table.addCell(String.format("%s   %2$tH:%2$tM", screening.getDate().toString(), screening.getTime()));
            table.addCell(String.format("%d '", movie.getDuration()));
            table.addCell(movie.getMovieTitle());
            table.addCell(String.valueOf(movie.getYear()));
        }
        return table;
    }
}
