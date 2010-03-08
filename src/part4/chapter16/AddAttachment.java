package part4.chapter16;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfString;

import part3.chapter11.Peace;

public class AddAttachment {

	public static final String RESULT = "results/part4/chapter15/with_attachment.pdf";
	public static final String RESOURCE = "resources/xml/peace.xml";

    /**
     * Manipulates a PDF file src with the file dest as result
     * @param src the original PDF
     * @param dest the resulting PDF
     * @throws IOException
     * @throws DocumentException
     */
    public void manipulatePdf(String src, String dest, String resource) throws IOException, DocumentException {
    	PdfReader reader = new PdfReader(src);
    	PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
    	PdfAnnotation attachment = PdfAnnotation.createFileAttachment(
				stamper.getWriter(), new Rectangle(40, 530, 50, 580),
				"XML data", null,
				resource, resource.substring(resource.lastIndexOf('/') + 1));
		attachment.put(PdfName.NAME, new PdfString("Paperclip"));
		stamper.addAnnotation(attachment, 1);
		stamper.close();
    }
    
	public static void main(String[] args) throws IOException, DocumentException {
		Peace.main(args);
		new AddAttachment().manipulatePdf(Peace.RESULT, RESULT, RESOURCE);
	}
}
