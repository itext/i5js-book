package part4.chapter15;

import java.io.IOException;

import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;

public class ImageRenderListener implements RenderListener {
	
	public void beginTextBlock() {
	}

	public void endTextBlock() {
	}

	public void renderImage(ImageRenderInfo renderInfo) {
		System.out.println(renderInfo.getRef());
		PRStream stream = (PRStream)PdfReader.getPdfObject(renderInfo.getRef());
		try {
			System.out.println(PdfReader.getStreamBytesRaw(stream));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void renderText(TextRenderInfo renderInfo) {
	}

	public void reset() {
	}

}
