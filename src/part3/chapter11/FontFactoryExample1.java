/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part3.chapter11;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfWriter;

public class FontFactoryExample1 {

    /** The resulting PDF file. */
    public static final String RESULT
        = "results/part3/chapter11/font_factory_1.pdf";

    /**
     * Creates a PDF document.
     * @param filename the path to the new PDF document
     * @throws DocumentException 
     * @throws IOException 
     * @throws    DocumentException 
     * @throws    IOException
     */
	public void createPdf(String filename) throws IOException, DocumentException {
		// step 1
		Document document = new Document();
		// step 2
		PdfWriter.getInstance(document, new FileOutputStream(filename));
		// step 3: we open the document
		document.open();
		// step 4:
		Font[] fonts = new Font[10];
		fonts[0] = FontFactory.getFont("Times-Roman");
		fonts[1] = FontFactory.getFont("Courier", 10);
		fonts[2] = FontFactory.getFont("Courier", 10, Font.BOLD);
		fonts[3] = FontFactory.getFont(FontFactory.TIMES, 10, Font.BOLD, new CMYKColor(255, 0, 0, 64));
		fonts[4] = FontFactory.getFont("c:/windows/fonts/arial.ttf", BaseFont.CP1252, BaseFont.EMBEDDED);
		FontFactory.register("resources/fonts/cmr10.afm");
		fonts[5] = FontFactory.getFont("CMR10", BaseFont.CP1252, BaseFont.EMBEDDED);
		fonts[5].getBaseFont().setPostscriptFontName("Computer Modern");
		FontFactory.register("c:/windows/fonts/gara.ttf", "Manning");
		FontFactory.register("c:/windows/fonts/garabd.ttf", "Manning-bold");
		FontFactory.register("c:/windows/fonts/garait.ttf", "Manning-italic");
		fonts[6] = FontFactory.getFont("Manning", BaseFont.CP1252, BaseFont.EMBEDDED);
		fonts[7] = FontFactory.getFont("Manning-bold", BaseFont.CP1252, BaseFont.EMBEDDED, 10);
		fonts[8] = FontFactory.getFont("Manning", BaseFont.CP1252, BaseFont.EMBEDDED, 10, Font.ITALIC);
		fonts[9] = FontFactory.getFont("garamond vet", BaseFont.CP1252, BaseFont.EMBEDDED, 10, Font.UNDEFINED, new CMYKColor(0, 255, 0, 64));
		System.out.println("Registered fonts");
		for (String f : FontFactory.getRegisteredFonts()) {
			System.out.println(f);
		}
		System.out.println("Registered font families");
		for (String f : FontFactory.getRegisteredFamilies()) {
			System.out.println(f);
		}
		// add the content
		for (int i = 0; i < 10; i++) {
			document.add(new Paragraph(
				"Quick brown fox jumps over the lazy dog", fonts[i]));
		}
		// step 5: we close the document
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
		new FontFactoryExample1().createPdf(RESULT);
	}
}
