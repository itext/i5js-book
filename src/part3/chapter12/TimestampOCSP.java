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

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.security.ExternalSignature;
import com.itextpdf.text.pdf.security.MakeSignature.CryptoStandard;
import com.itextpdf.text.pdf.security.BouncyCastleDigest;
import com.itextpdf.text.pdf.security.ExternalDigest;
import com.itextpdf.text.pdf.security.PrivateKeySignature;
import com.itextpdf.text.pdf.security.MakeSignature;
import com.itextpdf.text.pdf.security.OcspClient;
import com.itextpdf.text.pdf.security.OcspClientBouncyCastle;
import com.itextpdf.text.pdf.security.TSAClient;
import com.itextpdf.text.pdf.security.TSAClientBouncyCastle;

public class TimestampOCSP {

    /** The resulting PDF */
    public static String SIGNED0 = "results/part3/chapter12/without.pdf";
    /** The resulting PDF */
    public static String SIGNED1 = "results/part3/chapter12/ts.pdf";
    /** The resulting PDF */
    public static String SIGNED2 = "results/part3/chapter12/ocsp.pdf";
    /** The resulting PDF */
    public static String SIGNED3 = "results/part3/chapter12/ts_oscp.pdf";

    /**
     * A properties file that is PRIVATE.
     * You should make your own properties file and adapt this line.
     */
    public static String PATH = "c:/home/blowagie/key.properties";
    /** Some properties used when signing. */
    public static Properties properties = new Properties();
    
    /**
     * Manipulates a PDF file src with the file dest as result
     * @param src the original PDF
     * @param dest the resulting PDF
     * @throws IOException
     * @throws DocumentException
     * @throws GeneralSecurityException 
     */
    public void signPdf(String src, String dest, boolean withTS, boolean withOCSP)
        throws IOException, DocumentException, GeneralSecurityException {
        // Keystore and certificate chain
    	String keystore = properties.getProperty("PRIVATE");
        String password = properties.getProperty("PASSWORD");
        KeyStore ks = KeyStore.getInstance("PKCS12", "BC");
        ks.load(new FileInputStream(keystore), password.toCharArray());
        String alias = (String)ks.aliases().nextElement();
        PrivateKey pk = (PrivateKey)ks.getKey(alias, password.toCharArray());
        Certificate[] chain = ks.getCertificateChain(alias);
        // reader and stamper
        PdfReader reader = new PdfReader(src);
        FileOutputStream fout = new FileOutputStream(dest);
        PdfStamper stp = PdfStamper.createSignature(reader, fout, '\0');
        // appearance
        PdfSignatureAppearance sap = stp.getSignatureAppearance();
        sap.setReason("I'm approving this.");
        sap.setLocation("Foobar");
        sap.setVisibleSignature(new Rectangle(72, 732, 144, 780), 1, "Signature");
        // preserve some space for the contents
        // digital signature
        ExternalSignature es = new PrivateKeySignature(pk, "SHA-256", "BC");
        // If we add a time stamp:
        TSAClient tsc = null;
        if (withTS) {
            String tsa_url    = properties.getProperty("TSA");
            String tsa_login  = properties.getProperty("TSA_LOGIN");
            String tsa_passw  = properties.getProperty("TSA_PASSWORD");
            tsc = new TSAClientBouncyCastle(tsa_url, tsa_login, tsa_passw);
        }
        // If we use OCSP:
        OcspClient ocsp = null;
        if (withOCSP) {
            ocsp = new OcspClientBouncyCastle();
        }
        ExternalDigest digest = new BouncyCastleDigest();
        MakeSignature.signDetached(sap, digest, es, chain, null, ocsp, tsc, 0, CryptoStandard.CMS);
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
        TimestampOCSP signatures = new TimestampOCSP();
        signatures.signPdf(Signatures.ORIGINAL, SIGNED0, false, false);
        signatures.signPdf(Signatures.ORIGINAL, SIGNED1, true, false);
        signatures.signPdf(Signatures.ORIGINAL, SIGNED2, false, true);
        signatures.signPdf(Signatures.ORIGINAL, SIGNED3, true, true);
    }
}
