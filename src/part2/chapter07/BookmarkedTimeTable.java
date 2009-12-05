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
    public static final String RESULT = "results/part2/chapter07/time_table_bookmarks.pdf";
    
    /**
     * Creates a PDF file containing a time table for our film festival.
     * @param    filename    the name of the PDF file
     * @throws SQLException 
     */
    @SuppressWarnings("unchecked")
    public void manipulatePdf(String src, String dest)
        throws IOException, DocumentException, SQLException {
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        ArrayList outlines = new ArrayList();
        HashMap map = new HashMap();
        outlines.add(map);
        map.put("Title", "Calendar");
        ArrayList kids = new ArrayList();
        map.put("Kids", kids);
        int page = 1;
        List<Date> days = PojoFactory.getDays(connection);
        for (Date day : days) {
            HashMap kid = new HashMap();
            kids.add(kid);
            kid.put("Title", day.toString());
            kid.put("Action", "GoTo");
            kid.put("Page", String.format("%d Fit", page++));
        }
        PdfReader reader = new PdfReader(src);
        PdfStamper stamper = new PdfStamper(reader,
                new FileOutputStream(dest));
        stamper.setOutlines(outlines);
        stamper.close();
        connection.close();
    }
    
    /**
     * Main method creating the PDF.
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException 
     * @throws SQLException 
     */
    public static void main(String[] args) throws IOException, DocumentException, SQLException {
        MovieTemplates.main(args);
        new BookmarkedTimeTable().manipulatePdf(MovieTemplates.RESULT, RESULT);
    }
}
