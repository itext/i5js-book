/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part1.chapter01;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Creates a PDF with the biggest possible page size.
 */
public class HelloWorldMaximum {

    /** Path to the resulting PDF file. */
    public static final String RESULT = "results/part1/chapter01/hello_maximum.pdf";
    
    /**
     * Creates a PDF file: hello_maximum.pdf
     * @param    args    no arguments needed
     */
    public static void main(String[] args) throws DocumentException, IOException {
        // step 1
        Document document = new Document(new Rectangle(14400, 14400));
        // step 2
        PdfWriter.getInstance(document, new FileOutputStream(RESULT)).setUserunit(75000f);
        // step 3
        document.open();
        // step 4
        document.add(new Paragraph("Hello World"));
        // step 5
        document.close();
    }
}