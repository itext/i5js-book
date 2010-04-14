/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part2.chapter07;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import part1.chapter03.MovieTemplates;

import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.PojoFactory;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class BookmarkedTimeTable {
    
    /** The resulting PDF. */
    public static final String RESULT
        = "results/part2/chapter07/time_table_bookmarks.pdf";

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
    	// Create a database connection
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        // Create a list with bookmarks
        ArrayList<HashMap<String, Object>> outlines = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map = new HashMap<String, Object>();
        outlines.add(map);
        map.put("Title", "Calendar");
        ArrayList<HashMap<String, Object>> kids = new ArrayList<HashMap<String, Object>>();
        map.put("Kids", kids);
        int page = 1;
        List<Date> days = PojoFactory.getDays(connection);
        for (Date day : days) {
            HashMap<String, Object> kid = new HashMap<String, Object>();
            kids.add(kid);
            kid.put("Title", day.toString());
            kid.put("Action", "GoTo");
            kid.put("Page", String.format("%d Fit", page++));
        }
        // Create a reader
        PdfReader reader = new PdfReader(src);
        // Create a stamper
        PdfStamper stamper
            = new PdfStamper(reader, new FileOutputStream(dest));
        stamper.setOutlines(outlines);
        // Close the stamper
        stamper.close();
        // Close the database connection
        connection.close();
    }
    
    /**
     * Main method creating the PDF.
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException 
     * @throws SQLException 
     */
    public static void main(String[] args)
        throws IOException, DocumentException, SQLException {
        MovieTemplates.main(args);
        new BookmarkedTimeTable().manipulatePdf(
            MovieTemplates.RESULT, RESULT);
    }
}
