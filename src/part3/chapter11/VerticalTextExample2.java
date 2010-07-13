/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part3.chapter11;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.VerticalText;

public class VerticalTextExample2 extends VerticalTextExample1 {

    /** The resulting PDF file. */
    public static final String RESULT
        = "results/part3/chapter11/vertical_text_2.pdf";
    
    /**
     * Creates a PDF document.
     * @param filename the path to the new PDF document
     * @throws DocumentException 
     * @throws IOException 
     * @throws    DocumentException 
     * @throws    IOException
     */
    public void createPdf(String filename) throws IOException, DocumentException {
        // step 1
        Document document = new Document(new Rectangle(420, 600));
        // step 2
        PdfWriter writer
            = PdfWriter.getInstance(document, new FileOutputStream(filename));
        // step 3: we open the document
        document.open();
        // step 4
        BaseFont bf = BaseFont.createFont(
            "KozMinPro-Regular", "Identity-V", BaseFont.NOT_EMBEDDED);
        Font font = new Font(bf, 20);
        VerticalText vt = new VerticalText(writer.getDirectContent());
        vt.setVerticalLayout(390, 570, 540, 12, 30);
        font = new Font(bf, 20);
        vt.addText(new Phrase(convertCIDs(TEXT1), font));
        vt.go();
        vt.setAlignment(Element.ALIGN_RIGHT);
        vt.addText(new Phrase(convertCIDs(TEXT2), font));
        vt.go();
        // step 5: we close the document
        document.close();
    }

    /**
     * Converts the CIDs of the horizontal characters of a String
     * into a String with vertical characters.
     * @param text The String with the horizontal characters
     * @return A String with vertical characters
     */
    public String convertCIDs(String text) {
        char cid[] = text.toCharArray();
        for (int k = 0; k < cid.length; ++k) {
            char c = cid[k];
            if (c == '\n')
                cid[k] = '\uff00';
            else
                cid[k] = (char) (c - ' ' + 8720);
        }
        return new String(cid);
    }

    /**
     * Main method.
     *
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException
     */
    public static void main(String[] args) throws IOException, DocumentException {
        new VerticalTextExample2().createPdf(RESULT);
    }
}
