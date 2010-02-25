package part4.chapter13;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseField;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;

import part2.chapter08.Subscribe;

public class InspectForm {

    public static final String RESULTTXT = "results/part4/chapter13/fieldflags.txt";
    /**
     * Inspects a PDF file src with the file dest as result
     * @param src the original PDF
     * @param dest the resulting PDF
     * @throws IOException
     * @throws DocumentException
     */
    public void inspectPdf(String src, String dest)
        throws IOException, DocumentException {
    	OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(dest));
    	PdfReader reader = new PdfReader(src);
    	AcroFields form = reader.getAcroFields();
    	Map<String,AcroFields.Item> fields = form.getFields();
    	AcroFields.Item item;
    	PdfDictionary dict;
    	int flags;
    	for (Map.Entry<String,AcroFields.Item> entry : fields.entrySet()) {
    		out.write(entry.getKey());
    		item = entry.getValue();
    		dict = item.getMerged(0);
    		flags = dict.getAsNumber(PdfName.FF).intValue();
    		if ((flags & BaseField.PASSWORD) > 0)
    			out.write(" -> password");
    		if ((flags & BaseField.MULTILINE) > 0)
    			out.write(" -> multiline");
    		out.write('\n');
    	}
    	out.flush();
    	out.close();
    }
    
	public static void main(String[] args) throws IOException, DocumentException {
		new Subscribe().createPdf(Subscribe.RESULT);
		new InspectForm().inspectPdf(Subscribe.RESULT, RESULTTXT);
	}
}
