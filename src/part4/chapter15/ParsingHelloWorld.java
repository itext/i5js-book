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

import part1.chapter01.HelloWorld;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PRTokeniser;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

public class ParsingHelloWorld {

	public static final String PDF = "results/part4/chapter15/hello_reverse.pdf";
	public static final String TEXT1 = "results/part4/chapter15/result1.txt";
	public static final String TEXT2 = "results/part4/chapter15/result2.txt";
	
	/**
	 * Generates a PDF file with the text 'Hello World'
	 * @throws DocumentException 
	 * @throws IOException 
	 */
	public void createPdf(String filename) throws DocumentException, IOException {
		Document document = new Document();
		PdfWriter writer
		  = PdfWriter.getInstance(document, new FileOutputStream(filename));
		document.open();
		// we add the text to the direct content, but not in the right order
		PdfContentByte cb = writer.getDirectContent();
		BaseFont bf = BaseFont.createFont();
        cb.beginText();
        cb.setFontAndSize(bf, 12);
        cb.moveText(88.66f, 367); 
        cb.showText("ld");
        cb.moveText(-22f, 0); 
        cb.showText("Wor");
        cb.moveText(-15.33f, 0); 
        cb.showText("llo");
        cb.moveText(-15.33f, 0); 
        cb.showText("He");
        cb.endText();
        PdfTemplate tmp = cb.createTemplate(250, 25);
        tmp.beginText();
        tmp.setFontAndSize(bf, 12);
        tmp.moveText(0, 7);
        tmp.showText("Hello People");
        tmp.endText();
        cb.addTemplate(tmp, 36, 343);
		document.close();
	}
	
	public void parsePdf(String src, String dest) throws IOException {
		PdfReader reader = new PdfReader(src);
		// we can inspect the syntax of the imported page
		byte[] streamBytes = reader.getPageContent(1);
		PRTokeniser tokenizer = new PRTokeniser(streamBytes);
        PrintWriter out = new PrintWriter(new FileOutputStream(dest));
		while (tokenizer.nextToken()) {
			if (tokenizer.getTokenType() == PRTokeniser.TokenType.STRING) {
				out.println(tokenizer.getStringValue());
			}
		}
		out.flush();
		out.close();
	}
	
	public static void main(String[] args) throws DocumentException, IOException {
		ParsingHelloWorld example = new ParsingHelloWorld();
		HelloWorld.main(args);
		example.createPdf(PDF);
		example.parsePdf(HelloWorld.RESULT, TEXT1);
		example.parsePdf(PDF, TEXT2);
	}
}
