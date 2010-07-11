/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part4.chapter13;

import java.io.FileOutputStream;
import java.io.IOException;

import part1.chapter03.MovieTemplates;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class RotatePages {

    /** The resulting PDF. */
    public static final String RESULT
        = "results/part4/chapter13/timetable_rotated.pdf";
    
    /**
     * Manipulates a PDF file src with the file dest as result
     * @param src the original PDF
     * @param dest the resulting PDF
     * @throws IOException
     * @throws DocumentException
     */
    public void manipulatePdf(String src, String dest)
        throws IOException, DocumentException {
        PdfReader reader = new PdfReader(MovieTemplates.RESULT);
        int n = reader.getNumberOfPages();
        int rot;
        PdfDictionary pageDict;
        for (int i = 1; i <= n; i++) {
            rot = reader.getPageRotation(i);
            pageDict = reader.getPageN(i);
            pageDict.put(PdfName.ROTATE, new PdfNumber(rot + 90));
        }
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(RESULT));
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
        new MovieTemplates().createPdf(MovieTemplates.RESULT);
        new RotatePages().manipulatePdf(MovieTemplates.RESULT, RESULT);
    }
}
