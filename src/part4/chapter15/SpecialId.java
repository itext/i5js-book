package part4.chapter15;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfImage;
import com.itextpdf.text.pdf.PdfIndirectObject;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfWriter;

public class SpecialId {
	public static String RESULT = "results/part4/chapter15/special_id.pdf";
	public static String RESOURCE = "resources/img/bruno.jpg";

    /**
     * Creates a PDF document.
     * @param filename the path to the new PDF document
     * @throws DocumentException 
     * @throws IOException 
     * @throws    DocumentException 
     * @throws    IOException
     */
	public void createPdf(String filename) throws IOException, DocumentException {
		Document document = new Document(new Rectangle(400, 300));
		PdfWriter writer = PdfWriter.getInstance(document,
				new FileOutputStream(filename));
		document.open();
		Image img = Image.getInstance(RESOURCE);
		img.scaleAbsolute(400, 300);
		img.setAbsolutePosition(0, 0);
		PdfImage stream = new PdfImage(img, "", null);
		stream.put(new PdfName("ITXT_SpecialId"), new PdfName("123456789"));
		PdfIndirectObject ref = writer.addToBody(stream);
		img.setDirectReference(ref.getIndirectReference());
		document.add(img);
		document.close();
	}

    /**
     * Main method.
     *
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException
     */
    public static void main(String[] args) throws IOException, DocumentException {
    	new SpecialId().createPdf(RESULT);
    }
}
