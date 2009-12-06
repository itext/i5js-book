/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part1.chapter04;

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
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.BaseColor;

public class ColumnTable {

    public static final String RESULT = "results/part1/chapter04/column_table.pdf";
    
    public static void main(String[] args) throws SQLException, DocumentException, IOException {
        new ColumnTable().createPdf(RESULT);
    }
    
    public void createPdf(String filename) throws SQLException, DocumentException, IOException {
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
        document.open();
        ColumnText column = new ColumnText(writer.getDirectContent());
        List<Date> days = PojoFactory.getDays(connection);
        float[][] x = {
                { document.left(), document.left() + 380 },
                { document.right() - 380, document.right() }
            };
        for (Date day : days) {
            column.addElement(getTable(connection, day));
            int count = 0;
            float height = 0;
            int status = ColumnText.START_COLUMN;
            while (ColumnText.hasMoreText(status)) {
                if (count == 0) {
                    height = addHeaderTable(document, day, writer.getPageNumber());
                }
                column.setSimpleColumn(
                    x[count][0], document.bottom(),
                    x[count][1], document.top() - height - 10);
                status = column.go();
                if (++count > 1) {
                    count = 0;
                    document.newPage();
                }
            }
            document.newPage();
        }
        document.close();
        connection.close();

    }
    
    public float addHeaderTable(Document document, Date day, int page) throws DocumentException {
        PdfPTable header = new PdfPTable(3);
        header.setWidthPercentage(100);
        header.getDefaultCell().setBackgroundColor(BaseColor.BLACK);
        Font font = new Font(Font.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
        Phrase p = new Phrase("Foobar Film Festival", font);
        header.addCell(p);
        header.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        p = new Phrase(day.toString(), font);
        header.addCell(p);
        header.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
        p = new Phrase(String.format("page %d", page), font);
        header.addCell(p);
        document.add(header);
        return header.getTotalHeight();
    }

    public PdfPTable getTable(DatabaseConnection connection, Date day) throws SQLException, DocumentException, IOException {
        PdfPTable table = new PdfPTable(new float[] { 2, 1, 2, 5, 1 });
        table.setWidthPercentage(100f);
        table.getDefaultCell().setUseAscender(true);
        table.getDefaultCell().setUseDescender(true);
        table.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
        for (int i = 0; i < 2; i++) {
            table.addCell("Location");
            table.addCell("Time");
            table.addCell("Run Length");
            table.addCell("Title");
            table.addCell("Year");
        }
        table.getDefaultCell().setBackgroundColor(null);
        table.setHeaderRows(2);
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
}
