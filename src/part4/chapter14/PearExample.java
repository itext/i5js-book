package part4.chapter14;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class PearExample extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1251626928914650961L;

	public PearExample() {
		circle = new Ellipse2D.Double();
		oval = new Ellipse2D.Double();
		leaf = new Ellipse2D.Double();
		stem = new Ellipse2D.Double();
		circ = new Area(circle);
		ov = new Area(oval);
		leaf1 = new Area(leaf);
		leaf2 = new Area(leaf);
		st1 = new Area(stem);
		st2 = new Area(stem);
	}
	
	/** Ellipse in Double precision */
	Ellipse2D.Double circle;
	/** Ellipse in Double precision */
	Ellipse2D.Double oval;
	/** Ellipse in Double precision */
	Ellipse2D.Double leaf;
	/** Ellipse in Double precision */
	Ellipse2D.Double stem;

	/** A geometric area */
	Area circ;
	/** A geometric area */
	Area ov;
	/** A geometric area */
	Area leaf1;
	/** A geometric area */
	Area leaf2;
	/** A geometric area */
	Area st1;
	/** A geometric area */
	Area st2;
	
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		double ew = 75;
		double eh = 75;

		g2.setColor(Color.green);

		// Creates the first leaf by filling the intersection of two Area
		// objects created from an ellipse.
		leaf.setFrame(ew - 16, eh - 29, 15.0, 15.0);
		leaf1 = new Area(leaf);
		leaf.setFrame(ew - 14, eh - 47, 30.0, 30.0);
		leaf2 = new Area(leaf);
		leaf1.intersect(leaf2);
		g2.fill(leaf1);

		// Creates the second leaf.
		leaf.setFrame(ew + 1, eh - 29, 15.0, 15.0);
		leaf1 = new Area(leaf);
		leaf2.intersect(leaf1);
		g2.fill(leaf2);

		g2.setColor(Color.black);

		// Creates the stem by filling the Area resulting from the subtraction
		// of two Area objects created from an ellipse.
		stem.setFrame(ew, eh - 42, 40.0, 40.0);
		st1 = new Area(stem);
		stem.setFrame(ew + 3, eh - 47, 50.0, 50.0);
		st2 = new Area(stem);
		st1.subtract(st2);
		g2.fill(st1);

		g2.setColor(Color.yellow);

		// Creates the pear itself by filling the Area resulting from the union
		// of two Area objects created by two different ellipses.
		circle.setFrame(ew - 25, eh, 50.0, 50.0);
		oval.setFrame(ew - 19, eh - 20, 40.0, 70.0);
		circ = new Area(circle);
		ov = new Area(oval);
		circ.add(ov);
		g2.fill(circ);
	}
	
	public static void main(String s[]) {
		PearExample pear = new PearExample();
		JFrame f = new JFrame("Pear example");
	    f.getContentPane().add( pear, "Center" );

        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
            	System.exit(0);
            }
        });
        f.setSize(new Dimension(160, 200));
        f.setVisible(true);
    }

}