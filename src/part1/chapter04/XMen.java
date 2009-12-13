/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part1.chapter04;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class XMen {

    /** The resulting PDF file. */
    public static final String RESULT = "results/part1/chapter04/x_men.pdf";
    /** Path to the resources. */
    public static final String RESOURCE = "resources/posters/%s.jpg";
    
    /**
     * Creates a PDF with information about the movies
     * @param    filename the name of the PDF file that will be created.
     * @throws    DocumentException 
     * @throws    IOException
     */
    public void createPdf(String filename)
        throws IOException, DocumentException {
    	// step 1
        Document document = new Document(PageSize.A4.rotate());
        // step 2
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        // step 3
        document.open();
        // step 4
        // we'll use 4 images in this example
        Image[] img = {
                Image.getInstance(String.format(RESOURCE, "0120903")),
                Image.getInstance(String.format(RESOURCE, "0290334")),
                Image.getInstance(String.format(RESOURCE, "0376994")),
                Image.getInstance(String.format(RESOURCE, "0348150"))
        };
        // Creates a table with 6 columns
        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        // first movie
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP);
        table.addCell("X-Men");
        // we wrap he image in a PdfPCell
        PdfPCell cell = new PdfPCell(img[0]);
        table.addCell(cell);
        // second movie
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell("X2");
        // we wrap the image in a PdfPCell and let iText scale it
        cell = new PdfPCell(img[1], true);
        table.addCell(cell);
        // third movie
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
        table.addCell("X-Men: The Last Stand");
        // we add the image with addCell()
        table.addCell(img[2]);
        // fourth movie
        table.addCell("Superman Returns");
        cell = new PdfPCell();
        // we add it with addElement(); it can only take 50% of the width.
        img[3].setWidthPercentage(50);
        cell.addElement(img[3]);
        table.addCell(cell);
        // we complete the table (otherwise the last row won't be rendered)
        table.completeRow();
        document.add(table);
        document.close();
    }

    /**
     * Main method.
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException
     */
    public static void main(String[] args) throws IOException, DocumentException {
        new XMen().createPdf(RESULT);
    }
}
