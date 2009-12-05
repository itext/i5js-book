/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part2.chapter07;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfWriter;

public class LaunchAction {
    /** The resulting PDF file. */
    public static final String RESULT = "results/part2/chapter07/launch_action.pdf";

    public static void main(String[] args) throws IOException, DocumentException {

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(RESULT));
        document.open();
        Paragraph p = new Paragraph(new Chunk(
            "Click to open test.txt in Notepad.")
            .setAction(new PdfAction("c:/windows/notepad.exe",
            "test.txt", "open", "C:\\itext-core\\book\\resources\\txt")));
        document.add(p);
        document.close();
    }
}