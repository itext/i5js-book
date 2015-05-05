/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */
package part3.chapter12;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.util.Properties;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.security.ExternalDigest;
import com.itextpdf.text.pdf.security.ExternalSignature;
import com.itextpdf.text.pdf.security.MakeSignature.CryptoStandard;
import com.itextpdf.text.pdf.security.BouncyCastleDigest;
import com.itextpdf.text.pdf.security.PrivateKeySignature;
import com.itextpdf.text.pdf.security.MakeSignature;

public class SignatureExternalHash {

    /**
     * A properties file that is PRIVATE.
     * You should make your own properties file and adapt this line.
     */
    public static String PATH = "c:/home/blowagie/key.properties";
    /** Some properties used when signing. */
    public static Properties properties = new Properties();

    /** The resulting PDF */
    public static String SIGNED1 = "results/part3/chapter12/externalhash_1.pdf";
    /** The resulting PDF */
    public static String SIGNED2 = "results/part3/chapter12/externalhash_2.pdf";
    /** The resulting PDF */
    public static String SIGNED3 = "results/part3/chapter12/externalhash_3.pdf";

    /**
     * Manipulates a PDF file src with the file dest as result
     * @param src the original PDF
     * @param dest the resulting PDF
     * @throws GeneralSecurityException 
     * @throws IOException 
     * @throws DocumentException 
     * @throws FileNotFoundException 
     * @throws KeyStoreException 
     * @throws Exception 
     */
    public void signPdfDetached(String src, String dest) throws GeneralSecurityException, IOException, DocumentException {
    	// Private key and certificate
        String path = properties.getProperty("PRIVATE");
        String keystore_password = properties.getProperty("PASSWORD");
        String key_password = properties.getProperty("PASSWORD");
        KeyStore ks = KeyStore.getInstance("pkcs12", "BC");
        ks.load(new FileInputStream(path), keystore_password.toCharArray());
        String alias = (String)ks.aliases().nextElement();
        PrivateKey pk = (PrivateKey) ks.getKey(alias, key_password.toCharArray());
        Certificate[] chain = ks.getCertificateChain(alias);
        
        // reader and stamper
        PdfReader reader = new PdfReader(src);
        FileOutputStream os = new FileOutputStream(dest);
        PdfStamper stamper = PdfStamper.createSignature(reader, os, '\0');
        
        // appearance
        PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
        appearance.setReason("External hash example");
        appearance.setLocation("Foobar");
        appearance.setVisibleSignature(new Rectangle(72, 732, 144, 780), 1, "sig");
        
        // digital signature
        ExternalSignature es = new PrivateKeySignature(pk, "SHA-256", "BC");
        ExternalDigest digest = new BouncyCastleDigest();
        MakeSignature.signDetached(appearance, digest, es, chain, null, null, null, 0, CryptoStandard.CMS);
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
        new Signatures().createPdf(Signatures.ORIGINAL);
        SignatureExternalHash signatures = new SignatureExternalHash();
        signatures.signPdfDetached(Signatures.ORIGINAL, SIGNED1);
    }
}
