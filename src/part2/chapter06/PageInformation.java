/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part2.chapter06;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;

import part1.chapter01.HelloWorldLandscape1;
import part1.chapter01.HelloWorldLandscape2;
import part1.chapter03.MovieTemplates;
import part1.chapter05.Hero1;

public class PageInformation {

    public static final String RESULT = "results/part2/chapter06/page_info.txt";
    public static void main(String[] args) throws DocumentException, IOException {
        HelloWorldLandscape1.main(args);
        HelloWorldLandscape2.main(args);
        MovieTemplates.main(args);
        Hero1.main(args);
        
        PrintWriter writer = new PrintWriter(new FileOutputStream(RESULT));
        inspect(writer, HelloWorldLandscape1.RESULT);
        inspect(writer, HelloWorldLandscape2.RESULT);
        inspect(writer, MovieTemplates.RESULT);
        inspect(writer, Hero1.RESULT);
        writer.close();
    }
    
    public static void inspect(PrintWriter writer, String filename) throws IOException {
        PdfReader reader = new PdfReader(filename);
        writer.println(filename);
        writer.print("Number of pages: ");
        writer.println(reader.getNumberOfPages());
        Rectangle mediabox = reader.getPageSize(1);
        writer.print("Size of page 1: [");
        writer.print(mediabox.getLeft());
        writer.print(',');
        writer.print(mediabox.getBottom());
        writer.print(',');
        writer.print(mediabox.getRight());
        writer.print(',');
        writer.print(mediabox.getTop());
        writer.println("]");
        writer.print("Rotation of page 1: ");
        writer.println(reader.getPageRotation(1));
        writer.print("Page size with rotation of page 1: ");
        writer.println(reader.getPageSizeWithRotation(1));
        writer.print("Is rebuilt? ");
        writer.println(reader.isRebuilt());
        writer.print("Is encrypted? ");
        writer.println(reader.isEncrypted());
        writer.println();
        writer.flush();
    }
}
