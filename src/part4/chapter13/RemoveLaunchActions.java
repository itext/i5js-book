/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */
package part4.chapter13;

import java.io.FileOutputStream;
import java.io.IOException;

import part2.chapter07.LaunchAction;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfString;

public class RemoveLaunchActions {
    /** The resulting PDF. */
    public static final String RESULT
        = "results/part4/chapter13/launch_removed.pdf";
    
    /**
     * Manipulates a PDF file src with the file dest as result
     * @param src the original PDF
     * @param dest the resulting PDF
     * @throws IOException 
     * @throws DocumentException
     */
    public void manipulatePdf(String src, String dest) throws DocumentException, IOException {
        PdfReader reader = new PdfReader(src);
        PdfObject object;
        PdfDictionary action;
        for (int i = 1; i < reader.getXrefSize(); i++) {
            object = reader.getPdfObject(i);
            if (object instanceof PdfDictionary) {
                action = ((PdfDictionary)object).getAsDict(PdfName.A);
                if (action == null) continue;
                if (PdfName.LAUNCH.equals(action.getAsName(PdfName.S))) {
                    action.remove(PdfName.F);
                    action.remove(PdfName.WIN);
                    action.put(PdfName.S, PdfName.JAVASCRIPT);
                    action.put(PdfName.JS,
                        new PdfString("app.alert('Launch Application Action removed by iText');\r"));
                }
            }
        }
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        stamper.close();

    }
    /**
     * Main method creating the PDF.
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException 
     */
    public static void main(String[] args)
        throws IOException, DocumentException {
        new LaunchAction().createPdf(LaunchAction.RESULT);
        new RemoveLaunchActions().manipulatePdf(LaunchAction.RESULT, RESULT);
    }
}
