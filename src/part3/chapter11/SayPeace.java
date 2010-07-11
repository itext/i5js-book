/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part3.chapter11;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class SayPeace extends DefaultHandler {

    /** The resulting PDF file. */
    public static final String RESULT
        = "results/part3/chapter11/say_peace.pdf";
    /** The XML file with the text. */
    public static final String RESOURCE
        = "resources/xml/say_peace.xml";
    
    /** The font that is used for the peace message. */
    public Font f;

    /** The document to which we are going to add our message. */
    protected Document document;

    /** The StringBuffer that holds the characters. */
    protected StringBuffer buf = new StringBuffer();
    
    /** The table that holds the text. */
    protected PdfPTable table;
    
    /** The current cell. */
    protected PdfPCell cell;

    /**
     * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
     *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
     */
    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {
        if ("message".equals(qName)) {
            buf = new StringBuffer();
            cell = new PdfPCell();
            cell.setBorder(PdfPCell.NO_BORDER);
            if ("RTL".equals(attributes.getValue("direction"))) {
                cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            }
        }
        else if ("pace".equals(qName)) {
            table = new PdfPTable(1);
            table.setWidthPercentage(100);
        }
    }

    /**
     * @see org.xml.sax.ContentHandler#endElement(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        if ("big".equals(qName)) {
            Chunk bold = new Chunk(strip(buf), f);
            bold.setTextRenderMode(
                    PdfContentByte.TEXT_RENDER_MODE_FILL_STROKE, 0.5f,
                    GrayColor.GRAYBLACK);
            Paragraph p = new Paragraph(bold);
            p.setAlignment(Element.ALIGN_LEFT);
            cell.addElement(p);
            buf = new StringBuffer();
        }
        else if ("message".equals(qName)) {
            Paragraph p = new Paragraph(strip(buf), f);
            p.setAlignment(Element.ALIGN_LEFT);
            cell.addElement(p);
            table.addCell(cell);
            buf = new StringBuffer();
        }
        else if ("pace".equals(qName)) {
            try {
                document.add(table);
            } catch (DocumentException e) {
                throw new ExceptionConverter(e);
            }
        }
    }

    /**
     * @see org.xml.sax.ContentHandler#characters(char[], int, int)
     */
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        buf.append(ch, start, length);
    }

    /**
     * Replaces all the newline characters by a space.
     * 
     * @param buf
     *            the original StringBuffer
     * @return a String without newlines
     */
    protected String strip(StringBuffer buf) {
        int pos;
        while ((pos = buf.indexOf("\n")) != -1)
            buf.replace(pos, pos + 1, " ");
        while (buf.charAt(0) == ' ')
            buf.deleteCharAt(0);
        return buf.toString();
    }

    /**
     * Creates the handler to read the peace message.
     * 
     * @param document
     * @param is
     * @throws FactoryConfigurationError
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws FactoryConfigurationError
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws DocumentException
     */
    public SayPeace(Document document, InputSource is)
            throws ParserConfigurationException, SAXException,
            FactoryConfigurationError, DocumentException, IOException {
        this.document = document;
        f = new Font(BaseFont.createFont("c:/windows/fonts/arialuni.ttf",
                BaseFont.IDENTITY_H, BaseFont.EMBEDDED));
        SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
        parser.parse(is, this);
    }

    /**
     * Generates a PDF with a Peace message in English, Arabic and Hebrew.
     * 
     * @param args no arguments needed
     */
    public static void main(String[] args) {
        // step 1:
        Document document = new Document(PageSize.A4);
        try {
            // step 2:
            PdfWriter.getInstance(document, new FileOutputStream(RESULT));
            // step 3:
            document.open();
            // step 4:
            new SayPeace(document,
                new InputSource(new FileInputStream(RESOURCE)));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        // step 5: we close the document
        document.close();
    }
}