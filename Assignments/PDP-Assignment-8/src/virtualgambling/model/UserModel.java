package virtualgambling.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import virtualgambling.model.bean.Portfolio;
import virtualgambling.model.bean.PurchaseInfo;
import virtualgambling.model.bean.Share;
import virtualgambling.model.exceptions.InvalidPurchaseOrderException;

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
   * Returns a portfolio associated with given name. Returns null if there is no portfolio
   * associated with the given name.
   *
   * @param portfolioName the name of the portfolio
   * @return the portfolio
   */
  Portfolio getPortfolio(String portfolioName);

  /**
   * Returns all portfolios for this {@link UserModel}. Returns empty list if there are none.
   *
   * @return all portfolios
   */
  List<Portfolio> getAllPortfolios();

  /**
   * <ul>
   * <li>If the date for purchase is back dated then the share will bought at closing price of
   * that day</li>
   * <li>If the date for purchase is in future then {@link InvalidPurchaseOrderException} will
   * be thrown</li>
   * <li>If the date for purchase is non working day then </li>
   * </ul>
   *
   * @param tickerName    a
   * @param portfolioName a
   * @param date          a
   * @param quantity      a
   * @return a
   */
  PurchaseInfo buyShares(String tickerName,
                         String portfolioName,
                         Date date,
                         long quantity);
  //todo can buy shares between 9am to 4pm on weekdays chuck holidays.

  BigDecimal getRemainingCapital();

  /**
   * It throws a {@link IllegalArgumentException} if the given is not between 9am to 4pm on
   * weekdays.
   *
   * @param share a
   * @param date  a
   * @throws IllegalArgumentException a
   */
  void addShareData(Share share, Date date) throws IllegalArgumentException;
}
