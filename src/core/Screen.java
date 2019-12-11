package core;

public class Screen implements ScreenSpec {

  String resolution;
  int refreshrate;
  int responsetime;

  public Screen(String resolution, int refreshrate, int responsetime) {
    this.resolution = resolution;
    this.refreshrate = refreshrate;
    this.responsetime = responsetime;
  }

  @Override
  public String getResolution() {
    return this.resolution;
  }

  @Override
  public int getRefreshRate() {
    return refreshrate;
  }

  @Override
  public int getResponseTime() {
    return responsetime;
  }

  public String toString() {
    return
        "Resolution: " + resolution + "\n" +
            "Refresh rate: " + refreshrate + "\n" +
            "Response time: " + responsetime;
  }
}
