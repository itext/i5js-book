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

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.SimpleNamedDestination;

import part1.chapter02.MovieLinks1;

public class ConcatenateNamedDestinations {

    public static String RESULT1 = "results/part2/chapter07/concatenated_links_1.pdf";
    public static String RESULT2 = "results/part2/chapter07/concatenated_links_2.pdf";
    
    public static void main(String[] args) throws IOException, DocumentException, SQLException {
        new MovieLinks1().createPdf(LinkActions.RESULT1);;
        new LinkActions().createPdf(LinkActions.RESULT2);;
        PdfReader[] readers = {
                new PdfReader(LinkActions.RESULT2),
                new PdfReader(LinkActions.RESULT1) };
        
        Document document = new Document();
        PdfCopy copy = new PdfCopy(document, new FileOutputStream(RESULT1));
        document.open();
        int n;
        for (int i = 0; i < readers.length; i++) {
            readers[i].consolidateNamedDestinations();
            n = readers[i].getNumberOfPages();
            for (int page = 0; page < n; ) {
                copy.addPage(copy.getImportedPage(readers[i], ++page));
            }
        }
        copy.addNamedDestinations(
                SimpleNamedDestination.getNamedDestination(readers[1], false),
                readers[0].getNumberOfPages());
        document.close();
        
        PdfReader reader = new PdfReader(RESULT1);
        reader.makeRemoteNamedDestinationsLocal();
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(RESULT2));
        stamper.close();
    }
}
