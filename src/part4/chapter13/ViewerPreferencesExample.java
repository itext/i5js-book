/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */
package part4.chapter13;

import java.io.IOException;
import java.sql.SQLException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;

public class ViewerPreferencesExample extends PageLayoutExample {

    /** The resulting PDF file. */
    public static final String RESULT1
        = "results/part4/chapter13/viewerpreferences1.pdf";
    /** The resulting PDF file. */
    public static final String RESULT2
        = "results/part4/chapter13/viewerpreferences2.pdf";
    /** The resulting PDF file. */
    public static final String RESULT3
        = "results/part4/chapter13/viewerpreferences3.pdf";
    /** The resulting PDF file. */
    public static final String RESULT4
        = "results/part4/chapter13/viewerpreferences4.pdf";
    /** The resulting PDF file. */
    public static final String RESULT5
        = "results/part4/chapter13/viewerpreferences5.pdf";
    /** The resulting PDF file. */
    public static final String RESULT6
        = "results/part4/chapter13/viewerpreferences6.pdf";

    /**
     * Main method.
     *
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException 
     * @throws SQLException
     */
    public static void main(String[] args)
        throws IOException, DocumentException, SQLException {
        ViewerPreferencesExample example = new ViewerPreferencesExample();
        example.createPdf(RESULT1,
            PdfWriter.PageModeFullScreen);
        example.createPdf(RESULT2,
            PdfWriter.PageModeUseThumbs);
        example.createPdf(RESULT3,
            PdfWriter.PageLayoutTwoColumnRight | PdfWriter.PageModeUseThumbs);
        example.createPdf(RESULT4,
            PdfWriter.PageModeFullScreen | PdfWriter.NonFullScreenPageModeUseOutlines);
        example.createPdf(RESULT5,
            PdfWriter.FitWindow | PdfWriter.HideToolbar);
        example.createPdf(RESULT6,
            PdfWriter.HideWindowUI);
    }
}
