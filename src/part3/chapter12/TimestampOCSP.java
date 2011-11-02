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
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Properties;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.OcspClientBouncyCastle;
import com.itextpdf.text.pdf.PdfDate;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPKCS7;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignature;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.TSAClient;
import com.itextpdf.text.pdf.TSAClientBouncyCastle;

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
        PdfSignatureAppearance sap = stp.getSignatureAppearance();
        sap.setVisibleSignature(new Rectangle(72, 732, 144, 780), 1, "Signature");
        sap.setCrypto(null, chain, null, PdfSignatureAppearance.SELF_SIGNED);
        PdfSignature dic = new PdfSignature(PdfName.ADOBE_PPKLITE, new PdfName("adbe.pkcs7.detached"));
        dic.setReason(sap.getReason());
        dic.setLocation(sap.getLocation());
        dic.setContact(sap.getContact());
        dic.setDate(new PdfDate(sap.getSignDate()));
        sap.setCryptoDictionary(dic);
        // preserve some space for the contents
        int contentEstimated = 15000;
        HashMap<PdfName,Integer> exc = new HashMap<PdfName,Integer>();
        exc.put(PdfName.CONTENTS, new Integer(contentEstimated * 2 + 2));
        sap.preClose(exc);

        // make the digest
        InputStream data = sap.getRangeStream();
        MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
        byte buf[] = new byte[8192];
        int n;
        while ((n = data.read(buf)) > 0) {
            messageDigest.update(buf, 0, n);
        }
        byte hash[] = messageDigest.digest();
        Calendar cal = Calendar.getInstance();
        // If we add a time stamp:
        TSAClient tsc = null;
        if (withTS) {
            String tsa_url    = properties.getProperty("TSA");
            String tsa_login  = properties.getProperty("TSA_LOGIN");
            String tsa_passw  = properties.getProperty("TSA_PASSWORD");
            tsc = new TSAClientBouncyCastle(tsa_url, tsa_login, tsa_passw);
        }
        // If we use OCSP:
        byte[] ocsp = null;
        if (withOCSP) {
            String url = PdfPKCS7.getOCSPURL((X509Certificate)chain[0]);
            CertificateFactory cf = CertificateFactory.getInstance("X509");
            FileInputStream is = new FileInputStream(properties.getProperty("ROOTCERT"));
            X509Certificate root = (X509Certificate) cf.generateCertificate(is);
            ocsp = new OcspClientBouncyCastle().getEncoded((X509Certificate)chain[0], root, url);
        }
        // Create the signature
        PdfPKCS7 sgn = new PdfPKCS7(pk, chain, null, "SHA1", null, false);
        byte sh[] = sgn.getAuthenticatedAttributeBytes(hash, cal, ocsp);
        sgn.update(sh, 0, sh.length);
        byte[] encodedSig = sgn.getEncodedPKCS7(hash, cal, tsc, ocsp);

        if (contentEstimated + 2 < encodedSig.length)
            throw new DocumentException("Not enough space");

        byte[] paddedSig = new byte[contentEstimated];
        System.arraycopy(encodedSig, 0, paddedSig, 0, encodedSig.length);
        // Replace the contents
        PdfDictionary dic2 = new PdfDictionary();
        dic2.put(PdfName.CONTENTS, new PdfString(paddedSig).setHexWriting(true));
        sap.close(dic2);

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
