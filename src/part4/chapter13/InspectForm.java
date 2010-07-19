/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */
package part4.chapter13;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseField;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;

import part2.chapter08.Subscribe;

public class InspectForm {

    /** A text file containing information about a form. */
    public static final String RESULTTXT = "results/part4/chapter13/fieldflags.txt";

    /**
     * Inspects a PDF file src with the file dest as result
     * @param src the original PDF
     * @param dest the resulting PDF
     * @throws IOException
     * @throws DocumentException
     */
    public void inspectPdf(String src, String dest)
        throws IOException, DocumentException {
        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(dest));
        PdfReader reader = new PdfReader(src);
        AcroFields form = reader.getAcroFields();
        Map<String,AcroFields.Item> fields = form.getFields();
        AcroFields.Item item;
        PdfDictionary dict;
        int flags;
        for (Map.Entry<String,AcroFields.Item> entry : fields.entrySet()) {
            out.write(entry.getKey());
            item = entry.getValue();
            dict = item.getMerged(0);
            flags = dict.getAsNumber(PdfName.FF).intValue();
            if ((flags & BaseField.PASSWORD) > 0)
                out.write(" -> password");
            if ((flags & BaseField.MULTILINE) > 0)
                out.write(" -> multiline");
            out.write('\n');
        }
        out.flush();
        out.close();
    }
    
    /**
     * Inspects a form.
     * @param args no arguments needed
     * @throws IOException
     * @throws DocumentException
     */
    public static void main(String[] args) throws IOException, DocumentException {
        new Subscribe().createPdf(Subscribe.RESULT);
        new InspectForm().inspectPdf(Subscribe.RESULT, RESULTTXT);
    }
}
