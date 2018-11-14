package virtualgambling.controller.command;

import java.math.BigDecimal;
import java.util.Date;
import java.util.function.Consumer;

import virtualgambling.model.UserModel;

/**
 * Created by gajjar.s, on 12:51 AM, 11/14/18
 */
public class PortfolioValueCommand implements Command {

  private final String portfolioName;
  private final Date date;
  private final Consumer<String> consumer;

  public PortfolioValueCommand(String portfolioName, Date date, Consumer<String> consumer) {
    this.portfolioName = portfolioName;
    this.date = date;
    this.consumer = consumer;
  }

  @Override
  public void execute(UserModel userModel) {
    BigDecimal portfolioValue = userModel.getPortfolioValue(portfolioName, date);
    this.consumer.accept(portfolioValue.toPlainString());
  }
}
