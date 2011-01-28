/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part3.chapter09;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lowagie.filmfestival.Movie;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocListener;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontProvider;
import com.itextpdf.text.Image;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.html.simpleparser.ChainedProperties;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.html.simpleparser.ImageProvider;
import com.itextpdf.text.html.simpleparser.StyleSheet;
import com.itextpdf.text.pdf.PdfWriter;

public class HtmlMovies2 extends HtmlMovies1 {

    /**
     * Inner class implementing the ImageProvider class.
     * This is needed if you want to resolve the paths to images.
     */
    public static class MyImageFactory implements ImageProvider {
        public Image getImage(String src, Map<String, String> h,
                ChainedProperties cprops, DocListener doc) {
            try {
                return Image.getInstance(
                    String.format("resources/posters/%s",
                        src.substring(src.lastIndexOf("/") + 1)));
            } catch (DocumentException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }    
    }

    /**
     * Inner class implementing the FontProvider class.
     * This is needed if you want to select the correct fonts.
     */
    public static class MyFontFactory implements FontProvider {
        public Font getFont(String fontname,
                String encoding, boolean embedded, float size,
                int style, BaseColor color) {
            return new Font(FontFamily.TIMES_ROMAN, size, style, color);
        }

        public boolean isRegistered(String fontname) {
            return false;
        }
    }

    /** The resulting HTML file. */
    public static final String HTML
        = "results/part3/chapter09/movies_2.html";
    /** The resulting PDF file. */
    public static final String RESULT1
        = "results/part3/chapter09/html_movies_2.pdf";
    /** Another resulting PDF file. */
    public static final String RESULT2
        = "results/part3/chapter09/html_movies_3.pdf";
    
    /**
     * Main method.
     * 
     * @param args no arguments needed
     * @throws IOException
     * @throws DocumentException
     * @throws SQLException
     */
    public static void main(String[] args)
        throws IOException, DocumentException, SQLException {
        HtmlMovies2 movies = new HtmlMovies2();
        // create a StyleSheet
        StyleSheet styles = new StyleSheet();
        styles.loadTagStyle("ul", "indent", "10");
        styles.loadTagStyle("li", "leading", "14");
        styles.loadStyle("country", "i", "");
        styles.loadStyle("country", "color", "#008080");
        styles.loadStyle("director", "b", "");
        styles.loadStyle("director", "color", "midnightblue");
        movies.setStyles(styles);
        // create extra properties
        HashMap<String,Object> map = new HashMap<String, Object>();
        map.put(HTMLWorker.FONT_PROVIDER, new MyFontFactory());
        map.put(HTMLWorker.IMG_PROVIDER, new MyImageFactory());
        movies.setProviders(map);
        // creates HTML and PDF (reusing a method from the super class)
        movies.createHtmlAndPdf(HTML, RESULT1);
        // creates another PDF file
        movies.createPdf(RESULT2);
    }

    /**
     * Creates a PDF document.
     * @param filename the path to the new PDF document
     * @throws    DocumentException 
     * @throws    IOException
     */
    public void createPdf(String filename)
        throws IOException, DocumentException {
        // step 1
        Document document = new Document();
        // step 2
        PdfWriter.getInstance(document, new FileOutputStream(RESULT2));
        // step 3
        document.open();
        // step 4
        List<Element> objects
            = HTMLWorker.parseToList(new FileReader(HTML), null, providers);
        for (Element element : objects) {
            document.add(element);
        }
        // step 5
        document.close();
    }

    /**
     * Creates an HTML snippet with info about a movie.
     * @param movie the movie for which we want to create HTML
     * @return a String with HTML code
     */
    public String createHtmlSnippet(Movie movie) {
        StringBuffer buf = new StringBuffer("<table width=\"500\">\n<tr>\n");
        buf.append("\t<td><img src=\"../../../resources/posters/");
        buf.append(movie.getImdb());
        buf.append(".jpg\" /></td>\t<td>\n");
        buf.append(super.createHtmlSnippet(movie));
        buf.append("\t</td>\n</tr>\n</table>");
        return buf.toString();
    }

}
