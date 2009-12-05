/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part1.chapter02;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.HyphenationAuto;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Writes a list of directors to a PDF file.
 */
public class MovieChain {
    
    /** The resulting PDF file. */
    public static final String RESULT = "results/part1/chapter02/kubrick.pdf";
    
    /**
     * Creates a PDF file with director names.
     * @param    filename    the name of the PDF file that needs to be created.
     * @throws    DocumentException 
     * @throws    IOException 
     * @throws    SQLException
     */
    public void createPdf(String filename)
        throws IOException, DocumentException, SQLException {

        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        List<Movie> kubrick = PojoFactory.getMovies(connection, 1);
        connection.close();
        
        Document document = new Document(new Rectangle(240, 240), 10, 10, 10, 10);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
        document.open();
        
        StringBuffer buf1 = new StringBuffer();
        for (Movie movie : kubrick) {
            buf1.append(movie.getMovieTitle().replace(' ', '\u00a0'));
            buf1.append('|');
        }

        Chunk chunk1 = new Chunk(buf1.toString());
        Paragraph paragraph = new Paragraph("A:\u00a0");
        paragraph.add(chunk1);
        paragraph.setAlignment(Element.ALIGN_JUSTIFIED);
        document.add(paragraph);
        document.add(Chunk.NEWLINE);
        
        chunk1.setSplitCharacter(new PipeSplitCharacter());
        paragraph = new Paragraph("B:\u00a0");
        paragraph.add(chunk1);
        paragraph.setAlignment(Element.ALIGN_JUSTIFIED);
        document.add(paragraph);
        document.add(Chunk.NEWLINE);

        StringBuffer buf2 = new StringBuffer();
        for (Movie movie : kubrick) {
            buf2.append(movie.getMovieTitle());
            buf2.append('|');
        }
        
        Chunk chunk2 = new Chunk(buf2.toString());
        paragraph = new Paragraph("C:\u00a0");
        paragraph.add(chunk2);
        paragraph.setAlignment(Element.ALIGN_JUSTIFIED);
        document.add(paragraph);
        document.newPage();
        
        chunk2.setHyphenation(new HyphenationAuto("en", "US", 2, 2));
        paragraph = new Paragraph("D:\u00a0");
        paragraph.add(chunk2);
        paragraph.setAlignment(Element.ALIGN_JUSTIFIED);
        document.add(paragraph);
        document.newPage();
        
        writer.setSpaceCharRatio(PdfWriter.NO_SPACE_CHAR_RATIO);
        paragraph = new Paragraph("E:\u00a0");
        paragraph.add(chunk2);
        paragraph.setAlignment(Element.ALIGN_JUSTIFIED);
        document.add(paragraph);
        
        document.close();
    }
    
    /**
     * Main method.
     * Reads information from a database and writes it to a PDF document
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException 
     * @throws SQLException
     */
    public static void main(String[] args) throws IOException, DocumentException, SQLException {
        new MovieChain().createPdf(RESULT);
    }
}
