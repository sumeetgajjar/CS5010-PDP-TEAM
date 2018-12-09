package virtualgambling.model.factory;

import util.Utils;
import virtualgambling.model.stockdatasource.StockDataSource;

/**
 * {@link StockDataSourceType} is an enum that represents the type of {@link StockDataSource} to
 * use.
 */
public enum StockDataSourceType {
  SIMPLE("In Memory"),
  ALPHA_VANTAGE("Alpha Vantage"),
  MOCK("Mock");

  private final String name;

  /**
   * Constructs a {@link StockDataSourceType} given the name of the stock data source type.
   *
   * @param name the name of the data source type as a string
   */
  StockDataSourceType(String name) {
    this.name = Utils.requireNonNull(name);
  }

  /**
   * Gets the name of the data source type in String representation form.
   *
   * @return the string representation of the name of the data source type
   */
  public String getName() {
    return name;
  }
}
