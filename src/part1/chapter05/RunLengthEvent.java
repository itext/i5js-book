/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part1.chapter05;

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
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.BaseColor;

public class RunLengthEvent {

    /** The resulting PDF. */
    public static final String RESULT
        = "results/part1/chapter05/run_length.pdf";

    /** Inner class to draw a bar inside a cell. */
    class RunLength implements PdfPCellEvent {
        
        public int duration;
        
        public RunLength(int duration) {
            this.duration = duration;
        }
        
        /**
         * @see com.lowagie.text.pdf.PdfPCellEvent#cellLayout(
         *      com.lowagie.text.pdf.PdfPCell, com.lowagie.text.Rectangle,
         *      com.lowagie.text.pdf.PdfContentByte[])
         */
        public void cellLayout(PdfPCell cell, Rectangle rect,
                PdfContentByte[] canvas) {
            PdfContentByte cb = canvas[PdfPTable.BACKGROUNDCANVAS];
            cb.saveState();
            if (duration < 90) {
                cb.setRGBColorFill(0x7C, 0xFC, 0x00);
            }
            else if (duration > 120) {
                cb.setRGBColorFill(0x8B, 0x00, 0x00);
            } 
            else {
                cb.setRGBColorFill(0xFF, 0xA5, 0x00);
            }
            cb.rectangle(rect.getLeft(), rect.getBottom(),
                rect.getWidth() * duration / 240, rect.getHeight());
            cb.fill();
            cb.restoreState();
        }
    }

    /** Inner class to add the words PRESS PREVIEW to a cell. */
    class PressPreview implements PdfPCellEvent {
        
        public BaseFont bf;
        
        public PressPreview() throws DocumentException, IOException {
            bf = BaseFont.createFont();
        }
        
        /**
         * @see com.lowagie.text.pdf.PdfPCellEvent#cellLayout(com.lowagie.text.pdf.PdfPCell,
         *      com.lowagie.text.Rectangle,
         *      com.lowagie.text.pdf.PdfContentByte[])
         */
        public void cellLayout(PdfPCell cell, Rectangle rect,
                PdfContentByte[] canvas) {
            PdfContentByte cb = canvas[PdfPTable.TEXTCANVAS];
            cb.beginText();
            cb.setFontAndSize(bf, 12);
            cb.showTextAligned(Element.ALIGN_RIGHT, "PRESS PREVIEW",
                rect.getRight() - 3, rect.getBottom() + 4.5f, 0);
            cb.endText();
        }
    }
    
    /** The press cell event. */
    public PdfPCellEvent press;

    /**
     * Creates a PDF document.
     * @param filename the path to the new PDF document
     * @throws    DocumentException 
     * @throws    IOException
     * @throws    SQLException
     */
    public void createPdf(String filename)
        throws SQLException, DocumentException, IOException {
        press = new PressPreview();
        // Create a database connection
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        // step 1
        Document document = new Document(PageSize.A4.rotate());
        // step 2
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        // step 3
        document.open();
        // step 4
        List<Date> days = PojoFactory.getDays(connection);
        for (Date day : days) {
            document.add(getTable(connection, day));
            document.newPage();
        }
        // step 5
        document.close();
        // Close the database connection
        connection.close();
    }
    
    /**
     * @param connection
     * @param day
     * @return
     * @throws SQLException
     * @throws DocumentException
     * @throws IOException
     */
    public PdfPTable getTable(DatabaseConnection connection, Date day)
        throws SQLException, DocumentException, IOException {
        PdfPTable table = new PdfPTable(new float[] { 2, 1, 2, 5, 1 });
        table.setWidthPercentage(100f);
        table.getDefaultCell().setPadding(3);
        table.getDefaultCell().setUseAscender(true);
        table.getDefaultCell().setUseDescender(true);
        table.getDefaultCell().setColspan(5);
        table.getDefaultCell().setBackgroundColor(BaseColor.RED);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(day.toString());
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        table.getDefaultCell().setColspan(1);
        table.getDefaultCell().setBackgroundColor(BaseColor.YELLOW);
        for (int i = 0; i < 2; i++) {
            table.addCell("Location");
            table.addCell("Time");
            table.addCell("Run Length");
            table.addCell("Title");
            table.addCell("Year");
        }
        table.getDefaultCell().setBackgroundColor(null);
        table.setHeaderRows(3);
        table.setFooterRows(1);
        List<Screening> screenings = PojoFactory.getScreenings(connection, day);
        Movie movie;
        PdfPCell runLength;
        for (Screening screening : screenings) {
            movie = screening.getMovie();
            table.addCell(screening.getLocation());
            table.addCell(String.format("%1$tH:%1$tM", screening.getTime()));
            runLength = new PdfPCell(table.getDefaultCell());
            runLength.setPhrase(new Phrase(String.format("%d '", movie.getDuration())));
            runLength.setCellEvent(new RunLength(movie.getDuration()));
            if (screening.isPress()) {
                runLength.setCellEvent(press);
            }
            table.addCell(runLength);
            table.addCell(movie.getMovieTitle());
            table.addCell(String.valueOf(movie.getYear()));
        }
        return table;
    }

    /**
     * Main method.
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException 
     * @throws SQLException
     */
    public static void main(String[] args) throws SQLException, DocumentException, IOException {
        new RunLengthEvent().createPdf(RESULT);
    }
}
