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

public class EncryptWithCertificate {

    /** The resulting PDF */
    public static String RESULT1 = "results/part3/chapter12/certificate_encryption.pdf";
    /** The resulting PDF */
    public static String RESULT2 = "results/part3/chapter12/certificate_decrypted.pdf";
    /** The resulting PDF */
    public static String RESULT3 = "results/part3/chapter12/certificate_encrypted.pdf";

    /**
     * A properties file that is PRIVATE.
     * You should make your own properties file and adapt this line.
     */
    public static String PATH = "c:/home/blowagie/key.properties";
    /** Some properties used when signing. */
    public static Properties properties = new Properties();
    
    /**
     * Creates a PDF that is encrypted using two different public certificates.
     * @param filename the path to the resulting PDF file
     * @throws IOException
     * @throws DocumentException
     * @throws GeneralSecurityException
     */
    public void createPdf(String filename)
        throws IOException, DocumentException, GeneralSecurityException {
        // step 1
        Document document = new Document();
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(RESULT1));
        Certificate cert1 = getPublicCertificate("resources/encryption/foobar.cer");
        Certificate cert2 = getPublicCertificate(properties.getProperty("PUBLIC"));
        writer.setEncryption(new Certificate[]{cert1, cert2},
            new int[]{PdfWriter.ALLOW_PRINTING, PdfWriter.ALLOW_COPY}, PdfWriter.ENCRYPTION_AES_128);
        // step 3
        document.open();
        // step 4
        document.add(new Paragraph("Hello World!"));
        // step 5
        document.close();
    }
    
    /**
     * Gets a public certificate from a certificate file.
     * @param path the path to the certificate
     * @return a Certificate object
     * @throws IOException
     * @throws CertificateException
     */
    public Certificate getPublicCertificate(String path)
        throws IOException, CertificateException {
        FileInputStream is = new FileInputStream(path);
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509Certificate cert = (X509Certificate) cf.generateCertificate(is);
        return cert;
    }
    
    /**
     * Gets a private key from a KeyStore.
     * @return a PrivateKey object
     * @throws GeneralSecurityException
     * @throws IOException
     */
    public PrivateKey getPrivateKey() throws GeneralSecurityException, IOException {
        String path = "resources/encryption/.keystore";
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(new FileInputStream(path), "f00b4r".toCharArray());
        PrivateKey pk = (PrivateKey)ks.getKey("foobar", "f1lmf3st".toCharArray());
        return pk;
    }
    
    /**
     * Decrypts a PDF that was encrypted using a certificate
     * @param src  The encrypted PDF
     * @param dest The decrypted PDF
     * @throws IOException
     * @throws DocumentException
     * @throws GeneralSecurityException
     */
    public void decryptPdf(String src, String dest)
        throws IOException, DocumentException, GeneralSecurityException {
        PdfReader reader = new PdfReader(src,
            getPublicCertificate("resources/encryption/foobar.cer"), getPrivateKey(), "BC");
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        stamper.close();
    }
    
    /**
     * Encrypts a PDF using a public certificate.
     * @param src  The original PDF document
     * @param dest The encrypted PDF document
     * @throws IOException
     * @throws DocumentException
     * @throws CertificateException
     */
    public void encryptPdf(String src, String dest)
        throws IOException, DocumentException, CertificateException {
        PdfReader reader = new PdfReader(src);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        Certificate cert = getPublicCertificate("resources/encryption/foobar.cer");
        stamper.setEncryption(new Certificate[]{cert},
            new int[]{PdfWriter.ALLOW_PRINTING}, PdfWriter.ENCRYPTION_AES_128);
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
    public static void main(String[] args)
        throws IOException, DocumentException, GeneralSecurityException {
        Security.addProvider(new BouncyCastleProvider());
        properties.load(new FileInputStream(PATH));
        EncryptWithCertificate hello = new EncryptWithCertificate();
        hello.createPdf(RESULT1);
        hello.decryptPdf(RESULT1, RESULT2);
        hello.encryptPdf(RESULT2, RESULT3);
    }
}
