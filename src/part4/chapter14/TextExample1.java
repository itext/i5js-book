/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */
package part4.chapter14;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.AttributedString;
import java.awt.font.TextAttribute;


import javax.swing.JFrame;
import javax.swing.JPanel;

public class TextExample1 extends JPanel {

    /** A String that needs to be displayed in a PDF document */
    private static AttributedString akira;

    /**
     * Creates a TextExample that will be used to demonstrate how text in AWT can be translated to PDF.
     */
    public TextExample1() {
        akira = new AttributedString(
            "Akira Kurosawa: \u9ed2\u6fa4 \u660e or \u9ed2\u6ca2 \u660e; " +
            "23 March 1910 - 6 September 1998.");
        akira.addAttribute(TextAttribute.FONT, new Font("SansSerif", Font.PLAIN, 12));
        akira.addAttribute(TextAttribute.FONT, new Font("SansSerif", Font.BOLD, 12), 0, 15);
        akira.addAttribute(TextAttribute.FONT, new Font("MS PGothic", Font.PLAIN, 12), 16, 20);
        akira.addAttribute(TextAttribute.FONT, new Font("MS PGothic", Font.PLAIN, 12), 24, 28);
    }

    /**
     * Draws the String to a Graphics object.
     * @see javax.swing.JComponent#paint(java.awt.Graphics)
     */
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawString(akira.getIterator(), 10, 16);
    }
    
    /**
     * Creates a JFrame and draws a String to it.
     * @param args no arguments needed
     */
    public static void main(String[] args) {
        TextExample1 Kurosawa = new TextExample1();
        JFrame f = new JFrame("Kurosawa");
        f.getContentPane().add( Kurosawa, "Center" );
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        f.setSize(new Dimension(500, 60));
        f.setVisible(true);
    }

    /** A serial version UID */
    private static final long serialVersionUID = -3639324875232824761L;
}
