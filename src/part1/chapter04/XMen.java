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

    public static final String RESULT = "results/part1/chapter04/x_men.pdf";
    public static final String RESOURCE = "resources/posters/%s.jpg";
    
    public static void main(String[] args) throws IOException, DocumentException {
        new XMen().createPdf(RESULT);
    }
    
    /**
     * Creates a PDF with information about the movies
     * @param    filename the name of the PDF file that will be created.
     * @throws    DocumentException 
     * @throws    IOException 
     * @throws    SQLException
     */
    public void createPdf(String filename)
        throws IOException, DocumentException {
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        document.open();
        Image[] img = {
                Image.getInstance(String.format(RESOURCE, "0120903")),
                Image.getInstance(String.format(RESOURCE, "0290334")),
                Image.getInstance(String.format(RESOURCE, "0376994")),
                Image.getInstance(String.format(RESOURCE, "0348150"))
        };
        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP);
        table.addCell("X-Men");
        PdfPCell cell = new PdfPCell(img[0]);
        table.addCell(cell);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell("X2");
        cell = new PdfPCell(img[1], true);
        table.addCell(cell);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
        table.addCell("X-Men: The Last Stand");
        table.addCell(img[2]);
        table.addCell("Superman Returns");
        cell = new PdfPCell();
        img[3].setWidthPercentage(50);
        cell.addElement(img[3]);
        table.addCell(cell);
        table.completeRow();
        document.add(table);
        document.close();
    }
}
