package virtualgambling.controller.command;

import java.util.Date;
import java.util.function.Consumer;

import virtualgambling.model.UserModel;

/**
 * This class represents a Buy Share command. It implements the {@link Command} interface.
 */
public class BuyShareCommand implements Command {
  private final String tickerName;
  private final String portfolioName;
  private final Date date;
  private final long quantity;
  private final Consumer<String> consumer;

  /**
   * Constructs a Object of {@link BuyShareCommand} with the given params.
   *
   * @param tickerName    the tickerName of the stock to buy
   * @param portfolioName the portfolioName in which the stock should be bought
   * @param date          the date at which stock should be bought
   * @param quantity      the number of shares to buy
   * @param consumer      the consumer of consume the result of command
   */
  public BuyShareCommand(String tickerName, String portfolioName, Date date, long quantity,
                         Consumer<String> consumer) {
    this.tickerName = tickerName;
    this.portfolioName = portfolioName;
    this.date = date;
    this.quantity = quantity;
    this.consumer = consumer;
  }

  /**
   * Executes the command and consumes the result of the command using the consumer.
   *
   * @param userModel the userModel
   */
  @Override
  public void execute(UserModel userModel) {
    this.consumer.accept(userModel.buyShares(tickerName, portfolioName, date,
            quantity).toString());
  }
}
