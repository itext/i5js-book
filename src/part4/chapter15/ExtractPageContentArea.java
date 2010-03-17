package part4.chapter15;

import java.awt.Rectangle;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.FilteredTextRenderListener;
import com.itextpdf.text.pdf.parser.LocationTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.RegionTextRenderFilter;

public class ExtractPageContentArea {

	public static final String RESULT = "results/part4/chapter15/preface_clipped.txt";

	public void parsePdf(String pdf, String txt) throws IOException {
		PdfReader reader = new PdfReader(pdf);
		Rectangle rect = new Rectangle(70, 80, 485, 500);
        FilteredTextRenderListener filterListener
          = new FilteredTextRenderListener(
            new LocationTextExtractionStrategy(),
            new RegionTextRenderFilter(rect) );
        
		PdfTextExtractor extractor
			= new PdfTextExtractor(reader, filterListener);
		PrintWriter out = new PrintWriter(new FileOutputStream(txt));
		for (int i = 1; i <= reader.getNumberOfPages(); i++) {
			out.println(extractor.getTextFromPage(i));
		}
		out.flush();
		out.close();
	}
	
	public static void main(String[] args) throws IOException, DocumentException {
		new ExtractPageContentArea().parsePdf(ExtractPageContent.PREFACE, RESULT);
	}
}
