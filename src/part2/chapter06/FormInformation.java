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

    public static final String DATASHEET = "resources/pdfs/datasheet.pdf";
    public static final String RESULT = "results/part2/chapter06/form_info.txt";
    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws DocumentException, IOException {

        PrintWriter writer = new PrintWriter(new FileOutputStream(RESULT));
        
        PdfReader reader = new PdfReader(DATASHEET);
        AcroFields form = reader.getAcroFields();
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

        writer.println("Possible values for CP_1:");
        String[] states = form.getAppearanceStates("CP_1");
        for (int i = 0; i < states.length; i++) {
            writer.print(" - ");
            writer.println(states[i]);
        }
        writer.println("Possible values for category:");
        states = form.getAppearanceStates("category");
        for (int i = 0; i < states.length - 1; i++) {
            writer.print(states[i]);
            writer.print(", ");
        }
        writer.println(states[states.length - 1]);
        
        writer.flush();
        writer.close();
    }

}
