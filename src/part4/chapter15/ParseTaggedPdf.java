/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part4.chapter15;

import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.TaggedPdfReaderTool;

public class ParseTaggedPdf {

    /** The resulting XML file. */
    public static final String RESULT
        = "results/part4/chapter15/moby_extracted.xml";

    /**
     * Creates a PDF file using a previous example,
     * then parses the document.
     * @param    args    no arguments needed
     */
    public static void main(String[] args)
        throws IOException, DocumentException, SAXException, ParserConfigurationException {
        StructuredContent.main(args);
        TaggedPdfReaderTool reader = new TaggedPdfReaderTool();
        reader.convertToXml(new PdfReader(StructuredContent.RESULT), new FileOutputStream(RESULT));
    }
}
