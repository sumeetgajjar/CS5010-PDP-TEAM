package virtualgambling.model.stockdatasource;

import java.util.Date;

import virtualgambling.model.bean.StockPrice;
import virtualgambling.model.exceptions.StockDataNotFoundException;

/**
 * <code>StockDataSource</code> represents a data source for retrieving financial stock market
 * related information.
 */
public interface StockDataSource {
  /**
   * Retrieves the stock price information for a given stock ticker and date.
   *
   * @param tickerName the ticker name of the stock
   * @param date       the date when the stock needs to be purchased
   * @return the price of the stock that matches the given ticker and date
   * @throws StockDataNotFoundException if stock price for the ticker for the given date is not
   *                                    found
   */
  StockPrice getPrice(String tickerName, Date date) throws StockDataNotFoundException;
}
