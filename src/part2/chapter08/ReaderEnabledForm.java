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

    /** The original PDF. */
    public static final String RESOURCE = "resources/pdfs/xfa_enabled.pdf";
    /** The resulting PDF. */
    public static final String RESULT1 = "results/part2/chapter08/xfa_broken.pdf";
    /** The resulting PDF. */
    public static final String RESULT2 = "results/part2/chapter08/xfa_removed.pdf";
    /** The resulting PDF. */
    public static final String RESULT3 = "results/part2/chapter08/xfa_preserved.pdf";

    /**
     * Manipulates a PDF file src with the file dest as result
     * @param src the original PDF
     * @param dest the resulting PDF
     * @throws IOException
     * @throws DocumentException
     */
    public void manipulatePdf(String src, String dest, boolean remove, boolean preserve)
        throws IOException, DocumentException {
        // create the reader
    	PdfReader reader = new PdfReader(src);
        // remove the usage rights (or not)
        if (remove)
            reader.removeUsageRights();
        // create the stamper
        PdfStamper stamper;
        // preserve the reader enabling by creating a PDF in append mode (or not)
        if (preserve) {
            stamper = new PdfStamper(reader, new FileOutputStream(dest), '\0', true);
        } else {
            stamper = new PdfStamper(reader, new FileOutputStream(dest));
        }
        // fill out the fields
        AcroFields form = stamper.getAcroFields();
        form.setField("movie[0].#subform[0].title[0]", "The Misfortunates");
        form.setField("movie[0].#subform[0].original[0]", "De helaasheid der dingen");
        form.setField("movie[0].#subform[0].duration[0]", "108");
        form.setField("movie[0].#subform[0].year[0]", "2009");
        // close the stamper
        stamper.close();
    }

    /**
     * Creates a PDF document.
     * @param filename the path to the new PDF document
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws TransformerFactoryConfigurationError
     * @throws TransformerException
     * @throws DocumentException
     */
    public static void main(String[] args) throws IOException, DocumentException,
        ParserConfigurationException, SAXException,
        TransformerFactoryConfigurationError, TransformerException {
        ReaderEnabledForm form = new ReaderEnabledForm();
        form.manipulatePdf(RESOURCE, RESULT1, false, false);
        form.manipulatePdf(RESOURCE, RESULT2, true, false);
        form.manipulatePdf(RESOURCE, RESULT3, false, true);
    }
}
