package part3.chapter12;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Properties;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

public class EncryptWithCertificate2 {

	public static String RESULT1 = "results/part3/chapter12/certificate_encryption2.pdf";
	public static String RESULT2 = "results/part3/chapter12/certificate_decrypted2.pdf";
	public static String RESULT3 = "results/part3/chapter12/certificate_encrypted2.pdf";
	
	public static String PATH = "c:/home/blowagie/key.properties";
	
	public static Properties properties = new Properties();
	
	public Certificate getPublicCertificate() throws IOException, CertificateException {
		FileInputStream is = new FileInputStream(properties.getProperty("PUBLIC"));
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
    	X509Certificate cert = (X509Certificate) cf.generateCertificate(is);
    	return cert;
	}
	
	public PrivateKey getPrivateKey() throws KeyStoreException, NoSuchProviderException, NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableKeyException {
		String path = properties.getProperty("PRIVATE");
		String keystore_password = properties.getProperty("PASSWORD");
		String key_password = properties.getProperty("PASSWORD");
		KeyStore ks = KeyStore.getInstance("pkcs12", "BC");
		ks.load(new FileInputStream(path), keystore_password.toCharArray());
		String alias = (String)ks.aliases().nextElement();
		PrivateKey pk = (PrivateKey)ks.getKey(alias, key_password.toCharArray());
		return pk;
	}
	
	public void createPdf(String filename) throws IOException, DocumentException, KeyStoreException, NoSuchProviderException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException {
        // step 1
        Document document = new Document();
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(RESULT1));
        Certificate cert = getPublicCertificate();
        writer.setEncryption(new Certificate[]{cert}, new int[]{PdfWriter.ALLOW_PRINTING}, PdfWriter.ENCRYPTION_AES_128);
        // step 3
        document.open();
        // step 4
        document.add(new Paragraph("Hello World!"));
        // step 5
        document.close();
	}
	
	public void decryptPdf(String src, String dest) throws IOException, DocumentException, CertificateException, KeyStoreException, NoSuchProviderException, NoSuchAlgorithmException, UnrecoverableKeyException {
		PdfReader reader = new PdfReader(src, getPublicCertificate(), getPrivateKey(), "BC");
		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
		stamper.close();
	}
	
	public void encryptPdf(String src, String dest) throws IOException, DocumentException, CertificateException {
		PdfReader reader = new PdfReader(src);
		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
		Certificate cert = getPublicCertificate();
		stamper.setEncryption(new Certificate[]{cert}, new int[]{PdfWriter.ALLOW_PRINTING}, PdfWriter.ENCRYPTION_AES_128);
		stamper.close();
	}
	
	public static void main(String[] args) throws IOException, DocumentException, KeyStoreException, NoSuchProviderException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException {
		Security.addProvider(new BouncyCastleProvider());
		properties.load(new FileInputStream(PATH));
		EncryptWithCertificate2 hello = new EncryptWithCertificate2();
		hello.createPdf(RESULT1);
		hello.decryptPdf(RESULT1, RESULT2);
		hello.encryptPdf(RESULT2, RESULT3);
	}
}
