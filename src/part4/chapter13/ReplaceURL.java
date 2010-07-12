/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part4.chapter13;

import java.io.FileOutputStream;
import java.io.IOException;

import part2.chapter08.ChildFieldEvent;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PushbuttonField;
import com.itextpdf.text.pdf.TextField;

public class ReplaceURL {

    /** The resulting PDF file. */
    public static final String RESULT1
        = "results/part4/chapter13/submit1.pdf";
    /** The resulting PDF file. */
    public static final String RESULT2
        = "results/part4/chapter13/submit2.pdf";

    /**
     * Creates a PDF document.
     * @param filename the path to the new PDF document
     * @throws    DocumentException 
     * @throws    IOException
     */
    public void createPdf(String filename) throws IOException, DocumentException {
    	// step 1
        Document document = new Document();
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
        // step 3
        document.open();
        // step 4
        PdfFormField personal = PdfFormField.createEmpty(writer);
        personal.setFieldName("personal");
        PdfPTable table = new PdfPTable(3);
        PdfPCell cell;
        
        table.addCell("Your name:");
        cell = new PdfPCell();
        cell.setColspan(2);
        TextField field = new TextField(writer, new Rectangle(0, 0), "name");
        field.setFontSize(12);
        cell.setCellEvent(new ChildFieldEvent(personal, field.getTextField(), 1));
        table.addCell(cell);
        table.addCell("Login:");
        cell = new PdfPCell();
        field = new TextField(writer, new Rectangle(0, 0), "loginname");
        field.setFontSize(12);
        cell.setCellEvent(new ChildFieldEvent(personal, field.getTextField(), 1));
        table.addCell(cell);
        cell = new PdfPCell();
        field = new TextField(writer, new Rectangle(0, 0), "password");
        field.setOptions(TextField.PASSWORD);
        field.setFontSize(12);
        cell.setCellEvent(new ChildFieldEvent(personal, field.getTextField(), 1));
        table.addCell(cell);
        table.addCell("Your motivation:");
        cell = new PdfPCell();
        cell.setColspan(2);
        cell.setFixedHeight(60);
        field = new TextField(writer, new Rectangle(0, 0), "reason");
        field.setOptions(TextField.MULTILINE);
        field.setFontSize(12);
        cell.setCellEvent(new ChildFieldEvent(personal, field.getTextField(), 1));
        table.addCell(cell);
        document.add(table);
        writer.addAnnotation(personal);
        
        PushbuttonField button1 = new PushbuttonField(
            writer, new Rectangle(90, 660, 140, 690), "post");
        button1.setText("POST");
        button1.setBackgroundColor(new GrayColor(0.7f));
        button1.setVisibility(PushbuttonField.VISIBLE_BUT_DOES_NOT_PRINT);
        PdfFormField submit1 = button1.getField();
        submit1.setAction(PdfAction.createSubmitForm("/book/request", null,
            PdfAction.SUBMIT_HTML_FORMAT | PdfAction.SUBMIT_COORDINATES));
        writer.addAnnotation(submit1);
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
    public void manipulatePdf(String src, String dest)
        throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        AcroFields form = reader.getAcroFields();
        AcroFields.Item item = form.getFieldItem("post");
        PdfDictionary field = item.getMerged(0);
        PdfDictionary action = field.getAsDict(PdfName.A);
        PdfDictionary f = action.getAsDict(PdfName.F);
        f.put(PdfName.F, new PdfString("http://itextpdf.com:8080/book/request"));
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
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
        ReplaceURL form = new ReplaceURL();
        form.createPdf(RESULT1);
        form.manipulatePdf(RESULT1, RESULT2);
    }
}
