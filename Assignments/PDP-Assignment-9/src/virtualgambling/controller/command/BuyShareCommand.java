package virtualgambling.controller.command;

import java.util.Date;
import java.util.function.Consumer;

import virtualgambling.model.UserModel;

/**
 * This class represents a Buy Share command. It extends {@link AbstractUserModelCommand} class.
 */
public class BuyShareCommand extends AbstractUserModelCommand {
  private final String tickerName;
  private final String portfolioName;
  private final Date date;
  private final long quantity;
  private final Consumer<String> consumer;

  /**
   * Constructs a Object of {@link BuyShareCommand} with the given params.
   *
   * @param userModel     the user model
   * @param tickerName    the tickerName of the stock to buy
   * @param portfolioName the portfolioName in which the stock should be bought
   * @param date          the date at which stock should be bought
   * @param quantity      the number of shares to buy
   * @param consumer      the consumer to consume the result of command
   */
  public BuyShareCommand(UserModel userModel, String tickerName, String portfolioName, Date date,
                         long quantity,
                         Consumer<String> consumer) {
    super(userModel);
    this.tickerName = tickerName;
    this.portfolioName = portfolioName;
    this.date = date;
    this.quantity = quantity;
    this.consumer = consumer;
  }

  /**
   * Executes this command and consumes the result of the command using the consumer.
   */
  @Override
  public void execute() {
    this.consumer.accept(userModel.buyShares(tickerName, portfolioName, date,
            quantity).toString());
  }
}
