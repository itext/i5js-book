/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part2.chapter08;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.TextField;

public class TextFieldFonts {

    /** The resulting PDF. */
    public static final String RESULT1 = "results/part2/chapter08/unicode_field_1.pdf";
    /** The resulting PDF. */
    public static final String RESULT2 = "results/part2/chapter08/unicode_field_2.pdf";
    /** The resulting PDF. */
    public static final String RESULT3 = "results/part2/chapter08/unicode_field_3.pdf";
    /** The resulting PDF. */
    public static final String RESULT4 = "results/part2/chapter08/unicode_field_4.pdf";
    /** The resulting PDF. */
    public static final String RESULT5 = "results/part2/chapter08/unicode_field_5.pdf";
    /** The resulting PDF. */
    public static final String RESULT6 = "results/part2/chapter08/unicode_field_6.pdf";
    /** The resulting PDF. */
    public static final String RESULT7 = "results/part2/chapter08/unicode_field_7.pdf";
    /** The resulting PDF. */
    public static final String RESULT8 = "results/part2/chapter08/unicode_field_8.pdf";
    /** A String containing Chinese characters/ */
    public static final String TEXT = "These are the protagonists in 'Hero', a movie by Zhang Yimou:\n"
        + "\u7121\u540d (Nameless), \u6b98\u528d (Broken Sword), "
        + "\u98db\u96ea (Flying Snow), \u5982\u6708 (Moon), "
        + "\u79e6\u738b (the King), and \u9577\u7a7a (Sky).";
    /** A String containing Korean characters. */
    public static final String BINJIP = "The Korean title of the movie 3-Iron is \ube48\uc9d1 (Bin-Jip)";

    /**
     * Creates a PDF document.
     * @param filename the path to the new PDF document
     * @param appearacnes sets the need appearances flag if true
     * @param font adds a substitution font if true
     * @throws    DocumentException 
     * @throws    IOException
     */
    public void createPdf(String filename, boolean appearances, boolean font)
        throws IOException, DocumentException {
    	// step 1
        Document document = new Document();
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
        // step 3
        document.open();
        // step 4
        writer.getAcroForm().setNeedAppearances(appearances);
        TextField text = new TextField(writer, new Rectangle(36, 806, 559, 780), "description");
        text.setOptions(TextField.MULTILINE);
        if (font) {
            BaseFont unicode =
                BaseFont.createFont("c:/windows/fonts/arialuni.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            text.setExtensionFont(BaseFont.createFont());
            ArrayList<BaseFont> list = new ArrayList<BaseFont>();
            list.add(unicode);
            text.setSubstitutionFonts(list);
        }
        text.setText(TEXT);
        writer.addAnnotation(text.getTextField());
        // step 5
        document.close();
    }

    /**
     * Manipulates a PDF file src with the file dest as result
     * @param src the original PDF
     * @param dest the resulting PDF
     * @throws IOException
     * @throws DocumentException
     */
    public void manipulatePdf(String src, String dest) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        AcroFields form = stamper.getAcroFields();
        form.setField("description", BINJIP);
        stamper.close();
    }

    /**
     * Manipulates a PDF file src with the file dest as result
     * @param src the original PDF
     * @param dest the resulting PDF
     * @throws IOException
     * @throws DocumentException
     */
    public void manipulatePdfFont1(String src, String dest) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        AcroFields form = stamper.getAcroFields();
        BaseFont unicode =
            BaseFont.createFont("HYSMyeongJoStd-Medium", "UniKS-UCS2-H", BaseFont.NOT_EMBEDDED);
        form.setFieldProperty("description", "textfont", unicode, null);
        form.setField("description", BINJIP);
        stamper.close();
    }

    /**
     * Manipulates a PDF file src with the file dest as result
     * @param src the original PDF
     * @param dest the resulting PDF
     * @throws IOException
     * @throws DocumentException
     */
    public void manipulatePdfFont2(String src, String dest) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        AcroFields form = stamper.getAcroFields();
        BaseFont unicode =
            BaseFont.createFont("c:/windows/fonts/arialuni.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        form.addSubstitutionFont(unicode);
        form.setField("description", BINJIP);
        stamper.close();
    }
    
    /**
     * Main method
     * @param args no arguments needed
     * @throws IOException
     * @throws DocumentException
     */
    public static void main(String[] args) throws IOException, DocumentException {
        TextFieldFonts example = new TextFieldFonts();
        example.createPdf(RESULT1, false, false);
        example.createPdf(RESULT2, true, false);
        example.createPdf(RESULT3, false, true);
        example.manipulatePdf(RESULT1, RESULT4);
        example.manipulatePdf(RESULT2, RESULT5);
        example.manipulatePdf(RESULT3, RESULT6);
        example.manipulatePdfFont1(RESULT3, RESULT7);
        example.manipulatePdfFont2(RESULT3, RESULT8);
    }
}
