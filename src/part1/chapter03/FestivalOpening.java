/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part1.chapter03;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

public class FestivalOpening {

    /** The resulting PDF. */
    public static final String RESULT
        = "results/part1/chapter03/festival_opening.pdf";
    /** The movie poster. */
    public static final String RESOURCE = "resources/img/loa.jpg";

    /**
     * Main method.
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException
     */
    public static void main(String[] args)
        throws IOException, DocumentException {
    	// step 1
        Document document = new Document(PageSize.POSTCARD, 30, 30, 30, 30);
        // step 2
        PdfWriter writer
            = PdfWriter.getInstance(document, new FileOutputStream(RESULT));
        // step 3
        document.open();
        // step 4
        // Create and add a Paragraph
        Paragraph p
            = new Paragraph("Foobar Film Festival", new Font(FontFamily.HELVETICA, 22));
        p.setAlignment(Element.ALIGN_CENTER);
        document.add(p);
        // Create and add an Image
        Image img = Image.getInstance(RESOURCE);
        img.setAbsolutePosition(
            (PageSize.POSTCARD.getWidth() - img.getScaledWidth()) / 2,
            (PageSize.POSTCARD.getHeight() - img.getScaledHeight()) / 2);
        document.add(img);
        // Now we go to the next page
        document.newPage();
        document.add(p);
        document.add(img);
        // Add text on top of the image
        PdfContentByte over = writer.getDirectContent();
        over.saveState();
        float sinus = (float)Math.sin(Math.PI / 60);
        float cosinus = (float)Math.cos(Math.PI / 60);
        BaseFont bf = BaseFont.createFont();
        over.beginText();
        over.setTextRenderingMode(PdfContentByte.TEXT_RENDER_MODE_FILL_STROKE);
        over.setLineWidth(1.5f);
        over.setRGBColorStroke(0xFF, 0x00, 0x00);
        over.setRGBColorFill(0xFF, 0xFF, 0xFF);
        over.setFontAndSize(bf, 36);
        over.setTextMatrix(cosinus, sinus, -sinus, cosinus, 50, 324);
        over.showText("SOLD OUT");
        over.endText();
        over.restoreState();
        // Add a rectangle under the image
        PdfContentByte under = writer.getDirectContentUnder();
        under.saveState();
        under.setRGBColorFill(0xFF, 0xD7, 0x00);
        under.rectangle(5, 5,
            PageSize.POSTCARD.getWidth() - 10, PageSize.POSTCARD.getHeight() - 10);
        under.fill();
        under.restoreState();
        // step 5
        document.close();
    }
}
