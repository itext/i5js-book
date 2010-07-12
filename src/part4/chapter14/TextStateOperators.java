/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */
package part4.chapter14;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfTextArray;
import com.itextpdf.text.pdf.PdfWriter;

public class TextStateOperators {

    /** The resulting PDF. */
    public static final String RESULT = "results/part4/chapter14/text_operators.pdf";

    /**
     * Creates a PDF document.
     * @param filename the path to the new PDF document
     * @throws DocumentException 
     * @throws IOException
     */
    public void createPdf(String filename) throws IOException, DocumentException {
        // step 1
        Document document = new Document();
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document,
                new FileOutputStream(filename));
        // step 3
        document.open();
        // step 4
        PdfContentByte canvas = writer.getDirectContent();
        String text = "AWAY again";
        BaseFont bf = BaseFont.createFont();
        canvas.beginText();
        // line 1
        canvas.setFontAndSize(bf, 16);
        canvas.moveText(36, 806);
        canvas.moveTextWithLeading(0, -24);
        canvas.showText(text);
        // line 2
        canvas.setWordSpacing(20);
        canvas.newlineShowText(text);
        // line 3
        canvas.setCharacterSpacing(10);
        canvas.newlineShowText(text);
        canvas.setWordSpacing(0);
        canvas.setCharacterSpacing(0);
        // line 4
        canvas.setHorizontalScaling(50);
        canvas.newlineShowText(text);
        canvas.setHorizontalScaling(100);
        // line 5
        canvas.newlineShowText(text);
        canvas.setTextRise(15);
        canvas.setFontAndSize(bf, 12);
        canvas.setColorFill(BaseColor.RED);
        canvas.showText("2");
        canvas.setColorFill(GrayColor.GRAYBLACK);
        // line 6
        canvas.setLeading(56);
        canvas.newlineShowText("Changing the leading: " + text);
        canvas.setLeading(24);
        canvas.newlineText();
        // line 7
        PdfTextArray array = new PdfTextArray("A");
        array.add(120);
        array.add("W");
        array.add(120);
        array.add("A");
        array.add(95);
        array.add("Y again");
        canvas.showText(array);
        canvas.endText();

        canvas.setColorFill(BaseColor.BLUE);
        canvas.beginText();
        canvas.setTextMatrix(360, 770);
        canvas.setTextRenderingMode(PdfContentByte.TEXT_RENDER_MODE_FILL);
        canvas.setFontAndSize(bf, 24);
        canvas.showText(text);
        canvas.endText();

        canvas.beginText();
        canvas.setTextMatrix(360, 730);
        canvas.setTextRenderingMode(PdfContentByte.TEXT_RENDER_MODE_STROKE);
        canvas.setFontAndSize(bf, 24);
        canvas.showText(text);
        canvas.endText();

        canvas.beginText();
        canvas.setTextMatrix(360, 690);
        canvas.setTextRenderingMode(PdfContentByte.TEXT_RENDER_MODE_FILL_STROKE);
        canvas.setFontAndSize(bf, 24);
        canvas.showText(text);
        canvas.endText();

        canvas.beginText();
        canvas.setTextMatrix(360, 650);
        canvas.setTextRenderingMode(PdfContentByte.TEXT_RENDER_MODE_INVISIBLE);
        canvas.setFontAndSize(bf, 24);
        canvas.showText(text);
        canvas.endText();

        PdfTemplate template = canvas.createTemplate(200, 36);
        template.setLineWidth(2);
        for (int i = 0; i < 6; i++) {
            template.moveTo(0, i * 6);
            template.lineTo(200, i * 6);
        }
        template.stroke();
        
        canvas.saveState();
        canvas.beginText();
        canvas.setTextMatrix(360, 610);
        canvas.setTextRenderingMode(PdfContentByte.TEXT_RENDER_MODE_FILL_CLIP);
        canvas.setFontAndSize(bf, 24);
        canvas.showText(text);
        canvas.endText();
        canvas.addTemplate(template, 360, 610);
        canvas.restoreState();

        canvas.saveState();
        canvas.beginText();
        canvas.setTextMatrix(360, 570);
        canvas.setTextRenderingMode(PdfContentByte.TEXT_RENDER_MODE_STROKE_CLIP);
        canvas.setFontAndSize(bf, 24);
        canvas.showText(text);
        canvas.endText();
        canvas.addTemplate(template, 360, 570);
        canvas.restoreState();

        canvas.saveState();
        canvas.beginText();
        canvas.setTextMatrix(360, 530);
        canvas.setTextRenderingMode(PdfContentByte.TEXT_RENDER_MODE_FILL_STROKE_CLIP);
        canvas.setFontAndSize(bf, 24);
        canvas.showText(text);
        canvas.endText();
        canvas.addTemplate(template, 360, 530);
        canvas.restoreState();
        
        canvas.saveState();
        canvas.beginText();
        canvas.setTextMatrix(360, 490);
        canvas.setTextRenderingMode(PdfContentByte.TEXT_RENDER_MODE_CLIP);
        canvas.setFontAndSize(bf, 24);
        canvas.showText(text);
        canvas.endText();
        canvas.addTemplate(template, 360, 490);
        canvas.restoreState();

        // step 5
        document.close();
    }
    
    /**
     * Main method.
     *
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException
     */
    public static void main(String[] args) throws IOException, DocumentException {
        new TextStateOperators().createPdf(RESULT);
    }

}
