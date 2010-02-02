/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part2.chapter07;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import part1.chapter03.MovieTemplates;

public class NamedActions {
    /** The resulting PDF. */
    public static final String RESULT
        = "results/part2/chapter07/named_actions.pdf";

    /**
     * Manipulates a PDF file src with the file dest as result
     * @param src the original PDF
     * @param dest the resulting PDF
     * @throws IOException
     * @throws DocumentException
     */
    public void manipulatePdf(String src, String dest)
        throws IOException, DocumentException {
    	// Create a table with named actions
        Font symbol = new Font(FontFamily.SYMBOL, 20);
        PdfPTable table = new PdfPTable(4);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        Chunk first = new Chunk(String.valueOf((char)220), symbol);
        first.setAction(new PdfAction(PdfAction.FIRSTPAGE));
        table.addCell(new Phrase(first));
        Chunk previous = new Chunk(String.valueOf((char)172), symbol);
        previous.setAction(new PdfAction(PdfAction.PREVPAGE));
        table.addCell(new Phrase(previous));
        Chunk next = new Chunk(String.valueOf((char)174), symbol);
        next.setAction(new PdfAction(PdfAction.NEXTPAGE));
        table.addCell(new Phrase(next));
        Chunk last = new Chunk(String.valueOf((char)222), symbol);
        last.setAction(new PdfAction(PdfAction.LASTPAGE));
        table.addCell(new Phrase(last));
        table.setTotalWidth(120);
        
        // Create a reader
        PdfReader reader = new PdfReader(src);
        // Create a stamper
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        // Add the table to each page
        PdfContentByte canvas;
        for (int i = 0; i < reader.getNumberOfPages(); ) {
            canvas = stamper.getOverContent(++i);
            table.writeSelectedRows(0, -1, 696, 36, canvas);
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
        new NamedActions().manipulatePdf(MovieTemplates.RESULT, RESULT);
    }
}
