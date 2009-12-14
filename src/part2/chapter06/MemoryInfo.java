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

import part1.chapter03.MovieTemplates;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;

public class MemoryInfo {

	/** The resulting PDF file. */
    public static final String RESULT
        = "results/part2/chapter06/memory_info.txt";
    
    /**
     * Main method.
     * @param args no arguments needed
     * @throws DocumentException 
     * @throws IOException
     */
    public static void main(String[] args)
        throws IOException, DocumentException {
    	// Use a previous example to create a PDF
        MovieTemplates.main(args);
        // Create a writer for a report file
        PrintWriter writer
            = new PrintWriter(new FileOutputStream(RESULT));
        // Do a full read
        fullRead(writer, MovieTemplates.RESULT);
        // Do a partial read
        partialRead(writer, MovieTemplates.RESULT);
        // Close the text file writer
        writer.close();
    }
    
    /**
     * Do a full read of a PDF file
     * @param writer a writer to a report file
     * @param filename the file to read
     * @throws IOException
     */
    public static void fullRead(PrintWriter writer, String filename)
        throws IOException {
        long before = getMemoryUse();
        PdfReader reader = new PdfReader(filename);
        reader.getNumberOfPages();
        writer.println(String.format("Memory used by full read: %d",
                    getMemoryUse() - before));
        writer.flush();
    }
    
    /**
     * Do a partial read of a PDF file
     * @param writer a writer to a report file
     * @param filename the file to read
     * @throws IOException
     */
    public static void partialRead(PrintWriter writer, String filename)
        throws IOException {
        long before = getMemoryUse();
        PdfReader reader = new PdfReader(
                new RandomAccessFileOrArray(filename), null);
        reader.getNumberOfPages();
        writer.println(String.format("Memory used by partial read: %d",
                    getMemoryUse() - before));
        writer.flush();
    }

    /**
     * Returns the current memory use.
     * 
     * @return the current memory use
     */
    public static long getMemoryUse() {
        garbageCollect();
        garbageCollect();
        garbageCollect();
        garbageCollect();
        long totalMemory = Runtime.getRuntime().totalMemory();
        garbageCollect();
        garbageCollect();
        long freeMemory = Runtime.getRuntime().freeMemory();
        return (totalMemory - freeMemory);
    }

    /**
     * Makes sure all garbage is cleared from the memory.
     */
    public static void garbageCollect() {
        try {
            System.gc();
            Thread.sleep(100);
            System.runFinalization();
            Thread.sleep(100);
            System.gc();
            Thread.sleep(100);
            System.runFinalization();
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}