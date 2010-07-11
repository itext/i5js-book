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
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

public class Monospace {

    /** The resulting PDF file. */
    public static final String RESULT
        = "results/part3/chapter11/monospace.pdf";
    /** A movie title. */
    public static final String MOVIE
        = "Aanrijding in Moscou";
    
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
        Document document = new Document();
        // step 2
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        // step 3: we open the document
        document.open();
        // step 4
        BaseFont bf1 = BaseFont.createFont("c:/windows/fonts/arial.ttf",
            BaseFont.CP1252, BaseFont.EMBEDDED);
        Font font1 = new Font(bf1, 12);
        document.add(new Paragraph("Movie title: Moscou, Belgium", font1));
        document.add(new Paragraph("directed by Christophe Van Rompaey", font1));
        document.add(new Paragraph(MOVIE, font1));
        BaseFont bf2 = BaseFont.createFont("c:/windows/fonts/cour.ttf",
            BaseFont.CP1252, BaseFont.EMBEDDED);
        Font font2 = new Font(bf2, 12);
        document.add(new Paragraph(MOVIE, font2));
        BaseFont bf3 = BaseFont.createFont("c:/windows/fonts/arialbd.ttf",
            BaseFont.CP1252, BaseFont.EMBEDDED);
        Font font3 = new Font(bf3, 12);
        int widths[] = bf3.getWidths();
        for (int k = 0; k < widths.length; ++k) {
            if (widths[k] != 0)
                widths[k] = 600;
        }
        bf3.setForceWidthsOutput(true);
        document.add(new Paragraph(MOVIE, font3));
        // step 5: we close the document
        document.close();
    }

    /**
     * Main method.
     *
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException
     */
    public static void main(String[] args) throws IOException, DocumentException {
        new Monospace().createPdf(RESULT);
    }
}
