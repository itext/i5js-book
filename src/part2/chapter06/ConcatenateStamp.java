/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part2.chapter06;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;

import part1.chapter02.MovieHistory;
import part1.chapter02.MovieLinks1;

public class ConcatenateStamp {

    public static String RESULT = "results/part2/chapter06/concatenated_stamped.pdf";
    
    public static void main(String[] args) throws IOException, DocumentException, SQLException {
        MovieLinks1.main(args);
        MovieHistory.main(args);
        
        Document document = new Document();
        PdfCopy copy = new PdfCopy(document, new FileOutputStream(RESULT));
        document.open();
        PdfReader reader1 = new PdfReader(MovieLinks1.RESULT);
        int n1 = reader1.getNumberOfPages();
        PdfReader reader2 = new PdfReader(MovieHistory.RESULT);
        int n2 = reader2.getNumberOfPages();
        PdfImportedPage page;
        PdfCopy.PageStamp stamp;
        for (int i = 0; i < n1; ) {
            page = copy.getImportedPage(reader1, ++i);
            stamp = copy.createPageStamp(page);
            ColumnText.showTextAligned(
                    stamp.getUnderContent(), Element.ALIGN_CENTER,
                    new Phrase(String.format("page %d of %d", i, n1 + n2)),
                    297.5f, 28, 0);
            stamp.alterContents();
            copy.addPage(page);
        }
        for (int i = 0; i < n2; ) {
            page = copy.getImportedPage(reader2, ++i);
            stamp = copy.createPageStamp(page);
            ColumnText.showTextAligned(
                    stamp.getUnderContent(), Element.ALIGN_CENTER,
                    new Phrase(String.format("page %d of %d", n1 + i, n1 + n2)),
                    297.5f, 28, 0);
            stamp.alterContents();
            copy.addPage(page);
        }
        document.close();
    }
}
