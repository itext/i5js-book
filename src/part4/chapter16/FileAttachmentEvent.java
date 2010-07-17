/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part4.chapter16;

import java.io.IOException;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfFileSpecification;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfWriter;

public class FileAttachmentEvent implements PdfPCellEvent {

    /** The PdfWriter to which the file has to be attached. */
    protected PdfWriter writer;
    /** The file specification that will be used to create an annotation. */
    protected PdfFileSpecification fs;
    /** The description that comes with the annotation. */
    protected String description;
    
    /**
     * Creates a FileAttachmentEvent.
     * 
     * @param writer      the writer to which the file attachment has to be added.
     * @param fs          the file specification.
     * @param description a description for the file attachment.
     */
    public FileAttachmentEvent(PdfWriter writer, PdfFileSpecification fs, String description) {
        this.writer = writer;
        this.fs = fs;
        this.description = description;
    }
    
    /**
     * Implementation of the cellLayout method.
     * @see com.itextpdf.text.pdf.PdfPCellEvent#cellLayout(
     *     com.itextpdf.text.pdf.PdfPCell, com.itextpdf.text.Rectangle, com.itextpdf.text.pdf.PdfContentByte[])
     */
    public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
        try {
            PdfAnnotation annotation = PdfAnnotation.createFileAttachment(writer,
                new Rectangle(position.getLeft() - 20, position.getTop() - 15,
                    position.getLeft() - 5, position.getTop() - 5),
                description, fs);
            annotation.setName(description);
            writer.addAnnotation(annotation);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}