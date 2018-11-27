package virtualgambling.controller.command;

import java.util.function.Consumer;
import java.util.function.Supplier;

import virtualgambling.model.UserModel;
import virtualgambling.model.exceptions.PortfolioNotFoundException;

/**
 * This command represents a command to get composition of a portfolio. It implements the {@link
 * Command} interface.
 */
public class GetCompositionCommand extends AbstractCommand {

  private final String portfolioName;

  /**
   * Constructs a object of {@link GetCompositionCommand} with the given params.
   *
   * @param supplier the supplier of type string
   * @param consumer the consumer of type string
   */
  public GetCompositionCommand(Supplier<String> supplier, Consumer<String> consumer) {
    super(supplier, consumer);
    this.consumer.accept("Please Enter the Portfolio Name");
    this.portfolioName = supplier.get();
  }

  /**
   * Executes this command and consumes the result of the command using the consumer.
   *
   * @param userModel the userModel
   */
  @Override
  public void execute(UserModel userModel) throws PortfolioNotFoundException {
    String portfolioComposition = userModel.getPortfolio(portfolioName).toString();
    this.consumer.accept(portfolioComposition);
  }
}
