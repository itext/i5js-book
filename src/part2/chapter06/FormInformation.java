/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part2.chapter06;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;

public class FormInformation {

    /** The original PDF file. */
    public static final String DATASHEET
        = "resources/pdfs/datasheet.pdf";
    /** The resulting PDF file. */
    public static final String RESULT
        = "results/part2/chapter06/form_info.txt";    
    
    /**
     * Main method.
     * @param args no arguments needed
     * @throws DocumentException 
     * @throws IOException
     */
    public static void main(String[] args)
        throws DocumentException, IOException {
        // Create a writer for the report file
        PrintWriter writer
            = new PrintWriter(new FileOutputStream(RESULT));
        // Create a reader to extract info
        PdfReader reader = new PdfReader(DATASHEET);
        // Get the fields from the reader (read-only!!!)
        AcroFields form = reader.getAcroFields();
        // Loop over the fields and get info about them
        Set<String> fields = form.getFields().keySet();
        for (String key : fields) {
            writer.print(key + ": ");
            switch (form.getFieldType(key)) {
            case AcroFields.FIELD_TYPE_CHECKBOX:
                writer.println("Checkbox");
                break;
            case AcroFields.FIELD_TYPE_COMBO:
                writer.println("Combobox");
                break;
            case AcroFields.FIELD_TYPE_LIST:
                writer.println("List");
                break;
            case AcroFields.FIELD_TYPE_NONE:
                writer.println("None");
                break;
            case AcroFields.FIELD_TYPE_PUSHBUTTON:
                writer.println("Pushbutton");
                break;
            case AcroFields.FIELD_TYPE_RADIOBUTTON:
                writer.println("Radiobutton");
                break;
            case AcroFields.FIELD_TYPE_SIGNATURE:
                writer.println("Signature");
                break;
            case AcroFields.FIELD_TYPE_TEXT:
                writer.println("Text");
                break;
            default:
                writer.println("?");
            }
        }
        // Get possible values for field "CP_1"
        writer.println("Possible values for CP_1:");
        String[] states = form.getAppearanceStates("CP_1");
        for (int i = 0; i < states.length; i++) {
            writer.print(" - ");
            writer.println(states[i]);
        }
        // Get possible values for field "category"
        writer.println("Possible values for category:");
        states = form.getAppearanceStates("category");
        for (int i = 0; i < states.length - 1; i++) {
            writer.print(states[i]);
            writer.print(", ");
        }
        writer.println(states[states.length - 1]);
        // flush and close the report file
        writer.flush();
        writer.close();
    }

}
