/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part4.chapter15;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.PdfImageObject;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;

public class MyImageRenderListener implements RenderListener {

	/** The new document to which we've added a border rectangle. */
	protected String path = "";
	
	public MyImageRenderListener(String path) {
		this.path = path;
	}
	
	public void beginTextBlock() {
	}

	public void endTextBlock() {
	}

	public void renderImage(ImageRenderInfo renderInfo) {
		try {
			String filename;
			FileOutputStream os;
			PdfImageObject image = renderInfo.getImage();
			PdfName filter = (PdfName)image.get(PdfName.FILTER);
			if (PdfName.DCTDECODE.equals(filter)) {
				filename = String.format(path, renderInfo.getRef().getNumber(), "jpg");
				os = new FileOutputStream(filename);
				os.write(image.getStreamBytes());
				os.flush();
				os.close();
			}
			else if (PdfName.JPXDECODE.equals(filter)) {
				filename = String.format(path, renderInfo.getRef().getNumber(), "jp2");
				os = new FileOutputStream(filename);
				os.write(image.getStreamBytes());
				os.flush();
				os.close();
			}
			else {
				BufferedImage awtimage = renderInfo.getImage().getBufferedImage();
				if (awtimage != null) {
					filename = String.format(path, renderInfo.getRef().getNumber(), "png");
					ImageIO.write(awtimage, "png", new FileOutputStream(filename));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void renderText(TextRenderInfo renderInfo) {
	}
}
