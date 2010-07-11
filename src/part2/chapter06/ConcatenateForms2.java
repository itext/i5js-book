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

    /** The original PDF file. */
    public static final String DATASHEET
        = "resources/pdfs/datasheet.pdf";
    /** The resulting PDF file. */
    public static final String RESULT
        = "results/part2/chapter06/concatenated_forms2.pdf";
    
    /**
     * Main method.
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException
     */
    public static void main(String[] args)
        throws IOException, DocumentException {
    	// Create a PdfCopyFields object
        PdfCopyFields copy
            = new PdfCopyFields(new FileOutputStream(RESULT));
        // add a document
        copy.addDocument(new PdfReader(renameFieldsIn(DATASHEET, 1)));
        // add a document
        copy.addDocument(new PdfReader(renameFieldsIn(DATASHEET, 2)));
        // Close the PdfCopyFields object
        copy.close();
    }
    
    /**
     * Renames the fields in an interactive form.
     * @param datasheet the path to the original form
     * @param i a number that needs to be appended to the field names
     * @return a byte[] containing an altered PDF file
     * @throws IOException
     * @throws DocumentException
     */
    private static byte[] renameFieldsIn(String datasheet, int i)
        throws IOException, DocumentException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // Create the stamper
        PdfStamper stamper = new PdfStamper(new PdfReader(datasheet), baos);
        // Get the fields
        AcroFields form = stamper.getAcroFields();
        // Loop over the fields
        Set<String> keys = new HashSet<String>(form.getFields().keySet());
        for (String key : keys) {
            // rename the fields
            form.renameField(key, String.format("%s_%d", key, i));
        }
        // close the stamper
        stamper.close();
        return baos.toByteArray();
    }
}
