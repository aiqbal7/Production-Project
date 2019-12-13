package core;

import db.DBHandler;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
  private ListView<String> produceListView;


  @FXML
  private void addProduct(ActionEvent event) throws SQLException {
    System.out.println("Adding product");

    String sql = "INSERT INTO PRODUCT(NAME, TYPE, MANUFACTURER) VALUES (?, ?, ?)";
    PreparedStatement stmt = connection.prepareStatement(sql);
    stmt.setString(1, productName.getText());
    stmt.setString(2, productType.getValue().name());
    stmt.setString(3, manufacturer.getText());
    stmt.executeUpdate();
    productLine = populateList();
  }

  @FXML
  private void recordProduction(ActionEvent event) {
    System.out.println("Record Production");
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
    productType.getItems().addAll(ItemType.values());
    productType.getSelectionModel().selectFirst();
    chooseQuality.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    chooseQuality.setEditable(true);
    chooseQuality.getSelectionModel().selectFirst();
    testMultimedia();

    productLine = populateList();
    productTableID.setCellValueFactory(new PropertyValueFactory("Id"));
    productTableName.setCellValueFactory(new PropertyValueFactory("Name"));
    productTableManufacturer.setCellValueFactory(new PropertyValueFactory("Manufacturer"));
    productTableType.setCellValueFactory(new PropertyValueFactory("Type"));
    productTable.setItems(productLine);

    produceListView.setItems(FXCollections.observableArrayList(productLine.stream().map(Product::getName).collect(Collectors.toList())));
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

  private ObservableList<Product> populateList() {
    ObservableList<Product> list = FXCollections.observableArrayList();
    try {
      String sql = "SELECT * from PRODUCT";
      Statement selectStmt = this.connection.createStatement();
      ResultSet resultSet = selectStmt.executeQuery(sql);
      while (resultSet.next()) {
        list.add(new Widget(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(4), ItemType.valueOf(resultSet.getString(3))));
      }
    } finally {
      return list;
    }
  }
}