package part4.chapter15;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfStructureElement;

public class StructureParser extends DefaultHandler {
	protected PdfStructureElement top;
	protected List<PdfStructureElement> elements;

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
