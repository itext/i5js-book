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
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;

/**
 * Creates a PDF listing Stanley Kubrick movies and the corresponding posters.
 * JPGs of the movie posters are added as file attachment annotations.
 * The attachments can be extracted.
 */
public class KubrickDvds {
    
    /** Path to the resources. */
    public static final String RESOURCE
        = "resources/posters/%s.jpg";
    /** The filename of the resulting PDF. */
    public static final String FILENAME = "kubrick_dvds.pdf";
    /** The path to the resulting PDFs */
    public static final String PATH = "results/part4/chapter16/%s";
    /** The filename of the PDF with the movies by Stanley Kubrick. */
    public static final String RESULT = String.format(PATH, FILENAME);
    
    /**
     * Creates a PDF listing Stanley Kubrick movies in my DVD Collection. 
     * @throws IOException 
     * @throws DocumentException 
     * @throws SQLException
     */
    public static void main(String[] args) throws IOException, DocumentException, SQLException {
        KubrickDvds kubrick = new KubrickDvds();
        FileOutputStream os = new FileOutputStream(RESULT);
        os.write(kubrick.createPdf());
        os.flush();
        os.close();
        kubrick.extractAttachments(RESULT);
    }
    
    /**
     * Extracts attachments from an existing PDF.
     * @param src   the path to the existing PDF
     * @param dest  where to put the extracted attachments
     * @throws IOException
     */
    public void extractAttachments(String src) throws IOException {
        PdfReader reader = new PdfReader(src);
        PdfArray array;
        PdfDictionary annot;
        PdfDictionary fs;
        PdfDictionary refs;
        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
            array = reader.getPageN(i).getAsArray(PdfName.ANNOTS);
            if (array == null) continue;
            for (int j = 0; j < array.size(); j++) {
                annot = array.getAsDict(j);
                if (PdfName.FILEATTACHMENT.equals(annot.getAsName(PdfName.SUBTYPE))) {
                    fs = annot.getAsDict(PdfName.FS);
                    refs = fs.getAsDict(PdfName.EF);
                    for (PdfName name : refs.getKeys()) {
                        FileOutputStream fos
                            = new FileOutputStream(String.format(PATH, fs.getAsString(name).toString()));
                        fos.write(PdfReader.getStreamBytes((PRStream)refs.getAsStream(name)));
                        fos.flush();
                        fos.close();
                    }
                }
            }
        }
    }

    /**
     * Creates the PDF.
     * @return the bytes of a PDF file.
     * @throws DocumentExcetpion
     * @throws IOException
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
        document.add(new Paragraph("This is a list of Kubrick movies available in DVD stores."));
        PdfAnnotation annot;
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        Set<Movie> movies = new TreeSet<Movie>();
        movies.addAll(PojoFactory.getMovies(connection, 1));
        movies.addAll(PojoFactory.getMovies(connection, 4));
        ListItem item;
        Chunk chunk;
        List list = new List();
        for (Movie movie : movies) {
            annot = PdfAnnotation.createFileAttachment(
                    writer, null, movie.getMovieTitle(false), null,
                    String.format(RESOURCE, movie.getImdb()), String.format("%s.jpg", movie.getImdb()));
            item = new ListItem(movie.getMovieTitle(false));
            item.add("\u00a0\u00a0");
            chunk = new Chunk("\u00a0\u00a0\u00a0\u00a0");
            chunk.setAnnotation(annot);
            item.add(chunk);
            list.add(item);
        }
        document.add(list);
        // step 5
        document.close();
        connection.close();
        return baos.toByteArray();
    }
}
