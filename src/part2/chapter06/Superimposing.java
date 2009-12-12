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
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

public class Superimposing {

    /** The resulting PDF. */
    public static final String SOURCE = "results/part2/chapter06/opening.pdf";
    /** The resulting PDF. */
    public static final String RESULT = "results/part2/chapter06/festival_opening.pdf";
    /** The movie poster. */
    public static final String RESOURCE = "resources/img/loa.jpg";
    
    public static void main(String[] args) throws IOException, DocumentException {
        new Superimposing().createPdf(SOURCE);
        PdfReader reader = new PdfReader(SOURCE);
        Document document = new Document(PageSize.POSTCARD);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(RESULT));
        document.open();
        PdfContentByte canvas = writer.getDirectContent();
        PdfImportedPage page;
        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
            page = writer.getImportedPage(reader, i);
            canvas.addTemplate(page, 1f, 0, 0, 1, 0, 0);
        }
        document.close();
    }

    /**
     * Creates a PDF document.
     * @param filename the path to the new PDF document
     * @throws    DocumentException 
     * @throws    IOException
     */
    public void createPdf(String filename) throws IOException, DocumentException {
        
        Document document = new Document(PageSize.POSTCARD, 30, 30, 30, 30);
        PdfWriter writer = PdfWriter.getInstance(document,
                new FileOutputStream(filename));
        document.open();

        PdfContentByte under = writer.getDirectContentUnder();
        
        under.setRGBColorFill(0xFF, 0xD7, 0x00);
        under.rectangle(5, 5, PageSize.POSTCARD.getWidth() - 10, PageSize.POSTCARD.getHeight() - 10);
        under.fill();
        document.newPage();

        Image img = Image.getInstance(RESOURCE);
        img.setAbsolutePosition((PageSize.POSTCARD.getWidth() - img.getScaledWidth()) / 2,
                (PageSize.POSTCARD.getHeight() - img.getScaledHeight()) / 2);
        document.add(img);
        document.newPage();

        Paragraph p = new Paragraph("Foobar Film Festival", new Font(Font.HELVETICA, 22));
        p.setAlignment(Element.ALIGN_CENTER);
        document.add(p);
        document.newPage();

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
        
        document.close();
    }
}
