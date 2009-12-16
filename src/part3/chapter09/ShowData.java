/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part3.chapter09;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ShowData
 */
public class ShowData extends HttpServlet {

    /**
     * Show keys and values passed to the query string with GET
     * as plain text.
     * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
     */
    @SuppressWarnings("unchecked")
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        Enumeration<String> parameters = request.getParameterNames();
        String parameter;
        while (parameters.hasMoreElements()) {
            parameter = parameters.nextElement();
            out.println(
                String.format("%s: %s", parameter, request.getParameter(parameter)));
        }
    }
    
    /**
     * Shows the stream passed to the server with POST as plain text.
     * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        response.setContentType("text/plain");
        OutputStream os = response.getOutputStream();
        InputStream is = request.getInputStream();
        byte[] b = new byte[256];  
        int read;  
        while ((read = is.read(b)) != -1) {  
            os.write(b, 0, read);  
        } 
    }

    /**
     * Serial version UID
     */
    private static final long serialVersionUID = 388110564278942780L;
}
