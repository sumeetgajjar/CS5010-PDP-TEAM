package virtualgambling.controller.command.enhancedusermodelcommand;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import virtualgambling.model.EnhancedUserModel;
import virtualgambling.model.strategy.RecurringWeightedInvestmentStrategy;
import virtualgambling.model.strategy.Strategy;

/**
 * This class represents a command which buys Share with different weights on recurring basis. Each
 * ticker should have an associated percentage with it. It extends {@link
 * AbstractEnhancedUserModelCommand} class.
 */
public class BuySharesWithRecurringWeightedStrategyCommand extends AbstractEnhancedUserModelCommand {

  private final String portfolioName;
  private final BigDecimal amountToInvest;
  private final Strategy strategy;
  private final double commission;

  /**
   * Constructs a BuySharesWithRecurringWeightedStrategyCommand object with given params.
   *
   * <p>The constructor will throw an IllegalArgumentException if any of the parameters are null
   * or if the weights do not sum up to 1.
   *
   * @param enhancedUserModel the enhanced user model
   * @param portfolioName     the portfolio to invest in
   * @param amountToInvest    the amount to invest in
   * @param stockWeights      the weights associated with each stocks for purchase
   * @param startDate         the start date for the recurring investment
   * @param endDate           the end date for the recurring investment
   * @param dayFrequency      the recurring interval
   * @param commission        the commission for each transaction
   */
  public BuySharesWithRecurringWeightedStrategyCommand(EnhancedUserModel enhancedUserModel,
                                                       String portfolioName,
                                                       BigDecimal amountToInvest, Map<String,
          Double> stockWeights, Date startDate,
                                                       Date endDate, int dayFrequency,
                                                       double commission)
          throws IllegalArgumentException {

    super(enhancedUserModel);
    this.portfolioName = portfolioName;
    this.amountToInvest = amountToInvest;
    this.strategy = new RecurringWeightedInvestmentStrategy(
            startDate, stockWeights, dayFrequency, endDate);
    this.commission = commission;
  }

  @Override
  public void execute() {
    this.enhancedUserModel.buyShares(portfolioName, amountToInvest, strategy, commission);
  }
}
