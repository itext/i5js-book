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

import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;
import com.itextpdf.text.Annotation;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

public class MoviePosters1 {
    /** The resulting PDF file. */
    public static final String RESULT
        = "results/part2/chapter07/movie_posters_1.pdf";
    /** Path to the resources. */
    public static final String RESOURCE
        = "resources/posters/%s.jpg";
    /** Path to IMDB. */
    public static final String IMDB
        = "http://imdb.com/title/tt%s/";
    
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
        Document document = new Document(PageSize.A4);
        // step 2
        PdfWriter writer
            = PdfWriter.getInstance(document, new FileOutputStream(filename));
        // step 3
        document.open();
        // step 4
        PdfContentByte canvas = writer.getDirectContent();
        // Create a reusable XObject
        PdfTemplate celluloid = canvas.createTemplate(595, 84.2f);
        celluloid.rectangle(8, 8, 579, 68);
        for (float f = 8.25f; f < 581; f+= 6.5f) {
            celluloid.roundRectangle(f, 8.5f, 6, 3, 1.5f);
            celluloid.roundRectangle(f, 72.5f, 6, 3, 1.5f);
        }
        celluloid.setGrayFill(0.1f);
        celluloid.eoFill();
        writer.releaseTemplate(celluloid);
        // Add the XObject ten times
        for (int i = 0; i < 10; i++) {
            canvas.addTemplate(celluloid, 0, i * 84.2f);
        }
        // Add the movie posters
        Image img;
        Annotation annotation;
        float x = 11.5f;
        float y = 769.7f;
        for (Movie movie : PojoFactory.getMovies(connection)) {
            img = Image.getInstance(String.format(RESOURCE, movie.getImdb()));
            img.scaleToFit(1000, 60);
            img.setAbsolutePosition(x + (45 - img.getScaledWidth()) / 2, y);
            annotation = new Annotation(0, 0, 0, 0,
                String.format(IMDB, movie.getImdb()));
            img.setAnnotation(annotation);
            canvas.addImage(img);
            x += 48;
            if (x > 578) {
                x = 11.5f;
                y -= 84.2f;
            }
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
        throws IOException, SQLException, DocumentException {
        new MoviePosters1().createPdf(RESULT);
    }
}
