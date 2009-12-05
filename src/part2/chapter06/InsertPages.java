/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part2.chapter06;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class InsertPages {
    /** The resulting PDF. */
    public static final String RESULT1 = "results/part2/chapter06/inserted_pages.pdf";
    /** The resulting PDF. */
    public static final String RESULT2 = "results/part2/chapter06/reordered.pdf";

    public static void main(String[] args) throws IOException, DocumentException, SQLException {
        StampStationery.main(args);
        new InsertPages().manipulatePdf(StampStationery.RESULT, RESULT1);
        PdfReader reader = new PdfReader(RESULT1);
        reader.selectPages("3-41,1-2");
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(RESULT2));
        stamper.close();
    }
    
    public void manipulatePdf(String src, String dest) throws IOException, DocumentException, SQLException {
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        Statement stm = connection.createStatement();
        ResultSet rs = stm.executeQuery(
            "SELECT country, id FROM film_country ORDER BY country");
        ColumnText ct = new ColumnText(null);
        while (rs.next()) {
            ct.addElement(new Paragraph(24, new Chunk(rs.getString("country"))));
        }
        stm.close();
        connection.close();
        
        PdfReader reader = new PdfReader(src);
        PdfReader stationery = new PdfReader(Stationery.STATIONERY);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        PdfImportedPage page = stamper.getImportedPage(stationery, 1);
        int i = 0;
        while(true) {
            stamper.insertPage(++i, reader.getPageSize(1));
            stamper.getUnderContent(i).addTemplate(page, 0, 0);
            ct.setCanvas(stamper.getOverContent(i));
            ct.setSimpleColumn(36, 36, 559, 770);
            if (!ColumnText.hasMoreText(ct.go()))
                break;
        }
        stamper.close();
    }
}
