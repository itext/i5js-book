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
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.Set;
import java.util.TreeSet;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfDestination;
import com.itextpdf.text.pdf.PdfFileSpecification;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
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

/**
 * This example explains the use of portable collections,
 * a new feature introduced in PDF 1.7
 */
public class KubrickCollection {

    /** An image file used in this example */
    public static final String IMG_BOX = "resources/img/kubrick_box.jpg";
    /** An image file used in this example */
    public static final String IMG_KUBRICK = "resources/img/kubrick.jpg";
    /** The resulting PDF file. */
    public static final String RESULT = "results/part4/chapter16/kubrick_collection.pdf";
    /** The name of a field in the collection schema. */
    public static final String TYPE_FIELD        = "TYPE";
    /** A caption of a field in the collection schema. */
    public static final String TYPE_CAPTION        = "File type";
    /** The name of a field in the collection schema. */
    public static final String FILE_FIELD        = "FILE";
    /** A caption of a field in the collection schema. */
    public static final String FILE_CAPTION        = "File name";
    /** Sort order for the keys in the collection. */
    public String[] KEYS = { TYPE_FIELD, FILE_FIELD };
    
    /**
     * Creates the PDF.
     * @return the bytes of a PDF file.
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
        PdfCollection collection = new PdfCollection(PdfCollection.HIDDEN);
        PdfCollectionSchema schema = getCollectionSchema();
        collection.setSchema(schema);
        PdfCollectionSort sort = new PdfCollectionSort(KEYS);
        collection.setSort(sort);
        writer.setCollection(collection);

        PdfCollectionItem collectionitem = new PdfCollectionItem(schema);
        PdfFileSpecification fs;
        fs = PdfFileSpecification.fileEmbedded(writer, IMG_KUBRICK, "kubrick.jpg", null);
        fs.addDescription("Stanley Kubrick", false);
        collectionitem.addItem(TYPE_FIELD, "JPEG");
        fs.addCollectionItem(collectionitem);
        writer.addFileAttachment(fs);
        
        ByteArrayOutputStream txt = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(txt);
        Image img = Image.getInstance(IMG_BOX);
        document.add(img);
        List list = new List(List.UNORDERED, 20);
        PdfDestination dest = new PdfDestination(PdfDestination.FIT);
        dest.addFirst(new PdfNumber(1));
        PdfTargetDictionary intermediate;
        PdfTargetDictionary target;
        Chunk chunk;
        ListItem item;
        PdfAction action = null;
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        Set<Movie> box = new TreeSet<Movie>();
        java.util.List<Movie> movies = PojoFactory.getMovies(connection, 1);
        box.addAll(movies);
        box.addAll(PojoFactory.getMovies(connection, 4));
        connection.close();
        for (Movie movie : box) {
            if (movie.getYear() > 1960) {
                out.println(String.format(
                    "%s;%s;%s", movie.getMovieTitle(), movie.getYear(), movie.getDuration()));
                item = new ListItem(movie.getMovieTitle());
                if (!"0278736".equals(movie.getImdb())) {
                    target = new PdfTargetDictionary(true);
                    target.setEmbeddedFileName(movie.getTitle());
                    intermediate = new PdfTargetDictionary(true);
                    intermediate.setFileAttachmentPage(1);
                    intermediate.setFileAttachmentIndex(1);
                    intermediate.setAdditionalPath(target);
                    action = PdfAction.gotoEmbedded(null, intermediate, dest, true);
                    chunk = new Chunk(" (see info)");
                    chunk.setAction(action);
                    item.add(chunk);
                }
                list.add(item);
            }
        }
        out.flush();
        out.close();
        document.add(list);
        fs = PdfFileSpecification.fileEmbedded(writer, null, "kubrick.txt", txt.toByteArray());
        fs.addDescription("Kubrick box: the movies", false);
        collectionitem.addItem(TYPE_FIELD, "TXT");
        fs.addCollectionItem(collectionitem);
        writer.addFileAttachment(fs);

        PdfPTable table = new PdfPTable(1);
        table.setSpacingAfter(10);
        PdfPCell cell = new PdfPCell(new Phrase("All movies by Kubrick"));
        cell.setBorder(PdfPCell.NO_BORDER);
        fs = PdfFileSpecification.fileEmbedded(
            writer, null, KubrickMovies.FILENAME, new KubrickMovies().createPdf());
        collectionitem.addItem(TYPE_FIELD, "PDF");
        fs.addCollectionItem(collectionitem);
        target = new PdfTargetDictionary(true);
        target.setFileAttachmentPagename("movies");
        target.setFileAttachmentName("The movies of Stanley Kubrick");
        cell.setCellEvent(new PdfActionEvent(writer, PdfAction.gotoEmbedded(null, target, dest, true)));
        cell.setCellEvent(new FileAttachmentEvent(writer, fs, "The movies of Stanley Kubrick"));
        cell.setCellEvent(new LocalDestinationEvent(writer, "movies"));
        table.addCell(cell);
        writer.addFileAttachment(fs);

        cell = new PdfPCell(new Phrase("Kubrick DVDs"));
        cell.setBorder(PdfPCell.NO_BORDER);
        fs = PdfFileSpecification.fileEmbedded(
            writer, null, KubrickDvds.FILENAME, new KubrickDvds().createPdf());
        collectionitem.addItem(TYPE_FIELD, "PDF");
        fs.addCollectionItem(collectionitem);
        cell.setCellEvent(new FileAttachmentEvent(writer, fs, "Kubrick DVDs"));
        table.addCell(cell);
        writer.addFileAttachment(fs);
        
        cell = new PdfPCell(new Phrase("Kubrick documentary"));
        cell.setBorder(PdfPCell.NO_BORDER);
        fs = PdfFileSpecification.fileEmbedded(
            writer, null, KubrickDocumentary.FILENAME, new KubrickDocumentary().createPdf());
        collectionitem.addItem(TYPE_FIELD, "PDF");
        fs.addCollectionItem(collectionitem);
        cell.setCellEvent(new FileAttachmentEvent(writer, fs, "Kubrick Documentary"));
        table.addCell(cell);
        writer.addFileAttachment(fs);

        document.newPage();
        document.add(table);
        // step 5
        document.close();
    }
    
    /**
     * Creates a Collection schema that can be used to organize the movies of Stanley Kubrick
     * in a collection: year, title, duration, DVD acquisition, filesize (filename is present, but hidden).
     * @return    a collection schema
     */
    private static PdfCollectionSchema getCollectionSchema() {
        PdfCollectionSchema schema = new PdfCollectionSchema();
        
        PdfCollectionField type = new PdfCollectionField(TYPE_CAPTION, PdfCollectionField.TEXT);
        type.setOrder(0);
        schema.addField(TYPE_FIELD, type);
        
        PdfCollectionField filename = new PdfCollectionField(FILE_FIELD, PdfCollectionField.FILENAME);
        filename.setOrder(1);
        schema.addField(FILE_FIELD, filename);
        
        return schema;
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
        new KubrickCollection().createPdf(RESULT);
    }
}
