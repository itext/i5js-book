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
