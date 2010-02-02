/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part1.chapter05;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.MovieComparator;
import com.lowagie.filmfestival.PojoFactory;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.BaseColor;

public class MovieYears {
    
    /** The resulting PDF file. */
    public static final String RESULT = "results/part1/chapter05/movie_years.pdf";

    /**
     * Inner class to add functionality to a Chunk in a generic way.
     */
    class GenericTags extends PdfPageEventHelper {
         
        public void onGenericTag(PdfWriter writer, Document pdfDocument,
                Rectangle rect, String text) {
            if ("strip".equals(text))
                strip(writer.getDirectContent(), rect);
            else if ("ellipse".equals(text))
                ellipse(writer.getDirectContentUnder(), rect);
            else
                countYear(text);
        }

        public void strip(PdfContentByte content, Rectangle rect) {
            content.rectangle(rect.getLeft() - 1, rect.getBottom() - 5f,
                    rect.getWidth(), rect.getHeight() + 8);
            content.rectangle(rect.getLeft(), rect.getBottom() - 2,
                    rect.getWidth() - 2, rect.getHeight() + 2);
            float y1 = rect.getTop() + 0.5f;
            float y2 = rect.getBottom() - 4;
            for (float f = rect.getLeft(); f < rect.getRight() - 4; f += 5) {
                content.rectangle(f, y1, 4f, 1.5f);
                content.rectangle(f, y2, 4f, 1.5f);
            }
            content.eoFill();
        }
        
        public void ellipse(PdfContentByte content, Rectangle rect) {
            content.saveState();
            content.setRGBColorFill(0x00, 0x00, 0xFF);
            content.ellipse(rect.getLeft() - 3f, rect.getBottom() - 5f,
                    rect.getRight() + 3f, rect.getTop() + 3f);
            content.fill();
            content.restoreState();
        }

        TreeMap<String, Integer> years = new TreeMap<String, Integer>();
        
        public void countYear(String text) {
            Integer count = years.get(text);
            if (count == null) {
                years.put(text, 1);
            }
            else {
                years.put(text, count + 1);
            }
        }
    }
    
    /**
     * Inner class to add lines when a paragraph begins and ends.
     */
    class ParagraphPositions extends PdfPageEventHelper {
        public void onParagraph(PdfWriter writer, Document pdfDocument,
                float paragraphPosition) {
            drawLine(writer.getDirectContent(),
                    pdfDocument.left(), pdfDocument.right(), paragraphPosition - 8);
        }

        public void onParagraphEnd(PdfWriter writer, Document pdfDocument,
                float paragraphPosition) {
            drawLine(writer.getDirectContent(),
                    pdfDocument.left(), pdfDocument.right(), paragraphPosition - 5);
        }
        
        public void drawLine(PdfContentByte cb, float x1, float x2, float y) {
            cb.moveTo(x1, y);
            cb.lineTo(x2, y);
            cb.stroke();
        }
    }

    /**
     * Creates a PDF document.
     * @param filename the path to the new PDF document
     * @throws    DocumentException 
     * @throws    IOException
     */
    public void createPdf(String filename) throws IOException, DocumentException, SQLException {
    	// Create a database connection
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        // step 1
        Document document = new Document();
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
        GenericTags event = new GenericTags();
        writer.setPageEvent(event);
        writer.setPageEvent(new ParagraphPositions());
        // step 3
        document.open();
        // step 4
        Font bold = new Font(FontFamily.HELVETICA, 11, Font.BOLD);
        Font italic = new Font(FontFamily.HELVETICA, 11, Font.ITALIC);
        Font white = new Font(FontFamily.HELVETICA, 12, Font.BOLD | Font.ITALIC, BaseColor.WHITE);
        Paragraph p;
        Chunk c;
        
        Set<Movie> movies = 
            new TreeSet<Movie>(new MovieComparator(MovieComparator.BY_YEAR));
        movies.addAll(PojoFactory.getMovies(connection));
        for (Movie movie : movies) {
            p = new Paragraph(22);
            c = new Chunk(String.format("%d ", movie.getYear()), bold);
            c.setGenericTag("strip");
            p.add(c);
            c = new Chunk(movie.getMovieTitle());
            c.setGenericTag(String.valueOf(movie.getYear()));
            p.add(c);
            c = new Chunk(String.format(" (%d minutes)  ", movie.getDuration()), italic);
            p.add(c);
            c = new Chunk("IMDB", white);
            c.setAnchor("http://www.imdb.com/title/tt" + movie.getImdb());
            c.setGenericTag("ellipse");
            p.add(c);
            document.add(p);
        }
        document.newPage();
        writer.setPageEvent(null);
        for (Map.Entry<String,Integer> entry : event.years.entrySet()) {
            p = new Paragraph(String.format("%s: %d movie(s)", entry.getKey(), entry.getValue()));
            document.add(p);
        }
        // step 5
        document.close();
        // Close the database connection
        connection.close();
    }
    
    /**
     * Main method.
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException 
     * @throws SQLException
     */
    public static void main(String[] args)
        throws IOException, DocumentException, SQLException {
        new MovieYears().createPdf(RESULT);
    }
}
