package virtualgambling.model.strategy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
                                             Date endDate) {
    this.checkInvariantForStockWeights(Utils.requireNonNull(stockWeights));
    this.startDate = Utils.removeTimeFromDate(Utils.requireNonNull(startDate));
    this.stockWeights = stockWeights;
    if (dayFrequency < 1) {
      throw new IllegalArgumentException("Frequency cannot be less than 1 day");
    }
    this.dayFrequency = dayFrequency;
    if (Objects.isNull(endDate)) {
      this.endDate = this.getDefaultEndDate();
    } else {
      endDate = Utils.removeTimeFromDate(endDate);
      if (endDate.compareTo(startDate) < 0) {
        throw new IllegalArgumentException("end date cannot be before the start date");
      }
    }
  }

  public RecurringWeightedInvestmentStrategy(Date startDate,
                                             Map<String, Double> stockWeights, int dayFrequency) {
    this(startDate, stockWeights, dayFrequency, null);
  }

  @Override
  public List<SharePurchaseOrder> execute(BigDecimal amountToInvest, StockDAO stockDAO) throws IllegalArgumentException, StrategyExecutionException {
    Calendar calendar = Utils.getCalendarInstance();
    calendar.setTime(this.startDate);

    List<SharePurchaseOrder> sharePurchaseOrders = new ArrayList<>();

    while (calendar.getTime().compareTo(endDate) <= 0) {
      List<SharePurchaseOrder> purchaseOrders =
              this.stockWeights.entrySet().stream().map(tickerAndAmount -> {
                String tickerName = tickerAndAmount.getKey();
                Double stockWeight = tickerAndAmount.getValue();
                BigDecimal price = stockDAO.getPrice(tickerName, startDate);
                BigDecimal amountAvailableForThisStock = amountToInvest.multiply(BigDecimal.valueOf(
                        stockWeight
                        ).divide(
                        BigDecimal.valueOf(100), BigDecimal.ROUND_DOWN)
                );
                long quantity =
                        amountAvailableForThisStock.divide(price, BigDecimal.ROUND_DOWN).longValueExact();
                if (quantity <= 0) {
                  throw new StrategyExecutionException("Unable to buy even a single stock");
                }
                return stockDAO.createPurchaseOrder(tickerName, quantity, this.startDate);
              })
                      .collect(Collectors.toList());
      sharePurchaseOrders.addAll(purchaseOrders);
      calendar.add(Calendar.DATE, dayFrequency);
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
}
