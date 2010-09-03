/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */
package part4.chapter15;

import java.awt.Graphics2D;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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
    public static final String RESULT = "results/part4/chapter15/foobar.pdf";
    /** The map (shapes). */
    public static final String CITY = "resources/xml/foobarcity.svg";
    /** The map (text = street names in English). */
    public static final String STREETS = "resources/xml/foobarstreets.svg";
    
    /** The SVG document factory. */
    protected SAXSVGDocumentFactory factory;
    /** The SVG bridge context. */
    protected BridgeContext ctx;
    /** The GVT builder */
    protected GVTBuilder builder;
    
    /** Creates an SvgToPdf object. */
    public SvgToPdf() {
        String parser = XMLResourceDescriptor.getXMLParserClassName();
        factory = new SAXSVGDocumentFactory(parser);
        
        UserAgent userAgent = new UserAgentAdapter();
        DocumentLoader loader = new DocumentLoader(userAgent);
        ctx = new BridgeContext(userAgent, loader);
        ctx.setDynamicState(BridgeContext.DYNAMIC);

        builder = new GVTBuilder();
    }
    
    /**
     * Draws an SVG file to a PdfTemplate.
     * @param map      the template to which the SVG has to be drawn.
     * @param resource the SVG content.
     * @throws IOException
     */
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
        // step 1
    	Document document = new Document(new Rectangle(6000, 6000));
        // step 2
    	PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(RESULT));
        // step 3
    	document.open();
    	// step 4
        PdfContentByte cb = writer.getDirectContent();
        PdfTemplate map = cb.createTemplate(6000, 6000);
        drawSvg(map, CITY);
        cb.addTemplate(map, 0, 0);
        PdfTemplate streets = cb.createTemplate(6000, 6000);
        drawSvg(streets, STREETS);
        cb.addTemplate(streets, 0, 0);
        // step 5
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
        new SvgToPdf().createPdf(RESULT);
    }
}
