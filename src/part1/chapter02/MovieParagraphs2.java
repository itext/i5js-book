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
import com.lowagie.filmfestival.Country;
import com.lowagie.filmfestival.Director;
import com.lowagie.filmfestival.FilmFonts;
import com.lowagie.filmfestival.PojoFactory;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoToElementFactory;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class MovieParagraphs2 extends MovieParagraphs1 {

    public static final String RESULT = "results/part1/chapter02/movie_paragraphs_2.pdf";
    
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
        new MovieParagraphs2().createPdf(RESULT);
    }
    
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
        Document document = new Document();
        // step 2
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        // step 3
        document.open();
        // step 4
        List<Movie> movies = PojoFactory.getMovies(connection);
        for (Movie movie : movies) {
            // Create a paragraph with the title
            Paragraph title = new Paragraph(
                PojoToElementFactory.getMovieTitlePhrase(movie));
            title.setAlignment(Element.ALIGN_LEFT);
            document.add(title);
            // Add the original title next to it using a dirty hack
            if (movie.getOriginalTitle() != null) {
                Paragraph dummy = new Paragraph("\u00a0", FilmFonts.NORMAL);
                dummy.setLeading(-18);
                document.add(dummy);
                Paragraph originalTitle = new Paragraph(
                    PojoToElementFactory.getOriginalTitlePhrase(movie));
                originalTitle.setAlignment(Element.ALIGN_RIGHT);
                document.add(originalTitle);
            }
            // Info about the director
            Paragraph director;
            float indent = 20;
            // Loop over the directors
            for (Director pojo : movie.getDirectors()) {
                director = new Paragraph(
                    PojoToElementFactory.getDirectorPhrase(pojo));
                director.setIndentationLeft(indent);
                document.add(director);
                indent += 20;
            }
            // Info about the country
            Paragraph country;
            indent = 20;
            // Loop over the countries
            for (Country pojo : movie.getCountries()) {
                country = new Paragraph(
                    PojoToElementFactory.getCountryPhrase(pojo));
                country.setAlignment(Element.ALIGN_RIGHT);
                country.setIndentationRight(indent);
                document.add(country);
                indent += 20;
            }
            // Extra info about the movie
            Paragraph info = createYearAndDuration(movie);
            info.setAlignment(Element.ALIGN_CENTER);
            info.setSpacingAfter(36);
            document.add(info);
        }
        // step 5
        document.close();
        // Close the database connection
        connection.close();
    }
}
