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
import java.util.HashMap;
import java.util.List;

import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.FilmFonts;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;
import com.lowagie.filmfestival.PojoToElementFactory;
import com.lowagie.filmfestival.Screening;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.BaseColor;

public class NestedTables {

    public static final String RESULT = "results/part1/chapter04/nested_tables.pdf";
    /** Path to the resources. */
    public static final String RESOURCE = "resources/posters/%s.jpg";
    
    public HashMap<String,Image> images = new HashMap<String,Image>();
    
    public static void main(String[] args) throws SQLException, DocumentException, IOException {
        new NestedTables().createPdf(RESULT);
    }
    
    public void createPdf(String filename) throws SQLException, DocumentException, IOException {
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        document.open();
        List<Date> days = PojoFactory.getDays(connection);
        for (Date day : days) {
            document.add(getTable(connection, day));
            document.newPage();
        }
        document.close();
        connection.close();

    }

    public PdfPTable getTable(DatabaseConnection connection, Date day) throws SQLException, DocumentException, IOException {
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100f);
        Font f = new Font();
        f.setColor(BaseColor.WHITE);
        PdfPCell cell = new PdfPCell(new Phrase(day.toString(), f));
        cell.setBackgroundColor(BaseColor.BLACK);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        List<Screening> screenings = PojoFactory.getScreenings(connection, day);
        for (Screening screening : screenings) {
            table.addCell(getTable(connection, screening));
        }
        return table;
    }

    private PdfPTable getTable(DatabaseConnection connection, Screening screening) throws DocumentException, IOException {
        PdfPTable table = new PdfPTable(4);
        table.setWidths(new int[]{1, 5, 10, 10});
        
        Movie movie = screening.getMovie();
        PdfPCell cell = new PdfPCell();
        cell.addElement(fullTitle(screening));
        cell.setColspan(4);
        cell.setBorder(PdfPCell.NO_BORDER);
        BaseColor color = WebColors.getRGBColor("#" + movie.getEntry().getCategory().getColor());
        cell.setBackgroundColor(color);
        table.addCell(cell);
        
        cell = new PdfPCell();
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setUseAscender(true);
        cell.setUseDescender(true);
        table.addCell(cell);
        
        cell = new PdfPCell(getImage(movie.getImdb()));
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);
        
        cell = new PdfPCell();
        cell.addElement(PojoToElementFactory.getDirectorList(movie));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setUseAscender(true);
        cell.setUseDescender(true);
        table.addCell(cell);
        
        cell = new PdfPCell();
        cell.addElement(PojoToElementFactory.getCountryList(movie));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setUseAscender(true);
        cell.setUseDescender(true);
        table.addCell(cell);

        return table;
    }
    
    private static PdfPTable fullTitle(Screening screening) throws DocumentException {
        PdfPTable table = new PdfPTable(3);
        table.setWidths(new int[]{3, 15, 2});
        table.setWidthPercentage(100);
        
        PdfPCell cell = new PdfPCell();
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setBackgroundColor(BaseColor.WHITE);
        cell.setUseAscender(true);
        cell.setUseDescender(true);
        String s = String.format("%s \u2013 %2$tH:%2$tM", screening.getLocation(), screening.getTime().getTime());
        cell.addElement(new Paragraph(s));
        table.addCell(cell);
        
        Movie movie = screening.getMovie();
        Paragraph p = new Paragraph();
        p.add(new Phrase(movie.getMovieTitle(), FilmFonts.BOLD));
        p.setLeading(16);
        if (movie.getOriginalTitle() != null) {
            p.add(new Phrase(" (" + movie.getOriginalTitle() + ")"));
        }
        cell = new PdfPCell();
        cell.addElement(p);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setUseAscender(true);
        cell.setUseDescender(true);
        table.addCell(cell);
        
        cell = new PdfPCell();
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setBackgroundColor(BaseColor.WHITE);
        cell.setUseAscender(true);
        cell.setUseDescender(true);
        p = new Paragraph(String.format("%d'", movie.getDuration()));
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);
        return table;
    }
    
    public Image getImage(String imdb) throws DocumentException, IOException {
        Image img = images.get(imdb);
        if (img == null) {
            img = Image.getInstance(String.format(RESOURCE, imdb));
            img.scaleToFit(80, 72);
            images.put(imdb, img);
        }
        return img;
    }
}
