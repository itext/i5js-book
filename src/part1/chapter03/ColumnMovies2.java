/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part1.chapter03;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import part1.chapter02.StarSeparator;

import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.FilmFonts;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;
import com.lowagie.filmfestival.PojoToElementFactory;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfWriter;

public class ColumnMovies2 {

    /** The resulting PDF file. */
    public static final String RESULT = "results/part1/chapter03/column_movies2.pdf";
    
    /** Definition of two columns */
    public static final float[][] COLUMNS = {
        { 40, 36, 219, 579 } , { 234, 36, 414, 579 },
        { 428, 36, 608, 579 } , { 622, 36, 802, 579 }
    };
    
    /**
     * Creates a PDF with information about the movies
     * @param    filename the name of the PDF file that will be created.
     * @throws    DocumentException 
     * @throws    IOException 
     * @throws    SQLException
     */
    public void createPdf(String filename)
        throws IOException, DocumentException, SQLException {
    	// Create a database connection
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        // step 1
        Document document = new Document(PageSize.A4.rotate());
        // step 2
        PdfWriter writer
            = PdfWriter.getInstance(document, new FileOutputStream(filename));
        // step 3
        document.open();
        // step 4
        List<Movie> movies = PojoFactory.getMovies(connection);
        ColumnText ct = new ColumnText(writer.getDirectContent());
        int column = 0;
        ct.setSimpleColumn(
            COLUMNS[column][0], COLUMNS[column][1],
            COLUMNS[column][2], COLUMNS[column][3]);
        int status = ColumnText.START_COLUMN;
        float y;
        for (Movie movie : movies) {
            y = ct.getYLine();
            addContent(ct, movie);
            status = ct.go(true);
            if (ColumnText.hasMoreText(status)) {
                column = (column + 1) % 4;
                if (column == 0)
                    document.newPage();
                ct.setSimpleColumn(
                    COLUMNS[column][0], COLUMNS[column][1],
                    COLUMNS[column][2], COLUMNS[column][3]);
                y = COLUMNS[column][3];
            }
            ct.setYLine(y);
            ct.setText(null);
            addContent(ct, movie);
            status = ct.go();
        }
        // step 5
        document.close();
        // Close the database connection
        connection.close();
    }
    
    /**
     * Adds info about a movie in a ColumnText object
     * @param ct A ColumnText object
     * @param movie A Movie POJO
     */
    public void addContent(ColumnText ct, Movie movie) {
        Paragraph p;
        p = new Paragraph(new Paragraph(movie.getTitle(), FilmFonts.BOLD));
        p.setAlignment(Element.ALIGN_CENTER);
        p.setSpacingBefore(16);
        ct.addElement(p);
        if (movie.getOriginalTitle() != null) {
            p = new Paragraph(movie.getOriginalTitle(), FilmFonts.ITALIC);
            p.setAlignment(Element.ALIGN_RIGHT);
            ct.addElement(p);
        }
        p = new Paragraph();
        p.add(PojoToElementFactory.getYearPhrase(movie));
        p.add(" ");
        p.add(PojoToElementFactory.getDurationPhrase(movie));
        p.setAlignment(Element.ALIGN_JUSTIFIED_ALL);
        ct.addElement(p);
        p = new Paragraph(new Chunk(new StarSeparator()));
        p.setSpacingAfter(12);
        ct.addElement(p);
    }

    /**
     * Main method.
     *
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException 
     * @throws SQLException
     */
    public static void main(String[] args) throws IOException, DocumentException, SQLException {
        new ColumnMovies2().createPdf(RESULT);
    }
}
