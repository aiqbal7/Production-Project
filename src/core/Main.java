package core;

// Java program to create multiple tabs
// and add it to the TabPane

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

  // launch the application
  public void start(Stage stage) throws IOException {

    Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

    // set title for the stage
    stage.setTitle("Production Project");

    // create a scene
    Scene scene = new Scene(root);
    scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

    // set the scene
    stage.setScene(scene);

    stage.show();
  }

  // Main Method
  public static void main(String[] args) {

    // launch the application
    launch(args);
  }

}