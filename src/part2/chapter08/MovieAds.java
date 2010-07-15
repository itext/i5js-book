/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part2.chapter08;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Director;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Utilities;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSmartCopy;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PushbuttonField;
import com.itextpdf.text.pdf.TextField;
import com.itextpdf.text.pdf.AcroFields.FieldPosition;

public class MovieAds {

    /** The resulting PDF. */
    public static final String RESULT = "results/part2/chapter08/festival.pdf";
    /** The resulting PDF: a small template for an individual ad. */
    public static final String TEMPLATE = "results/part2/chapter08/template.pdf";
    /** The source PDF file: the template for the complete ad. */
    public static final String RESOURCE = "resources/pdfs/movie_overview.pdf";
    /** Path to the movie posters */
    public static final String IMAGE = "resources/posters/%s.jpg";
    /** Field name for the poster */
    public static final String POSTER = "poster";
    /** Field name for the text */
    public static final String TEXT = "text";
    /** Field name for the year */
    public static final String YEAR = "year";

    /**
     * Main method
     * @param args no arguments needed
     * @throws IOException
     * @throws DocumentException
     * @throws SQLException
     */
    public static void main(String[] args) throws IOException, DocumentException, SQLException {
        MovieAds movieAds = new MovieAds();
        // create a template that will be used for the ads
        movieAds.createTemplate(TEMPLATE);
        // open the connection to the database
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        // step 1
        Document document = new Document();
        // step 2
        PdfSmartCopy copy = new PdfSmartCopy(document, new FileOutputStream(RESULT));
        // step 3
        document.open();
        // step 4
        PdfReader reader;
        PdfStamper stamper = null;
        ByteArrayOutputStream baos = null;
        AcroFields form = null;
        int count = 0;
        for (Movie movie : PojoFactory.getMovies(connection)) {
            if (count == 0) {
                baos = new ByteArrayOutputStream();
                reader = new PdfReader(RESOURCE);
                stamper = new PdfStamper(reader, baos);
                stamper.setFormFlattening(true);
                form = stamper.getAcroFields();
            }
            count++;
            PdfReader ad = new PdfReader(movieAds.fillTemplate(TEMPLATE, movie));
            PdfImportedPage page = stamper.getImportedPage(ad, 1);
            PushbuttonField bt = form.getNewPushbuttonFromField("movie_" + count);
            bt.setLayout(PushbuttonField.LAYOUT_ICON_ONLY);
            bt.setProportionalIcon(true);
            bt.setTemplate(page);
            form.replacePushbuttonField("movie_" + count, bt.getField());
            if (count == 16) {
                stamper.close();
                reader = new PdfReader(baos.toByteArray());
                copy.addPage(copy.getImportedPage(reader, 1));
                count = 0;
            }
        }
        if (count > 0) {
            stamper.close();
            reader = new PdfReader(baos.toByteArray());
            copy.addPage(copy.getImportedPage(reader, 1));
        }
        // step 5
        document.close();
        // close the database connection
        connection.close();
    }
    
    /**
     * Create a small template that will be used for an individual ad.
     * @param filename the filename of the add
     * @throws IOException
     * @throws DocumentException
     */
    public void createTemplate(String filename) throws IOException, DocumentException {
    	// step 1
        Document document = new Document(
            new Rectangle(Utilities.millimetersToPoints(35), Utilities.millimetersToPoints(50)));
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
        writer.setViewerPreferences(PdfWriter.PageLayoutSinglePage);
        // step 3
        document.open();
        // step 4
        PushbuttonField poster = new PushbuttonField(writer, new Rectangle(
                Utilities.millimetersToPoints(0), Utilities.millimetersToPoints(25),
                Utilities.millimetersToPoints(35), Utilities.millimetersToPoints(50)),
                POSTER);
        poster.setBackgroundColor(new GrayColor(0.4f));
        writer.addAnnotation(poster.getField());
        TextField movie = new TextField(writer, new Rectangle(
                Utilities.millimetersToPoints(0), Utilities.millimetersToPoints(7),
                Utilities.millimetersToPoints(35), Utilities.millimetersToPoints(25)),
                TEXT);
        movie.setOptions(TextField.MULTILINE);
        writer.addAnnotation(movie.getTextField());
        TextField screening = new TextField(writer, new Rectangle(
                Utilities.millimetersToPoints(0), Utilities.millimetersToPoints(0),
                Utilities.millimetersToPoints(35), Utilities.millimetersToPoints(7)),
                YEAR);
        screening.setAlignment(Element.ALIGN_CENTER);
        screening.setBackgroundColor(new GrayColor(0.4f));
        screening.setTextColor(GrayColor.GRAYWHITE);
        writer.addAnnotation(screening.getTextField());
        // step 5
        document.close();
    }
    
    /**
     * Fill out the small template with information about the movie.
     * @param filename the template for an individual ad
     * @param movie the movie that needs to be in the ad
     * @return a byte[] containing an individual ad
     * @throws IOException
     * @throws DocumentException
     */
    public byte[] fillTemplate(String filename, Movie movie) throws IOException, DocumentException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfReader reader = new PdfReader(filename);
        PdfStamper stamper = new PdfStamper(reader, baos);
        AcroFields form = stamper.getAcroFields();
        // change the background color of the poster and add a new icon
        BaseColor color = WebColors.getRGBColor("#" + movie.getEntry().getCategory().getColor());
        PushbuttonField bt = form.getNewPushbuttonFromField(POSTER);
        bt.setLayout(PushbuttonField.LAYOUT_ICON_ONLY);
        bt.setProportionalIcon(true);
        bt.setImage(Image.getInstance(String.format(IMAGE, movie.getImdb())));
        bt.setBackgroundColor(color);
        form.replacePushbuttonField(POSTER, bt.getField());
        // write the text using the appropriate font size
        PdfContentByte canvas = stamper.getOverContent(1);
        float size = 12;
        FieldPosition f = form.getFieldPositions(TEXT).get(0);
        while (addParagraph(createMovieParagraph(movie, size),
                canvas, f, true) && size > 6) {
            size -= 0.2;
        }
        addParagraph(createMovieParagraph(movie, size), canvas, f, false);
        // fill out the year and change the background color
        form.setFieldProperty(YEAR, "bgcolor", color, null);
        form.setField(YEAR, String.valueOf(movie.getYear()));
        // flatten the form and close the stamper
        stamper.setFormFlattening(true);
        stamper.close();
        return baos.toByteArray();
    }
    
    /**
     * Add a paragraph at an absolute position.
     * @param p the paragraph that needs to be added
     * @param canvas the canvas on which the paragraph needs to be drawn
     * @param f the field position
     * @param simulate does the paragraph need to be added for real?
     * @return true if the paragraph didn't fit the rectangle
     * @throws DocumentException
     */
    public boolean addParagraph(Paragraph p, PdfContentByte canvas, FieldPosition f, boolean simulate)
        throws DocumentException {
        ColumnText ct = new ColumnText(canvas);
        ct.setSimpleColumn(
            f.position.getLeft(2), f.position.getBottom(2), f.position.getRight(2), f.position.getTop());
        ct.addElement(p);
        return ColumnText.hasMoreText(ct.go(simulate));
    }
    
    /**
     * Creates a paragraph containing info about a movie
     * @param movie the Movie pojo
     * @param fontsize the font size
     * @return a Paragraph object
     */
    public Paragraph createMovieParagraph(Movie movie, float fontsize) {
        Font normal = new Font(FontFamily.HELVETICA, fontsize);
        Font bold = new Font(FontFamily.HELVETICA, fontsize, Font.BOLD);
        Font italic = new Font(FontFamily.HELVETICA, fontsize, Font.ITALIC);
        Paragraph p = new Paragraph(fontsize * 1.2f);
        p.setFont(normal);
        p.setAlignment(Element.ALIGN_JUSTIFIED);
        p.add(new Chunk(movie.getMovieTitle(), bold));
        if (movie.getOriginalTitle() != null) {
            p.add(" ");
            p.add(new Chunk(movie.getOriginalTitle(), italic));
        }
        p.add(new Chunk(String.format("; run length: %s", movie.getDuration()), normal));
        p.add(new Chunk("; directed by:", normal));
        for (Director director : movie.getDirectors()) {
            p.add(" ");
            p.add(director.getGivenName());
            p.add(", ");
            p.add(director.getName());
        }
        return p;
    }
 
}
