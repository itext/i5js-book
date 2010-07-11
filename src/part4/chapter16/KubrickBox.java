/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */
package part4.chapter16;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;
import java.util.TreeSet;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfDestination;
import com.itextpdf.text.pdf.PdfFileSpecification;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.collection.PdfTargetDictionary;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;

/**
 * This example explains the use of portable collections,
 * a new feature introduced in PDF 1.7
 */
public class KubrickBox {

    /** The resulting PDF. */
    public static final String RESULT = "results/part4/chapter16/kubrick_box.pdf";
    /** The path to an image used in the example. */
    public static final String IMG_BOX = "resources/img/kubrick_box.jpg";
    /** Path to the resources. */
    public static final String RESOURCE = "resources/posters/%s.jpg";
    /** The relative widths of the PdfPTable columns. */
    public static final float[] WIDTHS = { 1 , 7 };

    /**
     * Creates the PDF.
     * @return the bytes of a PDF file.
     * @throws DocumentException
     * @throws IOException
     * @throws SQLException 
     */
    public void createPdf(String filename) throws DocumentException, IOException, SQLException {
        // step 1
        Document document = new Document();
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
        // step 3
        document.open();
        // step 4
        Image img = Image.getInstance(IMG_BOX);
        document.add(img);
        List list = new List(List.UNORDERED, 20);
        PdfDestination dest = new PdfDestination(PdfDestination.FIT);
        dest.addFirst(new PdfNumber(1));
        PdfTargetDictionary target;
        Chunk chunk;
        ListItem item;
        PdfAction action = null;
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        Set<Movie> box = new TreeSet<Movie>();
        box.addAll(PojoFactory.getMovies(connection, 1));
        box.addAll(PojoFactory.getMovies(connection, 4));
        connection.close();
        PdfFileSpecification fs;
        for (Movie movie : box) {
            if (movie.getYear() > 1960) {
                fs = PdfFileSpecification.fileEmbedded(writer, null,
                        String.format("kubrick_%s.pdf", movie.getImdb()),
                        createMoviePage(movie));
                fs.addDescription(movie.getTitle(), false);
                writer.addFileAttachment(fs);
                item = new ListItem(movie.getMovieTitle());
                target = new PdfTargetDictionary(true);
                target.setEmbeddedFileName(movie.getTitle());
                action = PdfAction.gotoEmbedded(null, target, dest, true);
                chunk = new Chunk(" (see info)");
                chunk.setAction(action);
                item.add(chunk);
                list.add(item);
            }
        }
        document.add(list);
        // step 5
        document.close();
    }

    /**
     * Creates the PDF.
     * @return the bytes of a PDF file.
     * @throws DocumentExcetpion
     * @throws IOException
     * @throws SQLException 
     */
    public byte[] createMoviePage(Movie movie) throws DocumentException, IOException {
        // step 1
        Document document = new Document();
        // step 2
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);
        // step 3
        document.open();
        // step 4
        Paragraph p = new Paragraph(movie.getMovieTitle(),
                FontFactory.getFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED, 16));
        document.add(p);
        document.add(Chunk.NEWLINE);
        PdfPTable table = new PdfPTable(WIDTHS);
        table.addCell(Image.getInstance(String.format(RESOURCE, movie.getImdb())));
        PdfPCell cell = new PdfPCell();
        cell.addElement(new Paragraph("Year: " + movie.getYear()));
        cell.addElement(new Paragraph("Duration: " + movie.getDuration()));
        table.addCell(cell);
        document.add(table);
        PdfDestination dest = new PdfDestination(PdfDestination.FIT);
        dest.addFirst(new PdfNumber(1));
        PdfTargetDictionary target = new PdfTargetDictionary(false);
        Chunk chunk = new Chunk("Go to original document");
        PdfAction action = PdfAction.gotoEmbedded(null, target, dest, false);
        chunk.setAction(action);
        document.add(chunk);
        // step 5
        document.close();
        return baos.toByteArray();
    }

    /**
     * Main method.
     *
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException
     * @throws SQLException
     */
    public static void main(String[] args) throws DocumentException, IOException, SQLException {
        new KubrickBox().createPdf(RESULT);
    }
}
