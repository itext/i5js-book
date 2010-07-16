/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part4.chapter13;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPageLabels;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.PdfWriter;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;

public class PageLabelExample {
    
    /** The resulting PDF file. */
    public static final String RESULT
        = "results/part4/chapter13/page_labels.pdf";
    /** The resulting PDF file. */
    public static final String RESULT2
        = "results/part4/chapter13/page_labels_changed.pdf";
    /** A text file containing page numbers and labels. */
    public static final String LABELS
        = "results/part4/chapter13/page_labels.txt";
    /** SQL statements */
    public static final String[] SQL = {
        "SELECT country FROM film_country ORDER BY country",
        "SELECT name FROM film_director ORDER BY name",
        "SELECT title FROM film_movietitle ORDER BY title"
    };
    /** SQL statements */
    public static final String[] FIELD = { "country", "name", "title" };
    
    /**
     * Creates a PDF document.
     * @param filename the path to the new PDF document
     * @throws    DocumentException 
     * @throws    IOException 
     * @throws    SQLException
     */
    public void createPdf(String filename)
        throws IOException, DocumentException, SQLException{
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        // step 1
        Document document = new Document(PageSize.A5);
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
        // step 3
        document.open();
        // step 4
        int[] start = new int[3];
        for (int i = 0; i < 3; i++) {
            start[i] = writer.getPageNumber();
            addParagraphs(document, connection, SQL[i], FIELD[i]);
            document.newPage();
        }
        PdfPageLabels labels = new PdfPageLabels();
        labels.addPageLabel(start[0], PdfPageLabels.UPPERCASE_LETTERS);
        labels.addPageLabel(start[1], PdfPageLabels.DECIMAL_ARABIC_NUMERALS);
        labels.addPageLabel(start[2],
            PdfPageLabels.DECIMAL_ARABIC_NUMERALS, "Movies-", start[2] - start[1] + 1);
        writer.setPageLabels(labels);
        // step 5
        document.close();
        connection.close();
        
    }
    
    /**
     * Performs an SQL query and writes the results to a PDF using the Paragraph object.
     * @param document    The document to which the paragraphs have to be added
     * @param connection  The database connection that has to be used
     * @param sql         The SQL statement
     * @param field       The name of the field that has to be shown
     * @throws SQLException
     * @throws DocumentException
     * @throws IOException
     */
    public void addParagraphs(Document document, DatabaseConnection connection, String sql, String field)
        throws SQLException, DocumentException, IOException {
        Statement stm = connection.createStatement();
        ResultSet rs = stm.executeQuery(sql);
        while (rs.next()) {
            document.add(new Paragraph(new String(rs.getBytes(field), "UTF-8")));
        }
    }
    
    /**
     * Reads the page labels from an existing PDF
     * @param src  the path to an existing PDF
     * @param dest the path to the resulting text file
     * @throws IOException
     */
    public void listPageLabels(String src, String dest) throws IOException {
        // no PDF, just a text file
        PrintStream out = new PrintStream(new FileOutputStream(dest));
        String[] labels = PdfPageLabels.getPageLabels(new PdfReader(src));
        for (int i = 0; i < labels.length; i++) {
            out.println(labels[i]);
        }
        out.flush();
        out.close();
    }
    
    /**
     * Manipulates the page labels at the lowest PDF level.
     * @param src  The path to the source file
     * @param dest The path to the changed PDF
     * @throws IOException
     * @throws DocumentException
     */
    public void manipulatePageLabel(String src, String dest) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        PdfDictionary root = reader.getCatalog();
        PdfDictionary labels = root.getAsDict(PdfName.PAGELABELS);
        PdfArray nums = labels.getAsArray(PdfName.NUMS);
        int n;
        PdfDictionary pagelabel;
        for (int i = 0; i < nums.size(); i++) {
            n = nums.getAsNumber(i).intValue();
            i++;
            if (n == 5) {
                pagelabel = nums.getAsDict(i);
                pagelabel.remove(PdfName.ST);
                pagelabel.put(PdfName.P, new PdfString("Film-"));
            }
        }
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        stamper.close();
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
        PageLabelExample labels = new PageLabelExample();
        labels.createPdf(RESULT);
        labels.listPageLabels(RESULT, LABELS);
        labels.manipulatePageLabel(RESULT, RESULT2);
    }
}
