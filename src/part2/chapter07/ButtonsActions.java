/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part2.chapter07;

import java.io.FileOutputStream;
import java.io.IOException;

import part1.chapter03.MovieTemplates;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PushbuttonField;
import com.itextpdf.text.pdf.RGBColor;

public class ButtonsActions {

    /** The resulting PDF. */
    public static final String RESULT = "results/part2/chapter07/save_mail_timetable.pdf";
    
    public static void main(String[] args) throws IOException, DocumentException {
        MovieTemplates.main(args);
        new ButtonsActions().manipulatePdf(MovieTemplates.RESULT, RESULT);
    }

    public void manipulatePdf(String src, String dest) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        int n = reader.getNumberOfPages();
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        PushbuttonField saveAs = new PushbuttonField(stamper.getWriter(), new Rectangle(
                636, 10, 716, 30), "Save");
        saveAs.setBorderColor(RGBColor.BLACK);
        saveAs.setText("Save");
        saveAs.setTextColor(RGBColor.RED);
        saveAs.setLayout(PushbuttonField.LAYOUT_LABEL_ONLY);
        saveAs.setRotation(90);
        PdfAnnotation saveAsButton = saveAs.getField();
        saveAsButton.setAction(PdfAction.javaScript("app.execMenuItem('SaveAs')",
                stamper.getWriter()));
        PushbuttonField mail = new PushbuttonField(stamper.getWriter(), new Rectangle(
                736, 10, 816, 30), "Mail");
        mail.setBorderColor(RGBColor.BLACK);
        mail.setText("Mail");
        mail.setTextColor(RGBColor.RED);
        mail.setLayout(PushbuttonField.LAYOUT_LABEL_ONLY);
        mail.setRotation(90);
        PdfAnnotation mailButton = mail.getField();
        mailButton.setAction(PdfAction.javaScript("app.execMenuItem('AcroSendMail:SendMail')",
                stamper.getWriter()));
        for (int page = 1; page <= n; page++) {
            stamper.addAnnotation(saveAsButton, page);
            stamper.addAnnotation(mailButton, page);
        }
        stamper.close();
    }
}
