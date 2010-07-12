/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part2.chapter08;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PushbuttonField;

import part2.chapter07.Advertisement;

public class ReplaceIcon {

    /** Image that will be used as an icon. */
    public static final String RESOURCE = "resources/img/iia2.jpg";
    /** The resulting PDF. */
    public static final String RESULT = "results/part2/chapter08/advertisement2.pdf";

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
        PushbuttonField ad = form.getNewPushbuttonFromField("advertisement");
        ad.setLayout(PushbuttonField.LAYOUT_ICON_ONLY);
        ad.setProportionalIcon(true);
        ad.setImage(Image.getInstance(RESOURCE));
        form.replacePushbuttonField("advertisement", ad.getField());
        stamper.close();
    }

    /**
     * Main method
     * @param args no arguments needed
     * @throws SQLException
     * @throws DocumentException
     * @throws IOException
     */
    public static void main(String[] args) throws SQLException, DocumentException, IOException {
        Advertisement.main(args);
        new ReplaceIcon().manipulatePdf(Advertisement.RESULT, RESULT);
    }
}
