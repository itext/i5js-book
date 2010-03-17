package part4.chapter15;

import java.io.IOException;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

public class ExtractPageContent {

	public static final String RESOURCE = "resources/pdfs/preface.pdf";
	public static final String RESULT = "results/part4/chapter15/preface.txt";
	
	public void parsePdf(String pdf, String txt) throws IOException {
		PdfReader reader = new PdfReader(pdf);
		TextExtractionStrategy strategy
			= new SimpleTextExtractionStrategy();
		PdfTextExtractor extractor
			= new PdfTextExtractor(reader, strategy);
		for (int i = 1; i <= reader.getNumberOfPages(); i++) {
			System.out.println(extractor.getTextFromPage(i));
		}
	}
	
	public static void main(String[] args) throws IOException {
		new ExtractPageContent().parsePdf(RESOURCE, RESULT);
	}
}
