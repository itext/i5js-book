/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */
package part4.chapter16;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfActionEvent implements PdfPCellEvent {

    /** The writer to which we are going to add the action. */
    protected PdfWriter writer;
    /** The action we're going to add. */
    protected PdfAction action;
    
    /** Creates a new Action event. */
    public PdfActionEvent(PdfWriter writer, PdfAction action) {
        this.writer = writer;
        this.action = action;
    }
    
    /** Implementation of the cellLayout method. */
    public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
        writer.addAnnotation(new PdfAnnotation(writer,
            position.getLeft(), position.getBottom(), position.getRight(), position.getTop(),
            action));
    }
}
