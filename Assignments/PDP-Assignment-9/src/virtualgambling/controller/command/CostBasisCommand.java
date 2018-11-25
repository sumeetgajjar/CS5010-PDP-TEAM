package virtualgambling.controller.command;

import java.math.BigDecimal;
import java.util.Date;
import java.util.function.Consumer;

import util.Utils;
import virtualgambling.model.UserModel;

/**
 * This class represents a command to get Cost Basis of a portfolio. It implements the {@link
 * Command} interface.
 */
public class CostBasisCommand implements Command {
  private final String portfolioName;
  private final Date date;
  private final Consumer<String> consumer;

  /**
   * Constructs an object of {@link CostBasisCommand} with the given params.
   *
   * @param portfolioName the name of the portfolio
   * @param date          the date at which the cost basis is to be calculated
   * @param consumer      the consumer to consume the result of command
   */
  public CostBasisCommand(String portfolioName, Date date, Consumer<String> consumer) {
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
    BigDecimal costBasisOfPortfolio =
            userModel.getPortfolio(this.portfolioName).getCostBasis(this.date);
    consumer.accept(Utils.getFormattedCurrencyNumberString(costBasisOfPortfolio));
  }
}
