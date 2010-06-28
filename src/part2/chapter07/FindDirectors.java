/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part2.chapter07;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import part1.chapter04.NestedTables;

import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Utilities;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

public class FindDirectors {
    
    /** The resulting PDF file. */
    public static final String RESULT
        = "results/part2/chapter07/find_directors.pdf";

    /** Path to the resources. */
    public static final String RESOURCE
        = "resources/js/find_director.js";
    
    /**
     * Creates a PDF file with director names.
     * @param filename the name of the PDF file that needs to be created.
     * @throws DocumentException 
     * @throws IOException 
     * @throws SQLException
     */
    public void createPdf(String filename)
        throws IOException, DocumentException, SQLException {
    	// Create a database connection and statement
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        Statement stm = connection.createStatement();
    	// step 1
        Document tmp = new Document();
        // step 2
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(tmp, baos);
        // step 3
        tmp.open();
        // step 4
        ResultSet rs = stm.executeQuery(
           "SELECT name, given_name FROM film_director ORDER BY name, given_name");
        while (rs.next()) {
            tmp.add(createDirectorParagraph(writer, rs));
        }
        // step 5
        tmp.close();
        // Close the database connection and statement
        stm.close();
        connection.close();
        
        // Create readers
        PdfReader[] readers = {
                new PdfReader(baos.toByteArray()),
                new PdfReader(NestedTables.RESULT) };
        // step 1
        Document document = new Document();
        // step 2
        PdfCopy copy = new PdfCopy(document, new FileOutputStream(filename));
        // step 3
        document.open();
        // step 4
        copy.addJavaScript(Utilities.readFileToString(RESOURCE));
        int n;
        for (int i = 0; i < readers.length; i++) {
            n = readers[i].getNumberOfPages();
            for (int page = 0; page < n; ) {
                copy.addPage(copy.getImportedPage(readers[i], ++page));
            }
        }
        // step 5
        document.close();
    }
    
    /**
     * Creates a Phrase with the name and given name of a director
     * using different fonts.
     * @param rs the ResultSet containing director records.
     */
    public Paragraph createDirectorParagraph(PdfWriter writer, ResultSet rs)
        throws UnsupportedEncodingException, SQLException {
        String n = new String(rs.getBytes("name"), "UTF-8");
        Chunk name = new Chunk(n);
        name.setAction(PdfAction.javaScript(
                String.format("findDirector('%s');", n), writer));
        name.append(", ");
        name.append(new String(rs.getBytes("given_name"), "UTF-8"));
        return new Paragraph(name);
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
        NestedTables.main(args);
        new FindDirectors().createPdf(RESULT);
    }
}
