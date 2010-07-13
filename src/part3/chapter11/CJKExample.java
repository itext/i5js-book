/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part3.chapter11;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

public class CJKExample {

    /** The resulting PDF file. */
    public static final String RESULT
        = "results/part3/chapter11/cjk_example.pdf";
    /** Movies, their director and original title */
    public static final String[][] MOVIES = {
        {
            "STSong-Light", "UniGB-UCS2-H",
            "Movie title: House of The Flying Daggers (China)",
            "directed by Zhang Yimou",
            "\u5341\u950a\u57cb\u4f0f"
        },
        {
            "KozMinPro-Regular", "UniJIS-UCS2-H",
            "Movie title: Nobody Knows (Japan)",
            "directed by Hirokazu Koreeda",
            "\u8ab0\u3082\u77e5\u3089\u306a\u3044"
        },
        {
            "HYGoThic-Medium", "UniKS-UCS2-H",
            "Movie title: '3-Iron' aka 'Bin-jip' (South-Korea)",
            "directed by Kim Ki-Duk",
            "\ube48\uc9d1"
        }
    };
    
    /**
     * Creates a PDF document.
     * @param filename the path to the new PDF document
     * @throws DocumentException 
     * @throws IOException 
     * @throws    DocumentException 
     * @throws    IOException
     */
    public void createPdf(String filename) throws IOException, DocumentException {
        // step 1
        Document document = new Document();
        // step 2
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        // step 3
        document.open();
        // step 4
        BaseFont bf;
        Font font;
        
        for (int i = 0; i < 3; i++) {
            bf = BaseFont.createFont(MOVIES[i][0], MOVIES[i][1], BaseFont.NOT_EMBEDDED);
            font = new Font(bf, 12);
            document.add(new Paragraph(bf.getPostscriptFontName(), font));
            for (int j = 2; j < 5; j++)
                document.add(new Paragraph(MOVIES[i][j], font));
            document.add(Chunk.NEWLINE);
        }

        
        // step 5
        document.close();
    }

    /**
     * Main method.
     *
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException
     */
    public static void main(String[] args) throws IOException, DocumentException {
        new CJKExample().createPdf(RESULT);
    }
}
