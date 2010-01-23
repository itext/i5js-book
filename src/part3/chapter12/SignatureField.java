/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part3.chapter12;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.util.Properties;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfAppearance;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

public class SignatureField {
	
	public static String ORIGINAL = "results/part3/chapter12/unsigned.pdf";
	public static String SIGNED1 = "results/part3/chapter12/signed_1.pdf";
	public static String SIGNED2 = "results/part3/chapter12/signed_2.pdf";

    /** One of the resources. */
    public static final String RESOURCE
        = "resources/img/1t3xt.gif";

	public static String PATH = "c:/home/blowagie/key.properties";
	public static Properties properties = new Properties();
	
    /**
     * Creates a PDF document.
     * @param filename the path to the new PDF document
     * @throws DocumentException 
     * @throws IOException 
     */
	public void createPdf(String filename) throws IOException, DocumentException {
		Document document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
		document.open();
		document.add(new Paragraph("Hello World!"));
		PdfFormField field = PdfFormField.createSignature(writer);
		field.setWidget(new Rectangle(72, 732, 144, 780), PdfAnnotation.HIGHLIGHT_INVERT);
		field.setFieldName("mySig");
		field.setFlags(PdfAnnotation.FLAGS_PRINT);
		field.setPage();
		field.setMKBorderColor(BaseColor.BLACK);
        field.setMKBackgroundColor(BaseColor.WHITE);
        PdfAppearance tp = PdfAppearance.createAppearance(writer, 72, 48);
        tp.rectangle(0.5f, 0.5f, 71.5f, 47.5f);
        tp.stroke();
        field.setAppearance(PdfAnnotation.APPEARANCE_NORMAL, tp);
        writer.addAnnotation(field);
		document.close();
	}

    /**
     * Manipulates a PDF file src with the file dest as result
     * @param src the original PDF
     * @param dest the resulting PDF
     * @throws IOException
     * @throws DocumentException
     * @throws GeneralSecurityException 
     */
	public void signPdf(String src, String dest, boolean certified, boolean graphic) throws IOException, DocumentException, GeneralSecurityException {
		String path = properties.getProperty("PRIVATE");
		String keystore_password = properties.getProperty("PASSWORD");
		String key_password = properties.getProperty("PASSWORD");
		KeyStore ks = KeyStore.getInstance("pkcs12", "BC");
		ks.load(new FileInputStream(path), keystore_password.toCharArray());
		String alias = (String)ks.aliases().nextElement();
		PrivateKey pk = (PrivateKey)ks.getKey(alias, key_password.toCharArray());
		Certificate[] chain = ks.getCertificateChain(alias);

		PdfReader reader = new PdfReader(ORIGINAL);
		PdfStamper stamper = PdfStamper.createSignature(reader, new FileOutputStream(dest), '\0');
		PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
		appearance.setVisibleSignature("mySig");
		appearance.setReason("It's personal.");
		appearance.setLocation("Foobar");
		appearance.setCrypto(pk, chain, null, PdfSignatureAppearance.SELF_SIGNED);
		if (certified)
			appearance.setCertificationLevel(PdfSignatureAppearance.CERTIFIED_NO_CHANGES_ALLOWED);
		if (graphic) {
			appearance.setSignatureGraphic(Image.getInstance(RESOURCE));
			appearance.setRenderingMode(PdfSignatureAppearance.RenderingMode.GRAPHIC);
		}
		stamper.close();
	}
	
    /**
     * Main method.
     *
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException
     * @throws GeneralSecurityException 
     */
	public static void main(String[] args) throws IOException, DocumentException, GeneralSecurityException {
		Security.addProvider(new BouncyCastleProvider());
		properties.load(new FileInputStream(PATH));
		SignatureField signatures = new SignatureField();
		signatures.createPdf(ORIGINAL);
		signatures.signPdf(ORIGINAL, SIGNED1, false, false);
		signatures.signPdf(ORIGINAL, SIGNED2, true, true);
	}
}
