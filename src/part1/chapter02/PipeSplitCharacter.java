/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part1.chapter02;

import com.itextpdf.text.SplitCharacter;
import com.itextpdf.text.pdf.PdfChunk;

/**
 * Implementation of the SplitCharacter interface.
 * Use an instance of this class with Chunk.setSplitCharacter();
 */
public class PipeSplitCharacter implements SplitCharacter {
	
    /**
     * @see com.itextpdf.text.SplitCharacter#isSplitCharacter(int, int, int, char[],
     *      com.itextpdf.text.pdf.PdfChunk[])
     */
    public boolean isSplitCharacter(int start, int current, int end, char[] cc,
            PdfChunk[] ck) {
        char c;
        if (ck == null)
            c = cc[current];
        else
            c = (char)ck[Math.min(current, ck.length - 1)]
                    .getUnicodeEquivalent(cc[current]);
        return (c == '|' || c <= ' ' || c == '-');
    }

}
