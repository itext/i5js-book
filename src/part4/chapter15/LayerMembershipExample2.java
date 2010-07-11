/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */
package part4.chapter15;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfLayer;
import com.itextpdf.text.pdf.PdfLayerMembership;
import com.itextpdf.text.pdf.PdfVisibilityExpression;
import com.itextpdf.text.pdf.PdfWriter;

public class LayerMembershipExample2 {

    /** The resulting PDF. */
    public static String RESULT = "results/part4/chapter15/layer_membership2.pdf";

    /**
     * Creates a PDF document.
     * @param filename the path to the new PDF document
     * @throws DocumentException 
     * @throws IOException 
     */
    public void createPdf(String filename) throws DocumentException,
            IOException {
        // step 1
        Document document = new Document();
        // step 2
        PdfWriter writer = PdfWriter.getInstance(
                document, new FileOutputStream(RESULT));
        writer.setPdfVersion(PdfWriter.VERSION_1_6);
        // step 3
        document.open();
        // step 4
        PdfContentByte cb = writer.getDirectContent();

        PdfLayer dog = new PdfLayer("layer 1", writer);
        PdfLayer tiger = new PdfLayer("layer 2", writer);
        PdfLayer lion = new PdfLayer("layer 3", writer);
        PdfLayerMembership cat = new PdfLayerMembership(writer);
        PdfVisibilityExpression ve1 = new PdfVisibilityExpression(PdfVisibilityExpression.OR);
        ve1.add(tiger);
        ve1.add(lion);
        cat.setVisibilityExpression(ve1);
        PdfLayerMembership no_cat = new PdfLayerMembership(writer);
        PdfVisibilityExpression ve2 = new PdfVisibilityExpression(PdfVisibilityExpression.NOT);
        ve2.add(ve1);
        no_cat.setVisibilityExpression(ve2);
        cb.beginLayer(dog);
        ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, new Phrase("dog"),
                50, 775, 0);
        cb.endLayer();
        cb.beginLayer(tiger);
        ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, new Phrase("tiger"),
                50, 750, 0);
        cb.endLayer();
        cb.beginLayer(lion);
        ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, new Phrase("lion"),
                50, 725, 0);
        cb.endLayer();
        cb.beginLayer(cat);
        ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, new Phrase("cat"),
                50, 700, 0);
        cb.endLayer();
        cb.beginLayer(no_cat);
        ColumnText.showTextAligned(cb, Element.ALIGN_LEFT,
                new Phrase("no cat"), 50, 700, 0);
        cb.endLayer();

        // step 5
        document.close();
    }

    /**
     * A simple example with optional content.
     * 
     * @param args
     *            no arguments needed here
     * @throws IOException
     * @throws DocumentException
     */
    public static void main(String[] args) throws DocumentException,
            IOException {
        new LayerMembershipExample2().createPdf(RESULT);
    }
}
