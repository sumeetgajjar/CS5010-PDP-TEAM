package virtualgambling.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import virtualgambling.model.bean.SharePurchaseOrder;
import virtualgambling.model.exceptions.InsufficientCapitalException;
import virtualgambling.model.exceptions.StockDataNotFoundException;
import virtualgambling.model.exceptions.StrategyExecutionException;
import virtualgambling.model.strategy.Strategy;

/**
 * {@link EnhancedUserModel} is an extension of the {@link UserModel} interface and adds
 * tradingFeatures
 * that allow the user to buy shares and specify commission value. It also provides the option to
 * buy stock via a higher level strategy {@link Strategy}.
 */
public interface EnhancedUserModel extends UserModel {


  /**
   * Allows the user the buy shares given the ticker name, date, quantity, commissionPercentage into
   * the given portfolio.
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
   * <p>It allows the user to purchase stocks with a commission fee expressed in percentages. How
   * it works is as follows: If a user buys $1000 worth of stock on a 10% commission fee, then
   * he/she will get $1000 worth of stock, but $1100 will be reduced from his/her remaining
   * capital.
   *
   * @param tickerName           ticker name of the stock
   * @param portfolioName        portfolio into which to buy the stock
   * @param date                 the date on which the stock needs to be purchased
   * @param quantity             the number of shares to be purchased
   * @param commissionPercentage the percentage out of 100 of the final amount to be added as
   *                             commission charges.
   * @return SharePurchaseOrder that represents the transaction
   * @throws IllegalArgumentException     if any of the inputs are null or if quantity is not
   *                                      positive or if commissionPercentage is less than 0
   * @throws StockDataNotFoundException   If a given stock is not found or stock data for the given
   *                                      data and time is not found
   * @throws InsufficientCapitalException If the user does not have enough remaining capital to buy
   *                                      shares
   */
  SharePurchaseOrder buyShares(String tickerName,
                               String portfolioName,
                               Date date,
                               long quantity,
                               double commissionPercentage) throws IllegalArgumentException,
          StockDataNotFoundException, InsufficientCapitalException;

  /**
   * This method offers functionality to buy shares given a strategy, portfolioName, amountToInvest
   * and the commission percentage.
   *
   * <p>If the portfolio does not exist, a strategy creates one.
   *
   * <p>The amount to invest represents dollars to invest and cannot be less than $1
   *
   * @param portfolioName        portfolio into which to buy the stock
   * @param amountToInvest       amount to invest in each transaction that the strategy executes
   * @param strategy             the strategy with which to purchase stock
   * @param commissionPercentage the percentage out of 100 of the final amount to be added as
   *                             commission charges.
   * @return a list of sharePurchaseOrders that represents the transactions by the strategy
   * @throws IllegalArgumentException     if any of the inputs are null or if commissionPercentage
   *                                      is less than 0 or the amount to invest is less than 1
   * @throws StockDataNotFoundException   If a given stock is not found or stock data for the any
   *                                      data and time used by the strategy is not found
   * @throws InsufficientCapitalException If the user does not have enough remaining capital to buy
   *                                      shares
   * @throws StrategyExecutionException   if the strategy cannot execute a single successful dated
   *                                      transaction
   */
  List<SharePurchaseOrder> buyShares(String portfolioName,
                                     BigDecimal amountToInvest, Strategy strategy,
                                     double commissionPercentage)
          throws IllegalArgumentException, StockDataNotFoundException,
          InsufficientCapitalException, StrategyExecutionException;
}