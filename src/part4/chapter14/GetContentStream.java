/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part4.chapter14;

import java.io.FileOutputStream;
import java.io.IOException;

import part1.chapter01.HelloWorld;
import part1.chapter05.Hero1;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;

public class GetContentStream {
    /** The content stream of a first PDF. */
    public static final String RESULT1
        = "results/part4/chapter14/contentstream1.txt";
    /** The content stream of a second PDF. */
    public static final String RESULT2
        = "results/part4/chapter14/contentstream2.txt";
    
    /**
     * Reads the content stream of the first page of a PDF into a text file.
     * @param src    the path to a PDF file
     * @param result the path to the resulting text file
     * @throws IOException
     */
    public void readContent(String src, String result) throws IOException {
        PdfReader reader = new PdfReader(src);
        FileOutputStream out = new FileOutputStream(result);
        out.write(reader.getPageContent(1));
        out.flush();
        out.close();
    }
    
    /**
     * Main method.
     *
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException
     */
    public static void main(String[] args) throws IOException, DocumentException {
        new HelloWorld().createPdf(HelloWorld.RESULT);
        new Hero1().createPdf(Hero1.RESULT);
        GetContentStream example = new GetContentStream();
        example.readContent(HelloWorld.RESULT, RESULT1);
        example.readContent(Hero1.RESULT, RESULT2);
    }
}