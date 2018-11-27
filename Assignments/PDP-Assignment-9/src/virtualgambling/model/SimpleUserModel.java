package virtualgambling.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.Utils;
import virtualgambling.model.bean.Portfolio;
import virtualgambling.model.bean.SharePurchaseOrder;
import virtualgambling.model.exceptions.InsufficientCapitalException;
import virtualgambling.model.exceptions.PortfolioNotFoundException;
import virtualgambling.model.exceptions.StockDataNotFoundException;
import virtualgambling.model.stockdao.StockDAO;

/**
 * This class represents a Simple User Model. It implements {@link UserModel} interface.
 *
 * <p>{@link SimpleUserModel} performs several operations with {@link Date}s and it only
 * considers the year, month and day components out of the date - ignoring the time.
 */
public class SimpleUserModel implements UserModel {

  private static final BigDecimal DEFAULT_USER_CAPITAL = new BigDecimal("10000000");

  private final StockDAO stockDAO;
  private final Map<String, Portfolio> portfolios;
  private BigDecimal remainingCapital;

  /**
   * Constructs a {@link SimpleUserModel} object with given params.
   *
   * @param stockDAO the stockDAO
   * @throws IllegalArgumentException if the given stockDAO is null
   */
  public SimpleUserModel(StockDAO stockDAO) throws IllegalArgumentException {
    this.stockDAO = Utils.requireNonNull(stockDAO);
    this.portfolios = new HashMap<>();
    this.remainingCapital = DEFAULT_USER_CAPITAL;
  }

  /**
   * Creates a portfolio for this UserModel. The portfolio name should be a single word,should
   * contain at least 1 character, and cannot contain leading or trailing spaces. It will throw
   * {@link IllegalArgumentException} if the above conditions are not satisfied. It throws {@link
   * IllegalArgumentException} if the given portfolioName is null or empty. If a portfolio already
   * exists then it will throw an {@link IllegalArgumentException}.
   *
   * @param portfolioName the name of the portfolio
   * @throws IllegalArgumentException if the name is invalid
   */
  @Override
  public void createPortfolio(String portfolioName) throws IllegalArgumentException {
    Utils.requireNonNull(portfolioName);
    if (this.portfolios.containsKey(portfolioName)) {
      throw new IllegalArgumentException("Portfolio already exists");
    }

    if (portfolioName.length() == 0) {
      throw new IllegalArgumentException("Invalid Portfolio Name");
    }

    if (portfolioName.trim().length() != portfolioName.length()) {
      throw new IllegalArgumentException("Invalid Portfolio Name");
    }

    Portfolio portfolio = instantiatePortfolio(portfolioName, Collections.emptyList());
    this.portfolios.put(portfolioName, portfolio);
  }

  @Override
  public Portfolio getPortfolio(String portfolioName) throws PortfolioNotFoundException {
    Utils.requireNonNull(portfolioName);
    if (this.portfolios.containsKey(portfolioName)) {
      return this.portfolios.get(portfolioName);
    } else {
      throw new PortfolioNotFoundException(String.format("portfolio by the name '%s' not found",
              portfolioName));
    }
  }

  @Override
  public List<Portfolio> getAllPortfolios() {
    return new ArrayList<>(this.portfolios.values());
  }

  @Override
  public SharePurchaseOrder buyShares(String tickerName, String portfolioName, Date date,
                                      long quantity) throws IllegalArgumentException,
          StockDataNotFoundException, InsufficientCapitalException {
    Utils.requireNonNull(tickerName);
    this.checkSanity(portfolioName, date);

    SharePurchaseOrder sharePurchaseOrder = this.stockDAO.createPurchaseOrder(tickerName, quantity,
            date, this.remainingCapital);
    addOrderToPortfolio(sharePurchaseOrder, portfolioName);
    this.remainingCapital = this.remainingCapital.subtract(sharePurchaseOrder.getCostOfPurchase());
    return sharePurchaseOrder;
  }

  @Override
  public BigDecimal getRemainingCapital() {
    return this.remainingCapital;
  }

  protected Portfolio instantiatePortfolio(String portfolioName,
                                           List<SharePurchaseOrder> sharePurchaseOrders) {
    return new Portfolio(portfolioName, stockDAO, sharePurchaseOrders);
  }

  private void checkSanity(String portfolioName, Date date) throws IllegalArgumentException {
    Utils.requireNonNull(portfolioName);
    Utils.requireNonNull(date);

    if (!this.portfolios.containsKey(portfolioName)) {
      throw new IllegalArgumentException("Portfolio not found");
    }

    if (Utils.isFutureDate(date)) {
      throw new IllegalArgumentException("Time cannot be in Future");
    }
  }

  private void addOrderToPortfolio(SharePurchaseOrder sharePurchaseOrder, String portfolioName) {
    Portfolio portfolio = this.portfolios.get(portfolioName);
    ArrayList<SharePurchaseOrder> newSharePurchaseOrders =
            new ArrayList<>(portfolio.getPurchases());
    newSharePurchaseOrders.add(sharePurchaseOrder);
    this.portfolios.put(portfolioName, instantiatePortfolio(portfolioName, newSharePurchaseOrders));
  }
}
