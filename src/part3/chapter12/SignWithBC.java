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
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.CertStore;
import java.security.cert.Certificate;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Properties;

import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSProcessable;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfDate;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPKCS7;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignature;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfString;

public class SignWithBC {

    /** The resulting PDF */
    public static String SIGNED1 = "results/part3/chapter12/bc_detached.pdf";
    /** The resulting PDF */
    public static String SIGNED2 = "results/part3/chapter12/bc_encapsulated.pdf";

    /**
     * A properties file that is PRIVATE.
     * You should make your own properties file and adapt this line.
     */
    public static String PATH = "c:/home/blowagie/key.properties";
    /** Some properties used when signing. */
    public static Properties properties = new Properties();
    
    /**
     * Signs an existing PDF
     * @param src      path to the existing PDF
     * @param dest     path to the resulting PDF
     * @param detached how to create the signature
     * @throws IOException
     * @throws DocumentException
     * @throws GeneralSecurityException
     * @throws CMSException
     */
    public void signPdf(String src, String dest, boolean detached)
        throws IOException, DocumentException, GeneralSecurityException, CMSException {
    	// private key and certificate
        String path = properties.getProperty("PRIVATE");
        String keystore_password = properties.getProperty("PASSWORD");
        String key_password = properties.getProperty("PASSWORD");
        KeyStore ks = KeyStore.getInstance("pkcs12", "BC");
        ks.load(new FileInputStream(path), keystore_password.toCharArray());
        String alias = (String)ks.aliases().nextElement();
        PrivateKey key = (PrivateKey)ks.getKey(alias, key_password.toCharArray());
        Certificate[] chain = ks.getCertificateChain(alias);
        // reader and stamper
        PdfReader reader = new PdfReader(src);
        PdfStamper stp = PdfStamper.createSignature(reader, new FileOutputStream(dest), '\0');
        PdfSignatureAppearance sap = stp.getSignatureAppearance();
        sap.setVisibleSignature(new Rectangle(72, 732, 144, 780), 1, null);
        sap.setSignDate(new GregorianCalendar());
        sap.setCrypto(null, chain, null, null);
        sap.setAcro6Layers(true);
        sap.setRenderingMode(PdfSignatureAppearance.RenderingMode.DESCRIPTION);
        PdfSignature dic;
        if (detached)
            dic = new PdfSignature(PdfName.ADOBE_PPKLITE, PdfName.ADBE_PKCS7_DETACHED);
        else
            dic = new PdfSignature(PdfName.ADOBE_PPKMS, PdfName.ADBE_PKCS7_SHA1);
        dic.setDate(new PdfDate(sap.getSignDate()));
        dic.setName(PdfPKCS7.getSubjectFields((X509Certificate)chain[0]).getField("CN"));
        dic.setReason("Signed with BC");
        dic.setLocation("Foobar");
        sap.setCryptoDictionary(dic);
        int csize = 4000;
        HashMap<PdfName,Integer> exc = new HashMap<PdfName,Integer>();
        exc.put(PdfName.CONTENTS, new Integer(csize * 2 + 2));
        sap.preClose(exc);
        // signature
        CMSSignedDataGenerator generator = new CMSSignedDataGenerator();
        generator.addSigner(key, (X509Certificate)chain[0], CMSSignedDataGenerator.DIGEST_SHA1);

        ArrayList<Certificate> list = new ArrayList<Certificate>();
        for (int i = 0; i < chain.length; i++) {
            list.add(chain[i]);
        }
        CertStore chainStore
            = CertStore.getInstance("Collection", new CollectionCertStoreParameters(list), "BC");
        generator.addCertificatesAndCRLs(chainStore);
        CMSSignedData signedData;

        if (detached) {
            CMSProcessable content = new CMSProcessableRange(sap);
            signedData = generator.generate(content, false, "BC");
        }
        else {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            InputStream s = sap.getRangeStream();
            int read = 0;
            byte[] buff = new byte[8192];
            while ((read = s.read(buff, 0, 8192)) > 0) {
                md.update(buff, 0, read);
            }
            CMSProcessable content = new CMSProcessableByteArray(md.digest());
            signedData = generator.generate(content, true, "BC");
        }
        byte[] pk = signedData.getEncoded();
        
        byte[] outc = new byte[csize];
        PdfDictionary dic2 = new PdfDictionary();
        System.arraycopy(pk, 0, outc, 0, pk.length);
        dic2.put(PdfName.CONTENTS, new PdfString(outc).setHexWriting(true));
        sap.close(dic2);
    }

    /**
     * CMSProcessable implementation.
     */
    class CMSProcessableRange implements CMSProcessable {
        private PdfSignatureAppearance sap;
        private byte[] buf = new byte[8192];

        public CMSProcessableRange(PdfSignatureAppearance sap) {
            this.sap = sap;
        }

        public void write(OutputStream outputStream) throws IOException, CMSException {
            InputStream s = sap.getRangeStream();
            int read = 0;
            while ((read = s.read(buf, 0, buf.length)) > 0) {
                outputStream.write(buf, 0, read);
            }
        }

        public Object getContent() {
            return sap;
        }
    }
    
    /**
     * Main method.
     *
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException
     * @throws GeneralSecurityException 
     * @throws CMSException
     */
    public static void main(String[] args)
        throws IOException, DocumentException, GeneralSecurityException, CMSException {
        Security.addProvider(new BouncyCastleProvider());
        properties.load(new FileInputStream(PATH));
        new Signatures().createPdf(Signatures.ORIGINAL);
        SignWithBC signatures = new SignWithBC();
        signatures.signPdf(Signatures.ORIGINAL, SIGNED1, true);
        signatures.signPdf(Signatures.ORIGINAL, SIGNED2, false);
    }
}
