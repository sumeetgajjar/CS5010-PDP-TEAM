package virtualgambling.controller.command;

import java.util.function.Consumer;

import virtualgambling.model.UserModel;

/**
 * This command represents a command to get composition of a portfolio. It implements the {@link
 * Command} interface.
 */
public class GetCompositionCommand implements Command {

  private final String portfolioName;
  private final Consumer<String> consumer;

  /**
   * Constructs a object of {@link GetCompositionCommand} with the given params.
   *
   * @param portfolioName the name of the portfolio
   * @param consumer      the consumer to consume the result of command
   */
  public GetCompositionCommand(String portfolioName, Consumer<String> consumer) {
    this.portfolioName = portfolioName;
    this.consumer = consumer;
  }

  /**
   * Executes this command and consumes the result of the command using the consumer.
   *
   * @param userModel the userModel
   */
  @Override
  public void execute(UserModel userModel) {
    String portfolioComposition = userModel.getPortfolioComposition(portfolioName);
    this.consumer.accept(portfolioComposition);
  }
}
