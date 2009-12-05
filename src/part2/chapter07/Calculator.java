/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part2.chapter07;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Utilities;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfAppearance;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.RGBColor;

public class Calculator {
    /** The resulting PDF. */
    public static final String RESULT = "results/part2/chapter07/calculator.pdf";
    /** Path to the resources. */
    public static final String RESOURCE = "resources/js/calculator.js";
    
    /** The font that will be used in the appearances. */
    public BaseFont bf;
    /** Position of the digits */
    Rectangle[] digits = new Rectangle[10];
    Rectangle plus, minus, mult, div, equals;
    Rectangle clearEntry, clear, result, move;

    public static void main(String[] args) throws DocumentException, IOException {
        Calculator calc = new Calculator();
        calc.initializeFont();
        calc.initializeRectangles();
        calc.createPdf(RESULT);
        
    }
    
    public void initializeFont() throws DocumentException, IOException {
        bf = BaseFont.createFont();
    }
    
    public void initializeRectangles() {
        digits[0] = createRectangle(3, 1, 1, 1);
        digits[1] = createRectangle(1, 3, 1, 1);
        digits[2] = createRectangle(3, 3, 1, 1);
        digits[3] = createRectangle(5, 3, 1, 1);
        digits[4] = createRectangle(1, 5, 1, 1);
        digits[5] = createRectangle(3, 5, 1, 1);
        digits[6] = createRectangle(5, 5, 1, 1);
        digits[7] = createRectangle(1, 7, 1, 1);
        digits[8] = createRectangle(3, 7, 1, 1);
        digits[9] = createRectangle(5, 7, 1, 1);
        plus = createRectangle(7, 7, 1, 1);
        minus = createRectangle(9, 7, 1, 1);
        mult = createRectangle(7, 5, 1, 1);
        div = createRectangle(9, 5, 1, 1);
        equals = createRectangle(7, 1, 3, 1);
        clearEntry = createRectangle(7, 9, 1, 1);
        clear = createRectangle(9, 9, 1, 1);
        result = createRectangle(1, 9, 5, 1);
        move = createRectangle(8, 3, 1, 1);
    }
    
    public void createPdf(String filename) throws IOException, DocumentException {
        Document document = new Document(new Rectangle(360, 360));
        PdfWriter writer =
            PdfWriter.getInstance(document, new FileOutputStream(filename));
        document.open();
        writer.addJavaScript(Utilities.readFileToString(RESOURCE));
        for (int i = 0; i < 10; i++) {
            addPushButton(writer, digits[i], String.valueOf(i), "this.augment(" + i + ")");
        }
        addPushButton(writer, plus, "+", "this.register('+')");
        addPushButton(writer, minus, "-", "this.register('-')");
        addPushButton(writer, mult, "x", "this.register('*')");
        addPushButton(writer, div, ":", "this.register('/')");
        addPushButton(writer, equals, "=", "this.calculateResult()");
        addPushButton(writer, clearEntry, "CE", "this.reset(false)");
        addPushButton(writer, clear, "C", "this.reset(true)");
        addTextField(writer, result, "result");
        addTextField(writer, move, "move");
        document.close();
    }

    public void addTextField(PdfWriter writer, Rectangle rect, String name) {
        PdfFormField field = PdfFormField.createTextField(writer, false, false, 0);
        field.setFieldName(name);
        field.setWidget(rect, PdfAnnotation.HIGHLIGHT_NONE);
        field.setQuadding(PdfFormField.Q_RIGHT);
        field.setFieldFlags(PdfFormField.FF_READ_ONLY);
        writer.addAnnotation(field);
    }

    public void addPushButton(PdfWriter writer, Rectangle rect, String btn, String script) {
        float w = rect.getWidth();
        float h = rect.getHeight();
        PdfFormField pushbutton = PdfFormField.createPushButton(writer);
        pushbutton.setFieldName("btn_" + btn);
        pushbutton.setWidget(rect, PdfAnnotation.HIGHLIGHT_PUSH);
        PdfContentByte cb = writer.getDirectContent();
        pushbutton.setAppearance(PdfAnnotation.APPEARANCE_NORMAL,
                createAppearance(cb, btn, RGBColor.GRAY, w, h));
        pushbutton.setAppearance(PdfAnnotation.APPEARANCE_ROLLOVER,
                createAppearance(cb, btn, RGBColor.RED, w, h));
        pushbutton.setAppearance(PdfAnnotation.APPEARANCE_DOWN,
                createAppearance(cb, btn, RGBColor.BLUE, w, h));
        pushbutton.setAdditionalActions(PdfName.U,
                PdfAction.javaScript(script, writer));
        pushbutton.setAdditionalActions(PdfName.E, PdfAction.javaScript(
                "this.showMove('" + btn + "');", writer));
        pushbutton.setAdditionalActions(PdfName.X, PdfAction.javaScript(
                "this.showMove(' ');", writer));
        writer.addAnnotation(pushbutton);
    }

    public PdfAppearance createAppearance(PdfContentByte cb, String btn, RGBColor color, float w, float h) {
        PdfAppearance app = cb.createAppearance(w, h);
        app.setColorFill(color);
        app.rectangle(2, 2, w - 4, h - 4);
        app.fill();
        app.beginText();
        app.setColorFill(RGBColor.BLACK);
        app.setFontAndSize(bf, h / 2);
        app.showTextAligned(Element.ALIGN_CENTER, btn, w / 2, h / 4, 0);
        app.endText();
        return app;
    }

    public Rectangle createRectangle(int column, int row, int width,
            int height) {
        column = column * 36 - 18;
        row = row * 36 - 18;
        return new Rectangle(column, row, column + width * 36, row + height
                * 36);
    }
}