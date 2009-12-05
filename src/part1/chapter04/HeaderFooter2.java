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
import com.lowagie.filmfestival.PojoFactory;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class HeaderFooter2 extends HeaderFooter1 {

    public static final String RESULT = "results/part1/chapter04/header_footer_2.pdf";
    
    public static void main(String[] args) throws SQLException, DocumentException, IOException {
        new HeaderFooter2().createPdf(RESULT);
    }
    
    public void createPdf(String filename) throws SQLException, DocumentException, IOException {
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        document.open();
        List<Date> days = PojoFactory.getDays(connection);
        for (Date day : days) {
            PdfPTable table = getTable(connection, day);
            table.setSplitLate(false);
            document.add(table);
            document.newPage();
        }
        document.close();
        connection.close();

    }
}
