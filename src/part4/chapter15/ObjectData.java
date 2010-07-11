/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */
package part4.chapter15;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.PdfStructureElement;
import com.itextpdf.text.pdf.PdfStructureTreeRoot;
import com.itextpdf.text.pdf.PdfWriter;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Director;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;

public class ObjectData {

    /** The resulting PDF. */
    public static final String RESULT = "results/part4/chapter15/objectdata.pdf";
    /** Path to the resources. */
    public static final String RESOURCE
        = "resources/posters/%s.jpg";
    /** SQL statement to get selected directors */
    public static final String SELECTDIRECTORS =
        "SELECT DISTINCT d.id, d.name, d.given_name, count(*) AS c "
        + "FROM film_director d, film_movie_director md "
        + "WHERE d.id = md.director_id AND d.id < 8 "
        + "GROUP BY d.id, d.name, d.given_name ORDER BY id";
    
    /**
     * Creates a PDF with information about the movies
     * @param    filename the name of the PDF file that will be created.
     * @throws    DocumentException 
     * @throws    IOException 
     * @throws    SQLException
     */
    public void createPdf(String filename)
        throws IOException, DocumentException, SQLException {
    	// step 1
        Document document = new Document();
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
        writer.setTagged();
        writer.setUserProperties(true);
        // step 3
        document.open();
        // step 4
        DatabaseConnection connection = new HsqldbConnection("filmfestival");

        PdfStructureTreeRoot tree = writer.getStructureTreeRoot();
        PdfStructureElement top = new PdfStructureElement(tree, new PdfName("Directors"));
        
        Map<Integer,PdfStructureElement> directors = new HashMap<Integer,PdfStructureElement>();
        Statement stm = connection.createStatement();
        ResultSet rs = stm.executeQuery(SELECTDIRECTORS);
        int id;
        Director director;
        PdfStructureElement e;
        while (rs.next()) {
            id = rs.getInt("id");
            director = PojoFactory.getDirector(rs);
            e = new PdfStructureElement(top, new PdfName("director" + id));
            PdfDictionary userproperties = new PdfDictionary();
            userproperties.put(PdfName.O, PdfName.USERPROPERTIES);
            PdfArray properties = new PdfArray();
            PdfDictionary property1 = new PdfDictionary();
            property1.put(PdfName.N, new PdfString("Name"));
            property1.put(PdfName.V, new PdfString(director.getName()));            
            properties.add(property1);
            PdfDictionary property2 = new PdfDictionary();
            property2.put(PdfName.N, new PdfString("Given name"));
            property2.put(PdfName.V, new PdfString(director.getGivenName()));            
            properties.add(property2);
            PdfDictionary property3 = new PdfDictionary();
            property3.put(PdfName.N, new PdfString("Posters"));
            property3.put(PdfName.V, new PdfNumber(rs.getInt("c")));            
            properties.add(property3);
            userproperties.put(PdfName.P, properties);
            e.put(PdfName.A, userproperties);
            directors.put(id, e);
        }
        
        Map<Movie,Integer> map = new TreeMap<Movie,Integer>();
        for (int i = 1; i < 8; i++) {
            List<Movie> movies = PojoFactory.getMovies(connection, i);
            for (Movie movie : movies) {
                map.put(movie, i);
            }
        }
        
        PdfContentByte canvas = writer.getDirectContent();
        Image img;
        float x = 11.5f;
        float y = 769.7f;
        for (Map.Entry<Movie,Integer> entry : map.entrySet()) {
            img = Image.getInstance(String.format(RESOURCE, entry.getKey().getImdb()));
            img.scaleToFit(1000, 60);
            img.setAbsolutePosition(x + (45 - img.getScaledWidth()) / 2, y);
            canvas.beginMarkedContentSequence(directors.get(entry.getValue()));
            canvas.addImage(img);
            canvas.endMarkedContentSequence();
            x += 48;
            if (x > 578) {
                x = 11.5f;
                y -= 84.2f;
            }
        }
        // step 5
        document.close();

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
        throws IOException, SQLException, DocumentException {
        new ObjectData().createPdf(RESULT);
    }
}
