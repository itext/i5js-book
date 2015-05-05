/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */
package part4.chapter13;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import part1.chapter02.MovieParagraphs1;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;

public class PageLayoutExample extends MovieParagraphs1 {

    /** The resulting PDF file. */
    public static final String RESULT1
        = "results/part4/chapter13/page_layout_single.pdf";
    /** The resulting PDF file. */
    public static final String RESULT2
        = "results/part4/chapter13/page_layout_column.pdf";
    /** The resulting PDF file. */
    public static final String RESULT3
        = "results/part4/chapter13/page_layout_columns_l.pdf";
    /** The resulting PDF file. */
    public static final String RESULT4
        = "results/part4/chapter13/page_layout_columns_r.pdf";
    /** The resulting PDF file. */
    public static final String RESULT5
        = "results/part4/chapter13/page_layout_pages_l.pdf";
    /** The resulting PDF file. */
    public static final String RESULT6
        = "results/part4/chapter13/page_layout_pages_r.pdf";

    /**
     * Creates a PDF with information about the movies
     * @param filename the name of the PDF file that will be created.
     * @param pagelayout the value for the viewerpreferences
     * @throws    DocumentException 
     * @throws    IOException 
     * @throws    SQLException
     */
    public void createPdf(String filename, int viewerpreference)
        throws IOException, DocumentException, SQLException {
        // Create a database connection
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        // step 1
        Document document = new Document();
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
        writer.setPdfVersion(PdfWriter.VERSION_1_5);
        writer.setViewerPreferences(viewerpreference);
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
        PageLayoutExample example = new PageLayoutExample();
        example.createPdf(RESULT1, PdfWriter.PageLayoutSinglePage);
        example.createPdf(RESULT2, PdfWriter.PageLayoutOneColumn);
        example.createPdf(RESULT3, PdfWriter.PageLayoutTwoColumnLeft);
        example.createPdf(RESULT4, PdfWriter.PageLayoutTwoColumnRight);
        example.createPdf(RESULT5, PdfWriter.PageLayoutTwoPageLeft);
        example.createPdf(RESULT6, PdfWriter.PageLayoutTwoPageRight);
    }
}
