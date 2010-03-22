package part4.chapter15;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;

public class PdfImageExtractor {

	protected PdfDictionary image_dict;
	protected byte[] image_bytes;
	
	public PdfImageExtractor(PdfDictionary image_dict, PRStream stream) throws IOException {
		this.image_dict = image_dict;
		if (PdfName.FLATEDECODE.equals(image_dict.getAsName(PdfName.FILTER)))
			this.image_bytes = PdfReader.getStreamBytes(stream);
		else
			this.image_bytes = PdfReader.getStreamBytesRaw(stream);
	}
	
	public void extractImage(String filename) throws IOException {
		PdfName filter = image_dict.getAsName(PdfName.FILTER);
		if (PdfName.DCTDECODE.equals(filter)) {
			extractJpg(new FileOutputStream(filename + ".jpg"));
		}
		else if (PdfName.FLATEDECODE.equals(filter)) {
			extractPng(new FileOutputStream(filename + ".png"));
		}
		else {
			//for (PdfName key : image_dict.getKeys()) {
				//System.out.print(key);
				//System.out.print(" ");
				//System.out.println(image_dict.get(key));
			//}
		}
	}
	
	public void extractImage(OutputStream os) throws IOException {
		PdfName filter = image_dict.getAsName(PdfName.FILTER);
		if (PdfName.DCTDECODE.equals(filter)) {
			extractJpg(os);
		}
		else if (PdfName.FLATEDECODE.equals(filter)) {
			extractPng(os);
		}
		else {
			//for (PdfName key : image_dict.getKeys()) {
				//System.out.print(key);
				//System.out.print(" ");
				//System.out.println(image_dict.get(key));
			//}
		}
	}
	
	protected void extractJpg(OutputStream os) throws IOException {
		BufferedImage bi = ImageIO.read(new ByteArrayInputStream(image_bytes));
		ImageIO.write(bi, "JPG", os);
	}
	
	protected void extractPng(OutputStream os) throws IOException {
		for (PdfName key : image_dict.getKeys()) {
			System.out.print(key);
			System.out.print(" ");
			System.out.println(image_dict.get(key));
		}
		BufferedImage bi = null;
		DataBuffer db = new DataBufferByte(image_bytes, image_bytes.length);
		int width = image_dict.getAsNumber(PdfName.WIDTH).intValue();
		int height = image_dict.getAsNumber(PdfName.HEIGHT).intValue();
		WritableRaster raster;
		int bpc = image_dict.getAsNumber(PdfName.BITSPERCOMPONENT).intValue();
		switch(bpc) {
		case 1:
			raster = Raster.createPackedRaster( db, width, height, 1, null );
			bi = new BufferedImage( width, height, BufferedImage.TYPE_BYTE_BINARY );
			bi.setData( raster );
			break;
		default:
			if (PdfName.DEVICERGB.equals(image_dict.getAsName(PdfName.COLORSPACE))) {
				if (width * height == image_bytes.length) {
					bi = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_INDEXED );
					raster = Raster.createPackedRaster(db, width, height, bpc, null);
					bi.setData(raster);
				}
				else {
					bi = new BufferedImage( width, height, BufferedImage.TYPE_INT_RGB );
					raster = Raster.createInterleavedRaster(db, width, height,
							width * 3, 3, new int[]{0, 1, 2}, null );
					bi.setData(raster);
				}
			}
		}
		if (bi != null) {
			ImageIO.write(bi, "png", os);
		}
		//WritableRaster raster = Raster.createInterleavedRaster(db, 16, 16, 48, 3, new int[]{0,1,2}, null);
        //ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
        //ColorModel cm = new ComponentColorModel(cs, false, false, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
        //BufferedImage bi = new BufferedImage(cm, raster, false, null);
        //ImageIO.write(bi, "bmp", new File("hello.bmp"));
	}
}
