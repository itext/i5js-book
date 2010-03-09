package part4.chapter14;

import java.awt.Graphics2D;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

public class PearToPdf {
    /** The resulting PDF. */
    public static final String RESULT = "results/part4/chapter14/pear.pdf";

    /**
     * Creates a PDF document.
     * @param filename the path to the new PDF document
     * @throws DocumentException 
     * @throws IOException
     */
	public void createPdf(String filename) throws IOException, DocumentException {
		Document document = new Document(new Rectangle(150, 150));
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
		document.open();
		PdfContentByte canvas = writer.getDirectContent();
		Graphics2D g2 = canvas.createGraphics(150, 150);
		PearExample pear = new PearExample();
		pear.paint(g2);
		g2.dispose();
		document.close();
	}
    /**
     * Main method.
     *
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException
     */
    public static void main(String[] args) throws IOException, DocumentException {
    	new PearToPdf().createPdf(RESULT);
    }
}
