/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part4.chapter16;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfIndirectObject;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfStream;
import com.itextpdf.text.pdf.PdfWriter;

public class EmbedFontPostFacto {

    /** The first resulting PDF file. */
    public static String RESULT1 = "results/part4/chapter16/without_font.pdf";
    /** The second resulting PDF file. */
    public static String RESULT2 = "results/part4/chapter16/with_font.pdf";
    /** A special font. */
    public static String FONT = "resources/fonts/wds011402.ttf";
    /** The name of the special font. */
    public static String FONTNAME = "WaltDisneyScriptv4.1";

    /**
     * Creates a PDF document.
     * @param filename the path to the new PDF document
     * @throws    DocumentException 
     * @throws    IOException
     */
    public void createPdf(String filename) throws IOException, DocumentException {
        // step 1
        Document document = new Document();
        // step 2
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        // step 3
        document.open();
        // step 4:
        Font font = new Font(BaseFont.createFont(FONT, "", BaseFont.NOT_EMBEDDED), 60);
        document.add(new Paragraph("iText in Action", font));
        // step 5
        document.close();
    }
    
    /**
     * Manipulates a PDF file src with the file dest as result
     * @param src the original PDF
     * @param dest the resulting PDF
     * @throws IOException
     * @throws DocumentException 
     */
    public void manipulatePdf(String src, String dest) throws IOException, DocumentException {
        // the font file
        RandomAccessFile raf = new RandomAccessFile(FONT, "r");
        byte fontfile[] = new byte[(int)raf.length()];
        raf.readFully(fontfile);
        raf.close();
        // create a new stream for the font file
        PdfStream stream = new PdfStream(fontfile);
        stream.flateCompress();
        stream.put(PdfName.LENGTH1, new PdfNumber(fontfile.length));
        // create a reader object
        PdfReader reader = new PdfReader(RESULT1);
        int n = reader.getXrefSize();
        PdfObject object;
        PdfDictionary font;
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(RESULT2));
        PdfName fontname = new PdfName(FONTNAME);
        for (int i = 0; i < n; i++) {
            object = reader.getPdfObject(i);
            if (object == null || !object.isDictionary())
                continue;
            font = (PdfDictionary)object;
            if (PdfName.FONTDESCRIPTOR.equals(font.get(PdfName.TYPE))
                && fontname.equals(font.get(PdfName.FONTNAME))) {
                PdfIndirectObject objref = stamper.getWriter().addToBody(stream);
                font.put(PdfName.FONTFILE2, objref.getIndirectReference());
            }
        }
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
        EmbedFontPostFacto example = new EmbedFontPostFacto();
        example.createPdf(RESULT1);
        example.manipulatePdf(RESULT1, RESULT2);
    }
}
