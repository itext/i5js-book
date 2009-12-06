/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part1.chapter03;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.BaseColor;

public class FoobarFilmFestival {

    public static final String RESULT = "results/part1/chapter03/foobar_film_festival.pdf";

    public static void main(String[] args) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(RESULT));
        document.open();

        Chunk c;
        String foobar = "Foobar Film Festival";
        
        Font helvetica = new Font(Font.HELVETICA, 12);
        BaseFont bf_helv = helvetica.getCalculatedBaseFont(false);
        float width_helv = bf_helv.getWidthPoint(foobar, 12);
        c = new Chunk(foobar + ": " + width_helv, helvetica);
        document.add(new Paragraph(c));
        document.add(new Paragraph(String.format("Chunk width: %f", c.getWidthPoint())));
        
        BaseFont bf_times = BaseFont.createFont(
            "c:/windows/fonts/times.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED);
        Font times = new Font(bf_times, 12);
        float width_times = bf_times.getWidthPoint(foobar, 12);
        c = new Chunk(foobar + ": " + width_times, times);
        document.add(new Paragraph(c));
        document.add(new Paragraph(String.format("Chunk width: %f", c.getWidthPoint())));
        
        document.add(Chunk.NEWLINE);

        document.add(new Paragraph("Ascent Helvetica: "
                + bf_helv.getAscentPoint(foobar, 12)));
        document.add(new Paragraph("Ascent Times: "
                + bf_times.getAscentPoint(foobar, 12)));
        document.add(new Paragraph("Descent Helvetica: "
                + bf_helv.getDescentPoint(foobar, 12)));
        document.add(new Paragraph("Descent Times: "
                + bf_times.getDescentPoint(foobar, 12)));
        
        document.add(Chunk.NEWLINE);
        width_helv = bf_helv.getWidthPointKerned(foobar, 12);
        c = new Chunk(foobar + ": " + width_helv, helvetica);
        document.add(new Paragraph(c));
        
        PdfContentByte canvas = writer.getDirectContent();
        canvas.saveState();
        canvas.setLineWidth(0.05f);
        canvas.moveTo(400, 806);
        canvas.lineTo(400, 626);
        canvas.moveTo(508.7f, 806);
        canvas.lineTo(508.7f, 626);
        canvas.moveTo(280, 788);
        canvas.lineTo(520, 788);
        canvas.moveTo(280, 752);
        canvas.lineTo(520, 752);
        canvas.moveTo(280, 716);
        canvas.lineTo(520, 716);
        canvas.moveTo(280, 680);
        canvas.lineTo(520, 680);
        canvas.moveTo(280, 644);
        canvas.lineTo(520, 644);
        canvas.stroke();
        canvas.restoreState();
        
        canvas.beginText();
        canvas.setFontAndSize(bf_helv, 12);
        canvas.showTextAligned(Element.ALIGN_LEFT, foobar, 400, 788, 0);
        canvas.showTextAligned(Element.ALIGN_RIGHT, foobar, 400, 752, 0);
        canvas.showTextAligned(Element.ALIGN_CENTER, foobar, 400, 716, 0);
        canvas.showTextAligned(Element.ALIGN_CENTER, foobar, 400, 680, 30);
        canvas.showTextAlignedKerned(Element.ALIGN_LEFT, foobar, 400, 644, 0);
        canvas.endText();

        canvas.saveState();
        canvas.setLineWidth(0.05f);
        canvas.moveTo(200, 590);
        canvas.lineTo(200, 410);
        canvas.moveTo(400, 590);
        canvas.lineTo(400, 410);
        canvas.moveTo(80, 572);
        canvas.lineTo(520, 572);
        canvas.moveTo(80, 536);
        canvas.lineTo(520, 536);
        canvas.moveTo(80, 500);
        canvas.lineTo(520, 500);
        canvas.moveTo(80, 464);
        canvas.lineTo(520, 464);
        canvas.moveTo(80, 428);
        canvas.lineTo(520, 428);
        canvas.stroke();
        canvas.restoreState();
        
        Phrase phrase = new Phrase(foobar, times);
        ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, phrase, 200, 572, 0);
        ColumnText.showTextAligned(canvas, Element.ALIGN_RIGHT, phrase, 200, 536, 0);
        ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, phrase, 200, 500, 0);
        ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, phrase, 200, 464, 30);
        ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, phrase, 200, 428, -30);
        
        c = new Chunk(foobar, times);
        c.setHorizontalScaling(0.5f);
        phrase = new Phrase(c);
        ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, phrase, 400, 572, 0);
        c = new Chunk(foobar, times);
        c.setSkew(15, 15);
        phrase = new Phrase(c);
        ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, phrase, 400, 536, 0);
        c = new Chunk(foobar, times);
        c.setSkew(0, 25);
        phrase = new Phrase(c);
        ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, phrase, 400, 500, 0);
        c = new Chunk(foobar, times);
        c.setTextRenderMode(PdfContentByte.TEXT_RENDER_MODE_STROKE, 0.1f, BaseColor.RED);
        phrase = new Phrase(c);
        ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, phrase, 400, 464, 0);
        c = new Chunk(foobar, times);
        c.setTextRenderMode(PdfContentByte.TEXT_RENDER_MODE_FILL_STROKE, 1, null);
        phrase = new Phrase(c);
        ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, phrase, 400, 428, -0);
        document.close();
    }
}
