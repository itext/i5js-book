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
    public static final String RESOURCE = "resources/pdfs/hero.pdf";
    public static final String IMAGE = "resources/img/close.png";
    public static final String RESULT = "results/part2/chapter07/advertisement.pdf";
    
    public static void main(String[] args) throws SQLException, DocumentException, IOException {
        NestedTables.main(args);
        new Advertisement().manipulatePdf(NestedTables.RESULT, RESOURCE, RESULT);
    }
    
    public void manipulatePdf(String src, String resource, String dest) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        PdfReader ad = new PdfReader(resource);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        
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
        stamper.addAnnotation(menubar, 1);
        
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
        stamper.addAnnotation(advertisement, 1);
        stamper.close();
    }
}
