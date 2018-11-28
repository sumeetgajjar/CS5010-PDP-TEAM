package virtualgambling.model.strategy;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import util.Utils;
import virtualgambling.model.bean.SharePurchaseOrder;

public class RecurringWeightedInvestmentStrategy extends AbstractWeightedInvestmentStrategy {
  private final int dayFrequency;
  private Date endDate;

  public RecurringWeightedInvestmentStrategy(Date startDate, Map<String, Double> stockWeights,
                                             int dayFrequency,
                                             Date endDate) {
    super(startDate, stockWeights);
    if (dayFrequency < 1) {
      throw new IllegalArgumentException("Frequency cannot be less than 1 day");
    }
    this.dayFrequency = dayFrequency;
    this.endDate = Utils.requireNonNull(endDate);
  }

  public RecurringWeightedInvestmentStrategy(Date startDate,
                                             Map<String, Double> stockWeights, int dayFrequency) {
    this(startDate, stockWeights, dayFrequency, Utils.getYesterdayDate());
  }

  @Override
  public List<SharePurchaseOrder> execute(BigDecimal amountToInvest) throws IllegalArgumentException {
    return null;
  }

  protected Date getDefaultEndDate() {
    return Utils.getYesterdayDate();
  }
}
