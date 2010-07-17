/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part4.chapter16;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfDestination;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfWriter;

public class LocalDestinationEvent implements PdfPCellEvent {

    /** The writer to which the local destination will be added. */
    protected PdfWriter writer;
    /** The name of the local destination. */
    protected String name;
    
    /** Constructs a local destination event. */
    public LocalDestinationEvent(PdfWriter writer, String name) {
        this.writer = writer;
        this.name = name;
    }
    
    /** Implementation of the cellLayout method. */
    public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
        writer.getDirectContent().localDestination(name,
            new PdfDestination(PdfDestination.FITH, position.getTop()));
    }
}
