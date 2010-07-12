/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part3.chapter12;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Director;
import com.lowagie.filmfestival.FilmFonts;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;

public class HelloWorldCompression {
    /** The resulting PDF file. */
    public static final String RESULT1
        = "results/part3/chapter12/compression_not_at_all.pdf";
    /** The resulting PDF file. */
    public static final String RESULT2
        = "results/part3/chapter12/compression_zero.pdf";
    /** The resulting PDF file. */
    public static final String RESULT3
        = "results/part3/chapter12/compression_normal.pdf";
    /** The resulting PDF file. */
    public static final String RESULT4
        = "results/part3/chapter12/compression_high.pdf";
    /** The resulting PDF file. */
    public static final String RESULT5
        = "results/part3/chapter12/compression_full.pdf";
    /** The resulting PDF file. */
    public static final String RESULT6
        = "results/part3/chapter12/compression_full_too.pdf";
    /** The resulting PDF file. */
    public static final String RESULT7
        = "results/part3/chapter12/compression_removed.pdf";
    
    /**
     * Creates a PDF with information about the movies
     * @param    filename the name of the PDF file that will be created.
     * @throws    DocumentException 
     * @throws    IOException 
     * @throws    SQLException
     */
    public void createPdf(String filename, int compression)
        throws IOException, DocumentException, SQLException {
        // step 1
        Document document = new Document();
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
        switch(compression) {
        case -1:
            Document.compress = false;
            break;
        case 0:
            writer.setCompressionLevel(0);
            break;
        case 2:
            writer.setCompressionLevel(9);
            break;
        case 3:
            writer.setFullCompression();
            break;
        }
        // step 3
        document.open();
        // step 4
        // Create database connection and statement
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        Statement stm = connection.createStatement();
        ResultSet rs = stm.executeQuery(
            "SELECT DISTINCT mc.country_id, c.country, count(*) AS c "
            + "FROM film_country c, film_movie_country mc "
            + "WHERE c.id = mc.country_id "
            + "GROUP BY mc.country_id, country ORDER BY c DESC");
        // Create a new list
        List list = new List(List.ORDERED);
        // loop over the countries
        while (rs.next()) {
            // create a list item for the country
            ListItem item = new ListItem(
                String.format("%s: %d movies",
                    rs.getString("country"), rs.getInt("c")),
                FilmFonts.BOLDITALIC);
            // create a movie list for each country
            List movielist = new List(List.ORDERED, List.ALPHABETICAL);
            movielist.setLowercase(List.LOWERCASE);
            for(Movie movie :
                PojoFactory.getMovies(connection, rs.getString("country_id"))) {
                ListItem movieitem = new ListItem(movie.getMovieTitle());
                List directorlist = new List(List.UNORDERED);
                for (Director director : movie.getDirectors()) {
                    directorlist.add(
                        String.format("%s, %s",
                            director.getName(), director.getGivenName()));
                }
                movieitem.add(directorlist);
                movielist.add(movieitem);
            }
            item.add(movielist);
            list.add(item);
        }
        document.add(list);
        // close the statement and the database connection
        stm.close();
        connection.close();
        // step 4
        document.close();
        Document.compress = true;
    }

    /**
     * Manipulates a PDF file src with the file dest as result
     * @param src the original PDF
     * @param dest the resulting PDF
     * @param pow the PDF will be N-upped with N = Math.pow(2, pow);
     * @throws IOException
     * @throws DocumentException
     */
    public void compressPdf(String src, String dest) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest), PdfWriter.VERSION_1_5);
        stamper.getWriter().setCompressionLevel(9);
        int total = reader.getNumberOfPages() + 1;
        for (int i = 1; i < total; i++) {
            reader.setPageContent(i, reader.getPageContent(i));
        }
        stamper.setFullCompression();
        stamper.close();
    }

    /**
     * Manipulates a PDF file src with the file dest as result
     * @param src the original PDF
     * @param dest the resulting PDF
     * @param pow the PDF will be N-upped with N = Math.pow(2, pow);
     * @throws IOException
     * @throws DocumentException
     */
    public void decompressPdf(String src, String dest) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        Document.compress = false;
        int total = reader.getNumberOfPages() + 1;
        for (int i = 1; i < total; i++) {
            reader.setPageContent(i, reader.getPageContent(i));
        }
        stamper.close();
        Document.compress = true;
    }
    
    /**
     * Generates a PDF file, then reads it to generate a copy that is fully
     * compressed and one that isn't compressed.
     * 
     * @param args
     *            no arguments needed here
     * @throws SQLException 
     * @throws DocumentException 
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException, DocumentException, SQLException {
        HelloWorldCompression hello = new HelloWorldCompression();
        hello.createPdf(RESULT1, -1);
        hello.createPdf(RESULT2, 0);
        hello.createPdf(RESULT3, 1);
        hello.createPdf(RESULT4, 2);
        hello.createPdf(RESULT5, 3);
        hello.compressPdf(RESULT2, RESULT6);
        hello.decompressPdf(RESULT6, RESULT7);
    }
}