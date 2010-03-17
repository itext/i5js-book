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

public class TextExample3 extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3639324875232824761L;
    // The LineBreakMeasurer used to line-break the paragraph.
    private LineBreakMeasurer lineMeasurer;
    // index of the first character in the paragraph.
    private int paragraphStart;

    // index of the first character after the end of the paragraph.
    private int paragraphEnd;

	private static AttributedString akira;


	public TextExample3() {
		akira = new AttributedString(
			"Akira Kurosawa (\u9ed2\u6fa4 \u660e or \u9ed2\u6ca2 \u660e, " +
			"Kurosawa Akira, 23 March 1910 - 6 September 1998) was a " +
			"Japanese film director, producer, screenwriter and editor. " +
			"In a career that spanned 50 years, Kurosawa directed 30 films. " +
			"He is widely regarded as one of the most important and " +
			"influential filmmakers in film history.");
		akira.addAttribute(TextAttribute.FONT, new Font("SansSerif", Font.PLAIN, 12));
		akira.addAttribute(TextAttribute.FONT, new Font("SansSerif", Font.BOLD, 12), 0, 14);
	    akira.addAttribute(TextAttribute.FONT, new Font("MS PGothic", Font.PLAIN, 12), 16, 20);
	    akira.addAttribute(TextAttribute.FONT, new Font("MS PGothic", Font.PLAIN, 12), 24, 28);
	}
	
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
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
        while (lineMeasurer.getPosition() < paragraphEnd) {
            TextLayout layout = lineMeasurer.nextLayout(breakWidth);
            drawPosY += layout.getAscent();
            layout.draw(g2d, 0, drawPosY);
            drawPosY += layout.getDescent() + layout.getLeading();
        }
	}
	
	public static void main(String s[]) {
		TextExample3 Kurosawa = new TextExample3();
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

}
