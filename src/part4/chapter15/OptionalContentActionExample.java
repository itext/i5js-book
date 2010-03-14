/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */
package part4.chapter15;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfLayer;
import com.itextpdf.text.pdf.PdfWriter;

public class OptionalContentActionExample {
	public static String RESULT = "results/part4/chapter15/layer_actions.pdf";

	public void createPdf(String filename) throws DocumentException,
			IOException {
		// step 1
		Document document = new Document();
		// step 2
		PdfWriter writer = PdfWriter.getInstance(
				document, new FileOutputStream(RESULT));
		writer.setPdfVersion(PdfWriter.VERSION_1_5);
		// step 3
		document.open();
		// step 4
		PdfLayer a1 = new PdfLayer("answer 1", writer);
		PdfLayer a2 = new PdfLayer("answer 2", writer);
		PdfLayer a3 = new PdfLayer("answer 3", writer);
		a1.setOn(false);
		a2.setOn(false);
		a3.setOn(false);

		BaseFont bf = BaseFont.createFont();
		PdfContentByte cb = writer.getDirectContent();
		cb.beginText();
		cb.setTextMatrix(50, 790);
		cb.setLeading(24);
		cb.setFontAndSize(bf, 18);
		cb.newlineShowText("Q1: Who is quick?");
		cb.beginLayer(a1);
		cb.setRGBColorFill(0xFF, 0x00, 0x00);
		cb.newlineShowText("A1: the fox");
		cb.resetRGBColorFill();
		cb.endLayer();
		cb.newlineShowText("Q2: Who is lazy?");
		cb.beginLayer(a2);
		cb.setRGBColorFill(0xFF, 0x00, 0x00);
		cb.newlineShowText("A2: the dog");
		cb.resetRGBColorFill();
		cb.endLayer();
		cb.newlineShowText("Q3: Who jumps over the lazy dog?");
		cb.beginLayer(a3);
		cb.setRGBColorFill(0xFF, 0x00, 0x00);
		cb.newlineShowText("A3: the quick fox");
		cb.resetRGBColorFill();
		cb.endLayer();
		cb.endText();

		ArrayList<Object> stateOn = new ArrayList<Object>();
		stateOn.add("ON");
		stateOn.add(a1);
		stateOn.add(a2);
		stateOn.add(a3);
		PdfAction actionOn = PdfAction.setOCGstate(stateOn, true);
		ArrayList<Object> stateOff = new ArrayList<Object>();
		stateOff.add("OFF");
		stateOff.add(a1);
		stateOff.add(a2);
		stateOff.add(a3);
		PdfAction actionOff = PdfAction.setOCGstate(stateOff, true);
		ArrayList<Object> stateToggle = new ArrayList<Object>();
		stateToggle.add("Toggle");
		stateToggle.add(a1);
		stateToggle.add(a2);
		stateToggle.add(a3);
		PdfAction actionToggle = PdfAction.setOCGstate(stateToggle, true);
		Phrase p = new Phrase("Change the state of the answers:");
		Chunk on = new Chunk(" on ").setAction(actionOn);
		p.add(on);
		Chunk off = new Chunk("/ off ").setAction(actionOff);
		p.add(off);
		Chunk toggle = new Chunk("/ toggle").setAction(actionToggle);
		p.add(toggle);
		document.add(p);
		// step 5
		document.close();
	}

	/**
	 * A simple example with optional content.
	 * 
	 * @param args
	 *            no arguments needed here
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static void main(String[] args) throws DocumentException,
			IOException {
		new OptionalContentActionExample().createPdf(RESULT);
	}
}
