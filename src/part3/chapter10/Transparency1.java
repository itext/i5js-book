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
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfTransparencyGroup;
import com.itextpdf.text.pdf.PdfWriter;

public class Transparency1 {

    /** The resulting PDF file. */
    public static final String RESULT
        = "results/part3/chapter10/transparency1.pdf";
    
    public static void main(String[] args) {
        // step 1
        Document document = new Document();
        try {
            // step 2
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(RESULT));
            // step 3
            document.open();
            // step 4
            PdfContentByte cb = writer.getDirectContent();
            float gap = (document.getPageSize().getWidth() - 400) / 3;

            pictureBackdrop(gap, 500, cb);
            pictureBackdrop(200 + 2 * gap, 500, cb);
            pictureBackdrop(gap, 500 - 200 - gap, cb);
            pictureBackdrop(200 + 2 * gap, 500 - 200 - gap, cb);

            pictureCircles(gap, 500, cb);
            cb.saveState();
            PdfGState gs1 = new PdfGState();
            gs1.setFillOpacity(0.5f);
            cb.setGState(gs1);
            pictureCircles(200 + 2 * gap, 500, cb);
            cb.restoreState();

            cb.saveState();
            PdfTemplate tp = cb.createTemplate(200, 200);
            PdfTransparencyGroup group = new PdfTransparencyGroup();
            tp.setGroup(group);
            pictureCircles(0, 0, tp);
            cb.setGState(gs1);
            cb.addTemplate(tp, gap, 500 - 200 - gap);
            cb.restoreState();

            cb.saveState();
            tp = cb.createTemplate(200, 200);
            tp.setGroup(group);
            PdfGState gs2 = new PdfGState();
            gs2.setFillOpacity(0.5f);
            gs2.setBlendMode(PdfGState.BM_HARDLIGHT);
            tp.setGState(gs2);
            pictureCircles(0, 0, tp);
            cb.addTemplate(tp, 200 + 2 * gap, 500 - 200 - gap);
            cb.restoreState();

            cb.resetRGBColorFill();
            ColumnText ct = new ColumnText(cb);
            Phrase ph = new Phrase("Ungrouped objects\nObject opacity = 1.0");
            ct.setSimpleColumn(ph, gap, 0, gap + 200, 500, 18, Element.ALIGN_CENTER);
            ct.go();

            ph = new Phrase("Ungrouped objects\nObject opacity = 0.5");
            ct.setSimpleColumn(ph, 200 + 2 * gap, 0, 200 + 2 * gap + 200, 500,
                18, Element.ALIGN_CENTER);
            ct.go();

            ph = new Phrase(
                "Transparency group\nObject opacity = 1.0\nGroup opacity = 0.5\nBlend mode = Normal");
            ct.setSimpleColumn(ph, gap, 0, gap + 200, 500 - 200 - gap, 18, Element.ALIGN_CENTER);
            ct.go();

            ph = new Phrase(
                "Transparency group\nObject opacity = 0.5\nGroup opacity = 1.0\nBlend mode = HardLight");
            ct.setSimpleColumn(ph, 200 + 2 * gap, 0, 200 + 2 * gap + 200, 500 - 200 - gap,
                18, Element.ALIGN_CENTER);
            ct.go();
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
    public static void pictureBackdrop(float x, float y, PdfContentByte cb) {
        cb.setColorStroke(GrayColor.GRAYBLACK);
        cb.setColorFill(new GrayColor(0.8f));
        cb.rectangle(x, y, 100, 200);
        cb.fill();
        cb.setLineWidth(2);
        cb.rectangle(x, y, 200, 200);
        cb.stroke();
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
        cb.setColorFill(BaseColor.RED);
        cb.circle(x + 70, y + 70, 50);
        cb.fill();
        cb.setColorFill(BaseColor.YELLOW);
        cb.circle(x + 100, y + 130, 50);
        cb.fill();
        cb.setColorFill(BaseColor.BLUE);
        cb.circle(x + 130, y + 70, 50);
        cb.fill();
    }

}
