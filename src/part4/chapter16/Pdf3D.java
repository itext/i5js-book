/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part4.chapter16;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfAppearance;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfIndirectObject;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfStream;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.PdfWriter;

public class Pdf3D {

    /** The resulting PDF file. */
    public static String RESULT = "results/part4/chapter16/pdf3d.pdf";
    /** The path to a 3D file. */
    public static String RESOURCE = "resources/img/teapot.u3d";

    /**
     * Main method.
     *
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException
     */
    public static void main(String[] args) throws IOException, DocumentException {
        new Pdf3D().createPdf(RESULT);
    }

    /**
     * Creates the PDF.
     * @param  filename   the path to the resulting PDF file
     * @throws DocumentException
     * @throws IOException
     */
    public void createPdf(String filename) throws IOException, DocumentException {
        // step 1
    	Document document = new Document();
    	// step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
        // step 3
        document.open();
        // step 4
        Rectangle rect = new Rectangle(100, 400, 500, 800);
        rect.setBorder(Rectangle.BOX);
        rect.setBorderWidth(0.5f);
        rect.setBorderColor(new BaseColor(0xFF, 0x00, 0x00));
        document.add(rect);
            
        PdfStream stream3D = new PdfStream(new FileInputStream(RESOURCE), writer);
        stream3D.put(PdfName.TYPE, new PdfName("3D"));
        stream3D.put(PdfName.SUBTYPE, new PdfName("U3D"));
        stream3D.flateCompress();
        PdfIndirectObject streamObject = writer.addToBody(stream3D);
        stream3D.writeLength();
            
        PdfDictionary dict3D = new PdfDictionary();
        dict3D.put(PdfName.TYPE, new PdfName("3DView"));
        dict3D.put(new PdfName("XN"), new PdfString("Default"));
        dict3D.put(new PdfName("IN"), new PdfString("Unnamed"));
        dict3D.put(new PdfName("MS"), PdfName.M);
        dict3D.put(new PdfName("C2W"),
            new PdfArray(new float[] { 1, 0, 0, 0, 0, -1, 0, 1, 0, 3, -235, 28 } ));
        dict3D.put(PdfName.CO, new PdfNumber(235));

        PdfIndirectObject dictObject = writer.addToBody(dict3D); 
            
        PdfAnnotation annot = new PdfAnnotation(writer, rect);
        annot.put(PdfName.CONTENTS, new PdfString("3D Model"));
        annot.put(PdfName.SUBTYPE, new PdfName("3D"));
        annot.put(PdfName.TYPE, PdfName.ANNOT);
        annot.put(new PdfName("3DD"), streamObject.getIndirectReference());
        annot.put(new PdfName("3DV"), dictObject.getIndirectReference());
        PdfAppearance ap = writer.getDirectContent().createAppearance(rect.getWidth(), rect.getHeight());
        annot.setAppearance(PdfAnnotation.APPEARANCE_NORMAL, ap);
        annot.setPage();

        writer.addAnnotation(annot);
        // step 5
        document.close();
    }
}
