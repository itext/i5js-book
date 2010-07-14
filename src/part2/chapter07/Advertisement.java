/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part2.chapter07;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import part1.chapter04.NestedTables;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PushbuttonField;
import com.itextpdf.text.BaseColor;

public class Advertisement {

    /** Path to a resource. */
    public static final String RESOURCE = "resources/pdfs/hero.pdf";
    /** Path to a resource. */
    public static final String IMAGE = "resources/img/close.png";
    /** The resulting PDF file. */
    public static final String RESULT
        = "results/part2/chapter07/advertisement.pdf";

    /**
     * Manipulates a PDF file src with the file dest as result
     * @param src the original PDF
     * @param resource a resource that will be used as advertisement
     * @param dest the resulting PDF
     * @throws IOException
     * @throws DocumentException
     */
    public void manipulatePdf(String src, String resource, String dest)
        throws IOException, DocumentException {
    	// Create a reader for the original document
        PdfReader reader = new PdfReader(src);
        // Create a reader for the advertisement resource
        PdfReader ad = new PdfReader(resource);
        // Create a stamper
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        // Create the advertisement annotation for the menubar
        Rectangle rect = new Rectangle(400, 772, 545, 792);
        PushbuttonField button = new PushbuttonField(
            stamper.getWriter(), rect, "click");
        button.setBackgroundColor(BaseColor.RED);
        button.setBorderColor(BaseColor.RED);
        button.setFontSize(10);
        button.setText("Close this advertisement");
        button.setImage(Image.getInstance(IMAGE));
        button.setLayout(PushbuttonField.LAYOUT_LABEL_LEFT_ICON_RIGHT);
        button.setIconHorizontalAdjustment(1);
        PdfFormField menubar = button.getField();
        String js = "var f1 = getField('click'); f1.display = display.hidden;"
            + "var f2 = getField('advertisement'); f2.display = display.hidden;";
        menubar.setAction(PdfAction.javaScript(js, stamper.getWriter()));
        // Add the annotation
        stamper.addAnnotation(menubar, 1);
        // Create the advertisement annotation for the content
        rect = new Rectangle(400, 550, 545, 772);
        button = new PushbuttonField(
            stamper.getWriter(), rect, "advertisement");
        button.setBackgroundColor(BaseColor.WHITE);
        button.setBorderColor(BaseColor.RED);
        button.setText("Buy the book iText in Action 2nd edition");
        button.setTemplate(stamper.getImportedPage(ad, 1));
        button.setLayout(PushbuttonField.LAYOUT_ICON_TOP_LABEL_BOTTOM);
        PdfFormField advertisement = button.getField();
        advertisement.setAction(
            new PdfAction("http://www.1t3xt.com/docs/book.php"));
        // Add the annotation
        stamper.addAnnotation(advertisement, 1);
        // Close the stamper
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
        throws SQLException, DocumentException, IOException {
        NestedTables.main(args);
        new Advertisement().manipulatePdf(
            NestedTables.RESULT, RESOURCE, RESULT);
    }
}
