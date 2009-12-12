/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part1.chapter02;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;

/**
 * We'll test our hsqldb database with this example
 */
public class DatabaseTest {
    
    /** The output of this database test: a text file with a list of countries. */
    public static final String RESULT = "results/part1/chapter02/countries.txt";
    
    /**
     * Writes the names of the countries that are in our database
     * @param    args    no arguments needed 
     * @throws FileNotFoundException 
     */
    public static void main(String[] args)
        throws SQLException, UnsupportedEncodingException, FileNotFoundException {
    	// no PDF, just a text file
        PrintStream out = new PrintStream(new FileOutputStream(RESULT));
        // Make the connection to the database
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        // create the statement
        Statement stm = connection.createStatement();
        // execute the query
        ResultSet rs = stm.executeQuery("SELECT country FROM film_country ORDER BY country");
        // loop over the results
        while (rs.next()) {
        	// write a country to the text file
            out.println(rs.getString("country"));
        }
        // close the statement
        stm.close();
        // close the database connection
        connection.close();
        // flush and close the print stream
        out.flush();
        out.close();
    }
}
