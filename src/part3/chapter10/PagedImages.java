/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part3.chapter10;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;
import com.itextpdf.text.pdf.codec.GifImage;
import com.itextpdf.text.pdf.codec.JBIG2Image;
import com.itextpdf.text.pdf.codec.TiffImage;

public class PagedImages {
    
    /** The resulting PDF file. */
    public static final String RESULT
        = "results/part3/chapter10/tiff_jbig2_gif.pdf";
    /** One of the resources. */
    public static final String RESOURCE1
        = "resources/img/marbles.tif";
    /** One of the resources. */
    public static final String RESOURCE2
        = "resources/img/amb.jb2";
    /** One of the resources. */
    public static final String RESOURCE3
        = "resources/img/animated_fox_dog.gif";

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
        // step 4
        addTif(document, RESOURCE1);
        document.newPage();
        addJBIG2(document, RESOURCE2);
        document.newPage();
        addGif(document, RESOURCE3);
        // step 5
        document.close();
    }
    
    /**
     * Adds the different pages inside a single TIFF file to a document.
     * @param document the document to which the pages have to be added
     * @param path     the path to the TIFF file
     * @throws DocumentException
     * @throws IOException
     */
    public static void addTif(Document document, String path) throws DocumentException, IOException {
        RandomAccessFileOrArray ra = new RandomAccessFileOrArray(RESOURCE1);
        int n = TiffImage.getNumberOfPages(ra);
        Image img;
        for (int i = 1; i <= n; i++) {
            img = TiffImage.getTiffImage(ra, i);
            img.scaleToFit(523, 350);
            document.add(img);
        }
    }
    
    /**
     * Adds the different pages inside a JBIG2 file to a document.
     * @param document the document to which the pages have to be added
     * @param path     the path to the JBIG2 file
     * @throws IOException
     * @throws DocumentException
     */
    public static void addJBIG2(Document document, String path) throws IOException, DocumentException {
        RandomAccessFileOrArray ra = new RandomAccessFileOrArray(RESOURCE2);
        int n = JBIG2Image.getNumberOfPages(ra);
        Image img;
        for (int i = 1; i <= n; i++) {
            img = JBIG2Image.getJbig2Image(ra, i);
            img.scaleToFit(523, 350);
            document.add(img);
        }
    }
    
    /**
     * Adds the different frames inside an animated GIF to a document.
     * @param document the document to which the frames have to be added
     * @param path     the path to the GIF file
     * @throws IOException
     * @throws DocumentException
     */
    public static void addGif(Document document, String path) throws IOException, DocumentException {
        GifImage img = new GifImage(RESOURCE3);
        int n = img.getFrameCount();
        for (int i = 1; i <= n; i++) {
            document.add(img.getImage(i));
        }
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
        new PagedImages().createPdf(RESULT);
    }
}
