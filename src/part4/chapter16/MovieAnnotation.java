/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part4.chapter16;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfFileSpecification;
import com.itextpdf.text.pdf.PdfWriter;

public class MovieAnnotation {

    /** The resulting PDF file. */
    public static final String RESULT
        = "results/part4/chapter16/movie.pdf";
    /** One of the resources. */
    public static final String RESOURCE
        = "resources/img/foxdog.mpg";
    
    /**
     * Creates a PDF document.
     * @param filename the path to the new PDF document
     * @throws    DocumentException 
     * @throws    IOException
     */
    public void createPdf(String filename) throws IOException, DocumentException {
        // step 1
        Document document = new Document();
        // step 2
        PdfWriter writer
          = PdfWriter.getInstance(document, new FileOutputStream(filename));
        // step 3
        document.open();
        // step 4
        PdfFileSpecification fs = PdfFileSpecification.fileEmbedded(writer,
                RESOURCE, "foxdog.mpg", null);
        writer.addAnnotation(PdfAnnotation.createScreen(writer,
                new Rectangle(200f, 700f, 400f, 800f), "Fox and Dog", fs,
                "video/mpeg", true));
        // step 5
        document.close();
    }
    
    /**
     * Main method.
     *
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException 
     * @throws DocumentException 
     * @throws IOException
     */
    public static void main(String[] args) throws IOException, DocumentException {
        new MovieAnnotation().createPdf(RESULT);
    }
}
