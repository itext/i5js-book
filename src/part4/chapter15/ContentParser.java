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

	protected Document document;
	protected PdfWriter writer;
	protected PdfContentByte canvas;
	protected List<PdfStructureElement> elements;
	protected PdfStructureElement current;
	protected ColumnText column;
	protected Font font;
	
	public ContentParser(Document document, PdfWriter writer, List<PdfStructureElement> elements) throws DocumentException, IOException {
		this.document = document;
		this.writer = writer;
		canvas = writer.getDirectContent();
		column = new ColumnText(canvas);
		column.setSimpleColumn(36, 36, 384, 569);
		this.elements = elements;
		font = new Font(BaseFont.createFont("c:/windows/fonts/arial.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED), 12);
	}

	/**
	 * @see org.xml.sax.ContentHandler#characters(char[], int, int)
	 */
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		buf.append(ch, start, length);
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
