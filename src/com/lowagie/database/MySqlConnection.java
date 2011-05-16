/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package com.lowagie.database;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * This is a helper class to access a mySql database.
 * The username and password of the database are supposed to be present
 * in a properties file mysql.props (in the working directory)
 */
public class MySqlConnection extends DatabaseConnection {

    /**
     * Creates the connection.
     * @param db_file_name_prefix the database name,
     * which is the prefix of the database file
     * @throws SQLException 
     */
    public MySqlConnection(String database) throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("mySql database driver not found");
        }
        Properties credentials = new Properties();
        try {
            credentials.load(new FileInputStream("mysql.props"));
        } catch (IOException e) {
            throw new SQLException("Can't read mysql.props.");
        }
        connection = DriverManager.getConnection(
                "jdbc:mysql://localhost/" + database,
                credentials.getProperty("username"),
                credentials.getProperty("password"));
    }

}
