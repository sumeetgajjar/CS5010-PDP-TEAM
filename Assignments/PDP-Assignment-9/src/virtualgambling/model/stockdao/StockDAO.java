package virtualgambling.model.stockdao;

import java.math.BigDecimal;
import java.util.Date;

import virtualgambling.model.bean.SharePurchaseOrder;
import virtualgambling.model.exceptions.StockDataNotFoundException;

/**
 * <code>StockDAO</code> represents a stock data access object that acts as a mediator between
 * the user and the data source. <code>StockDAO</code> provides features of retrieving stock prices
 * and buying stocks.
 *
 * <p>This interface may be extended in the future to support stock sale.
 */
public interface StockDAO {
  /**
   * Allows a user to create a purchase order given the tickerName, quantity, date and
   * remainingCapital.
   *
   * @param tickerName the stock ticker name
   * @param quantity   the quantity of the stock to purchase
   * @param date       the date of purchase
   * @return a share purchase order
   * @throws IllegalArgumentException if the arguments are null or if the quantity is 0 or if the
   *                                  date represents a day in the future or a weekend
   * @throws IllegalStateException    if the arguments are valid but the remainingCapital is not
   *                                  sufficient to create a valid order.
   */
  SharePurchaseOrder createPurchaseOrder(String tickerName, long quantity, Date date)
          throws IllegalStateException, IllegalArgumentException, StockDataNotFoundException;

  /**
   * Retrieves the stock price given the ticker name and the date.
   *
   * @param tickerName the name of the stock ticker
   * @param date       the date and time at which the price is needed
   * @return the stock price at the given date
   * @throws IllegalArgumentException   if any argument is null or if the date represents a day in
   *                                    the future or a weekend
   * @throws StockDataNotFoundException if the stock price at the given date is not found
   */
  BigDecimal getPrice(String tickerName, Date date) throws StockDataNotFoundException,
          IllegalArgumentException;
}
