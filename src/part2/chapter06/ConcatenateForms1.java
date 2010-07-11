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

    /** The original PDF file. */
    public static final String DATASHEET
        = "resources/pdfs/datasheet.pdf";
    /** The resulting PDF file. */
    public static final String RESULT
        = "results/part2/chapter06/concatenated_forms1.pdf";
    
    /**
     * Main method.
     * @param args no arguments needed
     * @throws DocumentException 
     * @throws IOException
     */
    public static void main(String[] args)
        throws IOException, DocumentException {
    	// Create a PdfCopyFields object
        PdfCopyFields copy
            = new PdfCopyFields(new FileOutputStream(RESULT));
        // add a document
        copy.addDocument(new PdfReader(DATASHEET));
        // add a document
        copy.addDocument(new PdfReader(DATASHEET));
        // close the PdfCopyFields object
        copy.close();
    }
}
