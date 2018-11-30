package virtualgambling.controller.command.usermodelcommand;

import java.util.function.Consumer;

import virtualgambling.model.UserModel;
import virtualgambling.model.exceptions.PortfolioNotFoundException;

/**
 * This command represents a command to get composition of a portfolio. It extends {@link
 * AbstractUserModelCommand} class.
 */
public class GetCompositionCommand extends AbstractUserModelCommand {

  private final String portfolioName;
  private final Consumer<String> consumer;

  /**
   * Constructs a object of {@link GetCompositionCommand} with the given params.
   *
   * @param userModel     the user model
   * @param portfolioName the name of the portfolio
   * @param consumer      the consumer to consume the result of command
   */
  public GetCompositionCommand(UserModel userModel, String portfolioName,
                               Consumer<String> consumer) {
    super(userModel);
    this.portfolioName = portfolioName;
    this.consumer = consumer;
  }

  /**
   * Executes this command and consumes the result of the command using the consumer.
   */
  @Override
  public void execute() throws PortfolioNotFoundException {
    String portfolioComposition = userModel.getPortfolio(portfolioName).toString();
    this.consumer.accept(portfolioComposition);
  }
}
