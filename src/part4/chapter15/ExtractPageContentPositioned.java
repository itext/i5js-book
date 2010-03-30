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

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.LocationTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;

public class ExtractPageContentPositioned {

	public static final String RESULT = "results/part4/chapter15/preface_positioned.txt";
	
	public void parsePdf(String pdf, String txt) throws IOException {
		PdfReader reader = new PdfReader(pdf);
		PdfReaderContentParser parser = new PdfReaderContentParser(reader);
		PrintWriter out = new PrintWriter(new FileOutputStream(txt));
		for (int i = 1; i <= reader.getNumberOfPages(); i++) {
			out.println(parser.processContent(i, new LocationTextExtractionStrategy()).getResultantText());
		}
		out.flush();
		out.close();
	}
	
	public static void main(String[] args) throws IOException, DocumentException {
		new ExtractPageContentPositioned().parsePdf(ExtractPageContent.PREFACE, RESULT);
	}
}
