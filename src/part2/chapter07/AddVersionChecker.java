/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part2.chapter07;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import part1.chapter01.HelloWorld;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Utilities;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class AddVersionChecker {

    public static final String RESULT = "results/part2/chapter07/version_checker.pdf";
    /** Path to the resources. */
    public static final String RESOURCE = "resources/js/viewer_version.js";

    /**
     * Main method.
     *
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException 
     * @throws SQLException
     */
    public static void main(String[] args) throws IOException, DocumentException, SQLException {
        HelloWorld.main(args);
        PdfReader reader = new PdfReader(HelloWorld.RESULT);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(RESULT));
        stamper.addJavaScript(Utilities.readFileToString(RESOURCE));
        stamper.close();
    }
}
