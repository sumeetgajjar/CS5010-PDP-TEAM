package virtualgambling.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import virtualgambling.model.bean.Portfolio;
import virtualgambling.model.bean.PurchaseInfo;
import virtualgambling.model.stockdatasource.StockExchange;

/**
 * Created by gajjar.s, on 9:45 PM, 11/12/18
 */
public class SimpleUserModel implements UserModel {

  public SimpleUserModel(StockExchange stockExchange) {

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

  }

  /**
   * Returns a portfolio associated with given name. Returns null if there is no portfolio
   * associated with the given name.
   *
   * @param portfolioName the name of the portfolio
   * @return the portfolio
   */
  @Override
  public Portfolio getPortfolio(String portfolioName) {
    return null;
  }

  /**
   * Returns all portfolios for this {@link UserModel}. Returns empty list if there are none.
   *
   * @return all portfolios
   */
  @Override
  public List<Portfolio> getAllPortfolios() {
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
