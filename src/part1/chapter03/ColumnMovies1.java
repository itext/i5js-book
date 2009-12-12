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

import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.FilmFonts;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;
import com.lowagie.filmfestival.PojoToElementFactory;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfWriter;

public class ColumnMovies1 {

    /** The resulting PDF file. */
    public static final String RESULT
        = "results/part1/chapter03/column_movies1.pdf";
    /** Path to the resources. */
    public static final String RESOURCE
        = "resources/posters/%s.jpg";
    
    /** Definition of two columns */
    public static final float[][] COLUMNS = {
        { 36, 36, 224, 579 } , { 230, 36, 418, 579 },
        { 424, 36, 612, 579 } , { 618, 36, 806, 579 }
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
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
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
        Image img;
        for (Movie movie : movies) {
            y = ct.getYLine();
            img = Image.getInstance(String.format(RESOURCE, movie.getImdb()));
            img.scaleToFit(80, 1000);
            addContent(ct, movie, img);
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
            addContent(ct, movie, img);
            status = ct.go();
        }
        // step 5
        document.close();
        // Close the database connection
        connection.close();
    }
    
    /**
     * Add content to a ColumnText object.
     * @param ct the ColumnText object
     * @param movie a Movie object
     * @param img the poster of the image
     */
    public void addContent(ColumnText ct, Movie movie, Image img) {
        ct.addElement(img);
        ct.addElement(new Paragraph(movie.getTitle(), FilmFonts.BOLD));
        if (movie.getOriginalTitle() != null) {
            ct.addElement(new Paragraph(movie.getOriginalTitle(), FilmFonts.ITALIC));
        }
        ct.addElement(PojoToElementFactory.getDirectorList(movie));
        ct.addElement(PojoToElementFactory.getYearPhrase(movie));
        ct.addElement(PojoToElementFactory.getDurationPhrase(movie));
        ct.addElement(PojoToElementFactory.getCountryList(movie));
        ct.addElement(Chunk.NEWLINE);
    }

    /**
     * Main method.
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException 
     * @throws SQLException
     */
    public static void main(String[] args) throws IOException, DocumentException, SQLException {
        new ColumnMovies1().createPdf(RESULT);
    }
}
