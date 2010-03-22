/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part4.chapter15;

import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.parser.GraphicsState;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.Matrix;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;
import com.itextpdf.text.pdf.parser.Vector;

public class MarginFinder implements RenderListener {
	/** The left margin. */
	protected float llx = Float.MAX_VALUE;
	/** The bottom margin. */
	protected float lly = Float.MAX_VALUE;
	/** The right margin. */
	protected float urx = Float.MIN_VALUE;
	/** The top margin. */
	protected float ury = Float.MIN_VALUE;

	/**
	 * Resets all margin values.
	 * @see com.itextpdf.text.pdf.parser.RenderListener#reset()
	 */
	public void reset() {
		llx = Float.MAX_VALUE;
		lly = Float.MAX_VALUE;
		urx = Float.MIN_VALUE;
		ury = Float.MIN_VALUE;
	}

	/**
	 * Method invokes by the PdfContentStreamProcessor.
	 * Passes a TextRenderInfo for every text chunk that is encountered.
	 * We'll use this object to obtain coordinates.
	 * @return TextRenderInfo object containing information about the text.
	 * @see com.itextpdf.text.pdf.parser.RenderListener#renderText(com.itextpdf.text.pdf.parser.TextRenderInfo)
	 */
	public void renderText(TextRenderInfo renderInfo) {
		// looking for the left margin
		llx = Math.min(llx, renderInfo.getStartPoint().get(Vector.I1));
		llx = Math.min(llx, renderInfo.getEndPoint().get(Vector.I1));
		// looking for the bottom margin
		float descent = getAscentDescent(renderInfo, false);
		lly = Math.min(lly, renderInfo.getStartPoint().get(Vector.I2) + descent);
		lly = Math.min(lly, renderInfo.getEndPoint().get(Vector.I2) + descent);
		// looking for the right margin
		urx = Math.max(urx, renderInfo.getStartPoint().get(Vector.I1));
		urx = Math.max(urx, renderInfo.getEndPoint().get(Vector.I1));
		// looking for the top margin
		float ascent = getAscentDescent(renderInfo, true);
		ury = Math.max(ury, renderInfo.getStartPoint().get(Vector.I2) + ascent);
		ury = Math.max(ury, renderInfo.getEndPoint().get(Vector.I2) + ascent);
	}
	
    /**
     * Helper method to compute the ascent or the descent.
     * @param ri The TextRenderInfo
     * @param ascent returns the ascent if true; returns the descent if false.
     * @param start returns the ascent or descent for the starting point if true.
     * @return
     */
    private float getAscentDescent(TextRenderInfo ri, boolean ascent) {
    	GraphicsState gs = ri.getGs();
    	Matrix matrix = ri.getTextToUserSpaceTransformMatrix();
    	int key = ascent ? BaseFont.ASCENT : BaseFont.DESCENT;
    	float tmp = gs.getFont().getFontDescriptor(key, gs.getFontSize());
    	Vector vector = new Vector(0, tmp, 1);
    	return vector.cross(matrix).get(Vector.I2) - matrix.get(Matrix.I32);
    }

	/**
	 * Getter for the left margin.
	 * @return the left margin
	 */
	public float getLlx() {
		return llx;
	}

	/**
	 * Getter for the bottom margin.
	 * @return the bottom margin
	 */
	public float getLly() {
		return lly;
	}

	/**
	 * Getter for the right margin.
	 * @return the right margin
	 */
	public float getUrx() {
		return urx;
	}

	/**
	 * Getter for the top margin.
	 * @return the top margin
	 */
	public float getUry() {
		return ury;
	}

	/**
	 * Gets the width of the text block.
	 * @return a width
	 */
	public float getWidth() {
		return urx - llx;
	}
	
	/**
	 * Gets the height of the text block.
	 * @return a height
	 */
	public float getHeight() {
		return ury - lly;
	}
	
	/**
	 * @see com.itextpdf.text.pdf.parser.RenderListener#beginTextBlock()
	 */
	public void beginTextBlock() {
	}

	/**
	 * @see com.itextpdf.text.pdf.parser.RenderListener#endTextBlock()
	 */
	public void endTextBlock() {
	}

	/**
	 * @see com.itextpdf.text.pdf.parser.RenderListener#renderImage(com.itextpdf.text.pdf.parser.ImageRenderInfo)
	 */
	public void renderImage(ImageRenderInfo renderInfo) {
	}
}
