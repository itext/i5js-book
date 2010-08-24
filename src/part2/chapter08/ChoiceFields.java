/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part2.chapter08;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfBorderDictionary;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.TextField;

public class ChoiceFields implements PdfPCellEvent {

    /** The resulting PDF. */
    public static final String RESULT1 = "results/part2/chapter08/choice_fields.pdf";
    /** The resulting PDF. */
    public static final String RESULT2 = "results/part2/chapter08/choice_filled.pdf";
    /** A choice field index. */
    protected int cf;
    /** An array with possible languages for a choice field. */
    public static final String[] LANGUAGES =
        { "English", "German", "French", "Spanish", "Dutch" };
    /** An array with export values for possible languages in a choice field. */
    public static final String[] EXPORTVALUES =
        { "EN", "DE", "FR", "ES", "NL" };

    /**
     * Creates a cell event that adds a Choice field to a cell.
     * @param cf a choice field index
     */
    public ChoiceFields(int cf) {
        this.cf = cf;
    }

    /**
     * Manipulates a PDF file src with the file dest as result
     * @param src the original PDF
     * @param dest the resulting PDF
     * @throws IOException
     * @throws DocumentException
     */
    public void manipulatePdf(String src, String dest) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        AcroFields form = stamper.getAcroFields();
        form.setField("choice_1", "NL");
        form.setListSelection("choice_2", new String[]{"German", "Spanish"});
        String[] languages = form.getListOptionDisplay("choice_3");
        String[] exportvalues = form.getListOptionExport("choice_3");
        int n = languages.length;
        String[] new_languages = new String[n + 2];
        String[] new_exportvalues = new String[n + 2];
        for (int i = 0; i < n; i++) {
            new_languages[i] = languages[i];
            new_exportvalues[i] = exportvalues[i];
        }
        new_languages[n] = "Chinese";
        new_exportvalues[n] = "CN";
        new_languages[n + 1] = "Japanese";
        new_exportvalues[n + 1] = "JP";
        form.setListOption("choice_3", new_exportvalues, new_languages);
        form.setField("choice_3", "CN");
        form.setField("choice_4", "Japanese");
        stamper.close();
    }

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
        PdfPCell cell;
        PdfPCell space;
        space = new PdfPCell();
        space.setBorder(Rectangle.NO_BORDER);
        space.setColspan(2);
        space.setFixedHeight(8);
        PdfPTable table = new PdfPTable(2);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell("Language of the movie:");
        cell = new PdfPCell();
        cell.setCellEvent(new ChoiceFields(1));
        table.addCell(cell);
        table.addCell(space);
        table.addCell("Subtitle languages:");
        cell = new PdfPCell();
        cell.setCellEvent(new ChoiceFields(2));
        cell.setFixedHeight(70);
        table.addCell(cell);
        table.addCell(space);
        table.addCell("Select preferred language:");
        cell = new PdfPCell();
        cell.setCellEvent(new ChoiceFields(3));
        table.addCell(cell);
        table.addCell(space);
        table.addCell("Language of the director:");
        cell = new PdfPCell();
        cell.setCellEvent(new ChoiceFields(4));
        table.addCell(cell);
        document.add(table);
        // step 5
        document.close();

    }

    /**
     * Creates a choice field and adds it to a cell.
     * see com.itextpdf.text.pdf.PdfPCellEvent#cellLayout(com.itextpdf.text.pdf.PdfPCell, 
     *     com.itextpdf.text.Rectangle, com.itextpdf.text.pdf.PdfContentByte[])
     */
    public void cellLayout(PdfPCell cell, Rectangle rectangle,
            PdfContentByte[] canvases) {
        PdfWriter writer = canvases[0].getPdfWriter();
        TextField text = new TextField(writer, rectangle,
                String.format("choice_%s", cf));
        try {
            switch(cf) {
            case 1:
                text.setChoices(LANGUAGES);
                text.setChoiceExports(EXPORTVALUES);
                text.setChoiceSelection(2);
                writer.addAnnotation(text.getListField());
                break;
            case 2:
                text.setChoices(LANGUAGES);
                text.setBorderColor(BaseColor.GREEN);
                text.setBorderStyle(PdfBorderDictionary.STYLE_DASHED);
                text.setOptions(TextField.MULTISELECT);
                ArrayList<Integer> selections = new ArrayList<Integer>();
                selections.add(0);
                selections.add(2);
                text.setChoiceSelections(selections);
                PdfFormField field = text.getListField();
                writer.addAnnotation(field);
                break;
            case 3:
                text.setBorderColor(BaseColor.RED);
                text.setBackgroundColor(BaseColor.GRAY);
                text.setChoices(LANGUAGES);
                text.setChoiceExports(EXPORTVALUES);
                text.setChoiceSelection(4);
                writer.addAnnotation(text.getComboField());
                break;
            case 4:
                text.setChoices(LANGUAGES);
                text.setOptions(TextField.EDIT);
                writer.addAnnotation(text.getComboField());
                break;
            }
        }
        catch(IOException ioe) {
            throw new ExceptionConverter(ioe);
        }
        catch(DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }
    
    /**
     * Main method
     * @param args no arguments needed
     * @throws IOException
     * @throws DocumentException
     */
    public static void main(String[] args) throws IOException, DocumentException {
        ChoiceFields fields = new ChoiceFields(0);
        fields.createPdf(RESULT1);
        fields.manipulatePdf(RESULT1, RESULT2);
    }
}
