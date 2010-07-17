/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part4.chapter15;

import java.io.IOException;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfStructureElement;
import com.itextpdf.text.pdf.PdfWriter;

public class ContentParser extends DefaultHandler {

    /** The StringBuffer that holds the characters. */
    protected StringBuffer buf = new StringBuffer();

    /** The document to which content parsed form XML will be added. */
    protected Document document;
    /** The writer to which PDF syntax will be written. */
    protected PdfWriter writer;
    /** The canvas to which content will be written. */
    protected PdfContentByte canvas;
    /** A list with structure elements. */
    protected List<PdfStructureElement> elements;
    /** The current structure element during the parsing process. */
    protected PdfStructureElement current;
    /** The column to which content will be added. */
    protected ColumnText column;
    /** The font used when content elements are created. */
    protected Font font;
    
    /**
     * Creates a new ContentParser
     * @param document
     * @param writer
     * @param elements
     * @throws DocumentException
     * @throws IOException
     */
    public ContentParser(Document document, PdfWriter writer, List<PdfStructureElement> elements)
        throws DocumentException, IOException {
        this.document = document;
        this.writer = writer;
        canvas = writer.getDirectContent();
        column = new ColumnText(canvas);
        column.setSimpleColumn(36, 36, 384, 569);
        this.elements = elements;
        font = new Font(
            BaseFont.createFont("c:/windows/fonts/arial.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED), 12);
    }

    /**
     * @see org.xml.sax.ContentHandler#characters(char[], int, int)
     */
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        for (int i = start; i < start + length; i++) {
            if (ch[i] == '\n')
                buf.append(' ');
            else
                buf.append(ch[i]);
        }
    }

    /**
     * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
     *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
     */
    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {
        if ("chapter".equals(qName)) return;
        current = elements.get(0);
        elements.remove(0);
        canvas.beginMarkedContentSequence(current);
    }
    
    /**
     * @see org.xml.sax.ContentHandler#endElement(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        if ("chapter".equals(qName)) return;
        try {
            String s = buf.toString().trim();
            buf = new StringBuffer();
            if (s.length() > 0) {
                Paragraph p = new Paragraph(s, font);
                p.setAlignment(Element.ALIGN_JUSTIFIED);
                column.addElement(p);
                int status = column.go();
                while (ColumnText.hasMoreText(status)) {
                    canvas.endMarkedContentSequence();
                    document.newPage();
                    canvas.beginMarkedContentSequence(current);
                    column.setSimpleColumn(36, 36, 384, 569);
                    status = column.go();
                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        canvas.endMarkedContentSequence();
    }
}
