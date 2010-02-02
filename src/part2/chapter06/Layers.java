/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part2.chapter06;

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
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

public class Layers {

    /** The resulting PDF. */
    public static final String SOURCE
        = "results/part2/chapter06/layers_orig.pdf";
    /** The resulting PDF. */
    public static final String RESULT
        = "results/part2/chapter06/layers.pdf";
    /** The movie poster. */
    public static final String RESOURCE
        = "resources/img/loa.jpg";
    
    /**
     * Main method.
     * @param args no arguments needed
     * @throws DocumentException 
     * @throws IOException
     */
    public static void main(String[] args)
        throws IOException, DocumentException {
        new Layers().createPdf(SOURCE);
        // Create a reader
        PdfReader reader = new PdfReader(SOURCE);
        // step 1
        Document document = new Document(PageSize.A5.rotate());
        // step 2
        PdfWriter writer
            = PdfWriter.getInstance(document, new FileOutputStream(RESULT));
        // step 3
        document.open();
        // step 4
        PdfContentByte canvas = writer.getDirectContent();
        PdfImportedPage page;
        BaseFont bf
            = BaseFont.createFont(BaseFont.ZAPFDINGBATS, "", BaseFont.EMBEDDED);
        for (int i = 0; i < reader.getNumberOfPages(); ) {
            page = writer.getImportedPage(reader, ++i);
            canvas.addTemplate(page, 1f, 0, 0.4f, 0.4f, 72, 50 * i);
            canvas.beginText();
            canvas.setFontAndSize(bf, 20);
            canvas.showTextAligned(Element.ALIGN_CENTER,
                String.valueOf((char)(181 + i)), 496, 150 + 50 * i, 0);
            canvas.endText();
        }
        // step 5
        document.close();
    }

    /**
     * Creates a PDF document.
     * @param filename the path to the new PDF document
     * @throws    DocumentException 
     * @throws    IOException
     */
    public void createPdf(String filename)
        throws IOException, DocumentException {
        // step 1
        Document document = new Document(PageSize.POSTCARD, 30, 30, 30, 30);
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document,
                new FileOutputStream(filename));
        // step 3
        document.open();
        // step 4
        PdfContentByte under = writer.getDirectContentUnder();
        // Page 1: a rectangle
        drawRectangle(under, PageSize.POSTCARD.getWidth(), PageSize.POSTCARD.getHeight());
        under.setRGBColorFill(0xFF, 0xD7, 0x00);
        under.rectangle(5, 5, PageSize.POSTCARD.getWidth() - 10, PageSize.POSTCARD.getHeight() - 10);
        under.fill();
        document.newPage();
        // Page 2: an image
        drawRectangle(under, PageSize.POSTCARD.getWidth(), PageSize.POSTCARD.getHeight());
        Image img = Image.getInstance(RESOURCE);
        img.setAbsolutePosition((PageSize.POSTCARD.getWidth() - img.getScaledWidth()) / 2,
                (PageSize.POSTCARD.getHeight() - img.getScaledHeight()) / 2);
        document.add(img);
        document.newPage();
        // Page 3: the words "Foobar Film Festival"
        drawRectangle(under, PageSize.POSTCARD.getWidth(), PageSize.POSTCARD.getHeight());;
        Paragraph p = new Paragraph("Foobar Film Festival", new Font(FontFamily.HELVETICA, 22));
        p.setAlignment(Element.ALIGN_CENTER);
        document.add(p);
        document.newPage();
        // Page 4: the words "SOLD OUT"
        drawRectangle(under, PageSize.POSTCARD.getWidth(), PageSize.POSTCARD.getHeight());
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
        over.setTextMatrix(0, 0);
        over.endText();
        over.restoreState();
        // step 5
        document.close();
    }
    
    /**
     * Draws a rectangle
     * @param content the direct content layer
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     */
    public static void drawRectangle(PdfContentByte content, float width, float height) {
        content.saveState();
        PdfGState state = new PdfGState();
        state.setFillOpacity(0.6f);
        content.setGState(state);
        content.setRGBColorFill(0xFF, 0xFF, 0xFF);
        content.setLineWidth(3);
        content.rectangle(0, 0, width, height);
        content.fillStroke();
        content.restoreState();
    }
}
