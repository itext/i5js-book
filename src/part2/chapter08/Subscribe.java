/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part2.chapter08;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.TextField;

public class Subscribe {

    /** The resulting PDF. */
    public static final String FORM = "results/part2/chapter08/subscribe.pdf";
    /** The resulting PDFs. */
    public static final String RESULT = "results/part2/chapter08/filled_form_%d.pdf";

    /**
     * Manipulates a PDF file src with the file dest as result
     * @param src the original PDF
     * @param dest the resulting PDF
     * @param cache a map that will be used to cache text field information
     * @param name the name of a person
     * @param login the login name of the same person
     * @throws IOException
     * @throws DocumentException
     */
    public void manipulatePdf(String src, String dest,
        HashMap<String,TextField> cache, String name, String login)
        throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        AcroFields form = stamper.getAcroFields();
        form.setFieldCache(cache);
        form.setExtraMargin(2, 0);
        form.removeField("personal.password");
        form.setField("personal.name", name);
        form.setField("personal.loginname", login);
        form.renameField("personal.reason", "personal.motivation");
        form.setFieldProperty("personal.loginname", "setfflags", TextField.READ_ONLY, null);
        stamper.setFormFlattening(true);
        stamper.partialFormFlattening("personal.name");
        stamper.close();
    }

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
        // step 5
        document.close();
    }
    
    /**
     * Main method
     * @param args no arguments needed
     * @throws IOException
     * @throws DocumentException
     */
    public static void main(String[] args) throws IOException, DocumentException {
        Subscribe subscribe = new Subscribe();
        subscribe.createPdf(FORM);
        HashMap<String,TextField> fieldCache = new HashMap<String,TextField>();
        subscribe.manipulatePdf(FORM, String.format(RESULT, 1), fieldCache, "Bruno Lowagie", "blowagie");
        subscribe.manipulatePdf(FORM, String.format(RESULT, 2), fieldCache, "Paulo Soares", "psoares");
        subscribe.manipulatePdf(FORM, String.format(RESULT, 3), fieldCache, "Mark Storer", "mstorer");
    }
}
