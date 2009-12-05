/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part2.chapter06;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfCopyFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class ConcatenateForms2 {

    public static final String DATASHEET = "resources/pdfs/datasheet.pdf";
    public static final String RESULT = "results/part2/chapter06/concatenated_forms2.pdf";
    public static void main(String[] args) throws IOException, DocumentException {
        PdfCopyFields copy = new PdfCopyFields(new FileOutputStream(RESULT));
        copy.addDocument(new PdfReader(renameFieldsIn(DATASHEET, 1)));
        copy.addDocument(new PdfReader(renameFieldsIn(DATASHEET, 2)));
        copy.close();
    }
    @SuppressWarnings("unchecked")
    private static byte[] renameFieldsIn(String datasheet, int i) throws IOException, DocumentException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfStamper stamper = new PdfStamper(new PdfReader(datasheet), baos);
        AcroFields form = stamper.getAcroFields();
        Set<String> keys = new HashSet(form.getFields().keySet());
        for (String key : keys) {
            form.renameField(key, String.format("%s_%d", key, i));
        }
        stamper.close();
        return baos.toByteArray();
    }
}
