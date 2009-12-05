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
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

public class MoviePosters {
    /** The resulting PDF file. */
    public static final String RESULT = "results/part1/chapter03/movie_posters.pdf";
    /** Path to the resources. */
    public static final String RESOURCE = "resources/posters/%s.jpg";
    
    /**
     * Creates a PDF with information about the movies
     * @param    filename the name of the PDF file that will be created.
     * @throws    DocumentException 
     * @throws    IOException 
     * @throws    SQLException
     */
    public void createPdf(String filename)
        throws IOException, DocumentException, SQLException {
        DatabaseConnection connection = new HsqldbConnection("filmfestival");    
        Document document = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
        writer.setCompressionLevel(0);
        document.open();
        
        PdfContentByte canvas = writer.getDirectContent();
        
        PdfTemplate celluloid = canvas.createTemplate(595, 84.2f);
        celluloid.rectangle(8, 8, 579, 68);
        for (float f = 8.25f; f < 581; f+= 6.5f) {
            celluloid.roundRectangle(f, 8.5f, 6, 3, 1.5f);
            celluloid.roundRectangle(f, 72.5f, 6, 3, 1.5f);
        }
        celluloid.setGrayFill(0.1f);
        celluloid.eoFill();
        writer.releaseTemplate(celluloid);
        
        for (int i = 0; i < 10; i++) {
            canvas.addTemplate(celluloid, 0, i * 84.2f);
        }
        
        document.newPage();
        
        for (int i = 0; i < 10; i++) {
            canvas.addTemplate(celluloid, 0, i * 84.2f);
        }
        
        List<Movie> movies = PojoFactory.getMovies(connection);
        Image img;
        float x = 11.5f;
        float y = 769.7f;
        for (Movie movie : movies) {
            img = Image.getInstance(String.format(RESOURCE, movie.getImdb()));
            img.scaleToFit(1000, 60);
            img.setAbsolutePosition(x + (45 - img.getScaledWidth()) / 2, y);
            canvas.addImage(img);
            x += 48;
            if (x > 578) {
                x = 11.5f;
                y -= 84.2f;
            }
        }
        
        document.newPage();
        
        canvas.addTemplate(celluloid, 0.8f, 0, 0.35f, 0.65f, 0, 600);
        
        Image tmpImage = Image.getInstance(celluloid);
        tmpImage.setAbsolutePosition(0, 480);
        document.add(tmpImage);
        
        tmpImage.setRotationDegrees(30);
        tmpImage.scalePercent(80);
        tmpImage.setAbsolutePosition(30, 500);
        document.add(tmpImage);
        
        tmpImage.setRotation((float)Math.PI / 2);
        tmpImage.setAbsolutePosition(200, 300);
        document.add(tmpImage);
        
        document.close();
        connection.close();
    }
    
    public static void main(String[] args) throws IOException, SQLException, DocumentException {
        new MoviePosters().createPdf(RESULT);
    }
}
