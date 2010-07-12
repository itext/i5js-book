/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */
package part4.chapter14;

import java.awt.Graphics2D;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.DefaultFontMapper;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.DefaultFontMapper.BaseFontParameters;

public class Text1ToPdf2 {
    /** The resulting PDF. */
    public static final String RESULT = "results/part4/chapter14/text12.pdf";

    /**
     * Creates a PDF document.
     * @param filename the path to the new PDF document
     * @throws DocumentException 
     * @throws IOException
     */
    public void createPdf(String filename) throws IOException, DocumentException {
    	// step 1
        Document document = new Document(new Rectangle(600, 60));
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
        // step 3
        document.open();
        // step 4
        PdfContentByte canvas = writer.getDirectContent();
        // create a font mapper
        DefaultFontMapper mapper = new DefaultFontMapper();
        // map MS Gothic to the corresponding font program
        BaseFontParameters parameters = new BaseFontParameters("c:/windows/fonts/msgothic.ttc,1");
        parameters.encoding = BaseFont.IDENTITY_H;
        mapper.putName("MS PGothic", parameters );
        // Create a Graphics2D object
        Graphics2D g2 = canvas.createGraphics(600, 60, mapper);
        // write the text to the Graphics2D
        TextExample1 text = new TextExample1();
        text.paint(g2);
        g2.dispose();
        // step 5
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
        new Text1ToPdf2().createPdf(RESULT);
    }
}
