package virtualgambling.model.strategy;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import util.Utils;
import virtualgambling.model.bean.SharePurchaseOrder;

public class RecurringWeightedInvestmentStrategy implements Strategy {
  private final Strategy strategy;
  private final int dayFrequency;
  private final Date startDate;
  private Date endDate;

  public RecurringWeightedInvestmentStrategy(Strategy strategy, Date startDate, int dayFrequency) {
    this.strategy = Utils.requireNonNull(strategy);
    this.startDate = Utils.requireNonNull(startDate);
    if (dayFrequency < 1) {
      throw new IllegalArgumentException("Frequency cannot be less than 1 day");
    }
    this.dayFrequency = dayFrequency;
    this.endDate = null;
  }

  public RecurringWeightedInvestmentStrategy(Strategy strategy, Date startDate, int dayFrequency,
                                             Date endDate) {
    this(strategy, startDate, dayFrequency);
    this.endDate = Utils.requireNonNull(endDate);
  }

  @Override
  public List<SharePurchaseOrder> execute(BigDecimal amountToInvest) throws IllegalArgumentException {
    return null;
  }
}
