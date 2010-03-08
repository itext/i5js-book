package part4.chapter16;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class ResizeImage {

	public static String RESULT = "results/part4/chapter15/resized_image.pdf";
	public static float FACTOR = 0.5f;
	
    /**
     * Manipulates a PDF file src with the file dest as result
     * @param src the original PDF
     * @param dest the resulting PDF
     * @throws IOException
     * @throws DocumentException 
     */
    public void manipulatePdf(String src, String dest) throws IOException, DocumentException {
    	PdfName key = new PdfName("ITXT_SpecialId");
    	PdfName value = new PdfName("123456789");
    	PdfReader reader = new PdfReader(SpecialId.RESULT);
		int n = reader.getXrefSize();
		PdfObject object;
		PRStream image;
		for (int i = 0; i < n; i++) {
			object = reader.getPdfObject(i);
			if (object == null || !object.isStream())
				continue;
			image = (PRStream)object;
			if (value.equals(image.get(key))) {
				BufferedImage bi = ImageIO.read(new ByteArrayInputStream(PdfReader.getStreamBytesRaw(image)));
                int width = (int)(bi.getWidth() * FACTOR);
                int height = (int)(bi.getHeight() * FACTOR);
                BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                AffineTransform at = AffineTransform.getScaleInstance(FACTOR, FACTOR);
                Graphics2D g = img.createGraphics();
                g.drawRenderedImage(bi, at);
                ByteArrayOutputStream newImage = new ByteArrayOutputStream();
                ImageIO.write(img, "JPG", newImage);
                image.setData(newImage.toByteArray(), false, PRStream.NO_COMPRESSION);
                image.put(PdfName.WIDTH, new PdfNumber(width));
                image.put(PdfName.HEIGHT, new PdfNumber(height));
                image.put(PdfName.FILTER, PdfName.DCTDECODE);
			}
		}
    	PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(RESULT));
    	stamper.close();
    }

    /**
     * Main method.
     *
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException
     */
    public static void main(String[] args) throws IOException, DocumentException {
    	new SpecialId().createPdf(SpecialId.RESULT);
    	new ResizeImage().manipulatePdf(SpecialId.RESULT, RESULT);
    }
}
