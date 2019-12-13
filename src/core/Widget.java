package core;

public class Widget extends Product {

  protected Widget(String name, String manufactuer, ItemType type) {
    super(name, manufactuer, type);
  }

  protected Widget(int id, String name, String manufactuer, ItemType type) {
    super(id, name, manufactuer, type);
  }
}
