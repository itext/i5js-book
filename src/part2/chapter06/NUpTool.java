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
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

public class NUpTool {

    /** Format of the resulting PDF files. */
    public static final String RESULT
        = "results/part2/chapter06/result%dup.pdf";
    
    /**
     * Manipulates a PDF file src with the file dest as result
     * @param src the original PDF
     * @param dest the resulting PDF
     * @param pow the PDF will be N-upped with N = Math.pow(2, pow);
     * @throws IOException
     * @throws DocumentException
     * @throws SQLException
     */
    public void manipulatePdf(String src, String dest, int pow)
        throws IOException, DocumentException {
    	// reader for the src file
        PdfReader reader = new PdfReader(src);
        // initializations
        Rectangle pageSize = reader.getPageSize(1);
        Rectangle newSize = (pow % 2) == 0 ?
            new Rectangle(pageSize.getWidth(), pageSize.getHeight()) :
            new Rectangle(pageSize.getHeight(), pageSize.getWidth());
        Rectangle unitSize = new Rectangle(pageSize.getWidth(), pageSize.getHeight());
        for (int i = 0; i < pow; i++) {
            unitSize = new Rectangle(unitSize.getHeight() / 2, unitSize.getWidth());
        }
        int n = (int)Math.pow(2, pow);
        int r = (int)Math.pow(2, pow / 2);
        int c = n / r;
        // step 1
        Document document = new Document(newSize, 0, 0, 0, 0);
        // step 2
        PdfWriter writer
           = PdfWriter.getInstance(document, new FileOutputStream(String.format(dest, n)));
        // step 3
        document.open();
        // step 4
        PdfContentByte cb = writer.getDirectContent();
        PdfImportedPage page;
        Rectangle currentSize;
        float offsetX, offsetY, factor;
        int total = reader.getNumberOfPages();
        for (int i = 0; i < total; ) {
            if (i % n == 0) {
                document.newPage();
            }
            currentSize = reader.getPageSize(++i);
            factor = Math.min(
                unitSize.getWidth() / currentSize.getWidth(),
                unitSize.getHeight() / currentSize.getHeight());
            offsetX = unitSize.getWidth() * ((i % n) % c)
              + (unitSize.getWidth() - (currentSize.getWidth() * factor)) / 2f;
            offsetY = newSize.getHeight() - (unitSize.getHeight() * (((i % n) / c) + 1))
              + (unitSize.getHeight() - (currentSize.getHeight() * factor)) / 2f;
            page = writer.getImportedPage(reader, i);
            cb.addTemplate(page, factor, 0, 0, factor, offsetX, offsetY);
        }
        // step 5
        document.close();
    }
    
    /**
     * Main method.
     * @param args no arguments needed
     * @throws DocumentException 
     * @throws IOException
     * @throws SQLException
     */
    public static void main(String[] args)
        throws IOException, DocumentException, SQLException {
        Stationery.main(args);
        new NUpTool().manipulatePdf(Stationery.RESULT, RESULT, 1);
        new NUpTool().manipulatePdf(Stationery.RESULT, RESULT, 2);
        new NUpTool().manipulatePdf(Stationery.RESULT, RESULT, 3);
        new NUpTool().manipulatePdf(Stationery.RESULT, RESULT, 4);
    }
}
