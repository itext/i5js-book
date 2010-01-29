/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part2.chapter08;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.XfaForm;

public class XfaMovie {

    public static final String RESOURCE = "resources/pdfs/xfa_movie.pdf";
    public static final String RESOURCEXFA = "resources/xml/xfa.xml";
    public static final String RESULTTXT1 = "results/part2/chapter08/movie_xfa.txt";
    public static final String RESULTTXT2 = "results/part2/chapter08/movie_acroform.txt";
    public static final String RESULTXML = "results/part2/chapter08/movie_xfa.xml";
    public static final String RESULTXMLFILLED = "results/part2/chapter08/movie_filled.xml";
    public static final String RESULTDATA = "results/part2/chapter08/movie.xml";
    public static final String RESULT1 = "results/part2/chapter08/xfa_filled_1.pdf";
    public static final String RESULT2 = "results/part2/chapter08/xfa_filled_2.pdf";
    public static final String RESULT3 = "results/part2/chapter08/xfa_filled_3.pdf";
    
    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, TransformerFactoryConfigurationError, TransformerException, DocumentException {
        XfaMovie xfa = new XfaMovie();
        xfa.readFieldnames(RESOURCE, RESULTTXT1);
        xfa.readXfa(RESOURCE, RESULTXML);
        xfa.fillData1(RESOURCE, RESULT1);
        xfa.readXfa(RESULT1, RESULTXMLFILLED);
        xfa.fillData2(RESOURCE, RESOURCEXFA, RESULT2);
        xfa.readData(RESULT2, RESULTDATA);
        xfa.fillData3(RESOURCE, RESULT3);
        xfa.readFieldnames(RESULT3, RESULTTXT2);
    }
    
    public void readFieldnames(String src, String dest) throws IOException {
        PrintStream out = new PrintStream(new FileOutputStream(dest));
        PdfReader reader = new PdfReader(src);
        AcroFields form = reader.getAcroFields();
        XfaForm xfa = form.getXfa();
        out.println(xfa.isXfaPresent() ? "XFA form" : "AcroForm");
        Set<String> fields = form.getFields().keySet();
        for (String key : fields) {
            out.println(key);
        }
        out.flush();
        out.close();
    }
    
    public void readXfa(String src, String dest) throws IOException, ParserConfigurationException, SAXException, TransformerFactoryConfigurationError, TransformerException {
        FileOutputStream os = new FileOutputStream(dest);
        PdfReader reader = new PdfReader(src);
        XfaForm xfa = new XfaForm(reader);
        Document doc = xfa.getDomDocument();
        Transformer tf = TransformerFactory.newInstance().newTransformer();
        tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        tf.setOutputProperty(OutputKeys.INDENT, "yes");
        tf.transform(new DOMSource(doc), new StreamResult(os));
        reader.close();
    }
    
    public void fillData1(String src, String dest) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        AcroFields form = stamper.getAcroFields();
        form.setField("movies[0].movie[0].imdb[0]", "1075110");
        form.setField("movies[0].movie[0].duration[0]", "108");
        form.setField("movies[0].movie[0].title[0]", "The Misfortunates");
        form.setField("movies[0].movie[0].original[0]", "De helaasheid der dingen");
        form.setField("movies[0].movie[0].year[0]", "2009");
        stamper.close();
    }
    
    public void fillData2(String src, String xml, String dest) throws IOException, DocumentException, ParserConfigurationException, SAXException {
        PdfReader reader = new PdfReader(src);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        XfaForm xfa = new XfaForm(reader);
        DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
        fact.setNamespaceAware(true);
        DocumentBuilder db = fact.newDocumentBuilder();
        Document doc = db.parse(new FileInputStream(xml));
        xfa.setDomDocument(doc);
        xfa.setChanged(true);
        XfaForm.setXfa(xfa, stamper.getReader(), stamper.getWriter());
        stamper.close();
    }
    
    public void readData(String src, String dest) throws IOException, ParserConfigurationException, SAXException, TransformerFactoryConfigurationError, TransformerException {
        FileOutputStream os = new FileOutputStream(dest);
        PdfReader reader = new PdfReader(src);
        XfaForm xfa = new XfaForm(reader);
        Node node = xfa.getDatasetsNode();
        NodeList list = node.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            if("data".equals(list.item(i).getLocalName())) {
                node = list.item(i);
                break;
            }
        }
        list = node.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            if("movies".equals(list.item(i).getLocalName())) {
                node = list.item(i);
                break;
            }
        }
        Transformer tf = TransformerFactory.newInstance().newTransformer();
        tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        tf.setOutputProperty(OutputKeys.INDENT, "yes");
        tf.transform(new DOMSource(node), new StreamResult(os));
        reader.close();
    }
    
    public void fillData3(String src, String dest) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        AcroFields form = stamper.getAcroFields();
        form.removeXfa();
        form.setField("movies[0].movie[0].imdb[0]", "1075110");
        form.setField("movies[0].movie[0].duration[0]", "108");
        form.setField("movies[0].movie[0].title[0]", "The Misfortunates");
        form.setField("movies[0].movie[0].original[0]", "De helaasheid der dingen");
        form.setField("movies[0].movie[0].year[0]", "2009");
        stamper.close();
    }
}
