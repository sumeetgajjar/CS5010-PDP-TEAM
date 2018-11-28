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

public class RecurringWeightedInvestmentStrategy implements Strategy {
  private final Date startDate;
  private final Map<String, Double> stockWeights;
  private final int dayFrequency;
  private Date endDate;

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

  private void checkStartDate(Date startDate) throws IllegalArgumentException {
    Utils.requireNonNull(startDate);
    if (Utils.doesDatesHaveSameDay(Utils.getTodayDate(), startDate)) {
      throw new IllegalArgumentException("Strategy cannot start from today");
    }
    if (Utils.isFutureDate(startDate)) {
      throw new IllegalArgumentException("Cannot start strategy from a future date");
    }
  }

  public RecurringWeightedInvestmentStrategy(Date startDate,
                                             Map<String, Double> stockWeights, int dayFrequency) {
    this(startDate, stockWeights, dayFrequency, null);
  }

  @Override
  public List<SharePurchaseOrder> execute(BigDecimal amountToInvest, StockDAO stockDAO) throws IllegalArgumentException, StrategyExecutionException {
    if (Objects.isNull(this.endDate)) {
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
}
