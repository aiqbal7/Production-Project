package core;

import db.DBHandler;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class Controller {

  private Connection connection;

  @FXML
  private TextField productName;

  @FXML
  private TextField manufacturer;

  @FXML
  private ChoiceBox<String> productType;

  @FXML
  private ComboBox<Integer> chooseQuality;

  @FXML
  private void addProduct(ActionEvent event) throws SQLException {
    System.out.println("Adding product");

    String sql = "INSERT INTO PRODUCT(NAME, TYPE, MANUFACTURER) VALUES (?, ?, ?)";
    PreparedStatement stmt = connection.prepareStatement(sql);
    stmt.setString(1, productName.getText());
    stmt.setString(2, productType.getValue());
    stmt.setString(3, manufacturer.getText());
    stmt.executeUpdate();

    sql = "SELECT * from PRODUCT";
    Statement selectStmt = connection.createStatement();
    ResultSet resultSet = selectStmt.executeQuery(sql);
    while (resultSet.next()) {
      System.out.println(
          resultSet.getString(1) + ", " + resultSet.getString(2) + ", " + resultSet.getString(3)
              + ", " + resultSet.getString(4));
    }
  }

  @FXML
  private void recordProduction(ActionEvent event) {
    System.out.println("Record Produciton");
  }

  @FXML
  public void initialize() throws SQLException {
    System.out.println("Initializing database");
    // Get Connection from dbHandler
    try {
      connection = DBHandler.initializeDB();
    } catch (ClassNotFoundException e) {
      System.err.println("Unable to connect to database: " + e);
    }
    productType.getItems().addAll("Electronics", "Toys", "Household", "Clothes");
    productType.getSelectionModel().selectFirst();
    chooseQuality.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    chooseQuality.setEditable(true);
    chooseQuality.getSelectionModel().selectFirst();
  }

}