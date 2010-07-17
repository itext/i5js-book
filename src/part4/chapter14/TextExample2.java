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
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;


import javax.swing.JFrame;
import javax.swing.JPanel;

public class TextExample2 extends JPanel {

    /** A String that needs to be displayed in a PDF document */
    private static AttributedString akira;

    /** A String that needs to be displayed in a PDF document */
    public static final String AKIRA =
        "Akira Kurosawa (\u9ed2\u6fa4 \u660e or \u9ed2\u6ca2 \u660e, " +
        "Kurosawa Akira, 23 March 1910 - 6 September 1998) was a " +
        "Japanese film director, producer, screenwriter and editor. " +
        "In a career that spanned 50 years, Kurosawa directed 30 films. " +
        "He is widely regarded as one of the most important and " +
        "influential filmmakers in film history.";

    /**
     * Creates a TextExample that will be used to demonstrate how text in AWT can be translated to PDF.
     */
    public TextExample2() {
        akira = new AttributedString(AKIRA);
        akira.addAttribute(TextAttribute.FONT, new Font("Arial Unicode MS", Font.PLAIN, 12));
    }

    /**
     * Draws the String to a Graphics object.
     * @see javax.swing.JComponent#paint(java.awt.Graphics)
     */
    public void paint(Graphics g) {
    	LineBreakMeasurer lineMeasurer = null;
    	int paragraphStart = 0;
    	int paragraphEnd = 0;
        Graphics2D g2d = (Graphics2D) g;
        g2d.setFont(new Font("Arial Unicode MS", Font.PLAIN, 12));
        if (lineMeasurer == null) {
            AttributedCharacterIterator paragraph = akira.getIterator();
            paragraphStart = paragraph.getBeginIndex();
            paragraphEnd = paragraph.getEndIndex();
            FontRenderContext frc = g2d.getFontRenderContext();
            lineMeasurer = new LineBreakMeasurer(paragraph, frc);
        }
        float breakWidth = (float)getSize().width;
        float drawPosY = 0;
        lineMeasurer.setPosition(paragraphStart);
        int start = 0;
        int end = 0;
        while (lineMeasurer.getPosition() < paragraphEnd) {
            TextLayout layout = lineMeasurer.nextLayout(breakWidth);
            drawPosY += layout.getAscent();
            end = start + layout.getCharacterCount();
            g2d.drawString(AKIRA.substring(start, end), 0, drawPosY);
            start = end;
            drawPosY += layout.getDescent() + layout.getLeading();
        }
    }
    
    /**
     * Creates a JFrame and draws a String to it.
     * @param args no arguments needed
     */
    public static void main(String[] args) {
        TextExample2 Kurosawa = new TextExample2();
        JFrame f = new JFrame("Kurosawa");
        f.getContentPane().add( Kurosawa, "Center" );

        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        f.setSize(new Dimension(300, 150));
        f.setVisible(true);
    }

    /** A serial version UID. */
    private static final long serialVersionUID = -3639324875232824761L;
}
