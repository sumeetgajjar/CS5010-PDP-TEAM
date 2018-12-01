package virtualgambling.controller.command.enhancedusermodelcommand;

import java.util.Date;
import java.util.function.Consumer;

import virtualgambling.model.EnhancedUserModel;

/**
 * This class represents a Buy Share command. It extends {@link AbstractEnhancedUserModelCommand}
 * class.
 */
public class BuyShareWithCommissionCommand extends AbstractEnhancedUserModelCommand {
  private final String tickerName;
  private final String portfolioName;
  private final Date date;
  private final long quantity;
  private final double commission;
  private final Consumer<String> consumer;

  /**
   * Constructs a Object of {@link BuyShareWithCommissionCommand} with the given params.
   *
   * @param enhancedUserModel the enhanced user model
   * @param tickerName        the tickerName of the stock to buy
   * @param portfolioName     the portfolioName in which the stock should be bought
   * @param date              the date at which stock should be bought
   * @param quantity          the number of shares to buy
   * @param commission        the commission for this transaction
   * @param consumer          the consumer to consume the result of command
   */
  public BuyShareWithCommissionCommand(EnhancedUserModel enhancedUserModel,
                                       String tickerName,
                                       String portfolioName, Date date,
                                       long quantity,
                                       double commission, Consumer<String> consumer) {
    super(enhancedUserModel);
    this.tickerName = tickerName;
    this.portfolioName = portfolioName;
    this.date = date;
    this.quantity = quantity;
    this.commission = commission;
    this.consumer = consumer;
  }

  /**
   * Executes this command and consumes the result of the command using the consumer.
   */
  @Override
  public void execute() {
    this.consumer.accept(enhancedUserModel.buyShares(tickerName, portfolioName, date,
            quantity, commission).toString());
  }
}
