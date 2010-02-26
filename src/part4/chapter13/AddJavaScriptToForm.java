/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part4.chapter13;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Utilities;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PushbuttonField;
import com.itextpdf.text.pdf.RadioCheckField;
import com.itextpdf.text.pdf.TextField;

public class AddJavaScriptToForm {
    /** The resulting PDF file. */
    public static final String ORIGINAL
        = "results/part4/chapter13/form_without_js.pdf";
    /** The resulting PDF file. */
    public static final String RESULT
        = "results/part4/chapter13/form_with_js.pdf";
    /** Path to the resources. */
    public static final String RESOURCE
        = "resources/js/extra.js";

    /**
     * Creates a PDF document.
     * @param filename the path to the new PDF document
     * @throws    DocumentException 
     * @throws    IOException 
     * @throws    SQLException
     */
	public void createPdf(String filename) throws IOException, DocumentException {
		Document document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
		document.open();
 
		BaseFont bf = BaseFont.createFont();
		PdfContentByte directcontent = writer.getDirectContent();
		directcontent.beginText();
		directcontent.setFontAndSize(bf, 12);
		directcontent.showTextAligned(Element.ALIGN_LEFT, "Married?", 36, 770, 0);
		directcontent.showTextAligned(Element.ALIGN_LEFT, "YES", 58, 750, 0);
		directcontent.showTextAligned(Element.ALIGN_LEFT, "NO", 102, 750, 0);
		directcontent.showTextAligned(Element.ALIGN_LEFT, "Name partner?", 36, 730, 0);
		directcontent.endText();
 
		PdfFormField married = PdfFormField.createRadioButton(writer, true);
		married.setFieldName("married");
		married.setValueAsName("yes");
		Rectangle rectYes = new Rectangle(40, 766, 56, 744);
		RadioCheckField yes = new RadioCheckField(writer, rectYes, null, "yes");
		yes.setChecked(true);
		married.addKid(yes.getRadioField());
		Rectangle rectNo = new Rectangle(84, 766, 100, 744);
		RadioCheckField no = new RadioCheckField(writer, rectNo, null, "no");
		no.setChecked(false);
		married.addKid(no.getRadioField());
		writer.addAnnotation(married);
 
		Rectangle rect = new Rectangle(40, 710, 200, 726);
		TextField partner = new TextField(writer, rect, "partner");
		partner.setBorderColor(GrayColor.GRAYBLACK);
		partner.setBorderWidth(0.5f);
		writer.addAnnotation(partner.getTextField());
 
		document.close();
	}
    
    /**
     * Manipulates a PDF file src with the file dest as result
     * @param src the original PDF
     * @param dest the resulting PDF
     * @throws IOException
     * @throws DocumentException
     */
    public void manipulatePdf(String src, String dest)
        throws IOException, DocumentException {
    	PdfReader reader = new PdfReader(src);
    	PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        stamper.getWriter().addJavaScript(Utilities.readFileToString(RESOURCE));
        AcroFields form = stamper.getAcroFields();
        AcroFields.Item fd = form.getFieldItem("married");
 
        PdfDictionary dictYes =
        	(PdfDictionary) PdfReader.getPdfObject(fd.getWidgetRef(0));
        PdfDictionary yesAction = dictYes.getAsDict(PdfName.AA);
        if (yesAction == null) yesAction = new PdfDictionary();
        yesAction.put(new PdfName("Fo"),
        		PdfAction.javaScript("setReadOnly(false);", stamper.getWriter()));
        dictYes.put(PdfName.AA, yesAction);
 
        PdfDictionary dictNo =
        	(PdfDictionary) PdfReader.getPdfObject(fd.getWidgetRef(1));
        PdfDictionary noAction = dictNo.getAsDict(PdfName.AA);
        if (noAction == null) noAction = new PdfDictionary();
        noAction.put(new PdfName("Fo"),
        		PdfAction.javaScript("setReadOnly(true);", stamper.getWriter()));
        dictNo.put(PdfName.AA, noAction);
 
		PdfWriter writer = stamper.getWriter();
		PushbuttonField button = new PushbuttonField(writer,
				new Rectangle(40, 690, 200, 710), "submit");
		button.setText("validate and submit");
		button.setOptions(PushbuttonField.VISIBLE_BUT_DOES_NOT_PRINT);
		PdfFormField validateAndSubmit = button.getField();
		validateAndSubmit.setAction(PdfAction.javaScript("validate();", stamper.getWriter()));
		stamper.addAnnotation(validateAndSubmit, 1);
		stamper.close();
    }
    
    /**
     * Main method.
     *
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException 
     * @throws SQLException
     */
    public static void main(String[] args)
        throws IOException, DocumentException {
    	AddJavaScriptToForm example = new AddJavaScriptToForm();
    	example.createPdf(ORIGINAL);
    	example.manipulatePdf(ORIGINAL, RESULT);
    }
}
