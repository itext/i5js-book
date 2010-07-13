/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part3.chapter11;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.itextpdf.text.Document;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.FontSelector;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class Peace extends DefaultHandler {

    /** The resulting PDF file. */
    public static final String RESULT
        = "results/part3/chapter11/peace.pdf";
    
    /** The XML file with the text. */
    public static final String RESOURCE
        = "resources/xml/peace.xml";
    
    /** Paths to and encodings of fonts we're going to use in this example */
    public static String[][] FONTS = {
        {"c:/windows/fonts/arialuni.ttf", BaseFont.IDENTITY_H},
        {"resources/fonts/abserif4_5.ttf", BaseFont.IDENTITY_H},
        {"resources/fonts/damase.ttf", BaseFont.IDENTITY_H},
        {"resources/fonts/fsex2p00_public.ttf", BaseFont.IDENTITY_H}
    };
    
    /** Holds he fonts that can be used for the peace message. */
    public FontSelector fs;

    /** The columns that contains the message. */
    protected PdfPTable table;

    /** The language. */
    protected String language;

    /** The countries. */
    protected String countries;

    /** Indicates when the text should be written from right to left. */
    protected boolean rtl;

    /** The StringBuffer that holds the characters. */
    protected StringBuffer buf = new StringBuffer();

    /**
     * Creates the handler for the pace.xml file.
     */
    public Peace() {
        fs = new FontSelector();
        for (int i = 0; i < FONTS.length; i++) {
            fs.addFont(FontFactory.getFont(FONTS[i][0], FONTS[i][1], BaseFont.EMBEDDED));
        }
        table = new PdfPTable(3);
        table.getDefaultCell().setPadding(3);
        table.getDefaultCell().setUseAscender(true);
        table.getDefaultCell().setUseDescender(true);
    }

    /**
     * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
     *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
     */
    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {
        if ("pace".equals(qName)) {
            buf = new StringBuffer();
            language = attributes.getValue("language");
            countries = attributes.getValue("countries");
            if ("RTL".equals(attributes.getValue("direction"))) {
                rtl = true;
            } else {
                rtl = false;
            }
        }
    }

    /**
     * @see org.xml.sax.ContentHandler#endElement(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        if ("pace".equals(qName)) {
            PdfPCell cell = new PdfPCell();
            cell.addElement(fs.process(buf.toString()));
            cell.setPadding(3);
            cell.setUseAscender(true);
            cell.setUseDescender(true);
            if (rtl) {
                cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            }
            table.addCell(language);
            table.addCell(cell);
            table.addCell(countries);
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
     * Returns the table.
     * 
     * @return the PdfPTable (get it after using the parser)
     */
    public PdfPTable getTable() {
        return table;
    }

    /**
     * This example reads a text file written in UTF-8. Each line is a pipe
     * delimited array containing the name of a language, the word 'peace'
     * written in that language
     * 
     * @param args
     *            no arguments needed
     */
    public static void main(String[] args) {
        // step 1
        Document doc = new Document(PageSize.A4.rotate());
        try {
            // step 2
            PdfWriter.getInstance(doc, new FileOutputStream(RESULT));
            // step 3
            doc.open();
            // step 4
            Peace p = new Peace();
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            parser.parse(new InputSource(new FileInputStream(RESOURCE)), p);
            doc.add(p.getTable());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // step 5
        doc.close();
    }
}