/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part4.chapter15;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfStructureElement;

public class StructureParser extends DefaultHandler {

    /** The top element in the PDF structure */
    protected PdfStructureElement top;
    /** The list of structure elements */
    protected List<PdfStructureElement> elements;

    /** Creates a parser that will parse an XML file into a structure tree. */
    public StructureParser(PdfStructureElement top, List<PdfStructureElement> elements) {
        this.top = top;
        this.elements = elements;
    }
    
    /**
     * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
     *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
     */
    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {
        if ("chapter".equals(qName)) return;
        elements.add(new PdfStructureElement(top, new PdfName(qName)));
    }
}
