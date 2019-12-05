package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBHandler {

  Connection conn = null;

  public static Connection initializeDB() throws ClassNotFoundException, SQLException {

    final String JDBC_DRIVER = "org.h2.Driver";
    final String DB_URL = "jdbc:h2:./res/res";
    final String USER = "";
    final String PASS = "";

    Class.forName(JDBC_DRIVER);
    Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);

    return conn;
  }
}