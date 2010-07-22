/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */
package part4.chapter14;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.GrayColor;

public class PathConstructionAndPainting {

    /** The resulting PDF. */
    public static final String RESULT = "results/part4/chapter14/paths.pdf";

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
        PdfWriter writer = PdfWriter.getInstance(document,
                new FileOutputStream(filename));
        // step 3
        document.open();
        // step 4
        PdfContentByte canvas = writer.getDirectContent();
        // draw squares
        createSquares(canvas, 50, 720, 80, 20);
        ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT,
            new Phrase(
                "Methods moveTo(), lineTo(), stroke(), closePathStroke(), fill(), and closePathFill()"),
                50, 700, 0);
        // draw Bezier curves
        createBezierCurves(canvas, 70, 600, 80, 670, 140, 690, 160, 630, 160);
        ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT,
            new Phrase("Different curveTo() methods, followed by stroke()"), 50, 580, 0);
        // draw stars and circles
        createStarsAndCircles(canvas, 50, 470, 40, 20);
        ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT,
            new Phrase("Methods fill(), eoFill(), newPath(), fillStroke(), and eoFillStroke()"),
                50, 450, 0);
        // draw different shapes using convenience methods
        canvas.saveState();
        canvas.setColorStroke(new GrayColor(0.2f));
        canvas.setColorFill(new GrayColor(0.9f));
        canvas.arc(50, 270, 150, 330, 45, 270);
        canvas.ellipse(170, 270, 270, 330);
        canvas.circle(320, 300, 30);
        canvas.roundRectangle(370, 270, 80, 60, 20);
        canvas.fillStroke();
        canvas.restoreState();
        Rectangle rect = new Rectangle(470, 270, 550, 330);
        rect.setBorderWidthBottom(10);
        rect.setBorderColorBottom(new GrayColor(0f));
        rect.setBorderWidthLeft(4);
        rect.setBorderColorLeft(new GrayColor(0.9f));
        rect.setBackgroundColor(new GrayColor(0.4f));
        canvas.rectangle(rect);
        ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT,
            new Phrase("Convenience methods"), 50, 250, 0);
        // step 5
        document.close();
    }
    
    /**
     * Draws a row of squares.
     * @param canvas the canvas to which the squares have to be drawn
     * @param x      X coordinate to position the row
     * @param y      Y coordinate to position the row
     * @param side   the side of the square
     * @param gutter the space between the squares
     */
    public void createSquares(PdfContentByte canvas,
        float x, float y, float side, float gutter) {
        canvas.saveState();
        canvas.setColorStroke(new GrayColor(0.2f));
        canvas.setColorFill(new GrayColor(0.9f));
        canvas.moveTo(x, y);
        canvas.lineTo(x + side, y);
        canvas.lineTo(x + side, y + side);
        canvas.lineTo(x, y + side);
        canvas.stroke();
        x = x + side + gutter;
        canvas.moveTo(x, y);
        canvas.lineTo(x + side, y);
        canvas.lineTo(x + side, y + side);
        canvas.lineTo(x, y + side);
        canvas.closePathStroke();
        x = x + side + gutter;
        canvas.moveTo(x, y);
        canvas.lineTo(x + side, y);
        canvas.lineTo(x + side, y + side);
        canvas.lineTo(x, y + side);
        canvas.fill();
        x = x + side + gutter;
        canvas.moveTo(x, y);
        canvas.lineTo(x + side, y);
        canvas.lineTo(x + side, y + side);
        canvas.lineTo(x, y + side);
        canvas.fillStroke();
        x = x + side + gutter;
        canvas.moveTo(x, y);
        canvas.lineTo(x + side, y);
        canvas.lineTo(x + side, y + side);
        canvas.lineTo(x, y + side);
        canvas.closePathFillStroke();
        canvas.restoreState();
    }
    
    /**
     * Draws a series of Bezier curves
     * @param cb the canvas to which the curves have to be drawn
     * @param x0 X coordinate of the start point
     * @param y0 Y coordinate of the start point
     * @param x1 X coordinate of the first control point
     * @param y1 Y coordinate of the first control point
     * @param x2 X coordinate of the second control point
     * @param y2 Y coordinate of the second control point
     * @param x3 X coordinate of the end point
     * @param y3 Y coordinate of the end point
     * @param distance the distance between the curves
     */
    public void createBezierCurves(PdfContentByte cb, float x0, float y0,
        float x1, float y1, float x2, float y2, float x3, float y3, float distance) {
        cb.moveTo(x0, y0);
        cb.lineTo(x1, y1);
        cb.moveTo(x2, y2);
        cb.lineTo(x3, y3);
        cb.moveTo(x0, y0);
        cb.curveTo(x1, y1, x2, y2, x3, y3);
        x0 += distance;
        x1 += distance;
        x2 += distance;
        x3 += distance;
        cb.moveTo(x2, y2);
        cb.lineTo(x3, y3);
        cb.moveTo(x0, y0);
        cb.curveTo(x2, y2, x3, y3);
        x0 += distance;
        x1 += distance;
        x2 += distance;
        x3 += distance;
        cb.moveTo(x0, y0);
        cb.lineTo(x1, y1);
        cb.moveTo(x0, y0);
        cb.curveTo(x1, y1, x3, y3);
        cb.stroke();

    }
    
    /**
     * Draws a row of stars and circles.
     * @param canvas the canvas to which the shapes have to be drawn
     * @param x      X coordinate to position the row
     * @param y      Y coordinate to position the row
     * @param radius the radius of the circles
     * @param gutter the space between the shapes
     */
    public static void createStarsAndCircles(PdfContentByte canvas,
        float x, float y, float radius, float gutter) {
        canvas.saveState();
        canvas.setColorStroke(new GrayColor(0.2f));
        canvas.setColorFill(new GrayColor(0.9f));
        createStar(canvas, x, y);
        createCircle(canvas, x + radius, y - 70, radius, true);
        createCircle(canvas, x + radius, y - 70, radius / 2, true);
        canvas.fill();
        x += 2 * radius + gutter;
        createStar(canvas, x, y);
        createCircle(canvas, x + radius, y - 70, radius, true);
        createCircle(canvas, x + radius, y - 70, radius / 2, true);
        canvas.eoFill();
        x += 2 * radius + gutter;
        createStar(canvas, x, y);
        canvas.newPath();
        createCircle(canvas, x + radius, y - 70, radius, true);
        createCircle(canvas, x + radius, y - 70, radius / 2, true);
        x += 2 * radius + gutter;
        createStar(canvas, x, y);
        createCircle(canvas, x + radius, y - 70, radius, true);
        createCircle(canvas, x + radius, y - 70, radius / 2, false);
        canvas.fillStroke();
        x += 2 * radius + gutter;
        createStar(canvas, x, y);
        createCircle(canvas, x + radius, y - 70, radius, true);
        createCircle(canvas, x + radius, y - 70, radius / 2, true);
        canvas.eoFillStroke();
        canvas.restoreState();
    }
    
    /**
     * Creates a path for a five pointed star.
     * This method doesn't fill or stroke the star!
     * @param canvas the canvas for which the star is constructed
     * @param x      the X coordinate of the center of the star
     * @param y      the Y coordinate of the center of the star
     */
    public static void createStar(PdfContentByte canvas, float x, float y) {
        canvas.moveTo(x + 10, y);
        canvas.lineTo(x + 80, y + 60);
        canvas.lineTo(x, y + 60);
        canvas.lineTo(x + 70, y);
        canvas.lineTo(x + 40, y + 90);
        canvas.closePath();
    }
 
    /**
     * Creates a path for circle using Bezier curvers.
     * The path can be constructed clockwise or counter-clockwise.
     * This method doesn't fill or stroke the circle!
     * @param canvas    the canvas for which the path is constructed 
     * @param x         the X coordinate of the center of the circle
     * @param y         the Y coordinate of the center of the circle
     * @param r         the radius
     * @param clockwise true if the circle has to be constructed clockwise
     */
    public static void createCircle(PdfContentByte canvas, float x, float y,
        float r, boolean clockwise) {
        float b = 0.5523f;
        if (clockwise) {
            canvas.moveTo(x + r, y);
            canvas.curveTo(x + r, y - r * b, x + r * b, y - r, x, y - r);
            canvas.curveTo(x - r * b, y - r, x - r, y - r * b, x - r, y);
            canvas.curveTo(x - r, y + r * b, x - r * b, y + r, x, y + r);
            canvas.curveTo(x + r * b, y + r, x + r, y + r * b, x + r, y);
        } else {
            canvas.moveTo(x + r, y);
            canvas.curveTo(x + r, y + r * b, x + r * b, y + r, x, y + r);
            canvas.curveTo(x - r * b, y + r, x - r, y + r * b, x - r, y);
            canvas.curveTo(x - r, y - r * b, x - r * b, y - r, x, y - r);
            canvas.curveTo(x + r * b, y - r, x + r, y - r * b, x + r, y);
        }
    }

    /**
     * Main method.
     *
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException
     */
    public static void main(String[] args) throws IOException, DocumentException {
        new PathConstructionAndPainting().createPdf(RESULT);
    }
}
