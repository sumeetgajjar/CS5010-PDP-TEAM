package virtualgambling.controller.command;

import java.math.BigDecimal;
import java.util.Date;
import java.util.function.Consumer;

import util.Utils;
import virtualgambling.model.UserModel;

/**
 * This class represents a command to get portfolio value of the given portfolio. It implements the
 * {@link Command} interface.
 */
public class PortfolioValueCommand implements Command {

  private final String portfolioName;
  private final Date date;
  private final Consumer<String> consumer;

  /**
   * Constructs a object of {@link PortfolioValueCommand} with the given params.
   *
   * @param portfolioName the name of the portfolio
   * @param date          the date at which the portfolio value is to be calculated
   * @param consumer      the consumer to consume the result of command
   */
  public PortfolioValueCommand(String portfolioName, Date date, Consumer<String> consumer) {
    this.portfolioName = portfolioName;
    this.date = date;
    this.consumer = consumer;
  }

  /**
   * Executes this command and consumes the result of the command using the consumer.
   *
   * @param userModel the userModel
   */
  @Override
  public void execute(UserModel userModel) {
    BigDecimal portfolioValue = userModel.getPortfolio(portfolioName).getValue(date);
    this.consumer.accept(Utils.getFormattedCurrencyNumberString(portfolioValue));
  }
}
