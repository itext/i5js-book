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

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfFileSpecification;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.xml.XMLUtil;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;

public class KubrickDocumentary {
    /** The filename of the PDF. */
    public static final String FILENAME = "kubrick_documentary.pdf";
    /** The path to the resulting PDFs */
    public static final String PATH = "results/part4/chapter16/%s";
    /** The filename of the PDF with the movies by Stanley Kubrick. */
    public static final String RESULT = String.format(PATH, FILENAME);
    
    /**
     * Creates the PDF.
     * @return the bytes of a PDF file.
     * @throws DocumentExcetpion
     * @throws IOException
     * @throws SQLException 
     */
    public byte[] createPdf() throws DocumentException, IOException, SQLException {
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        java.util.List<Movie> movies = PojoFactory.getMovies(connection, 1);
        connection.close();
        // step 1
        Document document = new Document();
        // step 2
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        // step 3
        document.open();
        // step 4
        document.add(new Paragraph(
            "'Stanley Kubrick: A Life in Pictures' is a documentary about Stanley Kubrick and his films:"));

        ByteArrayOutputStream txt = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(txt);
        out.println("<movies>");
        List list = new List(List.UNORDERED, 20);
        ListItem item;
        for (Movie movie : movies) {
            out.println("<movie>");
            out.println(
                String.format("<title>%s</title>", XMLUtil.escapeXML(movie.getMovieTitle(), true)));
            out.println(String.format("<year>%s</year>", movie.getYear()));
            out.println(String.format("<duration>%s</duration>", movie.getDuration()));
            out.println("</movie>");
            item = new ListItem(movie.getMovieTitle());
            list.add(item);
        }
        document.add(list);
        out.print("</movies>");
        out.flush();
        out.close();
        PdfFileSpecification fs
          = PdfFileSpecification.fileEmbedded(writer,
                  null, "kubrick.xml", txt.toByteArray());
        writer.addFileAttachment(fs);
        // step 5
        document.close();
        return baos.toByteArray();
    }
    
    /**
     * Extracts document level attachments
     * @param filename     a file from which document level attachments will be extracted
     * @throws IOException
     */
    public void extractDocLevelAttachments(String filename) throws IOException {
        PdfReader reader = new PdfReader(filename);
        PdfDictionary root = reader.getCatalog();
        PdfDictionary documentnames = root.getAsDict(PdfName.NAMES);
        PdfDictionary embeddedfiles = documentnames.getAsDict(PdfName.EMBEDDEDFILES);
        PdfArray filespecs = embeddedfiles.getAsArray(PdfName.NAMES);
        PdfDictionary filespec;
        PdfDictionary refs;
        FileOutputStream fos;
        PRStream stream;
        for (int i = 0; i < filespecs.size(); ) {
          filespecs.getAsString(i++);
          filespec = filespecs.getAsDict(i++);
          refs = filespec.getAsDict(PdfName.EF);
          for (PdfName key : refs.getKeys()) {
            fos = new FileOutputStream(String.format(PATH, filespec.getAsString(key).toString()));
            stream = (PRStream) PdfReader.getPdfObject(refs.getAsIndirectObject(key));
            fos.write(PdfReader.getStreamBytes(stream));
            fos.flush();
            fos.close();
          }
        }
    }

    /**
     * Main method.
     *
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException
     * @throws SQLException
     */
    public static void main(String[] args) throws IOException, DocumentException, SQLException {
        KubrickDocumentary kubrick = new KubrickDocumentary();
        FileOutputStream os = new FileOutputStream(RESULT);
        os.write(kubrick.createPdf());
        os.flush();
        os.close();
        kubrick.extractDocLevelAttachments(RESULT);
    }
}
