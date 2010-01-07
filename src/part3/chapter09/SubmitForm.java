/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part3.chapter09;

import java.io.FileOutputStream;
import java.io.IOException;

import part2.chapter08.Subscribe;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PushbuttonField;

public class SubmitForm {

    /** The resulting PDF file. */
    public static final String RESULT
        = "results/part3/chapter09/submit_me.pdf";
    
    /**
     * Manipulates a PDF file src with the file dest as result
     * @param src the original PDF
     * @param dest the resulting PDF
     * @throws IOException
     * @throws DocumentException
     */
    public void manipulatePdf(String src, String dest)
        throws IOException, DocumentException {
        // create a reader
        PdfReader reader = new PdfReader(src);
        // create a stamper
        PdfStamper stamper = new PdfStamper(reader,
            new FileOutputStream(dest));
        // create a submit button that posts the form as an HTML query string
        PushbuttonField button1 = new PushbuttonField(
            stamper.getWriter(), new Rectangle(90, 660, 140, 690), "post");
        button1.setText("POST");
        button1.setBackgroundColor(new GrayColor(0.7f));
        button1.setVisibility(PushbuttonField.VISIBLE_BUT_DOES_NOT_PRINT);
        PdfFormField submit1 = button1.getField();
        submit1.setAction(PdfAction.createSubmitForm(
            "/book/request", null,
            PdfAction.SUBMIT_HTML_FORMAT | PdfAction.SUBMIT_COORDINATES));
        // add the button
        stamper.addAnnotation(submit1, 1);
        // create a submit button that posts the form as FDF
        PushbuttonField button2 = new PushbuttonField(
            stamper.getWriter(), new Rectangle(200, 660, 250, 690), "FDF");
        button2.setBackgroundColor(new GrayColor(0.7f));
        button2.setText("FDF");
        button2.setVisibility(PushbuttonField.VISIBLE_BUT_DOES_NOT_PRINT);
        PdfFormField submit2 = button2.getField();
        submit2.setAction(PdfAction.createSubmitForm(
            "/book/request", null, PdfAction.SUBMIT_EXCL_F_KEY));
        // add the button
        stamper.addAnnotation(submit2, 1);
        // create a submit button that posts the form as XFDF
        PushbuttonField button3 = new PushbuttonField(
                stamper.getWriter(), new Rectangle(310, 660, 360, 690), "XFDF");
        button3.setBackgroundColor(new GrayColor(0.7f));
        button3.setText("XFDF");
        button3.setVisibility(PushbuttonField.VISIBLE_BUT_DOES_NOT_PRINT);
        PdfFormField submit3 = button3.getField();
        submit3.setAction(PdfAction.createSubmitForm(
            "/book/request", null, PdfAction.SUBMIT_XFDF));
        // add the button
        stamper.addAnnotation(submit3, 1);
        // create a reset button
        PushbuttonField button4 = new PushbuttonField(
            stamper.getWriter(), new Rectangle(420, 660, 470, 690), "reset");
        button4.setBackgroundColor(new GrayColor(0.7f));
        button4.setText("RESET");
        button4.setVisibility(PushbuttonField.VISIBLE_BUT_DOES_NOT_PRINT);
        PdfFormField reset = button4.getField();
        reset.setAction(PdfAction.createResetForm(null, 0));
        // add the button
        stamper.addAnnotation(reset, 1);
        // close the stamper
        stamper.close();
    }
    
    /**
     * Main method.
     *
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException
     */
    public static void main(String[] args)
        throws IOException, DocumentException {
        new Subscribe().createPdf(Subscribe.FORM);
        new SubmitForm().manipulatePdf(Subscribe.FORM, RESULT);
    }
}
