package virtualgambling.model.bean;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import util.Utils;
import virtualgambling.model.exceptions.StockDataNotFoundException;
import virtualgambling.model.stockdao.StockDAO;

/**
 * This class represents {@link Portfolio} of the User. The portfolio has a Name and a list of
 * purchases of Share associated with it.
 */
public class Portfolio {
  private final String name;
  private final StockDAO stockDAO;
  private final List<SharePurchaseOrder> purchases;

  /**
   * Constructs a Object of {@link Portfolio} with the given name.
   *
   * @param name the name of the portfolio.
   * @throws IllegalArgumentException if any of the given params are null
   */
  public Portfolio(String name, StockDAO stockDAO, List<SharePurchaseOrder> purchases)
          throws IllegalArgumentException {
    this.name = Utils.requireNonNull(name);
    this.stockDAO = Utils.requireNonNull(stockDAO);
    this.purchases = Utils.requireNonNull(purchases);
  }

  /**
   * Returns the name of this Portfolio.
   *
   * @return the name of this Portfolio
   */
  public String getName() {
    return this.name;
  }

  /**
   * Returns the list of purchases in this portfolio.
   *
   * @return the list of purchases in this portfolio
   */
  public List<SharePurchaseOrder> getPurchases() {
    return Collections.unmodifiableList(this.purchases);
  }

  /**
   * Returns the costBasis of this portfolio at the given date. It throws {@link
   * IllegalArgumentException} in the following cases.
   * <ul>
   * <li>If any of the given param is null</li>
   * <li>If the given portfolio does not exists</li>
   * <li>If the given date is greater than current date</li>
   * </ul>
   *
   * @param date the date
   * @return the costBasis of the given portfolio
   * @throws IllegalArgumentException if the given params are invalid
   */
  public BigDecimal getCostBasis(Date date) {
    this.checkSanity(date);
    return this.getPurchases().stream()
            .filter(sharePurchaseInfo -> sharePurchaseInfo.getDate().compareTo(date) <= 0)
            .map(SharePurchaseOrder::getCostOfPurchase)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  /**
   * Returns the total value of this portfolio at the given date. It throws {@link
   * IllegalArgumentException} in the following cases.
   * <ul>
   * <li>If any of the given param is null</li>
   * <li>If the given portfolio does not exists</li>
   * <li>If the given date is greater than current date</li>
   * </ul>
   *
   * @param date the date
   * @return the total value of the portfolio
   * @throws StockDataNotFoundException if the data is not found for the given date
   * @throws IllegalArgumentException   if the given params are invalid
   */
  public BigDecimal getValue(Date date) {
    this.checkSanity(date);
    BigDecimal totalPortfolioValue = BigDecimal.ZERO;

    List<SharePurchaseOrder> filteredPurchaseInfo = this.getPurchases().stream()
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
  public String toString() {
    Date dateTime = getTodayDate();

    StringBuilder composition = new StringBuilder();
    composition.append(String.format("%-20s%-20s%-20s%-20s%s", "Buy Date", "Stocks", "Quantity",
            "Cost Price", "Current Value"));
    composition.append(System.lineSeparator());
    List<SharePurchaseOrder> purchases = this.getPurchases();
    for (SharePurchaseOrder sharePurchaseOrder : purchases) {
      composition.append(String.format("%-20s%-20s%-20s%-20s%s",
              Utils.getDefaultFormattedDateStringFromDate(sharePurchaseOrder.getDate()),
              sharePurchaseOrder.getTickerName(),
              sharePurchaseOrder.getQuantity(),
              Utils.getFormattedCurrencyNumberString(sharePurchaseOrder.getUnitPrice()),
              Utils.getFormattedCurrencyNumberString(
                      this.stockDAO.getPrice(sharePurchaseOrder.getTickerName(), dateTime))));
      composition.append(System.lineSeparator());
    }

    BigDecimal portfolioValue = getValue(dateTime);
    BigDecimal costBasisOfPortfolio = getCostBasis(dateTime);

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
    return Utils.removeTimeFromDate(Utils.getCalendarInstance().getTime());
  }

  private void checkSanity(Date date) throws IllegalArgumentException {
    Utils.requireNonNull(date);

    if (Utils.isFutureDate(date)) {
      throw new IllegalArgumentException("Time cannot be in Future");
    }
  }
}
