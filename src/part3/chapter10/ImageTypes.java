/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part3.chapter10;

import java.awt.Toolkit;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.Barcode;
import com.itextpdf.text.pdf.BarcodeEAN;
import com.itextpdf.text.pdf.BarcodePDF417;
import com.itextpdf.text.pdf.PdfWriter;

public class ImageTypes {

    /** The resulting PDF file. */
    public static final String RESULT
        = "results/part3/chapter10/image_types.pdf";
    /** Paths to images. */
    public static final String[] RESOURCES = {
        "bruno_ingeborg.jpg",
        "map.jp2",
        "info.png",
        "close.bmp",
        "movie.gif",
        "butterfly.wmf",
        "animated_fox_dog.gif",
        "marbles.tif",
        "amb.jb2"
    };
    /** Path to an image. */
    public static final String RESOURCE = "resources/img/hitchcock.png";

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
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
        // step 3
        document.open();
        // step 4
        // Adding a series of images
        Image img;
        for (int i = 0; i < RESOURCES.length; i++) {
            img = Image.getInstance(String.format("resources/img/%s", RESOURCES[i]));
            if (img.getScaledWidth() > 300 || img.getScaledHeight() > 300) {
                img.scaleToFit(300, 300);
            }
            document.add(new Paragraph(
                    String.format("%s is an image of type %s", RESOURCES[i], img.getClass().getName())));
            document.add(img);
        }
        // Adding a java.awt.Image
        java.awt.Image awtImage = Toolkit.getDefaultToolkit().createImage(RESOURCE);
        img = com.itextpdf.text.Image.getInstance(awtImage, null);
        document.add(new Paragraph(
                String.format("%s is an image of type %s", "java.awt.Image", img.getClass().getName())));
        document.add(img);
        // Adding a barcode
        BarcodeEAN codeEAN = new BarcodeEAN();
        codeEAN.setCodeType(Barcode.EAN13);
        codeEAN.setCode("9781935182610");
        img = codeEAN.createImageWithBarcode(writer.getDirectContent(), null, null);
        document.add(new Paragraph(
                String.format("%s is an image of type %s", "barcode", img.getClass().getName())));
        document.add(img);
        // Adding a matrix code
        BarcodePDF417 pdf417 = new BarcodePDF417();
        String text = "iText in Action, a book by Bruno Lowagie.";
        pdf417.setText(text);
        img = pdf417.getImage();
        document.add(new Paragraph(
                String.format("%s is an image of type %s", "barcode", img.getClass().getName())));
        document.add(img);

        // step 5
        document.close();
    }


    /**
     * Main method.
     *
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException 
     * @throws DocumentException 
     * @throws IOException
     */
    public static void main(String[] args) throws IOException, DocumentException {
        new ImageTypes().createPdf(RESULT);
    }
}
