/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part1.chapter03;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.PdfWriter;

public class ImageDirect {

    /** The resulting PDF. */
    public static final String RESULT
        = "results/part1/chapter03/image_direct.pdf";
    /** The movie poster. */
    public static final String RESOURCE
        = "resources/img/loa.jpg";
    
    public static void main(String[] args)
        throws IOException, DocumentException {
    	// step 1
        Document document
            = new Document(PageSize.POSTCARD, 30, 30, 30, 30);
        // step 2
        PdfWriter writer
            = PdfWriter.getInstance(document, new FileOutputStream(RESULT));
        writer.setCompressionLevel(0);
        // step 3
        document.open();
        // step 4
        Image img = Image.getInstance(RESOURCE);
        img.setAbsolutePosition((PageSize.POSTCARD.getWidth() - img.getScaledWidth()) / 2,
                (PageSize.POSTCARD.getHeight() - img.getScaledHeight()) / 2);
        writer.getDirectContent().addImage(img);
        Paragraph p = new Paragraph("Foobar Film Festival", new Font(FontFamily.HELVETICA, 22));
        p.setAlignment(Element.ALIGN_CENTER);
        document.add(p);
        // step 5
        document.close();
    }
}
