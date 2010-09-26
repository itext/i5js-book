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

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfFileSpecification;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.collection.PdfCollection;
import com.itextpdf.text.pdf.collection.PdfCollectionField;
import com.itextpdf.text.pdf.collection.PdfCollectionItem;
import com.itextpdf.text.pdf.collection.PdfCollectionSchema;
import com.itextpdf.text.pdf.collection.PdfCollectionSort;
import com.itextpdf.text.pdf.collection.PdfTargetDictionary;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;

public class KubrickMovies {

    /** Path to the resources. */
    public static final String RESOURCE
        = "resources/posters/%s.jpg";
    /** The relative widths of the PdfPTable columns. */
    public static final float[] WIDTHS = { 1 , 7 };
    
    /** The filename of the PDF. */
    public static final String FILENAME = "kubrick_movies.pdf";
    /** The filename of the PDF with the movies by Stanley Kubrick. */
    public static final String RESULT = "results/part4/chapter16/" + FILENAME;
    
    /**
     * Creates a Collection schema that can be used to organize the movies of Stanley Kubrick
     * in a collection: year, title, duration, DVD acquisition, filesize (filename is present, but hidden).
     * @return    a collection schema
     */
    private static PdfCollectionSchema getCollectionSchema() {
        PdfCollectionSchema schema = new PdfCollectionSchema();
        
        PdfCollectionField size = new PdfCollectionField("File size", PdfCollectionField.SIZE);
        size.setOrder(4);
        schema.addField("SIZE", size);
        
        PdfCollectionField filename = new PdfCollectionField("File name", PdfCollectionField.FILENAME);
        filename.setVisible(false);
        schema.addField("FILE", filename);
        
        PdfCollectionField title = new PdfCollectionField("Movie title", PdfCollectionField.TEXT);
        title.setOrder(1);
        schema.addField("TITLE", title);
        
        PdfCollectionField duration = new PdfCollectionField("Duration", PdfCollectionField.NUMBER);
        duration.setOrder(2);
        schema.addField("DURATION", duration);
        
        PdfCollectionField year = new PdfCollectionField("Year", PdfCollectionField.NUMBER);
        year.setOrder(0);
        schema.addField("YEAR", year);
        
        return schema;
    }

    /**
     * Creates the PDF.
     * @return the bytes of a PDF file.
     * @throws SQLException 
     */
    public byte[] createPdf() throws DocumentException, IOException, SQLException {
        // step 1
        Document document = new Document();
        // step 2
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        // step 3
        document.open();
        // step 4
        document.add(new Paragraph(
            "This document contains a collection of PDFs, one per Stanley Kubrick movie."));
        
        PdfCollection collection = new PdfCollection(PdfCollection.DETAILS);
        PdfCollectionSchema schema = getCollectionSchema(); 
        collection.setSchema(schema);
        PdfCollectionSort sort = new PdfCollectionSort("YEAR");
        sort.setSortOrder(false);
        collection.setSort(sort);
        collection.setInitialDocument("Eyes Wide Shut");
        writer.setCollection(collection);
        
        PdfFileSpecification fs;
        PdfCollectionItem item;
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        java.util.List<Movie> movies = PojoFactory.getMovies(connection, 1);
        connection.close();
        for (Movie movie : movies) {
            fs = PdfFileSpecification.fileEmbedded(writer, null,
                String.format("kubrick_%s.pdf", movie.getImdb()),
                createMoviePage(movie));
            fs.addDescription(movie.getTitle(), false);

            item = new PdfCollectionItem(schema);
            item.addItem("TITLE", movie.getMovieTitle(false));
            if (movie.getMovieTitle(true) != null) {
                item.setPrefix("TITLE", movie.getMovieTitle(true));
            }
            item.addItem("DURATION", movie.getDuration());
            item.addItem("YEAR", movie.getYear());
            fs.addCollectionItem(item);
            writer.addFileAttachment(fs);
        }
        // step 5
        document.close();
        return baos.toByteArray();
    }

    /**
     * Creates the PDF.
     * @return the bytes of a PDF file.
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
        PdfTargetDictionary target = new PdfTargetDictionary(false);
        target.setAdditionalPath(new PdfTargetDictionary(false));
        Chunk chunk = new Chunk("Go to original document");
        PdfAction action = PdfAction.gotoEmbedded(null, target, new PdfString("movies"), false);
        chunk.setAction(action);
        document.add(chunk);
        // step 5
        document.close();
        return baos.toByteArray();
    }

    public static void main(String[] args) throws IOException, DocumentException, SQLException {
        KubrickMovies kubrick = new KubrickMovies();
        FileOutputStream os = new FileOutputStream(RESULT);
        os.write(kubrick.createPdf());
        os.flush();
        os.close();
    }
}