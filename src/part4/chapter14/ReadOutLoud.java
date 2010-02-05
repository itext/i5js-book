package part4.chapter14;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.PdfStructureElement;
import com.itextpdf.text.pdf.PdfStructureTreeRoot;
import com.itextpdf.text.pdf.PdfWriter;

public class ReadOutLoud {
	public static String RESULT = "results/part4/chapter14/read_out_loud.pdf";
	public static String RESOURCE = "resources/posters/0062622.jpg";

    /**
     * Creates a PDF document.
     * @param filename the path to the new PDF document
     * @throws DocumentException 
     * @throws IOException 
     * @throws    DocumentException 
     * @throws    IOException
     */
	public void createPdf(String filename) throws IOException, DocumentException {
		Document document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document,
				new FileOutputStream(filename));
		writer.setTagged();
		document.open();
		PdfContentByte cb = writer.getDirectContent();
		BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA,
				BaseFont.CP1252, BaseFont.NOT_EMBEDDED);

		PdfStructureTreeRoot root = writer.getStructureTreeRoot();
		PdfStructureElement div = new PdfStructureElement(root, new PdfName("Div"));
		PdfDictionary dict;

		cb.beginMarkedContentSequence(div);

		cb.beginText();
		cb.moveText(36, 788);
		cb.setFontAndSize(bf, 12);
		cb.setLeading(18);
		cb.showText("These are some famous movies by Stanley Kubrick: ");
		dict = new PdfDictionary();
		dict.put(PdfName.E, new PdfString("Doctor"));
		cb.beginMarkedContentSequence(new PdfName("Span"), dict, true);
		cb.newlineShowText("Dr.");
		cb.endMarkedContentSequence();
		cb.showText(" Strangelove or: How I Learned to Stop Worrying and Love the Bomb.");
		dict = new PdfDictionary();
		dict.put(PdfName.E, new PdfString("Eyes Wide Shut."));
		cb.beginMarkedContentSequence(new PdfName("Span"), dict, true);
		cb.newlineShowText("EWS");
		cb.endMarkedContentSequence();
		cb.endText();
		dict = new PdfDictionary();
		dict.put(PdfName.LANGUAGE, new PdfString("en-us"));
		dict.put(new PdfName("Alt"), new PdfString("2001: A Space Odyssey."));
		cb.beginMarkedContentSequence(new PdfName("Span"), dict, true);
		Image img = Image.getInstance(RESOURCE);
		img.setAbsolutePosition(36, 734 - img.getScaledHeight());
		cb.addImage(img);
		cb.endMarkedContentSequence();

		cb.endMarkedContentSequence();
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
    	new ReadOutLoud().createPdf(RESULT);
    }
}
