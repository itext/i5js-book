/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */
package part4.chapter16;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Utilities;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfBorderDictionary;
import com.itextpdf.text.pdf.PdfDeveloperExtension;
import com.itextpdf.text.pdf.PdfFileSpecification;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfIndirectReference;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PushbuttonField;
import com.itextpdf.text.pdf.TextField;
import com.itextpdf.text.pdf.richmedia.RichMediaActivation;
import com.itextpdf.text.pdf.richmedia.RichMediaAnnotation;
import com.itextpdf.text.pdf.richmedia.RichMediaCommand;
import com.itextpdf.text.pdf.richmedia.RichMediaConfiguration;
import com.itextpdf.text.pdf.richmedia.RichMediaExecuteAction;
import com.itextpdf.text.pdf.richmedia.RichMediaInstance;


public class FestivalCalendar2 {

    /** The resulting PDF file. */
    public static final String RESULT = "results/part4/chapter16/festival_calendar2.pdf";
    /** The path to a Flash application. */
    public static final String RESOURCE = "resources/swf/FestivalCalendar2.swf";
    /** The path to a JavaScript file. */
    public static final String JS = "resources/js/show_date.js";

    /**
     * Main method.
     *
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException
     */
    public static void main(String[] args) throws IOException, DocumentException {
        // step 1
    	Document document = new Document();
    	// step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(RESULT));
        writer.setPdfVersion(PdfWriter.PDF_VERSION_1_7);
        writer.addDeveloperExtension(PdfDeveloperExtension.ADOBE_1_7_EXTENSIONLEVEL3);
        // step 3
        document.open();
        // step 4
        writer.addJavaScript(Utilities.readFileToString(JS));
        // we prepare a RichMediaAnnotation
        RichMediaAnnotation richMedia = new RichMediaAnnotation(writer, new Rectangle(36, 560, 561, 760));
        // we embed the swf file
        PdfFileSpecification fs
            = PdfFileSpecification.fileEmbedded(writer, RESOURCE, "FestivalCalendar2.swf", null);
        // we declare the swf file as an asset
        PdfIndirectReference asset = richMedia.addAsset("FestivalCalendar2.swf", fs);
        // we create a configuration
        RichMediaConfiguration configuration = new RichMediaConfiguration(PdfName.FLASH);
        RichMediaInstance instance = new RichMediaInstance(PdfName.FLASH);
        instance.setAsset(asset);
        configuration.addInstance(instance);
        // we add the configuration to the annotation
        PdfIndirectReference configurationRef = richMedia.addConfiguration(configuration);
        // activation of the rich media
        RichMediaActivation activation = new RichMediaActivation();
        activation.setConfiguration(configurationRef);
        richMedia.setActivation(activation);
        // we add the annotation
        PdfAnnotation richMediaAnnotation = richMedia.createAnnotation();
        richMediaAnnotation.setFlags(PdfAnnotation.FLAGS_PRINT);
        writer.addAnnotation(richMediaAnnotation);
        
        String[] days = new String[]{"2011-10-12", "2011-10-13", "2011-10-14", "2011-10-15",
                "2011-10-16", "2011-10-17", "2011-10-18", "2011-10-19"};
        for (int i = 0; i < days.length; i++) {
            Rectangle rect = new Rectangle(36 + (65 * i), 765, 100 + (65 * i), 780);
            PushbuttonField button = new PushbuttonField(writer, rect, "button" + i);
            button.setBackgroundColor(new GrayColor(0.75f));
            button.setBorderStyle(PdfBorderDictionary.STYLE_BEVELED);
            button.setTextColor(GrayColor.GRAYBLACK);
            button.setFontSize(12);
            button.setText(days[i]);
            button.setLayout(PushbuttonField.LAYOUT_ICON_LEFT_LABEL_RIGHT);
            button.setScaleIcon(PushbuttonField.SCALE_ICON_ALWAYS);
            button.setProportionalIcon(true);
            button.setIconHorizontalAdjustment(0);
            PdfFormField field = button.getField();
            RichMediaCommand command = new RichMediaCommand(new PdfString("getDateInfo"));
            command.setArguments(new PdfString(days[i]));
            RichMediaExecuteAction action
                = new RichMediaExecuteAction(richMediaAnnotation.getIndirectReference(), command);
            field.setAction(action);
            writer.addAnnotation(field);
        }
        TextField text = new TextField(writer, new Rectangle(36, 785, 559, 806), "date");
        text.setOptions(TextField.READ_ONLY);
        writer.addAnnotation(text.getTextField());
        // step 5
        document.close();
    }
}
