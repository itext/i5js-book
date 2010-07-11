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
    /** A resulting PDF. */
    public static final String RESULT1
        = "results/part2/chapter06/inserted_pages.pdf";
    /** A resulting PDF. */
    public static final String RESULT2
        = "results/part2/chapter06/reordered.pdf";
    
    /**
     * Main method.
     * @param args no arguments needed
     * @throws DocumentException 
     * @throws IOException
     * @throws SQLException
     */
    public static void main(String[] args)
        throws IOException, DocumentException, SQLException {
    	// use a previous example to create a PDF
        StampStationery.main(args);
        // manipulate the PDF
        new InsertPages().manipulatePdf(StampStationery.RESULT, RESULT1);
        // reorder the pages in the PDF
        PdfReader reader = new PdfReader(RESULT1);
        reader.selectPages("3-41,1-2");
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(RESULT2));
        stamper.close();
    }
    
    /**
     * Manipulates a PDF file src with the file dest as result
     * @param src the original PDF
     * @param dest the resulting PDF
     * @throws IOException
     * @throws DocumentException
     * @throws SQLException
     */
    public void manipulatePdf(String src, String dest)
        throws IOException, DocumentException, SQLException {
    	// Create a database connection and statement
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        Statement stm = connection.createStatement();
        // Fill a ColumnText object with data
        ResultSet rs = stm.executeQuery(
            "SELECT country, id FROM film_country ORDER BY country");
        ColumnText ct = new ColumnText(null);
        while (rs.next()) {
            ct.addElement(new Paragraph(24, new Chunk(rs.getString("country"))));
        }
        // Close the statement and database connection
        stm.close();
        connection.close();
        
        // Create a reader for the original document and for the stationery
        PdfReader reader = new PdfReader(src);
        PdfReader stationery = new PdfReader(Stationery.STATIONERY);
        // Create a stamper
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        // Create an imported page for the stationery
        PdfImportedPage page = stamper.getImportedPage(stationery, 1);
        int i = 0;
        // Add the content of the ColumnText object 
        while(true) {
            // Add a new page
            stamper.insertPage(++i, reader.getPageSize(1));
            // Add the stationary to the new page
            stamper.getUnderContent(i).addTemplate(page, 0, 0);
            // Add as much content of the column as possible
            ct.setCanvas(stamper.getOverContent(i));
            ct.setSimpleColumn(36, 36, 559, 770);
            if (!ColumnText.hasMoreText(ct.go()))
                break;
        }
        // Close the stamper
        stamper.close();
    }
}
