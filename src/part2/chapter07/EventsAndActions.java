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

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfDestination;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

public class EventsAndActions {
    /** The resulting PDF. */
    public static final String RESULT
        = "results/part2/chapter07/events_and_actions.pdf";

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
    	// Create a reader
        PdfReader reader = new PdfReader(src);
        // Create a stamper
        PdfStamper stamper = new PdfStamper(reader,
                new FileOutputStream(dest));
        // Get the writer (to add actions and annotations)
        PdfWriter writer = stamper.getWriter();
        PdfAction action = PdfAction.gotoLocalPage(2,
            new PdfDestination(PdfDestination.FIT), writer);
        writer.setOpenAction(action);
        action = PdfAction.javaScript(
            "app.alert('Think before you print');", writer);
        writer.setAdditionalAction(PdfWriter.WILL_PRINT, action);
        action = PdfAction.javaScript(
            "app.alert('Think again next time!');", writer);
        writer.setAdditionalAction(PdfWriter.DID_PRINT, action);
        action = PdfAction.javaScript(
            "app.alert('We hope you enjoy the festival');", writer);
        writer.setAdditionalAction(PdfWriter.DOCUMENT_CLOSE, action);
        action = PdfAction.javaScript(
            "app.alert('This day is reserved for people with an accreditation "
            + "or an invitation.');", writer);
        stamper.setPageAction(PdfWriter.PAGE_OPEN, action, 1);
        action = PdfAction.javaScript(
            "app.alert('You can buy tickets for all the other days');", writer);
        stamper.setPageAction(PdfWriter.PAGE_CLOSE, action, 1);
        // Close the stamper
        stamper.close();
    }
    
    /**
     * Main method creating the PDF.
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException
     */
    public static void main(String[] args)
        throws IOException, DocumentException {
        MovieTemplates.main(args);
        new EventsAndActions().manipulatePdf(
            MovieTemplates.RESULT, RESULT);
    }
}
