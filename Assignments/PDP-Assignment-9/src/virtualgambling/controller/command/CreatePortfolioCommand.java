package virtualgambling.controller.command;

import java.util.function.Consumer;
import java.util.function.Supplier;

import virtualgambling.model.UserModel;

/**
 * This class represents a command to create a portfolio. It implements the {@link Command}
 * interface.
 */
public class CreatePortfolioCommand extends AbstractCommand {

  private final String portfolioName;

  public CreatePortfolioCommand(Supplier<String> supplier, Consumer<String> consumer) {
    super(supplier, consumer);
    this.consumer.accept("Please Enter the Portfolio Name");
    this.portfolioName = supplier.get();
  }

  /**
   * Executes this command.
   *
   * @param userModel the userModel
   */
  @Override
  public void execute(UserModel userModel) {
    userModel.createPortfolio(this.portfolioName);
  }
}
