package core;

import db.DBHandler;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class Controller {

  private Connection connection;
  private ObservableList<Product> productLine;

  @FXML
  private TextField productName;

  @FXML
  private TextField manufacturer;

  @FXML
  private ChoiceBox<ItemType> productType;

  @FXML
  private ComboBox<Integer> chooseQuality;

  @FXML
  private TextArea productionLog;

  @FXML
  private TableView<Product> productTable;

  @FXML
  private TableColumn<?, ?> productTableID;

  @FXML
  private TableColumn<?, ?> productTableName;

  @FXML
  private TableColumn<?, ?> productTableManufacturer;

  @FXML
  private TableColumn<?, ?> productTableType;

  @FXML
  private ListView<Product> produceListView;

  @FXML
  private TextField employeeName;

  @FXML
  private TextField employeePassword;

  @FXML
  private TextField employeeEmail;

  @FXML
  private TextArea employeeOutput;

  @FXML
  private Button addEmployee;


  @FXML
  private void addProduct(ActionEvent event) throws SQLException {
    System.out.println("Adding product");

    if (!verifyAddProduct(productName, manufacturer)) {
      return;
    }

    String sql = "INSERT INTO PRODUCT(NAME, TYPE, MANUFACTURER) VALUES (?, ?, ?)";
    PreparedStatement stmt = connection.prepareStatement(sql);
    stmt.setString(1, productName.getText());
    stmt.setString(2, productType.getValue().name());
    stmt.setString(3, manufacturer.getText());
    stmt.executeUpdate();
    stmt.close();
    setupProducts();
  }

  @FXML
  private void recordProduction(ActionEvent event) throws SQLException {
    System.out.println("Recording Production");
    if (!verifyProduction(produceListView, chooseQuality)) {
      return;
    }
    Product product = produceListView.getSelectionModel().getSelectedItem();
    ProductionRecord productionRecord = new ProductionRecord(product, 0);
    String sql =
        "INSERT INTO PRODUCTIONRECORD(PRODUCTION_NUM, PRODUCT_ID,"
            + " SERIAL_NUM, DATE_PRODUCED ) VALUES (?, ?, ?, ?)";

    PreparedStatement stmt = connection.prepareStatement(sql);
    stmt.setInt(1, productionRecord.getProductionNumber());
    stmt.setInt(2, productionRecord.getProductID());
    stmt.setString(3, productionRecord.getSerialNumber());
    stmt.setTimestamp(4, Timestamp.from(productionRecord.getDateProduced().toInstant()));

    stmt.executeUpdate();
    stmt.close();

    StringBuilder text = new StringBuilder(productionLog.getText());
    text.append("Recorded new Production\n====================\n");
    text.append(productionRecord.toString()).append("\n\n");
    productionLog.clear();
    productionLog.setText(text.toString());
  }

  @FXML
  private void addEmployee(ActionEvent event) {
    System.out.println("Adding Employee");
    if (!verifyAddEmployee(employeeName, employeePassword)) {
      return;
    }
    Employee employee = new Employee(employeeName.getText(), employeePassword.getText());
    StringBuilder text = new StringBuilder(employeeOutput.getText());
    text.append("Added New Employee\n=================\n");
    text.append(employee.toString()).append("\n\n");
    employeeOutput.clear();
    employeeOutput.setText(text.toString());
  }

  @FXML
  public void initialize() throws Exception {
    System.out.println("Initializing database");
    // Get Connection from dbHandler
    try {
      connection = DBHandler.initializeDB();
    } catch (ClassNotFoundException e) {
      System.err.println("Unable to connect to database: " + e);
    }
    productType.getItems().addAll(ItemType.values());
    productType.getSelectionModel().selectFirst();
    chooseQuality.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    chooseQuality.setEditable(true);
    chooseQuality.getSelectionModel().selectFirst();
    testMultimedia();
    productLine = populateList();
    setupProducts();
    productTableID.setCellValueFactory(new PropertyValueFactory("Id"));
    productTableName.setCellValueFactory(new PropertyValueFactory("Name"));
    productTableManufacturer.setCellValueFactory(new PropertyValueFactory("Manufacturer"));
    productTableType.setCellValueFactory(new PropertyValueFactory("Type"));
    productTable.setItems(productLine);

    produceListView.setItems(productLine);
  }

  public static void testMultimedia() {
    AudioPlayer newAudioProduct = new AudioPlayer("DP-X1A", "Onkyo",
        "DSD/FLAC/ALAC/WAV/AIFF/MQA/Ogg-Vorbis/MP3/AAC", "M3U/PLS/WPL");
    Screen newScreen = new Screen("720x480", 40, 22);
    MoviePlayer newMovieProduct = new MoviePlayer("DBPOWER MK101", "OracleProduction", newScreen,
        MonitorType.LCD);
    ArrayList<MultimediaControl> productList = new ArrayList<>();
    productList.add(newAudioProduct);
    productList.add(newMovieProduct);
    for (MultimediaControl p : productList) {
      System.out.println(p);
      p.play();
      p.stop();
      p.next();
      p.previous();
    }
  }

  private void setupProducts() {
    productLine.clear();
    productLine = populateList();
    productTable.setItems(productLine);
    produceListView.setItems(productLine);
  }

  private ObservableList<Product> populateList() {
    ObservableList<Product> list = FXCollections.observableArrayList();
    try {
      String sql = "SELECT * from PRODUCT";
      Statement selectStmt = this.connection.createStatement();
      ResultSet resultSet = selectStmt.executeQuery(sql);
      while (resultSet.next()) {
        list.add(new Widget(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(4),
            ItemType.valueOf(resultSet.getString(3))));
      }
    } finally {
      return list;
    }
  }

  private boolean verifyProduction(ListView<Product> produceListView,
      ComboBox<Integer> chooseQuality) {
    if (produceListView.getSelectionModel().getSelectedItem() == null) {
      System.err.println("Product must be selected");
      return false;
    } else if (chooseQuality.getSelectionModel().getSelectedItem() == null || !String
        .valueOf(chooseQuality.getSelectionModel().getSelectedItem()).matches("\\d+")) {
      System.err.println("Quality must be valid numeric");
      return false;
    }
    return true;
  }

  private boolean verifyAddEmployee(TextField employeeName, TextField employeePassword) {
    if (employeeName.getText().isEmpty()) {
      System.err.println("Employee name can't be empty");
      return false;
    } else if (employeePassword.getText().isEmpty()) {
      System.err.println("Employee pass can't be empty");
      return false;
    }
    return true;
  }

  private boolean verifyAddProduct(TextField productName, TextField manufacturer) {
    if (productName.getText().isEmpty()) {
      System.err.println("Product name can't be empty");
      return false;
    } else if (manufacturer.getText().isEmpty()) {
      System.err.println("Manufacturer can't be empty");
      return false;
    }
    return true;
  }
}