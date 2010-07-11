/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */
package part4.chapter14;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Map.Entry;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.DefaultFontMapper;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.DefaultFontMapper.BaseFontParameters;

public class Graphics2DFonts {
    /** The resulting PDF. */
    public static final String RESULT1
        = "results/part4/chapter14/awt_fonts.txt";
    /** The resulting PDF. */
    public static final String RESULT2
        = "results/part4/chapter14/pdf_fonts.txt";
    /** The resulting PDF. */
    public static final String RESULT3
        = "results/part4/chapter14/fonts.pdf";
    
    public static final Font[] FONTS = {
        new Font("Serif", Font.PLAIN, 12),
        new Font("Serif", Font.BOLD, 12),
        new Font("Serif", Font.ITALIC, 12),
        new Font("SansSerif", Font.PLAIN, 12),
        new Font("Monospaced", Font.PLAIN, 12)
    };

    public static void main(String[] args) throws IOException, DocumentException {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fontFamily = ge.getAvailableFontFamilyNames();
        PrintStream out1 = new PrintStream(new FileOutputStream(RESULT1));
        for (int i = 0; i < fontFamily.length; i++) {
            out1.println(fontFamily[i]);
        }
        out1.flush();
        out1.close();
        
        DefaultFontMapper mapper = new DefaultFontMapper();
        mapper.insertDirectory("c:/windows/fonts/");
        PrintStream out2 = new PrintStream(new FileOutputStream(RESULT2));
        for (Entry<String,BaseFontParameters> entry : mapper.getMapper().entrySet()) {
            out2.println(String.format("%s: %s", entry.getKey(), entry.getValue().fontName));
        }
        out2.flush();
        out2.close();
        
        float width = 150;
        float height = 150;
        Document document = new Document(new Rectangle(width, height));
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(RESULT3));
        document.open();
        PdfContentByte cb = writer.getDirectContent();
        Graphics2D g2d = cb.createGraphics(width, height, mapper);
        for (int i = 0; i < FONTS.length; ) {
            g2d.setFont(FONTS[i++]);
            g2d.drawString("Hello world", 5, 24 * i);
        }
        g2d.dispose();
        document.close();
    }
}
