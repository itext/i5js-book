package part3.chapter12;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Properties;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfPKCS7;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

public class Signatures {
	public static String ORIGINAL = "results/part3/chapter12/hello.pdf";
	public static String SIGNED1 = "results/part3/chapter12/signature_1.pdf";
	public static String SIGNED2 = "results/part3/chapter12/signature_2.pdf";
	public static String REVISION = "results/part3/chapter12/revision_1.pdf";

	public static String PATH = "c:/home/blowagie/key.properties";
	public static Properties properties = new Properties();
    /** One of the resources. */
    public static final String RESOURCE = "resources/img/logo.gif";
    
    /**
     * Creates a PDF document.
     * @param filename the path to the new PDF document
     * @throws DocumentException 
     * @throws IOException 
     */
	public void createPdf(String filename) throws IOException, DocumentException {
		Document document = new Document();
		PdfWriter.getInstance(document, new FileOutputStream(filename));
		document.open();
		document.add(new Paragraph("Hello World!"));
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
	public void signPdfFirstTime(String src, String dest) throws IOException, DocumentException, GeneralSecurityException {
		String path = properties.getProperty("PRIVATE");
		String keystore_password = properties.getProperty("PASSWORD");
		String key_password = properties.getProperty("PASSWORD");
		KeyStore ks = KeyStore.getInstance("pkcs12", "BC");
		ks.load(new FileInputStream(path), keystore_password.toCharArray());
		String alias = (String)ks.aliases().nextElement();
		PrivateKey key = (PrivateKey) ks.getKey(alias, key_password.toCharArray());
		Certificate[] chain = ks.getCertificateChain(alias);
		PdfReader reader = new PdfReader(src);
		FileOutputStream os = new FileOutputStream(dest);
		PdfStamper stamper = PdfStamper.createSignature(reader, os, '\0');
		PdfSignatureAppearance appearance = stamper
				.getSignatureAppearance();
		appearance.setCrypto(key, chain, null,
				PdfSignatureAppearance.SELF_SIGNED);
		appearance.setImage(Image.getInstance(RESOURCE));
		appearance.setReason("I've written this.");
		appearance.setLocation("Foobar");
		appearance.setVisibleSignature(new Rectangle(72, 732, 144, 780), 1,	"first");
		stamper.close();
	}
	
    /**
     * Manipulates a PDF file src with the file dest as result
     * @param src the original PDF
     * @param dest the resulting PDF
     * @throws IOException
     * @throws DocumentException
     * @throws GeneralSecurityException 
     */
	public void signPdfSecondTime(String src, String dest) throws IOException, DocumentException, GeneralSecurityException {
		String path = "resources/encryption/.keystore";
		String keystore_password = "f00b4r";
		String key_password = "f1lmf3st";
		String alias = "foobar";
		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
		ks.load(new FileInputStream(path), keystore_password.toCharArray());
		PrivateKey key = (PrivateKey) ks.getKey(alias, key_password.toCharArray());
		Certificate[] chain = ks.getCertificateChain(alias);
		PdfReader reader = new PdfReader(src);
		FileOutputStream os = new FileOutputStream(dest);
		PdfStamper stamper = PdfStamper.createSignature(reader, os, '\0', null, true);
		PdfSignatureAppearance appearance = stamper
				.getSignatureAppearance();
		appearance.setCrypto(key, chain, null,
				PdfSignatureAppearance.SELF_SIGNED);
		appearance.setReason("I'm approving this.");
		appearance.setLocation("Foobar");
		appearance.setVisibleSignature(new Rectangle(160, 732, 232, 780), 1, "second");
		stamper.close();

	}
	
	public void verifySecondSignature() throws GeneralSecurityException, IOException {
		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
		ks.load(null, null);
		CertificateFactory cf = CertificateFactory.getInstance("X509");
		FileInputStream is = new FileInputStream("resources/encryption/foobar.cer");
		X509Certificate cert = (X509Certificate) cf.generateCertificate(is);
		ks.setCertificateEntry("foobar", cert);
		
		PdfReader reader = new PdfReader(SIGNED2);
		AcroFields af = reader.getAcroFields();
		String name = "second";
		System.out.println("Signature covers whole document: " + af.signatureCoversWholeDocument(name));
		System.out.println("Document revision: " + af.getRevision(name) + " of " + af.getTotalRevisions());
		PdfPKCS7 pk = af.verifySignature(name);
		Calendar cal = pk.getSignDate();
		Certificate pkc[] = pk.getCertificates();
		System.out.println("Subject: " + PdfPKCS7.getSubjectFields(pk.getSigningCertificate()));
		System.out.println("Document modified: " + !pk.verify());
		Object fails[] = PdfPKCS7.verifyCertificates(pkc, ks, null, cal);
		if (fails == null)
			System.out.println("Certificates verified against the KeyStore");
		else
			System.out.println("Certificate failed: " + fails[1]);	
	}
	
	public void extractFirstRevision() throws IOException {
		PdfReader reader = new PdfReader(SIGNED2);
		AcroFields af = reader.getAcroFields();
		String name = "first";
		FileOutputStream os = new FileOutputStream(REVISION);
		byte bb[] = new byte[8192];
		InputStream ip = af.extractRevision(name);
		int n = 0;
		while ((n = ip.read(bb)) > 0)
			os.write(bb, 0, n);
		os.close();
		ip.close();
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
		Signatures signatures = new Signatures();
		signatures.createPdf(ORIGINAL);
		signatures.signPdfFirstTime(ORIGINAL, SIGNED1);
		signatures.signPdfSecondTime(SIGNED1, SIGNED2);
		signatures.verifySecondSignature();
		signatures.extractFirstRevision();
	}
}
