/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package com.lowagie.filmfestival;

import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;

/**
 * Contains a series of static Font objects that are used throughout the book.
 */
public class FilmFonts {

    /** A font used in our PDF file */
    public static final Font NORMAL = new Font(FontFamily.HELVETICA, 12, Font.NORMAL);
    /** A font used in our PDF file */
    public static final Font BOLD = new Font(FontFamily.HELVETICA, 12, Font.BOLD);
    /** A font used in our PDF file */
    public static final Font ITALIC = new Font(FontFamily.HELVETICA, 12, Font.ITALIC);
    /** A font used in our PDF file */
    public static final Font BOLDITALIC = new Font(FontFamily.HELVETICA, 12, Font.BOLDITALIC);

}
