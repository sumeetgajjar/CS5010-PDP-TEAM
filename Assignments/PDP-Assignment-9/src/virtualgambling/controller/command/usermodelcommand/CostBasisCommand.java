package virtualgambling.controller.command.usermodelcommand;

import java.math.BigDecimal;
import java.util.Date;
import java.util.function.Consumer;

import util.Utils;
import virtualgambling.model.UserModel;

/**
 * This class represents a command to get Cost Basis of a portfolio. It extends {@link
 * AbstractUserModelCommand} class.
 */
public class CostBasisCommand extends AbstractUserModelCommand {
  private final String portfolioName;
  private final Date date;
  private final Consumer<String> consumer;

  /**
   * Constructs an object of {@link CostBasisCommand} with the given params.
   *
   * @param userModel     the user model
   * @param portfolioName the name of the portfolio
   * @param date          the date at which the cost basis is to be calculated
   * @param consumer      the consumer to consume the result of command
   */
  public CostBasisCommand(UserModel userModel, String portfolioName, Date date,
                          Consumer<String> consumer) {
    super(userModel);
    this.portfolioName = portfolioName;
    this.date = date;
    this.consumer = consumer;
  }

  /**
   * Executes this command and consumes the result of the command using the consumer.
   */
  @Override
  public void execute() {
    BigDecimal costBasisOfPortfolio =
            userModel.getPortfolio(this.portfolioName).getCostBasisIncludingCommission(this.date);
    consumer.accept(Utils.getFormattedCurrencyNumberString(costBasisOfPortfolio));
  }
}
