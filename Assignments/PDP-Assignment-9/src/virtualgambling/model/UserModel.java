package virtualgambling.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import virtualgambling.model.bean.Portfolio;
import virtualgambling.model.bean.SharePurchaseOrder;
import virtualgambling.model.exceptions.InsufficientCapitalException;
import virtualgambling.model.exceptions.PortfolioNotFoundException;
import virtualgambling.model.exceptions.StockDataNotFoundException;

/**
 * This interface represents a UserModel for our VirtualGambling MVC app. A UserModel can create
 * portfolio, get cost basis and value of a portfolio at a certain date, examine its composition,
 * buy shares of some stock in a portfolio worth a certain amount at a certain date.
 */
public interface UserModel {


  /**
   * Creates a portfolio for this UserModel.
   *
   * @param portfolioName the name of the portfolio
   */
  void createPortfolio(String portfolioName);

  /**
   * Gets the portfolio matching portfolioName.
   *
   * @param portfolioName name of the portfolio
   * @return gets the portfolio matching portfolioName
   * @throws PortfolioNotFoundException if the portfolio is not found
   */
  Portfolio getPortfolio(String portfolioName) throws PortfolioNotFoundException;

  /**
   * Returns a new line separated string of portfolio name.
   *
   * <p>It will return a blank string if there are no portfolios yet.
   *
   * @return a new line separated string of portfolio name
   */
  List<Portfolio> getAllPortfolios();

  /**
   * Returns the amount of liquid cash that is remaining with the user.
   *
   * <p>The amount that a user starts with is implementation dependent.
   *
   * @return the amount of liquid cash that is remaining with the user.
   */
  BigDecimal getRemainingCapital();

  /**
   * Allows the user the buy shares given the ticker name, date, and quantity into the given
   * portfolio.
   *
   * <p>The following represents error cases:
   * <ul>
   * <li>If a given stock is not found or stock data for the given data and time is not found, it
   * will throw a {@link StockDataNotFoundException}</li>
   * <li>It throws a {@link IllegalArgumentException} if the given is not between 9am to 4pm on
   * weekdays.</li>
   * <li>If the user does not have enough remaining capital to buy shares, then {@link
   * InsufficientCapitalException} is thrown</li>
   * <li>if a portfolio does not exist with the portfolioName, then an {@link
   * IllegalArgumentException} is thrown</li>
   * <li>quantity should be positive integer, if not, then an {@link IllegalArgumentException} is
   * thrown</li>
   * <li>null inputs will result in an {@link IllegalArgumentException}</li>
   * </ul>
   *
   * @param tickerName    the name of the ticker
   * @param portfolioName the name of the portfolio to buy these shares in
   * @param date          the date at which the stocks need to be bought
   * @param quantity      amount of shares to be bought
   * @return a share purchase order that represents the order that was made
   */
  SharePurchaseOrder buyShares(String tickerName,
                               String portfolioName,
                               Date date,
                               long quantity) throws IllegalArgumentException,
          StockDataNotFoundException, InsufficientCapitalException;
}
