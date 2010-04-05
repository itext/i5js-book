package part4.chapter16;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfDeveloperExtension;
import com.itextpdf.text.pdf.PdfFileSpecification;
import com.itextpdf.text.pdf.PdfIndirectReference;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.richmedia.RichMediaActivation;
import com.itextpdf.text.pdf.richmedia.RichMediaAnnotation;
import com.itextpdf.text.pdf.richmedia.RichMediaConfiguration;
import com.itextpdf.text.pdf.richmedia.RichMediaInstance;
import com.itextpdf.text.pdf.richmedia.RichMediaParams;


public class FestivalCalendar1 {
	public static final String RESULT = "results/part4/chapter16/festival_calendar1.pdf";
	public static final String RESOURCE = "resources/swf/FestivalCalendar1.swf";

	public static void main(String[] args) throws IOException, DocumentException {
		
		Document document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(RESULT));
		writer.setPdfVersion(PdfWriter.PDF_VERSION_1_7);
		writer.addDeveloperExtension(PdfDeveloperExtension.ADOBE_1_7_EXTENSIONLEVEL3);
		document.open();
		// we prepare a RichMediaAnnotation
		RichMediaAnnotation richMedia = new RichMediaAnnotation(writer, new Rectangle(36, 400, 559,806));
		// we embed the swf file
		PdfFileSpecification fs = PdfFileSpecification.fileEmbedded(writer, RESOURCE, "FestivalCalendar1.swf", null);
		// we declare the swf file as an asset
		PdfIndirectReference asset = richMedia.addAsset("FestivalCalendar1.swf", fs);
		// we create a configuration
		RichMediaConfiguration configuration = new RichMediaConfiguration(PdfName.FLASH);
		RichMediaInstance instance = new RichMediaInstance(PdfName.FLASH);
		RichMediaParams flashVars = new RichMediaParams();
		String vars = new String("&day=2011-10-13");
		flashVars.setFlashVars(vars);
		instance.setParams(flashVars);
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
        
		document.close();
	}
}
