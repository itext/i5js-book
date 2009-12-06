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

import com.lowagie.filmfestival.Movie;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocListener;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontProvider;
import com.itextpdf.text.Image;
import com.itextpdf.text.html.simpleparser.ChainedProperties;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.html.simpleparser.ImageProvider;
import com.itextpdf.text.html.simpleparser.StyleSheet;
import com.itextpdf.text.pdf.PdfWriter;

public class HtmlMovies2 extends HtmlMovies1 {

    public static class MyImageFactory implements ImageProvider {
        @SuppressWarnings("unchecked")
        public Image getImage(String src, HashMap h,
                ChainedProperties cprops, DocListener doc) {
            try {
                return Image.getInstance(String.format("resources/posters/%s",
                        src.substring(src.lastIndexOf("/") + 1)));
            } catch (DocumentException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }    
    }
    
    public static class MyFontFactory implements FontProvider {
        public Font getFont(String fontname,
                String encoding, boolean embedded, float size,
                int style, BaseColor color) {
            return new Font(Font.TIMES_ROMAN, size, style, color);
        }

        public boolean isRegistered(String fontname) {
            return false;
        }
    }

    /** The resulting HTML file. */
    public static final String HTML = "results/part3/chapter09/movies_2.html";
    /** The resulting PDF file. */
    public static final String PDF = "results/part3/chapter09/html_movies_2.pdf";
    /** Another resulting PDF file. */
    public static final String RESULT = "results/part3/chapter09/html_movies_3.pdf";
    
    public static void main(String[] args) throws IOException, DocumentException, SQLException {
        HtmlMovies2 movies = new HtmlMovies2();
        
        StyleSheet styles = new StyleSheet();
        styles.loadTagStyle("ul", "indent", "10");
        styles.loadTagStyle("li", "leading", "14");
        styles.loadStyle("country", "i", "");
        styles.loadStyle("country", "color", "#008080");
        styles.loadStyle("director", "b", "");
        styles.loadStyle("director", "color", "midnightblue");
        movies.setStyles(styles);
        
        HashMap<String,Object> map = new HashMap<String, Object>();
        map.put("font_factory", new MyFontFactory());
        map.put("img_provider", new MyImageFactory());
        movies.setProviders(map);
        
        movies.createHtmlAndPdf(HTML, PDF);
        movies.createPdf(RESULT);
    }
    
    @SuppressWarnings("unchecked")
    public void createPdf(String filename) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(RESULT));
        document.open();
        List<Element> objects = HTMLWorker.parseToList(new FileReader(HTML), null, providers);
        for (Element element : objects) {
            document.add(element);
        }
        document.close();
    }
    
    public String createHtmlSnippet(Movie movie) {
        StringBuffer buf = new StringBuffer("<table width=\"500\">\n<tr>\n");
        buf.append("\t<td><img src=\"../../../resources/posters/");
        buf.append(movie.getImdb());
        buf.append(".jpg\" /></td>\t<td>\n");
        buf.append(super.createHtmlSnippet(movie));
        buf.append("\t</ul>\n\t</td>\n</tr>\n</table>");
        return buf.toString();
    }
    
    
}
