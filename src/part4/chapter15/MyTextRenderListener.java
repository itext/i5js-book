/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */
package part4.chapter15;

import java.io.PrintWriter;

import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;

public class MyTextRenderListener implements RenderListener {

	protected PrintWriter out;
	public MyTextRenderListener(PrintWriter out) {
		this.out = out;
	}
	
	public void beginTextBlock() {
		out.print("<");
	}

	public void endTextBlock() {
		out.println(">");
	}

	public void renderImage(ImageRenderInfo renderInfo) {
	}

	public void renderText(TextRenderInfo renderInfo) {
		out.print("<");
		out.print(renderInfo.getText());
		out.print(">");
	}
}
