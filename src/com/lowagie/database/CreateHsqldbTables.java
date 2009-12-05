/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package com.lowagie.database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;


/**
 * Creates the hsqldatabase using SQL scripts containing
 * CREATE statements with the table definitions, and
 * INSERT statements with the data.
 */
public class CreateHsqldbTables {

    /**
     * Imports a number of SQL scripts into an HSQLDB database.
     * @param    args    no arguments needed
     * @throws SQLException 
     * @throws IOException 
     * @throws UnsupportedEncodingException 
     */
    public static void main(String[] args)
        throws SQLException, UnsupportedEncodingException, IOException {
        DatabaseConnection conn = new HsqldbConnection("filmfestival");
        BufferedReader in;
        String line;
        conn.update("SET IGNORECASE TRUE");
        in = new BufferedReader(
                new FileReader("resources/scripts/filmfestival_hsqldb.sql"));
        while( (line = in.readLine()) != null) {
            conn.update(line);
        }
        in = new BufferedReader(
                new FileReader("resources/scripts/filmfestival.sql"));
        while( (line = in.readLine()) != null) {
            conn.update(line);
        }
        conn.close();
    }
}
