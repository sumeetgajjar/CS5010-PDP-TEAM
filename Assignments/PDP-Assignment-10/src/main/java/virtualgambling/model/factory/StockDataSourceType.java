package virtualgambling.model.factory;

public enum StockDataSourceType {
  SIMPLE("In Memory"),
  ALPHA_VANTAGE("Alpha Vantage"),
  MOCK("Mock");

  private final String name;

  StockDataSourceType(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
