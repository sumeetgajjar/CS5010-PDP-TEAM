package virtualgambling.controller.command;

import java.math.BigDecimal;
import java.util.Date;
import java.util.function.Consumer;
import java.util.function.Supplier;

import util.Utils;
import virtualgambling.model.UserModel;

/**
 * This class represents a command to get portfolio value of the given portfolio. It implements the
 * {@link Command} interface.
 */
public class PortfolioValueCommand extends AbstractCommand {

  private final String portfolioName;
  private final Date date;

  /**
   * Constructs a object of {@link PortfolioValueCommand} with the given params.
   *
   * @param supplier the supplier of type string
   * @param consumer the consumer of type string
   */
  public PortfolioValueCommand(Supplier<String> supplier, Consumer<String> consumer) {
    super(supplier, consumer);
    this.consumer.accept("Please Enter the Portfolio Name");
    this.portfolioName = supplier.get();
    this.consumer.accept("Please Enter the Date from which the value is to be calculated");
    this.date = Utils.getDateFromStringSupplier(supplier);
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
