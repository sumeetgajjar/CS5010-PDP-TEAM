package virtualgambling.model;

import java.math.BigDecimal;
import java.util.Date;

import virtualgambling.model.bean.SharePurchaseInfo;
import virtualgambling.model.exceptions.StockDataNotFoundException;

/**
 * Created by gajjar.s, on 8:11 PM, 11/11/18
 */
public interface UserModel {


  /**
   * Creates a portfolio for this UserModel.
   *
   * @param portfolioName the name of the portfolio
   */
  void createPortfolio(String portfolioName);

  /**
   * Returns the costBasis of the given portfolio at the given date.
   *
   * @param portfolioName the portfolioName
   * @param date          the date
   * @return the costBasis of the given portfolio
   */
  BigDecimal getCostBasisOfPortfolio(String portfolioName, Date date);

  BigDecimal getPortfolioValue(String portfolioName, Date date);

  /**
   * Returns the composition of portfolio in string format. Returns empty string if portfolio is
   * empty.
   *
   * @param portfolioName a
   * @return a
   * @throws IllegalArgumentException if the portfolio does not exists
   */
  String getPortfolioComposition(String portfolioName) throws IllegalArgumentException;

  /**
   * Returns a new line separated string of portfolio name.
   *
   * <p>It will return a blank string if there are no portfolios yet.
   *
   * @return a new line separated string of portfolio name
   */
  String getAllPortfolioNames();

  /**
   * Returns the amount of liquid cash that is remaining with the user.
   *
   * <p>The amount that a user starts with is implementation dependent.
   *
   * @return the amount of liquid cash that is remaining with the user.
   */
  BigDecimal getRemainingCapital();

  /**
   * <ul>
   * <li>If the date for purchase is back dated then the share will bought at closing price of
   * that day</li> // todo move to implementation
   * <li>If a given stock is not found or stock data for the given data and time is not found, it
   * will throw a {@link StockDataNotFoundException}</li>
   * <li>It throws a {@link IllegalArgumentException} if the given is not between 9am to 4pm on
   * weekdays.</li>
   * <li>If the user does not have enough remaining capital to buy shares, then {@link
   * IllegalStateException} is thrown</li>
   * <li>If a stock does not exist with the tickerName or if a portfolio does not exist with
   * the portfolioName, then an {@link IllegalArgumentException} is thrown</li>
   * <li>quantity should be positive, if not, then an {@link IllegalArgumentException} is thrown
   * </li>
   * <li>null inputs will result in an {@link IllegalArgumentException}</li>
   * <li>If the user does not have enough funds to create a purchase transaction, then we throw
   * an {@link IllegalStateException}.
   * </li>
   * </ul>
   *
   * @param tickerName    a
   * @param portfolioName a
   * @param date          a
   * @param quantity      a
   * @return a
   */
  SharePurchaseInfo buyShares(String tickerName,
                              String portfolioName,
                              Date date,
                              long quantity) throws IllegalArgumentException,
          StockDataNotFoundException,
          IllegalStateException;
}
