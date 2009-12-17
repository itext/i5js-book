/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part2.chapter07;

import java.io.FileOutputStream;
import java.io.IOException;

import part1.chapter03.MovieTemplates;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Utilities;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class PrintTimeTable {
    /** The resulting PDF. */
    public static final String RESULT
        = "results/part2/chapter07/print_timetable.pdf";

    /** Path to the resources. */
    public static final String RESOURCE
        = "resources/js/print_page.js";

    /**
     * Manipulates a PDF file src with the file dest as result
     * @param src the original PDF
     * @param resource a resource that will be used as advertisement
     * @param dest the resulting PDF
     * @throws IOException
     * @throws DocumentException
     */
    public void manipulatePdf(String src, String dest)
        throws IOException, DocumentException {
    	// Create the reader
        PdfReader reader = new PdfReader(src);
        int n = reader.getNumberOfPages();
        // Create the stamper
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        // Add JavaScript
        stamper.addJavaScript(Utilities.readFileToString(RESOURCE));
        // Create a Chunk with a chained action
        PdfContentByte canvas;
        Chunk chunk = new Chunk("print this page");
        PdfAction action = PdfAction.javaScript(
            "app.alert('Think before you print!');", stamper.getWriter());
        action.next(PdfAction.javaScript(
            "printCurrentPage(this.pageNum);", stamper.getWriter()));
        action.next(new PdfAction("http://www.panda.org/savepaper/"));
        chunk.setAction(action);
        Phrase phrase = new Phrase(chunk);
        // Add this Chunk to every page
        for (int i = 0; i < n; ) {
            canvas = stamper.getOverContent(++i);
            ColumnText.showTextAligned(
                canvas, Element.ALIGN_RIGHT, phrase, 816, 18, 0);
        }
        // Close the stamper
        stamper.close();
    }

    /**
     * Main method.
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException
     */
    public static void main(String[] args)
        throws IOException, DocumentException {
        MovieTemplates.main(args);
        new PrintTimeTable().manipulatePdf(
            MovieTemplates.RESULT, RESULT);
    }
}
