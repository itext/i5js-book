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

public class Burst {

    public static final String RESULT = "results/part2/chapter06/timetable_p%d.pdf";
    
    public static void main(String[] args) throws IOException, DocumentException {
        new MovieTemplates().createPdf(MovieTemplates.RESULT);
        PdfReader reader = new PdfReader(MovieTemplates.RESULT);
        Document document;
        PdfCopy copy;
        int n = reader.getNumberOfPages();
        for (int i = 0; i < n; ) {
            document = new Document();
            copy = new PdfCopy(document,
                    new FileOutputStream(String.format(RESULT, ++i)));
            document.open();
            copy.addPage(copy.getImportedPage(reader, i));
            document.close();
        }
    }
    
    
}
