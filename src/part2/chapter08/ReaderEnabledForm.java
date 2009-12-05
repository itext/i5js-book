/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part2.chapter08;

import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.xml.sax.SAXException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class ReaderEnabledForm {

    public static final String RESOURCE = "resources/pdfs/xfa_enabled.pdf";
    public static final String RESULT1 = "results/part2/chapter08/xfa_broken.pdf";
    public static final String RESULT2 = "results/part2/chapter08/xfa_removed.pdf";
    public static final String RESULT3 = "results/part2/chapter08/xfa_preserved.pdf";
    
    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, TransformerFactoryConfigurationError, TransformerException, DocumentException {
        ReaderEnabledForm form = new ReaderEnabledForm();
        form.manipulatePdf(RESOURCE, RESULT1, false, false);
        form.manipulatePdf(RESOURCE, RESULT2, true, false);
        form.manipulatePdf(RESOURCE, RESULT3, false, true);
    }
    
    public void manipulatePdf(String src, String dest, boolean remove, boolean preserve) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        if (remove)
            reader.removeUsageRights();
        PdfStamper stamper;
        if (preserve) {
            stamper = new PdfStamper(reader, new FileOutputStream(dest), '\0', true);
        } else {
            stamper = new PdfStamper(reader, new FileOutputStream(dest));
        }
        AcroFields form = stamper.getAcroFields();
        form.setField("movie[0].#subform[0].title[0]", "The Misfortunates");
        form.setField("movie[0].#subform[0].original[0]", "De helaasheid der dingen");
        form.setField("movie[0].#subform[0].duration[0]", "108");
        form.setField("movie[0].#subform[0].year[0]", "2009");
        stamper.close();
    }
}
