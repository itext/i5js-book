package part3.chapter09;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

/**
 * Servlet implementation class GetForm
 */
public class GetForm extends HttpServlet {

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/pdf");
        try {
            // We get a resource from our web app
            InputStream is
                = getServletContext().getResourceAsStream("/subscribe.pdf");
            // We create a reader with the InputStream
            PdfReader reader = new PdfReader(is, null);
            // We create an OutputStream for the new PDF
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // Now we create the PDF
            PdfStamper stamper = new PdfStamper(reader, baos);
            // We alter the fields of the existing PDF
            AcroFields fields = stamper.getAcroFields();
            fields.removeField("personal.password");
            Set<String> parameters = fields.getFields().keySet();
            StringBuffer buf = new StringBuffer();
            for (String parameter : parameters) {
            	buf.append(parameter);
            	buf.append("=");
            	buf.append(request.getParameter(parameter));
            	buf.append("&");
                fields.setField(parameter, request.getParameter(parameter));
            }
            fields.setField("personal.reason", buf.toString());
            stamper.setFormFlattening(true);
            stamper.close();
            // We write the PDF bytes to the OutputStream
            OutputStream os = response.getOutputStream();
            baos.writeTo(os);
            os.flush();
        } catch (DocumentException e) {
            throw new IOException(e.getMessage());
        }
	}

	/**
	 * Serial version uid.
	 */
	private static final long serialVersionUID = -3676321073288303561L;

}
