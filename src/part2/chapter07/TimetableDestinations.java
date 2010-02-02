/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part2.chapter07;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfDestination;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import part1.chapter03.MovieTemplates;

public class TimetableDestinations {
    /** The resulting PDF. */
    public static final String RESULT
        = "results/part2/chapter07/timetable_destinations.pdf";
    /** The font that is used for the navigation links. */
    public static final Font SYMBOL = new Font(FontFamily.SYMBOL, 20);
    /** A list to cache all the possible actions */
    public List<PdfAction> actions;

    /**
     * Manipulates a PDF file src with the file dest as result
     * @param src the original PDF
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
        // Make a list with all the possible actions
        actions = new ArrayList<PdfAction>();
        PdfDestination d;
        for (int i = 0; i < n; ) {
            d = new PdfDestination(PdfDestination.FIT);
            actions.add(PdfAction.gotoLocalPage(++i, d, stamper.getWriter()));
        }
        // Add a navigation table to every page
        PdfContentByte canvas;
        for (int i = 0; i < n; ) {
            canvas = stamper.getOverContent(++i);
            createNavigationTable(i, n).writeSelectedRows(0, -1, 696, 36, canvas);
        }
        // Close the stamper
        stamper.close();
    }
    
    /**
     * Create a table that can be used as a footer
     * @param pagenumber the page that will use the table as footer
     * @param total the total number of pages
     * @return a tabel
     */
    public PdfPTable createNavigationTable(int pagenumber, int total) {
        PdfPTable table = new PdfPTable(4);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        Chunk first = new Chunk(String.valueOf((char)220), SYMBOL);
        first.setAction(actions.get(0));
        table.addCell(new Phrase(first));
        Chunk previous = new Chunk(String.valueOf((char)172), SYMBOL);
        previous.setAction(actions.get(pagenumber - 2 < 0 ? 0 : pagenumber - 2));
        table.addCell(new Phrase(previous));
        Chunk next = new Chunk(String.valueOf((char)174), SYMBOL);
        next.setAction(actions.get(pagenumber >= total ? total - 1 : pagenumber));
        table.addCell(new Phrase(next));
        Chunk last = new Chunk(String.valueOf((char)222), SYMBOL);
        last.setAction(actions.get(total - 1));
        table.addCell(new Phrase(last));
        table.setTotalWidth(120);
        return table;
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
        new TimetableDestinations().manipulatePdf(
            MovieTemplates.RESULT, RESULT);
    }
}
