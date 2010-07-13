/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part3.chapter09;

import java.util.EmptyStackException;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.TextElementArray;
import com.itextpdf.text.Font.FontFamily;

public class XmlHandler extends DefaultHandler {

    /** The Document to which the content is written. */
    protected Document document;
    
    /**
     * This is a <CODE>Stack</CODE> of objects, waiting to be added to the
     * document.
     */
    protected Stack<TextElementArray> stack = new Stack<TextElementArray>();

    /** This is the current chunk to which characters can be added. */
    protected Chunk currentChunk = null;

    /** Stores the year when it's encountered in the XML. */
    protected String year = null;
    /** Stores the duration when it's encountered in the XML. */
    protected String duration = null;
    /** Stores the imdb ID when it's encountered in the XML. */
    protected String imdb = null;
    
    /**
     * Creates a handler for an iText Document.
     * @param document the document to which content needs to be added.
     */
    public XmlHandler(Document document) {
        this.document = document;
    }
    
    /**
     * @see org.xml.sax.ContentHandler#characters(char[], int, int)
     */
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        String content = new String(ch, start, length);
        if (content.trim().length() == 0)
            return;
        if (currentChunk == null) {
            currentChunk = new Chunk(content.trim());
        }
        else {
            currentChunk.append(" ");
            currentChunk.append(content.trim());
        }
    }

    /**
     * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
     *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
     */
    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {
        try {
            if ("directors".equals(qName) || "countries".equals(qName)) {
                stack.push(new List(List.UNORDERED));
            } else if ("director".equals(qName)
                    || "country".equals(qName)) {
                stack.push(new ListItem());
            }
            else if ("movie".equals(qName)) {
                flushStack();
                Paragraph p = new Paragraph();
                p.setFont(new Font(FontFamily.HELVETICA, 14, Font.BOLD));
                stack.push(p);
                year = attributes.getValue("year");
                duration = attributes.getValue("duration");
                imdb = attributes.getValue("imdb");
            }
            else if ("original".equals(qName)) {
                stack.push(new Paragraph("Original title: "));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @see org.xml.sax.ContentHandler#endElement(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        try {
            updateStack();
            if ("directors".equals(qName)) {
                flushStack();
                Paragraph p = new Paragraph(
                    String.format("Year: %s; duration: %s; ", year, duration));
                Anchor link = new Anchor("link to IMDB");
                link.setReference(
                    String.format("http://www.imdb.com/title/tt%s/", imdb));
                p.add(link);
                stack.push(p);
            }
            else if ("countries".equals(qName) || "title".equals(qName)) {
                flushStack();
            } else if ("original".equals(qName)
                    || "movie".equals(qName)) {
                currentChunk = Chunk.NEWLINE;
                updateStack();
            } else if ("director".equals(qName)
                    || "country".equals(qName)) {
                ListItem listItem = (ListItem) stack.pop();
                List list = (List) stack.pop();
                list.add(listItem);
                stack.push(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * If the currentChunk is not null, it is forwarded to the stack and made
     * null.
     */
    private void updateStack() {
        if (currentChunk != null) {
            TextElementArray current;
            try {
                current = stack.pop();
                if (!(current instanceof Paragraph)
                        || !((Paragraph) current).isEmpty())
                    current.add(new Chunk(" "));
            } catch (EmptyStackException ese) {
                current = new Paragraph();
            }
            current.add(currentChunk);
            stack.push(current);
            currentChunk = null;
        }
    }

    /**
     * Flushes the stack, adding all objects in it to the document.
     */
    private void flushStack() {
        try {
            while (stack.size() > 0) {
                Element element = (Element) stack.pop();
                try {
                    TextElementArray previous = (TextElementArray) stack.pop();
                    previous.add(element);
                    stack.push(previous);
                } catch (EmptyStackException es) {
                    document.add(element);
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

}
