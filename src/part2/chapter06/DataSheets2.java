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
import com.itextpdf.text.pdf.PdfSmartCopy;

public class DataSheets2 extends DataSheets1 {
    public static final String RESULT = "results/part2/chapter06/datasheets2.pdf";

    public static void main(String[] args) throws IOException, SQLException, DocumentException {
        new DataSheets2().createPdf(RESULT);
    }
    
    public void createPdf(String filename) throws IOException, DocumentException, SQLException {
        Document document = new Document();
        PdfSmartCopy copy = new PdfSmartCopy(document, new FileOutputStream(filename));
        document.open();
        addDataSheets(copy);
        document.close();
    }
}
