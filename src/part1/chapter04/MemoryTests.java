/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part1.chapter04;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.sql.SQLException;

import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.FilmFonts;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;
import com.lowagie.filmfestival.PojoToElementFactory;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.List;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class MemoryTests {

    public static final String RESULT0 = "results/part1/chapter04/test_results.txt";
    public static final String RESULT1 = "results/part1/chapter04/pdfptable_without_memory_management.pdf";
    public static final String RESULT2 = "results/part1/chapter04/pdfptable_with_memory_management.pdf";
    
    private boolean test;
    private long memory_use;
    private long initial_memory_use = 0l;
    private long maximum_memory_use = 0l;
    
    /** Path to the resources. */
    public static final String RESOURCE = "resources/posters/%s.jpg";
    /** The different epochs. */
    public static final String[] EPOCH =
        { "Forties", "Fifties", "Sixties", "Seventies", "Eighties", "Nineties", "Twenty-first Century" };
    
    /** The fonts for the title. */
    public static final Font[] FONT = new Font[4];
    static {
        FONT[0] = new Font(Font.HELVETICA, 24);
        FONT[1] = new Font(Font.HELVETICA, 18);
        FONT[2] = new Font(Font.HELVETICA, 14);
        FONT[3] = new Font(Font.HELVETICA, 12, Font.BOLD);
    }
    
    public static void main(String[] args) {
        MemoryTests tests = new MemoryTests();
        tests.createPdfs();
    }
    
    public void createPdfs() {
        try {
            PrintWriter writer = new PrintWriter(new FileOutputStream(RESULT0));
            resetMaximum(writer);
            test = false;
            println(writer, RESULT1);
            createPdfWithPdfPTable(writer, RESULT1);
            resetMaximum(writer);
            test = true;
            println(writer, RESULT2);
            createPdfWithPdfPTable(writer, RESULT2);
            resetMaximum(writer);
            writer.flush();
            writer.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void createPdfWithPdfPTable(Writer writer, String filename) throws IOException, DocumentException, SQLException {
        DatabaseConnection connection = new HsqldbConnection("filmfestival");    
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        document.open();

        PdfPTable table = new PdfPTable(new float[]{1, 7});
        if (test) table.setComplete(false);
        table.setWidthPercentage(100);
        java.util.List<Movie> movies = PojoFactory.getMovies(connection);
        List list;
        PdfPCell cell;
        int count = 0;
        for (Movie movie : movies) {
            table.setSpacingBefore(5);
            cell = new PdfPCell(Image.getInstance(String.format(RESOURCE, movie.getImdb())), true);
            cell.setBorder(PdfPCell.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell();
            Paragraph p = new Paragraph(movie.getTitle(), FilmFonts.BOLD);
            p.setAlignment(Element.ALIGN_CENTER);
            p.setSpacingBefore(5);
            p.setSpacingAfter(5);
            cell.addElement(p);
            cell.setBorder(PdfPCell.NO_BORDER);
            if (movie.getOriginalTitle() != null) {
                p = new Paragraph(movie.getOriginalTitle(), FilmFonts.ITALIC);
                p.setAlignment(Element.ALIGN_RIGHT);
                cell.addElement(p);
            }
            list = PojoToElementFactory.getDirectorList(movie);
            list.setIndentationLeft(30);
            cell.addElement(list);
            p = new Paragraph(String.format("Year: %d", movie.getYear()), FilmFonts.NORMAL);
            p.setIndentationLeft(15);
            p.setLeading(24);
            cell.addElement(p);
            p = new Paragraph(String.format("Run length: %d", movie.getDuration()), FilmFonts.NORMAL);
            p.setLeading(14);
            p.setIndentationLeft(30);
            cell.addElement(p);
            list = PojoToElementFactory.getCountryList(movie);
            list.setIndentationLeft(40);
            cell.addElement(list);
            table.addCell(cell);
            if (count++ % 10 == 0) {
                if (test)
                    document.add(table);
                checkpoint(writer);
            }
        }
        if (test) table.setComplete(true);
        document.add(table);
        
        checkpoint(writer);
        document.close();
    }
    
    private void checkpoint(Writer writer) {
        memory_use = getMemoryUse();
        maximum_memory_use = Math.max(maximum_memory_use, memory_use);
        println(writer, "memory use: ", memory_use);
    }
    
    private void resetMaximum(Writer writer) {
        println(writer, "maximum: ", maximum_memory_use);
        println(writer, "total used: ", maximum_memory_use - initial_memory_use);
        maximum_memory_use = 0l;
        initial_memory_use = getMemoryUse();
        println(writer, "initial memory use: ", initial_memory_use);
    }
    
    private void println(Writer writer, String message) {
        try {
            writer.write(message);
            writer.write("\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void println(Writer writer, String message, long l) {
        try {
            writer.write(message + l);
            writer.write("\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Returns the current memory use.
     * 
     * @return the current memory use
     */
    private static long getMemoryUse() {
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
    private static void garbageCollect() {
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
