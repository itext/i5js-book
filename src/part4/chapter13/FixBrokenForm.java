/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part4.chapter13;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class FixBrokenForm {

    /** The original PDF file. */
    public static final String ORIGINAL = "resources/pdfs/broken_form.pdf";
    /** The resulting PDF file. */
    public static final String FIXED = "results/part4/chapter13/fixed_form.pdf";

    /** The original PDF file that couldn't be filled out. */
    public static final String RESULT1 = "results/part4/chapter13/broken_form.pdf";
    /** The fixed PDF file that was correctly filled out. */
    public static final String RESULT2 = "results/part4/chapter13/filled_form.pdf";
    
    /**
     * Manipulates a PDF file src with the file dest as result
     * @param src the original PDF
     * @param dest the resulting PDF
     * @throws IOException
     * @throws DocumentException 
     */
    public void manipulatePdf(String src, String dest) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        PdfDictionary root = reader.getCatalog();
        PdfDictionary form = root.getAsDict(PdfName.ACROFORM);
        PdfArray fields = form.getAsArray(PdfName.FIELDS);

        PdfDictionary page;
        PdfArray annots;
        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
            page = reader.getPageN(i);
            annots = page.getAsArray(PdfName.ANNOTS);
            for (int j = 0; j < annots.size(); j++) {
                fields.add(annots.getAsIndirectObject(j));
            }
        }
        PdfStamper stamper = new PdfStamper(reader,
             new FileOutputStream(dest));
        stamper.close();
    }
    
    /**
     * @param src
     * @param dest
     * @throws IOException
     * @throws DocumentException
     */
    public void fillData(String src, String dest) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        AcroFields form = stamper.getAcroFields();
        form.setField("title", "The Misfortunates");
        form.setField("director", "Felix Van Groeningen");
        form.setField("year", "2009");
        form.setField("duration", "108");
        stamper.close();
    }
    
    /**
     * Main method.
     *
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException
     */
    public static void main(String[] args) throws IOException, DocumentException {
        FixBrokenForm example = new FixBrokenForm();
        example.manipulatePdf(ORIGINAL, FIXED);
        example.fillData(ORIGINAL, RESULT1);
        example.fillData(FIXED, RESULT2);
    }

}
