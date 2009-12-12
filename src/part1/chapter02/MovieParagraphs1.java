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
import com.lowagie.filmfestival.PojoToElementFactory;
import com.lowagie.filmfestival.PojoFactory;
import com.lowagie.filmfestival.Movie;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Writes a list of movies titles to a PDF file.
 */
public class MovieParagraphs1 {

    /** The resulting PDF file. */
    public static final String RESULT
        = "results/part1/chapter02/movie_paragraphs_1.pdf";
    
    /**
     * Creates a Paragraph containing information about a movie.
     * @param    movie    the movie for which you want to create a Paragraph
     */
    public Paragraph createMovieInformation(Movie movie) {
        Paragraph p = new Paragraph();
        p.setFont(FilmFonts.NORMAL);
        p.add(new Phrase("Title: ", FilmFonts.BOLDITALIC));
        p.add(PojoToElementFactory.getMovieTitlePhrase(movie));
        p.add(" ");
        if (movie.getOriginalTitle() != null) {
            p.add(new Phrase("Original title: ", FilmFonts.BOLDITALIC));
            p.add(PojoToElementFactory.getOriginalTitlePhrase(movie));
            p.add(" ");
        }
        p.add(new Phrase("Country: ", FilmFonts.BOLDITALIC));
        for (Country country : movie.getCountries()) {
            p.add(PojoToElementFactory.getCountryPhrase(country));
            p.add(" ");
        }
        p.add(new Phrase("Director: ", FilmFonts.BOLDITALIC));
        for (Director director : movie.getDirectors()) {
            p.add(PojoToElementFactory.getDirectorPhrase(director));
            p.add(" ");
        }
        p.add(createYearAndDuration(movie));
        return p;
    }
    
    /**
     * Creates a Paragraph containing information about the year
     * and the duration of a movie.
     * @param    movie    the movie for which you want to create a Paragraph
     */
    public Paragraph createYearAndDuration(Movie movie) {
        Paragraph info = new Paragraph();
        info.setFont(FilmFonts.NORMAL);
        info.add(new Chunk("Year: ", FilmFonts.BOLDITALIC));
        info.add(new Chunk(String.valueOf(movie.getYear()), FilmFonts.NORMAL));
        info.add(new Chunk(" Duration: ", FilmFonts.BOLDITALIC));
        info.add(new Chunk(String.valueOf(movie.getDuration()), FilmFonts.NORMAL));
        info.add(new Chunk(" minutes", FilmFonts.NORMAL));
        return info;
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
            Paragraph p = createMovieInformation(movie);
            p.setAlignment(Element.ALIGN_JUSTIFIED);
            p.setIndentationLeft(18);
            p.setFirstLineIndent(-18);
            document.add(p);
        }
        // step 5
        document.close();
        // Close the database connection
        connection.close();
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
        new MovieParagraphs1().createPdf(RESULT);
    }
}
