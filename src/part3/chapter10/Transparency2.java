/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part3.chapter10;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfShading;
import com.itextpdf.text.pdf.PdfShadingPattern;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfTransparencyGroup;
import com.itextpdf.text.pdf.PdfWriter;

public class Transparency2 {

    /** The resulting PDF file. */
    public static final String RESULT
        = "results/part3/chapter10/transparency2.pdf";
    
    public static void main(String[] args) {
        // step 1
        Document document = new Document();
        try {
            // step 2
            PdfWriter writer = PdfWriter.getInstance(
                    document,
                    new FileOutputStream(RESULT));
            // step 3
            document.open();
            // step 4
            PdfContentByte cb = writer.getDirectContent();
            float gap = (document.getPageSize().getWidth() - 400) / 3;

            pictureBackdrop(gap, 500, cb, writer);
            pictureBackdrop(200 + 2 * gap, 500, cb, writer);
            pictureBackdrop(gap, 500 - 200 - gap, cb, writer);
            pictureBackdrop(200 + 2 * gap, 500 - 200 - gap, cb, writer);
            PdfTemplate tp;
            PdfTransparencyGroup group;

            tp = cb.createTemplate(200, 200);
            pictureCircles(0, 0, tp);
            group = new PdfTransparencyGroup();
            group.setIsolated(true);
            group.setKnockout(true);
            tp.setGroup(group);
            cb.addTemplate(tp, gap, 500);

            tp = cb.createTemplate(200, 200);
            pictureCircles(0, 0, tp);
            group = new PdfTransparencyGroup();
            group.setIsolated(true);
            group.setKnockout(false);
            tp.setGroup(group);
            cb.addTemplate(tp, 200 + 2 * gap, 500);

            tp = cb.createTemplate(200, 200);
            pictureCircles(0, 0, tp);
            group = new PdfTransparencyGroup();
            group.setIsolated(false);
            group.setKnockout(true);
            tp.setGroup(group);
            cb.addTemplate(tp, gap, 500 - 200 - gap);

            tp = cb.createTemplate(200, 200);
            pictureCircles(0, 0, tp);
            group = new PdfTransparencyGroup();
            group.setIsolated(false);
            group.setKnockout(false);
            tp.setGroup(group);
            cb.addTemplate(tp, 200 + 2 * gap, 500 - 200 - gap);
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }

        // step 5: we close the document
        document.close();
    }

    /**
     * Prints a square and fills half of it with a gray rectangle.
     * 
     * @param x
     * @param y
     * @param cb
     * @throws Exception
     */
    public static void pictureBackdrop(float x, float y, PdfContentByte cb,
            PdfWriter writer) {
        PdfShading axial = PdfShading.simpleAxial(writer, x, y, x + 200, y,
                BaseColor.YELLOW, BaseColor.RED);
        PdfShadingPattern axialPattern = new PdfShadingPattern(axial);
        cb.setShadingFill(axialPattern);
        cb.setColorStroke(BaseColor.BLACK);
        cb.setLineWidth(2);
        cb.rectangle(x, y, 200, 200);
        cb.fillStroke();
    }

    /**
     * Prints 3 circles in different colors that intersect with eachother.
     * 
     * @param x
     * @param y
     * @param cb
     * @throws Exception
     */
    public static void pictureCircles(float x, float y, PdfContentByte cb) {
        PdfGState gs = new PdfGState();
        gs.setBlendMode(PdfGState.BM_MULTIPLY);
        gs.setFillOpacity(1f);
        cb.setGState(gs);
        cb.setColorFill(new CMYKColor(0f, 0f, 0f, 0.15f));
        cb.circle(x + 75, y + 75, 70);
        cb.fill();
        cb.circle(x + 75, y + 125, 70);
        cb.fill();
        cb.circle(x + 125, y + 75, 70);
        cb.fill();
        cb.circle(x + 125, y + 125, 70);
        cb.fill();
    }

}
