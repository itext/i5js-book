/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part3.chapter09;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.pdf.FdfWriter;

public class CreateFDF extends HttpServlet {

    /**
     * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
     */
    protected void service(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        response.setContentType("application/vnd.adobe.fdf");
        response.setHeader("Content-Disposition",
            "attachment; filename=\"subscribe.fdf\"");
        FdfWriter fdf = new FdfWriter();
        fdf.setFieldAsString("personal.name", request.getParameter("name"));
        fdf.setFieldAsString("personal.loginname", request.getParameter("loginname"));
        fdf.setFieldAsString("personal.password", request.getParameter("password"));
        fdf.setFieldAsString("personal.reason", request.getParameter("reason"));
        fdf.setFile("subscribe.pdf");
        fdf.writeTo(response.getOutputStream());
    }

    /**
     * Serial version UID
     */
    private static final long serialVersionUID = 4841218549441233308L;

}
