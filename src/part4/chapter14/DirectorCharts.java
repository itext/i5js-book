/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */
package part4.chapter14;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Director;
import com.lowagie.filmfestival.PojoFactory;

public class DirectorCharts {
    /** The resulting PDF. */
    public static final String RESULT = "results/part4/chapter14/director_charts.pdf";
    /** A query that needs to be visualized in a chart. */
    public static final String QUERY1 =
        "SELECT DISTINCT d.id, d.name, d.given_name, count(*) AS c "
            + "FROM film_director d, film_movie_director md "
            + "WHERE d.id = md.director_id "
            + "GROUP BY d.id, d.name, d.given_name ORDER BY c DESC, name LIMIT 9";
    /** A query that needs to be visualized in a chart. */
    public static final String QUERY2 =
        "SELECT DISTINCT d.id, d.name, d.given_name, count(*) AS c "
            + "FROM film_director d, film_movie_director md "
            + "WHERE d.id = md.director_id "
            + "GROUP BY d.id, d.name, d.given_name ORDER BY c DESC, name LIMIT 17";
    
    /**
     * Creates a PDF document.
     * @param filename the path to the new PDF document
     * @throws DocumentException 
     * @throws IOException
     * @throws SQLException 
     */
    public void createPdf(String filename) throws IOException, DocumentException, SQLException {
    	// step 1
        Document document = new Document();
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(RESULT));
        // step 3
        document.open();
        // step 4
        PdfContentByte cb = writer.getDirectContent();
        float width = PageSize.A4.getWidth();
        float height = PageSize.A4.getHeight() / 2;
        // Pie chart
        PdfTemplate pie = cb.createTemplate(width, height);
        Graphics2D g2d1 = pie.createGraphics(width, height);
        Rectangle2D r2d1 = new Rectangle2D.Double(0, 0, width, height);
        getPieChart().draw(g2d1, r2d1);
        g2d1.dispose();
        cb.addTemplate(pie, 0, height);
        // Bar chart
        PdfTemplate bar = cb.createTemplate(width, height);
        Graphics2D g2d2 = bar.createGraphics(width, height);
        Rectangle2D r2d2 = new Rectangle2D.Double(0, 0, width, height);
        getBarChart().draw(g2d2, r2d2);
        g2d2.dispose();
        cb.addTemplate(bar, 0, 0);
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
    public static void main(String[] args) throws IOException, DocumentException, SQLException {
        new DirectorCharts().createPdf(RESULT);
    }
    
    /**
     * Gets an example pie chart.
     * 
     * @return a pie chart
     * @throws SQLException 
     * @throws IOException 
     */
    public static JFreeChart getPieChart() throws SQLException, IOException {
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        Statement stm = connection.createStatement();
        ResultSet rs = stm.executeQuery(QUERY1);
        DefaultPieDataset dataset = new DefaultPieDataset();
        Director director;
        while (rs.next()) {
            director = PojoFactory.getDirector(rs);
            dataset.setValue(
                String.format("%s, %s", director.getName(), director.getGivenName()),
                rs.getInt("c"));
        }
        connection.close();
        return ChartFactory.createPieChart("Movies / directors", dataset,
                true, true, false);
    }
    
    /**
     * Gets an example bar chart.
     * 
     * @return a bar chart
     * @throws SQLException 
     * @throws IOException 
     */
    public static JFreeChart getBarChart() throws SQLException, IOException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        Statement stm = connection.createStatement();
        ResultSet rs = stm.executeQuery(QUERY2);
        Director director;
        while (rs.next()) {
            director = PojoFactory.getDirector(rs);
            dataset.setValue(
                rs.getInt("c"),
                "movies",
                String.format("%s, %s", director.getName(), director.getGivenName()));
        }
        connection.close();
        return ChartFactory.createBarChart("Movies / directors", "Director",
                "# Movies", dataset, PlotOrientation.HORIZONTAL, false,
                true, false);
    }
}
