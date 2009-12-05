/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part2.chapter06;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfCopyFields;
import com.itextpdf.text.pdf.PdfReader;

public class ConcatenateForms1 {

    public static final String DATASHEET = "resources/pdfs/datasheet.pdf";
    public static final String RESULT = "results/part2/chapter06/concatenated_forms1.pdf";
    public static void main(String[] args) throws IOException, DocumentException {
        PdfCopyFields copy = new PdfCopyFields(new FileOutputStream(RESULT));
        copy.addDocument(new PdfReader(DATASHEET));
        copy.addDocument(new PdfReader(DATASHEET));
        copy.close();
    }
}
