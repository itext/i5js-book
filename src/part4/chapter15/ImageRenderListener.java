/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part4.chapter15;

import java.io.IOException;

import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;

public class ImageRenderListener implements RenderListener {

	/** The new document to which we've added a border rectangle. */
	protected String path;
	protected int pagenumber;
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public void setPagenumber(int pagenumber) {
		this.pagenumber = pagenumber;
	}
	
	public int getPagenumber() {
		return pagenumber;
	}
	
	public void beginTextBlock() {
	}

	public void endTextBlock() {
	}

	public void renderImage(ImageRenderInfo renderInfo) {
		PRStream stream = (PRStream)PdfReader.getPdfObject(renderInfo.getRef());
		try {
			PdfImageExtractor extractor
			    = new PdfImageExtractor(stream, stream);
			extractor.extractImage(String.format(path, pagenumber, renderInfo.getRef().getNumber()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void renderText(TextRenderInfo renderInfo) {
	}

	public void reset() {
	}

}
