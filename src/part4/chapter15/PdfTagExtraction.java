/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part4.chapter15;

import java.io.IOException;

import com.itextpdf.text.pdf.PRTokeniser;

/**
 * Simple state machine that extracts strings from a content stream,
 * given a tag name and an MCID. This won't work for all tagged PDFs
 * as we're making assumptions about the PDF that is parsed.
 * Needs more work if you want to be able to parse any tagged PDF.
 */
public class PdfTagExtraction {

    /** A possible state: tag not found yet. */
    public static final int SEARCHING = 0;
    /** A possible state: tag found. */
    public static final int TAG_FOUND = 1;
    /** A possible state: tag found, inside dictionary. */
    public static final int DICTIONARY = 2;
    /** A possible state: tag and MCID found. */
    public static final int MCID_FOUND = 3;
    /** A possible state: read dictionary, looking for BDC. */
    public static final int SEARCHING_BDC = 4;
    /** A possible state: reading content. */
    public static final int START_READING = 5;
    /** A possible state: content read. */
    public static final int STOP_READING = 6;

    /**
     * Parses a content stream for marked content strings.
     * @param tag a marked content tag
     * @param mcid a marked content id
     * @param content the full content stream
     * @return a string with the content
     * @throws IOException
     */
    public static String parse(String tag, int mcid, byte[] content)
        throws IOException {
    	// We start searching
        int status = SEARCHING;
        // We didn't find any BDC operators yet
        int nesting = 0;
        // We'll store the strings in a buffer
        StringBuffer buf = new StringBuffer();
        // We create a PRTokeniser for the content stream
        PRTokeniser tokenizer = new PRTokeniser(content);
        // We parse the content stream
        while (status != STOP_READING) {
            // We move to the next valid token
            tokenizer.nextValidToken();
            // We escape from the loop when we've reach the end
            if (tokenizer.getTokenType() == PRTokeniser.TokenType.ENDOFFILE)
                return buf.toString();
            // Otherwise we look at the current status
            switch(status) {
            case SEARCHING:
                // Switch to TAG_FOUND if we have a match with the tag
                if (tokenizer.getTokenType() == PRTokeniser.TokenType.NAME
                    && tag.equals(tokenizer.getStringValue())) {
                    status = TAG_FOUND;
                }
                break;
            case TAG_FOUND:
                // The tag needs to be followed by a dictionary
                if (tokenizer.getTokenType() != PRTokeniser.TokenType.START_DIC)
                    status = SEARCHING;
                else
                    status = DICTIONARY;
                break;
            case DICTIONARY:
                // The dictionary needs to contain an /MCID
                while (status == DICTIONARY) {
                    String key;
                    if (tokenizer.getTokenType() == PRTokeniser.TokenType.NAME) {
                        key = tokenizer.getStringValue();
                        tokenizer.nextValidToken();
                        if ("MCID".equals(key))
                            if (tokenizer.intValue() == mcid)
                                status = MCID_FOUND;
                            else
                                status = SEARCHING;
                    }
                }
                break;
            case MCID_FOUND:
                // We look for the end of the dictionary
                if (tokenizer.getTokenType() == PRTokeniser.TokenType.END_DIC)
                    status = SEARCHING_BDC;
                break;
            case SEARCHING_BDC:
            	// As soon as we find a BDC, we start (or continue) reading
                if (tokenizer.getTokenType() == PRTokeniser.TokenType.OTHER
                    && "BDC".equals(tokenizer.getStringValue())) {
                    nesting++;	
                    status = START_READING;
                }
                break;
            case START_READING:
            	// This is based on an assumption: we don't know if a space is needed
                if (tokenizer.getTokenType() == PRTokeniser.TokenType.STRING)
                    buf.append(tokenizer.getStringValue()).append(" ");
                else if (tokenizer.getTokenType() == PRTokeniser.TokenType.OTHER)
                    // If we find an EMC, we may have reached the end of the MC
                    if ("EMC".equals(tokenizer.getStringValue()) && --nesting == 0)
                        status = STOP_READING;
                    // If we find a BDC, we have nested MC
                    else if ("BDC".equals(tokenizer.getStringValue()))
                        nesting++;
            }
        }
        return buf.toString();
    }
}
    
