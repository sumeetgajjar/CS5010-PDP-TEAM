package virtualgambling.controller.command.enhancedusermodelcommand;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import virtualgambling.model.EnhancedUserModel;
import virtualgambling.model.strategy.OneTimeWeightedInvestmentStrategy;
import virtualgambling.model.strategy.Strategy;

/**
 * This class represents a Buy Share command with the enhancement that the each ticker can have an
 * associated weight and then given an amount to purchase, the ticker will be bought in the ratio of
 * it's weight/total weight. It extends {@link AbstractEnhancedUserModelCommand} class.
 */
public class BuySharesWeightedCommand extends AbstractEnhancedUserModelCommand {

  private final String portfolioName;
  private final BigDecimal amountToInvest;
  private final Strategy strategy;
  private final double commission;

  /**
   * Constructs a BuySharesWeightedCommand that take in a set of tickers and their associated
   * weights.
   *
   * <p>The constructor will throw an IllegalArgumentException if any of the parameters are null
   * or if the weights do not sum up to 1.
   *
   * @param portfolioName  the portfolio name
   * @param amountToInvest the amount to invest
   * @param dateOfPurchase the date of purchase for the stocks
   * @param stockWeights   map of ticker to stocks
   * @param commission     the commission for each transaction
   */
  public BuySharesWeightedCommand(EnhancedUserModel enhancedUserModel,
                                  String portfolioName, BigDecimal amountToInvest,
                                  Date dateOfPurchase,
                                  Map<String, Double> stockWeights, double commission)

          throws IllegalArgumentException {

    super(enhancedUserModel);
    this.portfolioName = portfolioName;
    this.amountToInvest = amountToInvest;
    this.strategy = new OneTimeWeightedInvestmentStrategy(dateOfPurchase, stockWeights);
    this.commission = commission;
  }

  @Override
  public void execute() {
    this.enhancedUserModel.buyShares(portfolioName, amountToInvest, strategy, commission);
  }
}
