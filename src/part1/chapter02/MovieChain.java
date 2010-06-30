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
        // step 1
        Document document = new Document(new Rectangle(240, 240), 10, 10, 10, 10);
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
        // step 3
        document.open();
        // step 4
        // create a long Stringbuffer with movie titles
        StringBuffer buf1 = new StringBuffer();
        for (Movie movie : kubrick) {
            // replace spaces with non-breaking spaces
            buf1.append(movie.getMovieTitle().replace(' ', '\u00a0'));
            // use pipe as separator
            buf1.append('|');
        }
        // Create a first chunk
        Chunk chunk1 = new Chunk(buf1.toString());
        // wrap the chunk in a paragraph and add it to the document
        Paragraph paragraph = new Paragraph("A:\u00a0");
        paragraph.add(chunk1);
        paragraph.setAlignment(Element.ALIGN_JUSTIFIED);
        document.add(paragraph);
        document.add(Chunk.NEWLINE);
        // define the pipe character as split character
        chunk1.setSplitCharacter(new PipeSplitCharacter());
        // wrap the chunk in a second paragraph and add it
        paragraph = new Paragraph("B:\u00a0");
        paragraph.add(chunk1);
        paragraph.setAlignment(Element.ALIGN_JUSTIFIED);
        document.add(paragraph);
        document.add(Chunk.NEWLINE);

        // create a new StringBuffer with movie titles
        StringBuffer buf2 = new StringBuffer();
        for (Movie movie : kubrick) {
            buf2.append(movie.getMovieTitle());
            buf2.append('|');
        }
        // Create a second chunk 
        Chunk chunk2 = new Chunk(buf2.toString());
        // wrap the chunk in a paragraph and add it to the document
        paragraph = new Paragraph("C:\u00a0");
        paragraph.add(chunk2);
        paragraph.setAlignment(Element.ALIGN_JUSTIFIED);
        document.add(paragraph);
        document.newPage();
        // define hyphenation for the chunk
        chunk2.setHyphenation(new HyphenationAuto("en", "US", 2, 2));
        // wrap the second chunk in a second paragraph and add it
        paragraph = new Paragraph("D:\u00a0");
        paragraph.add(chunk2);
        paragraph.setAlignment(Element.ALIGN_JUSTIFIED);
        document.add(paragraph);
        
        // go to a new page
        document.newPage();
        // define a new space/char ratio
        writer.setSpaceCharRatio(PdfWriter.NO_SPACE_CHAR_RATIO);
        // wrap the second chunk in a third paragraph and add it
        paragraph = new Paragraph("E:\u00a0");
        paragraph.add(chunk2);
        paragraph.setAlignment(Element.ALIGN_JUSTIFIED);
        document.add(paragraph);
        // step 5
        document.close();
    }
    
    /**
     * Main method.
     *
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException 
     * @throws SQLException
     */
    public static void main(String[] args)
        throws IOException, DocumentException, SQLException {
        new MovieChain().createPdf(RESULT);
    }
}
