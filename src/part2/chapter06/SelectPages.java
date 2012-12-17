/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part2.chapter06;

import java.io.FileOutputStream;
import java.io.IOException;

import part1.chapter03.MovieTemplates;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class SelectPages {

	/** A resulting PDF file. */
    public static final String RESULT1 = "results/part2/chapter06/timetable_stamper.pdf";
	/** A resulting PDF file. */
    public static final String RESULT2 = "results/part2/chapter06/timetable_copy.pdf"; 
    
    /**
     * Main method.
     * @param args no arguments needed
     * @throws DocumentException 
     * @throws IOException
     */
    public static void main(String[] args) throws IOException, DocumentException {
        new MovieTemplates().createPdf(MovieTemplates.RESULT);
        PdfReader reader = new PdfReader(MovieTemplates.RESULT);
        reader.selectPages("4-8");
        manipulateWithStamper(reader);
        manipulateWithCopy(reader);
    }

    /**
     * Creates a new PDF based on the one in the reader
     * @param reader a reader with a PDF file
     * @throws IOException
     * @throws DocumentException
     */
    private static void manipulateWithStamper(PdfReader reader)
        throws IOException, DocumentException {
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(RESULT1));
        stamper.close();
    }

    /**
     * Creates a new PDF based on the one in the reader
     * @param reader a reader with a PDF file
     * @throws IOException
     * @throws DocumentException
     */
    private static void manipulateWithCopy(PdfReader reader)
        throws IOException, DocumentException {
        int n = reader.getNumberOfPages();
        Document document = new Document();
        PdfCopy copy = new PdfCopy(document, new FileOutputStream(RESULT2));
        document.open();
        for (int i = 0; i < n;) {
            copy.addPage(copy.getImportedPage(reader, ++i));
        }
        document.close();
    }
    
    
}
