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

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;

public class ExtractPageContent {

	public static final String PREFACE = "resources/pdfs/preface.pdf";
	public static final String RESULT = "results/part4/chapter15/preface.txt";
	
	public void parsePdf(String pdf, String txt) throws IOException {
		PdfReader reader = new PdfReader(pdf);
		PrintWriter out = new PrintWriter(new FileOutputStream(txt));
		for (int i = 1; i <= reader.getNumberOfPages(); i++) {
			out.println(PdfTextExtractor.getTextFromPage(reader, i, new SimpleTextExtractionStrategy()));
		}
		out.flush();
		out.close();
	}
	
	public static void main(String[] args) throws IOException {
		new ExtractPageContent().parsePdf(PREFACE, RESULT);
	}
}
