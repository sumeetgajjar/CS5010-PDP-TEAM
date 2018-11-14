package virtualgambling.model;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import util.Utils;
import virtualgambling.model.bean.Portfolio;
import virtualgambling.model.bean.SharePurchaseOrder;
import virtualgambling.model.exceptions.InsufficientCapitalException;
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
   * Creates a portfolio for this UserModel. The portfolio name should contain at least 1 character,
   * no spaces in the name and cannot contain leading or trailing spaces. It will throw {@link
   * IllegalArgumentException} if the given condition is not satisfied. It throws {@link
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

    Portfolio portfolio = new Portfolio(portfolioName);
    this.portfolios.put(portfolioName, portfolio);
  }

  /**
   * Returns the costBasis of the given portfolio at the given date. It throws {@link
   * IllegalArgumentException} in the following cases.
   * <ul>
   * <li>If any of the given param is null</li>
   * <li>If the given portfolio does not exists</li>
   * <li>If the given date is greater than current date</li>
   * </ul>
   *
   * @param portfolioName the portfolioName
   * @param date          the date
   * @return the costBasis of the given portfolio
   * @throws IllegalArgumentException if the given params are invalid
   */
  @Override
  public BigDecimal getCostBasisOfPortfolio(String portfolioName, Date date)
          throws IllegalArgumentException {

    this.checkSanity(portfolioName, date);

    Portfolio portfolio = this.portfolios.get(portfolioName);

    return portfolio.getPurchases().stream()
            .filter(sharePurchaseInfo -> sharePurchaseInfo.getDate().compareTo(date) <= 0)
            .map(sharePurchaseInfo ->
                    sharePurchaseInfo.getUnitPrice()
                            .multiply(new BigDecimal(sharePurchaseInfo.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  /**
   * Returns the total value of the given portfolio at the given date. It throws {@link
   * IllegalArgumentException} in the following cases.
   * <ul>
   * <li>If any of the given param is null</li>
   * <li>If the given portfolio does not exists</li>
   * <li>If the given date is greater than current date</li>
   * </ul>
   *
   * @param portfolioName the portfolioName
   * @param date          the date
   * @return the total value of the portfolio
   * @throws StockDataNotFoundException if the data is not found for the given date
   * @throws IllegalArgumentException   if the given params are invalid
   */
  @Override
  public BigDecimal getPortfolioValue(String portfolioName, Date date)
          throws StockDataNotFoundException, IllegalArgumentException {

    this.checkSanity(portfolioName, date);

    Portfolio portfolio = this.portfolios.get(portfolioName);
    BigDecimal totalPortfolioValue = BigDecimal.ZERO;

    List<SharePurchaseOrder> filteredPurchaseInfo = portfolio.getPurchases().stream()
            .filter(sharePurchaseInfo -> sharePurchaseInfo.getDate().compareTo(date) <= 0)
            .collect(Collectors.toList());

    for (SharePurchaseOrder sharePurchaseOrder : filteredPurchaseInfo) {
      long quantity = sharePurchaseOrder.getQuantity();
      BigDecimal price =
              this.stockDAO.getPrice(sharePurchaseOrder.getTickerName(), date);
      totalPortfolioValue = totalPortfolioValue.add(price.multiply(new BigDecimal(quantity)));

    }
    return totalPortfolioValue;
  }

  @Override
  public String getPortfolioComposition(String portfolioName) throws IllegalArgumentException {
    if (!this.portfolios.containsKey(portfolioName)) {
      throw new IllegalArgumentException("Portfolio not found");
    }

    Date dateTime = getTodayDate();
    Portfolio portfolio = this.portfolios.get(portfolioName);

    StringBuilder composition = new StringBuilder();
    composition.append(String.format("%-20s%-20s%-20s%s", "Buy Date", "Stocks", "Cost Price",
            "Current Value"));
    composition.append(System.lineSeparator());
    List<SharePurchaseOrder> purchases = portfolio.getPurchases();
    for (SharePurchaseOrder sharePurchaseOrder : purchases) {
      composition.append(String.format("%-20s%-20s%-20s%s",
              Utils.getDefaultFormattedDateStringFromDate(sharePurchaseOrder.getDate()),
              sharePurchaseOrder.getTickerName(),
              Utils.getFormattedCurrencyNumberString(sharePurchaseOrder.getUnitPrice()),
              Utils.getFormattedCurrencyNumberString(
                      this.stockDAO.getPrice(sharePurchaseOrder.getTickerName(), dateTime))));
      composition.append(System.lineSeparator());
    }

    BigDecimal portfolioValue = getPortfolioValue(portfolioName, dateTime);
    BigDecimal costBasisOfPortfolio = getCostBasisOfPortfolio(portfolioName, dateTime);

    composition.append(System.lineSeparator());
    composition.append(String.format("%-20s%s", "Total Value:",
            Utils.getFormattedCurrencyNumberString(portfolioValue)));
    composition.append(System.lineSeparator());

    composition.append(String.format("%-20s%s", "Total Cost:",
            Utils.getFormattedCurrencyNumberString(costBasisOfPortfolio)));
    composition.append(System.lineSeparator());

    composition.append(String.format("%-20s%s", "Profit:",
            Utils.getFormattedCurrencyNumberString(portfolioValue.subtract(costBasisOfPortfolio))));
    return composition.toString();
  }

  protected Date getTodayDate() {
    return Utils.removeTimeFromDate(Calendar.getInstance().getTime());
  }

  @Override
  public String getAllPortfolioNames() {
    return this.portfolios.values().stream()
            .map(Portfolio::getName)
            .collect(Collectors.joining(System.lineSeparator()));
  }

  @Override
  public SharePurchaseOrder buyShares(String tickerName, String portfolioName, Date date,
                                      long quantity) throws IllegalArgumentException,
          StockDataNotFoundException, InsufficientCapitalException {
    Utils.requireNonNull(tickerName);
    this.checkSanity(portfolioName, date);

    SharePurchaseOrder sharePurchaseOrder = this.stockDAO.createPurchaseOrder(tickerName, quantity,
            date, this.remainingCapital);
    this.portfolios.get(portfolioName).addPurchaseInfo(sharePurchaseOrder);
    this.remainingCapital = this.remainingCapital.subtract(sharePurchaseOrder.getCostOfPurchase());
    return sharePurchaseOrder;
  }

  @Override
  public BigDecimal getRemainingCapital() {
    return this.remainingCapital;
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

}
