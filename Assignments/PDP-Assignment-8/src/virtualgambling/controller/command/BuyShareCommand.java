package virtualgambling.controller.command;

import java.util.Date;
import java.util.function.Consumer;

import virtualgambling.model.UserModel;

public class BuyShareCommand implements Command {
  private final String tickerName;
  private final String portfolioName;
  private final Date date;
  private final long quantity;
  private final Consumer<String> consumer;

  public BuyShareCommand(String tickerName, String portfolioName, Date date, long quantity,
                         Consumer<String> consumer) {
    this.tickerName = tickerName;
    this.portfolioName = portfolioName;
    this.date = date;
    this.quantity = quantity;
    this.consumer = consumer;
  }

  @Override
  public void execute(UserModel userModel) {
    this.consumer.accept(userModel.buyShares(tickerName, portfolioName, date,
            quantity).toString());
  }
}
