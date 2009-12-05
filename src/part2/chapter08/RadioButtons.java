/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part2.chapter08;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.RadioCheckField;

public class RadioButtons {

    public static final String RESULT = "results/part2/chapter08/radiobuttons.pdf";
    public static final String[] LANGUAGES = { "English", "German", "French", "Spanish", "Dutch" };
    
    public static void main(String[] args) throws IOException, DocumentException {
        new RadioButtons().createPdf(RESULT);
    }

    public void createPdf(String filename) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter writer =
            PdfWriter.getInstance(document, new FileOutputStream(filename));
        document.open();
        PdfContentByte cb = writer.getDirectContent();
        BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
        
        PdfFormField radiogroup = PdfFormField.createRadioButton(writer, true);
        radiogroup.setFieldName("language");
        Rectangle rect = new Rectangle(40, 806, 60, 788);
        RadioCheckField radio;
        PdfFormField radiofield;
        for (int page = 0; page < LANGUAGES.length; ) {
            radio = new RadioCheckField(writer, rect, null, LANGUAGES[page]);
            radio.setBackgroundColor(new GrayColor(0.8f));
            radiofield = radio.getRadioField();
            radiofield.setPlaceInPage(++page);
            radiogroup.addKid(radiofield);
        }
        writer.addAnnotation(radiogroup);
        for (int i = 0; i < LANGUAGES.length; i++) {
            cb.beginText();
            cb.setFontAndSize(bf, 18);
            cb.showTextAligned(Element.ALIGN_LEFT, LANGUAGES[i], 70, 790, 0);
            cb.endText();
            document.newPage();
        }
        
        document.close();

    }
}
