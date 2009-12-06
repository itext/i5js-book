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
import com.lowagie.filmfestival.PojoToElementFactory;
import com.lowagie.filmfestival.Screening;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.BaseColor;

public class HeaderFooter1 {

    public static final String RESULT = "results/part1/chapter04/header_footer_1.pdf";
    
    public static void main(String[] args) throws SQLException, DocumentException, IOException {
        new HeaderFooter1().createPdf(RESULT);
    }
    
    public void createPdf(String filename) throws SQLException, DocumentException, IOException {
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        document.open();
        List<Date> days = PojoFactory.getDays(connection);
        for (Date day : days) {
            document.add(getTable(connection, day));
            document.newPage();
        }
        document.close();
        connection.close();

    }

    public PdfPTable getTable(DatabaseConnection connection, Date day) throws SQLException, DocumentException, IOException {
        PdfPTable table = new PdfPTable(new float[] { 2, 1, 2, 5, 1, 3, 2 });
        table.setWidthPercentage(100f);
        table.getDefaultCell().setUseAscender(true);
        table.getDefaultCell().setUseDescender(true);
        Font f = new Font();
        f.setColor(BaseColor.WHITE);
        PdfPCell cell = new PdfPCell(new Phrase(day.toString(), f));
        cell.setBackgroundColor(BaseColor.BLACK);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setColspan(7);
        table.addCell(cell);
        table.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
        for (int i = 0; i < 2; i++) {
            table.addCell("Location");
            table.addCell("Time");
            table.addCell("Run Length");
            table.addCell("Title");
            table.addCell("Year");
            table.addCell("Directors");
            table.addCell("Countries");
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
            cell = new PdfPCell();
            cell.setUseAscender(true);
            cell.setUseDescender(true);
            cell.addElement(PojoToElementFactory.getDirectorList(movie));
            table.addCell(cell);
            cell = new PdfPCell();
            cell.setUseAscender(true);
            cell.setUseDescender(true);
            cell.addElement(PojoToElementFactory.getCountryList(movie));
            table.addCell(cell);
        }
        return table;
    }
}
