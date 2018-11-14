package virtualgambling.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import virtualgambling.model.bean.Portfolio;
import virtualgambling.model.bean.PurchaseInfo;
import virtualgambling.model.stockdatasource.StockExchange;
import virtualgambling.util.Utils;

/**
 * Created by gajjar.s, on 9:45 PM, 11/12/18
 */
public class SimpleUserModel implements UserModel {

  private final StockExchange stockExchange;
  private final Map<String, Portfolio> portfolios;

  /**
   * Constructs a {@link SimpleUserModel} object with given params.
   *
   * @param stockExchange the stockExchange
   * @throws IllegalArgumentException if the given stockExchange is null
   */
  public SimpleUserModel(StockExchange stockExchange) throws IllegalArgumentException {
    this.stockExchange = Utils.requireNonNull(stockExchange);
    this.portfolios = new HashMap<>();
  }

  /**
   * Creates a portfolio for this UserModel. The portfolio name should contain at least 1 character
   * and cannot contain leading or trailing spaces. It will throw {@link IllegalArgumentException}
   * if the given condition is not satisfied. It throws {@link IllegalArgumentException} if the
   * given portfolioName is null or empty. If a portfolio already exists then it will throw an
   * {@link IllegalArgumentException}.
   *
   * @param portfolioName the name of the portfolio
   * @throws IllegalArgumentException if the name is invalid
   */
  @Override
  public void createPortfolio(String portfolioName) throws IllegalArgumentException {
    if (this.portfolios.containsKey(portfolioName)) {
      throw new IllegalArgumentException("Portfolio already exists");
    }

    if (portfolioName.length() == 0) {
      throw new IllegalArgumentException("Invalid Portfolio Name");
    }

    if (portfolioName.trim().length() != portfolioName.length()) {
      throw new IllegalArgumentException("Invalid Portfolio Name");
    }

    Portfolio portfolio = new Portfolio(portfolioName);
    this.portfolios.put(portfolioName, portfolio);
  }

  @Override
  public BigDecimal getCostBasisOfPortfolio(String portfolioName, Date date) {
    return null;
  }

  @Override
  public BigDecimal getPortfolioValue(String portfolioName, Date date) {
    return null;
  }

  @Override
  public String getPortfolioComposition(String portfolioName) throws IllegalArgumentException {
    return null;
  }

  @Override
  public PurchaseInfo buyShares(String tickerName, String portfolioName, Date date, long quantity) {
    return null;
  }

  @Override
  public BigDecimal getRemainingCapital() {
    return null;
  }
}
