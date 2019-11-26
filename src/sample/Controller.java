package sample;

import java.util.Arrays;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class Controller {

  @FXML
  private void addProduct(ActionEvent event) {
    System.out.println("Add product");
  }

  @FXML
  private void recordProduction(ActionEvent event) {
    System.out.println("Record Produciton");
  }

  public static void main(String[] args) {
    int[] arr = new int[]{1, 9, 3, 0};

    Arrays.sort(arr);

    System.out.println(Arrays.toString(arr));
  }

}