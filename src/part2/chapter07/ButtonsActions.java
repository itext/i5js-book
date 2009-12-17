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
import com.itextpdf.text.BaseColor;

public class ButtonsActions {

    /** The resulting PDF. */
    public static final String RESULT
        = "results/part2/chapter07/save_mail_timetable.pdf";

    /**
     * Manipulates a PDF file src with the file dest as result
     * @param src the original PDF
     * @param dest the resulting PDF
     * @throws IOException
     * @throws DocumentException
     */
    public void manipulatePdf(String src, String dest)
        throws IOException, DocumentException {
    	// Create a reader
        PdfReader reader = new PdfReader(src);
        int n = reader.getNumberOfPages();
        // Create a stamper
        PdfStamper stamper
            = new PdfStamper(reader, new FileOutputStream(dest));
        // Create pushbutton 1
        PushbuttonField saveAs =
            new PushbuttonField(stamper.getWriter(),
                new Rectangle(636, 10, 716, 30), "Save");
        saveAs.setBorderColor(BaseColor.BLACK);
        saveAs.setText("Save");
        saveAs.setTextColor(BaseColor.RED);
        saveAs.setLayout(PushbuttonField.LAYOUT_LABEL_ONLY);
        saveAs.setRotation(90);
        PdfAnnotation saveAsButton = saveAs.getField();
        saveAsButton.setAction(PdfAction.javaScript(
            "app.execMenuItem('SaveAs')", stamper.getWriter()));
        // Create pushbutton 2
        PushbuttonField mail
            = new PushbuttonField(stamper.getWriter(),
                new Rectangle(736, 10, 816, 30), "Mail");
        mail.setBorderColor(BaseColor.BLACK);
        mail.setText("Mail");
        mail.setTextColor(BaseColor.RED);
        mail.setLayout(PushbuttonField.LAYOUT_LABEL_ONLY);
        mail.setRotation(90);
        PdfAnnotation mailButton = mail.getField();
        mailButton.setAction(PdfAction.javaScript(
            "app.execMenuItem('AcroSendMail:SendMail')", stamper.getWriter()));
        // Add the annotations to every page of the document
        for (int page = 1; page <= n; page++) {
            stamper.addAnnotation(saveAsButton, page);
            stamper.addAnnotation(mailButton, page);
        }
        // Close the stamper
        stamper.close();
    }
    
    /**
     * Main method.
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException
     */
    public static void main(String[] args)
        throws IOException, DocumentException {
        MovieTemplates.main(args);
        new ButtonsActions().manipulatePdf(
            MovieTemplates.RESULT, RESULT);
    }
}
