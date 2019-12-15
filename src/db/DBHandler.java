package db;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBHandler {

  Connection conn = null;

  public static Connection initializeDB() throws ClassNotFoundException, SQLException, IOException {

    final String JDBC_DRIVER = "org.h2.Driver";
    final String DB_URL = "jdbc:h2:./res/res";
    final String USER = "";
    final String PASS;

    Properties prop = new Properties();
    prop.load(new FileInputStream("res/properties"));
    PASS = prop.getProperty("password");

    Class.forName(JDBC_DRIVER);
    Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);

    return conn;
  }
}