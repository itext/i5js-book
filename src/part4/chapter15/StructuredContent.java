/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part4.chapter15;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfStructureElement;
import com.itextpdf.text.pdf.PdfStructureTreeRoot;
import com.itextpdf.text.pdf.PdfWriter;

public class StructuredContent {

    /** The resulting PDF. */
    public static final String RESULT = "results/part4/chapter15/moby.pdf";
    /** An XML file that will be converted to PDF. */
    public static final String RESOURCE = "resources/xml/moby.xml";

    /**
     * Main method.
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public static void main(String[] args)
        throws IOException, DocumentException, SAXException, ParserConfigurationException {
        Document document = new Document(PageSize.A5);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(RESULT));
        writer.setTagged();
        document.open();
        PdfStructureTreeRoot root = writer.getStructureTreeRoot();
        root.mapRole(new PdfName("chapter"), PdfName.SECT);
        root.mapRole(new PdfName("title"), PdfName.H);
        root.mapRole(new PdfName("para"), PdfName.P);
        PdfStructureElement top = new PdfStructureElement(root, new PdfName("chapter"));
        SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
        List<PdfStructureElement> elements = new ArrayList<PdfStructureElement>();
        parser.parse(
            new InputSource(new FileInputStream(RESOURCE)),
            new StructureParser(top, elements));
        parser.parse(
            new InputSource(new FileInputStream(RESOURCE)),
            new ContentParser(document, writer, elements));
        document.close();
    }
}
