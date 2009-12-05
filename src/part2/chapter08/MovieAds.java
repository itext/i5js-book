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
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Utilities;
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
import com.itextpdf.text.pdf.RGBColor;
import com.itextpdf.text.pdf.TextField;

public class MovieAds {

    public static final String RESULT = "results/part2/chapter08/festival.pdf";
    public static final String TEMPLATE = "results/part2/chapter08/template.pdf";
    public static final String RESOURCE = "resources/pdfs/movie_overview.pdf";
    public static final String IMAGE = "resources/posters/%s.jpg";
    public static final String POSTER = "poster";
    public static final String TEXT = "text";
    public static final String YEAR = "year";

    public static void main(String[] args) throws IOException, DocumentException, SQLException {
        MovieAds movieAds = new MovieAds();
        movieAds.createTemplate(TEMPLATE);
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        Document document = new Document();
        PdfSmartCopy copy = new PdfSmartCopy(document, new FileOutputStream(RESULT));
        document.open();
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
        document.close();
        connection.close();
    }
    
    public byte[] fillTemplate(String filename, Movie movie) throws IOException, DocumentException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfReader reader = new PdfReader(TEMPLATE);
        PdfStamper stamper = new PdfStamper(reader, baos);
        AcroFields form = stamper.getAcroFields();
        RGBColor color = WebColors.getRGBColor("#" + movie.getEntry().getCategory().getColor());
        PushbuttonField bt = form.getNewPushbuttonFromField(POSTER);
        bt.setLayout(PushbuttonField.LAYOUT_ICON_ONLY);
        bt.setProportionalIcon(true);
        bt.setImage(Image.getInstance(String.format(IMAGE, movie.getImdb())));
        bt.setBackgroundColor(color);
        form.replacePushbuttonField(POSTER, bt.getField());
        
        PdfContentByte canvas = stamper.getOverContent(1);
        float size = 12;
        float[] f = form.getFieldPositions(TEXT);
        while (addParagraph(createMovieParagraph(movie, size),
                canvas, f, true) && size > 6) {
            size -= 0.2;
        }
        addParagraph(createMovieParagraph(movie, size), canvas, f, false);
        
        

        form.setField(YEAR, String.valueOf(movie.getYear()));
        form.setFieldProperty(YEAR, "bgcolor", color, null);
        form.setField(YEAR, String.valueOf(movie.getYear()));
        stamper.setFormFlattening(true);
        stamper.close();
        return baos.toByteArray();
    }
    
    public boolean addParagraph(Paragraph p, PdfContentByte canvas, float[] f, boolean simulate) throws DocumentException {
        ColumnText ct = new ColumnText(canvas);
        ct.setSimpleColumn(f[1] + 2, f[2] + 2, f[3] - 2, f[4]);
        ct.addElement(p);
        return ColumnText.hasMoreText(ct.go(simulate));
    }
    
    public Paragraph createMovieParagraph(Movie movie, float fontsize) {
        Font normal = new Font(Font.HELVETICA, fontsize);
        Font bold = new Font(Font.HELVETICA, fontsize, Font.BOLD);
        Font italic = new Font(Font.HELVETICA, fontsize, Font.ITALIC);
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
    
    public void createTemplate(String filename) throws IOException, DocumentException {
        Document document = new Document(new Rectangle(Utilities.millimetersToPoints(35), Utilities.millimetersToPoints(50)));
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
        writer.setViewerPreferences(PdfWriter.PageLayoutSinglePage);
        document.open();
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
        document.close();
    }
 
}
