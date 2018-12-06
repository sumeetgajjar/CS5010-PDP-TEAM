package virtualgambling.controller.command.enhancedusermodelcommand;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Consumer;

import util.Utils;
import virtualgambling.model.EnhancedUserModel;
import virtualgambling.model.bean.SharePurchaseOrder;
import virtualgambling.model.strategy.Strategy;

/**
 * This class represents a command which buys Share with different weights on recurring basis. Each
 * stock should have an associated percentage with it. It extends {@link
 * AbstractEnhancedUserModelCommand} class.
 */
public class BuySharesWithRecurringWeightedStrategyCommand extends
        AbstractEnhancedUserModelCommand {

  private final String portfolioName;
  private final BigDecimal amountToInvest;
  protected final Strategy strategy;
  private final double commission;
  private final Consumer<String> consumer;

  /**
   * Constructs a BuySharesWithRecurringWeightedStrategyCommand object with given params.
   *
   * @param enhancedUserModel the enhanced user model
   * @param portfolioName     the portfolio to invest in
   * @param amountToInvest    the amount to invest in
   * @param strategy          the strategy
   * @param commission        the commission for each transaction
   * @param consumer          the consumer to consume output of command
   * @throws IllegalArgumentException if the given params are null
   */
  public BuySharesWithRecurringWeightedStrategyCommand(EnhancedUserModel enhancedUserModel,
                                                       String portfolioName,
                                                       BigDecimal amountToInvest,
                                                       Strategy strategy,
                                                       double commission,
                                                       Consumer<String> consumer)
          throws IllegalArgumentException {

    super(enhancedUserModel);
    this.portfolioName = Utils.requireNonNull(portfolioName);
    this.amountToInvest = Utils.requireNonNull(amountToInvest);
    this.strategy = Utils.requireNonNull(strategy);
    this.commission = commission;
    this.consumer = consumer;
  }

  @Override
  public void execute() {
    List<SharePurchaseOrder> sharePurchaseOrders = this.enhancedUserModel.buyShares(portfolioName
            , amountToInvest, strategy, commission);
    for (SharePurchaseOrder sharePurchaseOrder : sharePurchaseOrders) {
      this.consumer.accept(sharePurchaseOrder.toString());
    }

  }
}
