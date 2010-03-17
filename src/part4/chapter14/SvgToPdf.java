/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */
package part4.chapter14;

import java.awt.Graphics2D;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.DocumentLoader;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.UserAgent;
import org.apache.batik.bridge.UserAgentAdapter;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.svg.SVGDocument;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

public class SvgToPdf {
    /** The resulting PDF. */
    public static final String RESULT = "results/part4/chapter14/foobar.pdf";
    /** The map. */
    public static final String CITY = "resources/xml/foobarcity.svg";
    /** The map. */
    public static final String STREETS = "resources/xml/foobarstreets.svg";
    
    protected SAXSVGDocumentFactory factory;
    protected BridgeContext ctx;
    protected GVTBuilder builder;
    public SvgToPdf() {
		String parser = XMLResourceDescriptor.getXMLParserClassName();
		factory = new SAXSVGDocumentFactory(parser);
		
		UserAgent userAgent = new UserAgentAdapter();
		DocumentLoader loader = new DocumentLoader(userAgent);
		ctx = new BridgeContext(userAgent, loader);
		ctx.setDynamicState(BridgeContext.DYNAMIC);

		builder = new GVTBuilder();
    }
	
	public void drawSvg(PdfTemplate map, String resource) throws IOException {
		Graphics2D g2d = map.createGraphics(6000, 6000);
		SVGDocument city = factory.createSVGDocument(new File(resource).toURL()
				.toString());
		GraphicsNode mapGraphics = builder.build(ctx, city);
		mapGraphics.paint(g2d);
		g2d.dispose();
	}
    
    /**
     * Creates a PDF document.
     * @param filename the path to the new PDF document
     * @throws DocumentException 
     * @throws IOException
     * @throws SQLException 
     */
	public void createPdf(String filename) throws IOException, DocumentException {
		Document document = new Document(new Rectangle(6000, 6000));
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(RESULT));
		document.open();
		PdfContentByte cb = writer.getDirectContent();
		PdfTemplate map = cb.createTemplate(6000, 6000);
		drawSvg(map, CITY);
		cb.addTemplate(map, 0, 0);
		PdfTemplate streets = cb.createTemplate(6000, 6000);
		drawSvg(streets, STREETS);
		cb.addTemplate(streets, 0, 0);
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
    	new SvgToPdf().createPdf(RESULT);
    }
}
