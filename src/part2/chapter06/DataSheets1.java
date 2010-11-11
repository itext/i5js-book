/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part2.chapter06;

import java.io.ByteArrayOutputStream;
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
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class DataSheets1 extends FillDataSheet {

    /** The resulting PDF file. */
    public static final String RESULT
        = "results/part2/chapter06/datasheets1.pdf";
    
    /**
     * Main method.
     * @param args no arguments needed
     * @throws DocumentException 
     * @throws IOException
     * @throws SQLException
     */
    public static void main(String[] args)
        throws IOException, SQLException, DocumentException {
        new DataSheets1().createPdf(RESULT);
    }

    /**
     * Creates a PDF document.
     * @param filename the path to the new PDF document
     * @throws    DocumentException 
     * @throws    IOException
     * @throws    SQLException
     */
    public void createPdf(String filename)
        throws IOException, DocumentException, SQLException {
    	// step 1
        Document document = new Document();
        // step 2
        PdfCopy copy
            = new PdfCopy(document, new FileOutputStream(filename));
        // step 3
        document.open();
        // step 4
        addDataSheets(copy);
        // step 5
        document.close();
    }
    
    /**
     * Fills out a data sheet, flattens it, and adds it to a combined PDF.
     * @param copy the PdfCopy instance (can also be PdfSmartCopy)
     * @throws SQLException
     * @throws IOException
     * @throws DocumentException
     */
    public void addDataSheets(PdfCopy copy)
        throws SQLException, IOException, DocumentException {
    	// Create a database connection
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        List<Movie> movies = PojoFactory.getMovies(connection);
        PdfReader reader;
        PdfStamper stamper;
        ByteArrayOutputStream baos;
        // Loop over all the movies and fill out the data sheet
        for (Movie movie : movies) {
            reader = new PdfReader(DATASHEET);
            baos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, baos);
            fill(stamper.getAcroFields(), movie);
            stamper.setFormFlattening(true);
            stamper.close();
            
            reader = new PdfReader(baos.toByteArray());
            copy.addPage(copy.getImportedPage(reader, 1));
            copy.freeReader(reader);
        }
        // Close the database connection
        connection.close();
    }
}
