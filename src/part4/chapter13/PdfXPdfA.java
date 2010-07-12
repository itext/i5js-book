/* in_action/chapterF/HelloWorldPdfX.java */

package part4.chapter13;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.ICC_Profile;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfXPdfA {

    /** The resulting PDF file. */
    public static final String RESULT1 = "results/part4/chapter13/x.pdf";
    /** The resulting PDF file. */
    public static final String RESULT2 = "results/part4/chapter13/a.pdf";

    /** A font program that is used. */
    public static final String FONT = "c:/windows/fonts/arial.ttf";
    /** A color profile that is used. */
    public static final String PROFILE = "resources/img/sRGB.profile";
    
    /**
     * Creates a PDF document.
     * @param filename the path to the new PDF document
     * @throws DocumentException 
     * @throws IOException
     */
    public void createPdfX(String filename) throws IOException, DocumentException {
        // step 1
        Document document = new Document();
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document,
            new FileOutputStream(filename));
        writer.setPDFXConformance(PdfWriter.PDFX1A2001);
        // step 3
        document.open();
        // step 4
        Font font = FontFactory.getFont(
            FONT, BaseFont.CP1252, BaseFont.EMBEDDED, Font.UNDEFINED,
            Font.UNDEFINED, new CMYKColor(255, 255, 0, 0));
        document.add(new Paragraph("Hello World", font));
        // step 5
        document.close();
    }

    /**
     * Creates a PDF document.
     * @param filename the path to the new PDF document
     * @throws DocumentException 
     * @throws IOException
     */
    public void createPdfA(String filename) throws IOException, DocumentException {
        // step 1
        Document document = new Document();
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document,
            new FileOutputStream(filename));
        writer.setPdfVersion(PdfWriter.PDF_VERSION_1_4);
        writer.setPDFXConformance(PdfWriter.PDFA1B);
        writer.createXmpMetadata();
        // step 3
        document.open();
        // step 4
        Font font = FontFactory.getFont(FONT, BaseFont.CP1252, BaseFont.EMBEDDED);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream(PROFILE));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);
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
        PdfXPdfA example = new PdfXPdfA();
        example.createPdfX(RESULT1);
        example.createPdfA(RESULT2);
    }
}