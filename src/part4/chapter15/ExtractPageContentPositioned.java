package part4.chapter15;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.LocationTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

public class ExtractPageContentPositioned {

	public static final String RESULT = "results/part4/chapter15/preface_positioned.txt";
	
	public void parsePdf(String pdf, String txt) throws IOException {
		PdfReader reader = new PdfReader(pdf);
		TextExtractionStrategy strategy
			= new LocationTextExtractionStrategy();
		PdfTextExtractor extractor
			= new PdfTextExtractor(reader, strategy);
		PrintWriter out = new PrintWriter(new FileOutputStream(txt));
		for (int i = 1; i <= reader.getNumberOfPages(); i++) {
			out.println(extractor.getTextFromPage(i));
		}
		out.flush();
		out.close();
	}
	
	public static void main(String[] args) throws IOException, DocumentException {
		new ExtractPageContentPositioned().parsePdf(ExtractPageContent.PREFACE, RESULT);
	}
}
