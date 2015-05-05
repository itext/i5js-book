/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part1.chapter04;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;
import com.lowagie.filmfestival.Screening;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.BaseColor;

public class ColumnTable {

    public static final String RESULT = "results/part1/chapter04/column_table.pdf";

    /**
     * Creates a PDF document.
     * @param filename the path to the new PDF document
     * @throws    DocumentException 
     * @throws    IOException
     * @throws    SQLException
     */
    public void createPdf(String filename)
        throws SQLException, DocumentException, IOException {
    	// Create the database connection
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        // step 1
        Document document = new Document(PageSize.A4.rotate());
        // step 2
        PdfWriter writer
            = PdfWriter.getInstance(document, new FileOutputStream(filename));
        // step 3
        document.open();
        // step 4
        ColumnText column = new ColumnText(writer.getDirectContent());
        List<Date> days = PojoFactory.getDays(connection);
        // COlumn definition
        float[][] x = {
                { document.left(), document.left() + 380 },
                { document.right() - 380, document.right() }
            };
        // Loop over the festival days
        for (Date day : days) {
            // add content to the column
            column.addElement(getTable(connection, day));
            int count = 0;
            float height = 0;
            int status = ColumnText.START_COLUMN;
            // render the column as long as it has content
            while (ColumnText.hasMoreText(status)) {
            	// add the top-level header to each new page
                if (count == 0) {
                    height = addHeaderTable(
                        document, day, writer.getPageNumber());
                }
                // set the dimensions of the current column
                column.setSimpleColumn(
                    x[count][0], document.bottom(),
                    x[count][1], document.top() - height - 10);
                // render as much content as possible
                status = column.go();
                // go to a new page if you've reached the last column
                if (++count > 1) {
                    count = 0;
                    document.newPage();
                }
            }
            document.newPage();
        }
        // step 5
        document.close();
        // Close the database connection
        connection.close();
    }
    
    /**
     * Add a header table to the document
     * @param document The document to which you want to add a header table
     * @param day The day that needs to be shown in the header table
     * @param page The page number that has to be shown in the header
     * @return the height of the resulting header table
     * @throws DocumentException
     */
    public float addHeaderTable(Document document, Date day, int page)
        throws DocumentException {
        PdfPTable header = new PdfPTable(3);
        header.setWidthPercentage(100);
        header.getDefaultCell().setBackgroundColor(BaseColor.BLACK);
        Font font = new Font(FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
        Phrase p = new Phrase("Foobar Film Festival", font);
        header.addCell(p);
        header.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        p = new Phrase(day.toString(), font);
        header.addCell(p);
        header.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
        p = new Phrase(String.format("page %d", page), font);
        header.addCell(p);
        document.add(header);
        return header.getTotalHeight();
    }

    /**
     * Creates a table with movie screenings for a specific day
     * @param connection a connection to the database
     * @param day a day
     * @return a table with screenings
     * @throws SQLException
     * @throws DocumentException
     * @throws IOException
     */
    public PdfPTable getTable(DatabaseConnection connection, Date day)
        throws SQLException, DocumentException, IOException {
        PdfPTable table = new PdfPTable(new float[] { 2, 1, 2, 5, 1 });
        table.setWidthPercentage(100f);
        table.getDefaultCell().setUseAscender(true);
        table.getDefaultCell().setUseDescender(true);
        table.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
        for (int i = 0; i < 2; i++) {
            table.addCell("Location");
            table.addCell("Time");
            table.addCell("Run Length");
            table.addCell("Title");
            table.addCell("Year");
        }
        table.getDefaultCell().setBackgroundColor(null);
        table.setHeaderRows(2);
        table.setFooterRows(1);
        List<Screening> screenings = PojoFactory.getScreenings(connection, day);
        Movie movie;
        for (Screening screening : screenings) {
            movie = screening.getMovie();
            table.addCell(screening.getLocation());
            table.addCell(String.format("%1$tH:%1$tM", screening.getTime()));
            table.addCell(String.format("%d '", movie.getDuration()));
            table.addCell(movie.getMovieTitle());
            table.addCell(String.valueOf(movie.getYear()));
        }
        return table;
    }

    /**
     * Main method.
     * @param args no arguments needed
     * @throws DocumentException 
     * @throws IOException
     * @throws SQLException
     */
    public static void main(String[] args)
        throws SQLException, DocumentException, IOException {
        new ColumnTable().createPdf(RESULT);
    }
}
