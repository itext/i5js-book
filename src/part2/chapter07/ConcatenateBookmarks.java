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
import java.util.List;

import part1.chapter02.MovieHistory;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.SimpleBookmark;

public class ConcatenateBookmarks {

    /** The resulting PDF. */
    public static final String RESULT = "results/part2/chapter07/concatenated_bookmarks.pdf";
    
    public static void main(String[] args) throws IOException, DocumentException, SQLException {
        BookmarkedTimeTable.main(args);
        MovieHistory.main(args);
        
        new ConcatenateBookmarks().manipulatePdf(
                new String[]{BookmarkedTimeTable.RESULT, MovieHistory.RESULT}, RESULT);
    }
    
    @SuppressWarnings("unchecked")
    public void manipulatePdf(String[] src, String dest) throws IOException, DocumentException {
        Document document = new Document();
        PdfCopy copy = new PdfCopy(document, new FileOutputStream(dest));
        document.open();
        PdfReader reader;
        int page_offset = 0;
        int n;
        ArrayList bookmarks = new ArrayList();
        List tmp;
        for (int i  = 0; i < src.length; i++) {
            reader = new PdfReader(src[i]);
            tmp = SimpleBookmark.getBookmark(reader);
            SimpleBookmark.shiftPageNumbers(tmp, page_offset, null);
            bookmarks.addAll(tmp);
            n = reader.getNumberOfPages();
            page_offset += n;
            for (int page = 0; page < n; ) {
                copy.addPage(copy.getImportedPage(reader, ++page));
            }
        }
        copy.setOutlines(bookmarks);
        document.close();
    }
}
