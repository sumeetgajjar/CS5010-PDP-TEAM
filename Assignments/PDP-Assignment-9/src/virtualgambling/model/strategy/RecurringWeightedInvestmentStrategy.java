package virtualgambling.model.strategy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import util.Utils;
import virtualgambling.model.bean.SharePurchaseOrder;
import virtualgambling.model.exceptions.StrategyExecutionException;
import virtualgambling.model.stockdao.StockDAO;

/**
 * {@link RecurringWeightedInvestmentStrategy} is a strategy to invest in a set of stocks that takes
 * ticker and weights and invest in them in a recurring fashion starting from some given day.
 *
 * <p> Optionally, a user may specify an end date. If no end date is specified, yesterday's date
 * is the end date.
 */
public class RecurringWeightedInvestmentStrategy implements Strategy {
  private final Date startDate;
  private final Map<String, Double> stockWeights;
  private final int dayFrequency;
  private Date endDate;

  /**
   * Constructs a {@link RecurringWeightedInvestmentStrategy} in terms of a start day, stockWeights,
   * dayFrequency and it's end date.
   *
   * @param startDate    start date of this strategy
   * @param stockWeights a map of stock tickers and it's weights
   * @param dayFrequency the frequency with which to invest starting from the start date
   * @param endDate      the day on which the strategy should stop recurring
   * @throws IllegalArgumentException if any of the inputs (except endDate) are null or if the
   *                                  stockWeights do not sum to 100 or if the dayFrequency is less
   *                                  than 1.
   */
  public RecurringWeightedInvestmentStrategy(Date startDate, Map<String, Double> stockWeights,
                                             int dayFrequency,
                                             Date endDate) throws IllegalArgumentException {
    this.checkInvariantForStockWeights(Utils.requireNonNull(stockWeights));
    this.checkStartDate(startDate);
    this.startDate = Utils.removeTimeFromDate(Utils.requireNonNull(startDate));
    this.stockWeights = stockWeights;
    if (dayFrequency < 1) {
      throw new IllegalArgumentException("Frequency cannot be less than 1 day");
    }
    this.dayFrequency = dayFrequency;
    setEndDate(endDate);
  }

  /**
   * Constructs a {@link RecurringWeightedInvestmentStrategy} in terms of a start day, stockWeights,
   * dayFrequency.
   *
   * @param startDate    start date of this strategy
   * @param stockWeights a map of stock tickers and it's weights
   * @param dayFrequency the frequency with which to invest starting from the start date
   * @throws IllegalArgumentException if any of the inputs are null or if the stockWeights do not
   *                                  sum to 100 or if the dayFrequency is less than 1.
   */
  public RecurringWeightedInvestmentStrategy(Date startDate,
                                             Map<String, Double> stockWeights, int dayFrequency) {
    this(startDate, stockWeights, dayFrequency, null);
  }

  /**
   * Executes a strategy such that the amount to invest is passed as input and the strategy
   * generates a list of purchase orders.
   *
   * <p>It throws an {@link IllegalArgumentException} if the following cases occur:
   * <ul>
   * <li>amountToInvest is null</li>
   * <li>amountToInvest is negative</li>
   * </ul>
   *
   * <p>Transactions are atomic for any period, if there is money to invest in stock A from
   * stockWeights and not in stock B, then the entire transaction is not added to the list of
   * transactions to return. In case all transactions fail, then {@link StrategyExecutionException}
   * is thrown.
   *
   * @param amountToInvest amount to invest in dollars
   * @return list of {@link SharePurchaseOrder}.
   */
  @Override
  public List<SharePurchaseOrder> execute(BigDecimal amountToInvest, StockDAO stockDAO) throws IllegalArgumentException, StrategyExecutionException {
    if (Objects.isNull(this.endDate)) {
      // by default set endDate to yesterday.
      setEndDate(getDefaultEndDate());
    }

    Calendar calendar = Utils.getCalendarInstance();
    calendar.setTime(this.startDate);

    List<SharePurchaseOrder> sharePurchaseOrders = new ArrayList<>();

    while (calendar.getTime().compareTo(endDate) <= 0) {
      Date dateOfPurchase = calendar.getTime();
      List<SharePurchaseOrder> purchaseOrders = new ArrayList<>();
      for (Map.Entry<String, Double> tickerAndAmount : this.stockWeights.entrySet()) {
        String tickerName = tickerAndAmount.getKey();
        Double stockWeight = tickerAndAmount.getValue();
        BigDecimal price = stockDAO.getPrice(tickerName, dateOfPurchase);
        BigDecimal amountAvailableForThisStock = amountToInvest.multiply(BigDecimal.valueOf(
                stockWeight
                ).divide(
                BigDecimal.valueOf(100), BigDecimal.ROUND_DOWN)
        );
        long quantity =
                amountAvailableForThisStock.divide(price, BigDecimal.ROUND_DOWN).longValue();
        if (quantity <= 0) {
          break;
        }
        purchaseOrders.add(stockDAO.createPurchaseOrder(tickerName, quantity, dateOfPurchase));
      }
      if (purchaseOrders.size() == this.stockWeights.size()) {
        // add only if all orders could be executed
        sharePurchaseOrders.addAll(purchaseOrders);
      }
      calendar.add(Calendar.DATE, dayFrequency);
    }
    if (sharePurchaseOrders.isEmpty()) {
      throw new StrategyExecutionException("Unable to buy even a single stock");
    }
    return sharePurchaseOrders;
  }

  /**
   * Gets the default end date if the endDate has not been provided to the constructor.
   *
   * @return a default end date if the endDate has not been provided via the constructor.
   */
  protected Date getDefaultEndDate() {
    return Utils.removeTimeFromDate(Utils.getYesterdayDate());
  }

  private void checkInvariantForStockWeights(Map<String, Double> stockWeights) {
    double weightSum = stockWeights.values().stream().mapToDouble(Double::doubleValue).sum();
    if (!Utils.areTwoDoublesEqual(weightSum, 100.0, 0.001)) {
      throw new IllegalArgumentException("Weights do not sum up to 100");
    }
  }

  private void setEndDate(Date endDate) {
    if (Objects.nonNull(endDate)) {
      endDate = Utils.removeTimeFromDate(endDate);
      if (!Utils.isFutureDate(endDate)) {
        this.endDate = endDate;
        if (this.endDate.compareTo(this.startDate) < 0) {
          throw new IllegalArgumentException("end date cannot be before the start date");
        }
      }
    }
  }

  private void checkStartDate(Date startDate) throws IllegalArgumentException {
    Utils.requireNonNull(startDate);
    if (Utils.doesDatesHaveSameDay(Utils.getTodayDate(), startDate)) {
      throw new IllegalArgumentException("Strategy cannot start from today");
    }
    if (Utils.isFutureDate(startDate)) {
      throw new IllegalArgumentException("Cannot start strategy from a future date");
    }
  }
}
