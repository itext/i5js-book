/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part4.chapter15;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;

public class ParseTaggedPdf {
    /** The resulting XML file. */
    public static final String RESULT
        = "results/part4/chapter15/moby_extracted.xml";
    /** The reader object from which the content streams are read. */
    PdfReader reader;
    /** The writer object to which the XML will be written */
    PrintWriter out;

    /**
     * Parses a string with structured content.
     * @param filename the name of the PDF file
     * @param xml the name of the resulting xml file
     */
    public void parsePdf(String filename, String xml) throws IOException {
        // create a reader for the PDF file
        reader = new PdfReader(filename);
        // create a writer for the XML
        out = new PrintWriter(new FileOutputStream(xml));
        // get the StructTreeRoot from the root object
        PdfDictionary catalog = reader.getCatalog();
        PdfDictionary struct = catalog.getAsDict(PdfName.STRUCTTREEROOT);
        // Inspect the child or children of the StructTreeRoot
        inspectChild(struct.getDirectObject(PdfName.K));
        out.flush();
        out.close();
    }
	
	/**
	 * Inspects a child of a structured element.
	 * This can be an array or a dictionary.
	 * @param k the child to inspect
	 * @throws IOException
	 */
	public void inspectChild(PdfObject k) throws IOException {
		if (k == null)
			return;
		if (k instanceof PdfArray)
			inspectChildArray((PdfArray)k);
		else if (k instanceof PdfDictionary)
			inspectChildDictionary((PdfDictionary)k);
	}

    /**
     * If the child of a structured element is an array,
     * we need to loop over the elements.
     * @param k the child array to inspect
     */
    public void inspectChildArray(PdfArray k) throws IOException {
        if (k == null) return;
        for (int i = 0; i < k.size(); i++) {
            inspectChild(k.getDirectObject(i));
        }
    }
	
	/**
	 * If the child of a structured element is a dictionary,
	 * we inspect the child; we may also draw a tag.
     * @param k the child dictionary to inspect
	 */
    public void inspectChildDictionary(PdfDictionary k)
        throws IOException {
        if (k == null) return;
        PdfName s = k.getAsName(PdfName.S);
        if (s != null) {
            String tag = s.toString().substring(1);
            out.print("<");
            out.print(tag);
            out.print(">");
            PdfDictionary dict = k.getAsDict(PdfName.PG);
            if (dict != null)
                parseTag(tag, k.getDirectObject(PdfName.K), dict);
            inspectChild(k.get(PdfName.K));
            out.print("</");
            out.print(tag);
            out.println(">");
        }
        else
            inspectChild(k.get(PdfName.K));
    }

    /**
     * Searches for a tag in a page.
     * @param tag the name of the tag
     * @param object an identifier to find the marked content
     * @param page a page dictionary
     * @throws IOException
     */
    public void parseTag(String tag, PdfObject object, PdfDictionary page) throws IOException {
    	PRStream stream = (PRStream) page.getAsStream(PdfName.CONTENTS);
		// if the identifier is a number, we can extract the content right away
		if (object instanceof PdfNumber) {
			PdfNumber mcid = (PdfNumber) object;
			out.print(PdfTagExtraction.parse(tag, mcid.intValue(), PdfReader.getStreamBytes(stream)));
		}
		// if the identifier is an array, we call the parseTag method recursively
		else if (object instanceof PdfArray) {
			PdfArray arr = (PdfArray) object;
			for (int i = 0; i < arr.size(); i++) {
				parseTag(tag, arr.getPdfObject(i), page);
			}
		}
		// if the identifier is a dictionary, we get the resources from the dictionary
		else if (object instanceof PdfDictionary) {
			PdfDictionary mcr = (PdfDictionary) object;
			parseTag(tag, mcr.getDirectObject(PdfName.MCID), mcr.getAsDict(PdfName.PG));
		}
	}

    /**
     * Creates a PDF file using a previous example,
     * then parses the document.
     * @param    args    no arguments needed
     */
    public static void main(String[] args)
        throws IOException, DocumentException, SAXException, ParserConfigurationException {
        StructuredContent.main(args);
        new ParseTaggedPdf().parsePdf("resources/pdfs/ch14.pdf"/*StructuredContent.RESULT*/, RESULT);
    }
}
