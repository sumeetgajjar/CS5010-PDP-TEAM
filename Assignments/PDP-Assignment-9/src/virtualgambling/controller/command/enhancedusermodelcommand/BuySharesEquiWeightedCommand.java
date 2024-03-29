package virtualgambling.controller.command.enhancedusermodelcommand;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;
import java.util.function.Consumer;

import util.Utils;
import virtualgambling.model.EnhancedUserModel;

/**
 * This class represents a Buy Share weighted command with the modification that each ticker has
 * equal weight. It extends the {@link BuySharesWeightedCommand} class.
 */
public class BuySharesEquiWeightedCommand extends BuySharesWeightedCommand {
  /**
   * Constructs a BuySharesEquiWeightedCommand in terms of the tickerNames such that each ticker has
   * an equal weight.
   *
   * @param enhancedUserModel the enhanced user model
   * @param portfolioName     the portfolio name
   * @param amountToInvest    the amount to invest
   * @param dateOfPurchase    the date of purchase for stocks
   * @param tickerNames       set of ticker names
   * @param commission        the commission for each transaction
   * @param consumer          the consumer to consume output of command
   * @throws IllegalArgumentException if the given model is null
   */
  public BuySharesEquiWeightedCommand(EnhancedUserModel enhancedUserModel, String portfolioName,
                                      BigDecimal amountToInvest, Date dateOfPurchase,
                                      Set<String> tickerNames, double commission,
                                      Consumer<String> consumer)
          throws IllegalArgumentException {
    super(enhancedUserModel,
            portfolioName,
            amountToInvest,
            dateOfPurchase,
            Utils.getStocksWithWeights(Utils.requireNonNull(tickerNames)),
            commission, consumer);
  }
}
