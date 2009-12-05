/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part2.chapter06;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;

import part1.chapter02.MovieHistory;
import part1.chapter02.MovieLinks1;

public class Concatenate {

    public static String RESULT = "results/part2/chapter06/concatenated.pdf";
    
    public static void main(String[] args) throws IOException, DocumentException, SQLException {
        MovieLinks1.main(args);
        MovieHistory.main(args);
        String[] files = { MovieLinks1.RESULT, MovieHistory.RESULT };
        
        Document document = new Document();
        PdfCopy copy = new PdfCopy(document, new FileOutputStream(RESULT));
        document.open();
        PdfReader reader;
        int n;
        for (int i = 0; i < files.length; i++) {
            reader = new PdfReader(files[i]);
            n = reader.getNumberOfPages();
            for (int page = 0; page < n; ) {
                copy.addPage(copy.getImportedPage(reader, ++page));
            }
        } 
        document.close();
    }
}
