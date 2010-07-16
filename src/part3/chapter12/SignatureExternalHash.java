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
import java.security.Signature;
import java.security.cert.Certificate;
import java.util.Properties;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfLiteral;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPKCS7;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSigGenericPKCS;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfString;

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
     * @throws IOException
     * @throws DocumentException
     * @throws GeneralSecurityException 
     */
    public void signPdfSelf(String src, String dest)
        throws IOException, DocumentException, GeneralSecurityException {
    	// Private key and certificate
        String path = properties.getProperty("PRIVATE");
        String keystore_password = properties.getProperty("PASSWORD");
        String key_password = properties.getProperty("PASSWORD");
        KeyStore ks = KeyStore.getInstance("pkcs12", "BC");
        ks.load(new FileInputStream(path), keystore_password.toCharArray());
        String alias = (String)ks.aliases().nextElement();
        PrivateKey key = (PrivateKey) ks.getKey(alias, key_password.toCharArray());
        Certificate[] chain = ks.getCertificateChain(alias);
        // reader and stamper
        PdfReader reader = new PdfReader(src);
        FileOutputStream os = new FileOutputStream(dest);
        PdfStamper stamper = PdfStamper.createSignature(reader, os, '\0');
        PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
        appearance.setCrypto(null, chain, null, PdfSignatureAppearance.SELF_SIGNED);
        appearance.setReason("External hash example");
        appearance.setLocation("Foobar");
        appearance.setVisibleSignature(new Rectangle(72, 732, 144, 780), 1,    "sig");
        appearance.setExternalDigest(new byte[128], null, "RSA");
        appearance.preClose();
        // digital signature
        Signature signature = Signature.getInstance("SHA1withRSA");
        signature.initSign(key);
        byte buf[] = new byte[8192];
        int n;
        InputStream inp = appearance.getRangeStream();
        while ((n = inp.read(buf)) > 0) {
            signature.update(buf, 0, n);
        }
        PdfPKCS7 sig = appearance.getSigStandard().getSigner();
        sig.setExternalDigest(signature.sign(), null, "RSA");
        PdfDictionary dic = new PdfDictionary();
        dic.put(PdfName.CONTENTS, new PdfString(sig.getEncodedPKCS1()).setHexWriting(true));
        appearance.close(dic);
    }
    /**
     * Manipulates a PDF file src with the file dest as result
     * @param src the original PDF
     * @param dest the resulting PDF
     * @throws IOException
     * @throws DocumentException
     * @throws GeneralSecurityException 
     */
    public void signPdfWinCer(String src, String dest, boolean sign)
        throws IOException, DocumentException, GeneralSecurityException {
        // private key and certificate
    	String path = properties.getProperty("PRIVATE");
        String keystore_password = properties.getProperty("PASSWORD");
        String key_password = properties.getProperty("PASSWORD");
        KeyStore ks = KeyStore.getInstance("pkcs12", "BC");
        ks.load(new FileInputStream(path), keystore_password.toCharArray());
        String alias = (String)ks.aliases().nextElement();
        PrivateKey key = (PrivateKey) ks.getKey(alias, key_password.toCharArray());
        Certificate[] chain = ks.getCertificateChain(alias);
        // reader and stamper
        PdfReader reader = new PdfReader(src);
        FileOutputStream os = new FileOutputStream(dest);
        PdfStamper stamper = PdfStamper.createSignature(reader, os, '\0');
        PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
        appearance.setCrypto(key, chain, null, PdfSignatureAppearance.WINCER_SIGNED);
        appearance.setReason("External hash example");
        appearance.setLocation("Foobar");
        appearance.setVisibleSignature(new Rectangle(72, 732, 144, 780), 1,    "sig");
        appearance.setExternalDigest(null, new byte[20], null);
        appearance.preClose();
        // signature
        MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
        byte buf[] = new byte[8192];
        int n;
        InputStream inp = appearance.getRangeStream();
        while ((n = inp.read(buf)) > 0) {
            messageDigest.update(buf, 0, n);
        }
        byte hash[] = messageDigest.digest();
        PdfSigGenericPKCS sg = appearance.getSigStandard();
        PdfLiteral slit = (PdfLiteral)sg.get(PdfName.CONTENTS);
        byte[] outc = new byte[(slit.getPosLength() - 2) / 2];
        PdfPKCS7 sig = sg.getSigner();
        if (sign) {
            Signature signature = Signature.getInstance("SHA1withRSA");
            signature.initSign(key);
            signature.update(hash);
            sig.setExternalDigest(signature.sign(), hash, "RSA");
        }
        else
            sig.setExternalDigest(null, hash, null);
        PdfDictionary dic = new PdfDictionary();
        byte[] ssig = sig.getEncodedPKCS7();
        System.arraycopy(ssig, 0, outc, 0, ssig.length);
        dic.put(PdfName.CONTENTS, new PdfString(outc).setHexWriting(true));
        appearance.close(dic);
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
        signatures.signPdfSelf(Signatures.ORIGINAL, SIGNED1);
        signatures.signPdfWinCer(Signatures.ORIGINAL, SIGNED2, false);
        signatures.signPdfWinCer(Signatures.ORIGINAL, SIGNED3, true);
    }
}
