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
import java.util.List;

import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PushbuttonField;

public class MoviePosters2 {
    /** The resulting PDF file. */
    public static final String RESULT = "results/part2/chapter07/movie_posters_2.pdf";
    public static final String INFO = "Movie produced in %s; run length: %s";
    public static final String JS1 =
        "var t = this.getAnnot(this.pageNum, 'IMDB%1$s'); t.popupOpen = true; var w = this.getField('b%1$s'); w.setFocus();";
    public static final String JS2 =
        "var t = this.getAnnot(this.pageNum, 'IMDB%s'); t.popupOpen = false;";
    
    /**
     * Creates a PDF with information about the movies
     * @param    filename the name of the PDF file that will be created.
     * @throws    DocumentException 
     * @throws    IOException 
     * @throws    SQLException
     */
    public void manipulatePdf(String src, String dest)
        throws IOException, DocumentException, SQLException {
        DatabaseConnection connection = new HsqldbConnection("filmfestival");    
        PdfReader reader = new PdfReader(src);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        List<Movie> movies = PojoFactory.getMovies(connection);
        Image img;
        float x = 11.5f;
        float y = 769.7f;
        float llx, lly, urx, ury;
        for (Movie movie : movies) {
            img = Image.getInstance(String.format(MoviePosters1.RESOURCE, movie.getImdb()));
            img.scaleToFit(1000, 60);
            llx = x + (45 - img.getScaledWidth()) / 2;
            lly = y;
            urx = x + img.getScaledWidth();
            ury = y + img.getScaledHeight();
            addPopup(stamper, new Rectangle(llx, lly, urx, ury),
                    movie.getMovieTitle(), String.format(INFO, movie.getYear(), movie.getDuration()), movie.getImdb());
            x += 48;
            if (x > 578) {
                x = 11.5f;
                y -= 84.2f;
            }
        }
        stamper.close();
        connection.close();
    }
    
    public void addPopup(PdfStamper stamper, Rectangle rect, String title, String contents, String imdb) throws IOException, DocumentException {
        PdfAnnotation text =
            PdfAnnotation.createText(stamper.getWriter(), rect, title, contents, false, "Comment");
        text.setName(String.format("IMDB%s", imdb));
        text.setFlags(PdfAnnotation.FLAGS_READONLY | PdfAnnotation.FLAGS_NOVIEW);
        
        PdfAnnotation popup = PdfAnnotation.createPopup(
                stamper.getWriter(), new Rectangle(rect.getLeft() + 10, rect.getBottom() + 10,
                        rect.getLeft() + 200, rect.getBottom() + 100),
                null, false);

        popup.put(PdfName.PARENT, text.getIndirectReference());
        text.put(PdfName.POPUP, popup.getIndirectReference());

        stamper.addAnnotation(text, 1);
        stamper.addAnnotation(popup, 1);
        
        PushbuttonField field = new PushbuttonField(
                stamper.getWriter(), rect,
                String.format("b%s", imdb));
        PdfAnnotation widget = field.getField();
        PdfAction enter = PdfAction.javaScript(String.format(JS1, imdb), stamper.getWriter());
        widget.setAdditionalActions(PdfName.E, enter);
        PdfAction exit = PdfAction.javaScript(String.format(JS2, imdb), stamper.getWriter());
        widget.setAdditionalActions(PdfName.X, exit);
        stamper.addAnnotation(widget, 1);

    }
    
    public static void main(String[] args) throws IOException, SQLException, DocumentException {
        MoviePosters1.main(args);
        new MoviePosters2().manipulatePdf(MoviePosters1.RESULT, RESULT);
    }
}
