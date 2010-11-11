/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part2.chapter07;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import part1.chapter02.MovieHistory;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.SimpleBookmark;

public class ConcatenateBookmarks {

    /** The resulting PDF. */
    public static final String RESULT
        = "results/part2/chapter07/concatenated_bookmarks.pdf";

    /**
     * Manipulates a PDF file src with the file dest as result
     * @param src the original PDF
     * @param dest the resulting PDF
     * @throws IOException
     * @throws DocumentException
     */
    public void manipulatePdf(String[] src, String dest)
        throws IOException, DocumentException {
    	// step 1
        Document document = new Document();
        // step 2
        PdfCopy copy
            = new PdfCopy(document, new FileOutputStream(dest));
        // step 3
        document.open();
        // step 4
        PdfReader reader;
        int page_offset = 0;
        int n;
        // Create a list for the bookmarks
        ArrayList<HashMap<String, Object>> bookmarks = new ArrayList<HashMap<String, Object>>();
        List<HashMap<String, Object>> tmp;
        for (int i  = 0; i < src.length; i++) {
            reader = new PdfReader(src[i]);
            // merge the bookmarks
            tmp = SimpleBookmark.getBookmark(reader);
            SimpleBookmark.shiftPageNumbers(tmp, page_offset, null);
            bookmarks.addAll(tmp);
            // add the pages
            n = reader.getNumberOfPages();
            page_offset += n;
            for (int page = 0; page < n; ) {
                copy.addPage(copy.getImportedPage(reader, ++page));
            }
            copy.freeReader(reader);
        }
        // Add the merged bookmarks
        copy.setOutlines(bookmarks);
        // step 5
        document.close();
    }

    /**
     * Main method.
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException
     * @throws SQLException
     */
    public static void main(String[] args)
        throws IOException, DocumentException, SQLException {
        BookmarkedTimeTable.main(args);
        MovieHistory.main(args);
        new ConcatenateBookmarks().manipulatePdf(
            new String[]{BookmarkedTimeTable.RESULT, MovieHistory.RESULT},
            RESULT);
    }
}
