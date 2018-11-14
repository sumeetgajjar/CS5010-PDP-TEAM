package virtualgambling.controller.command;

import java.util.function.Consumer;

import virtualgambling.model.UserModel;

/**
 * This class represents a command to get list of all portfolios. It implements the {@link Command}
 * interface.
 */
public class GetAllPortfolioCommand implements Command {

  private final Consumer<String> consumer;

  /**
   * Constructs a object of {@link GetAllPortfolioCommand} with the given params.
   *
   * @param consumer the consumer to consume the result of command
   */
  public GetAllPortfolioCommand(Consumer<String> consumer) {
    this.consumer = consumer;
  }

  /**
   * Executes this command and consumes the result of the command using the consumer.
   *
   * @param userModel the userModel
   */
  @Override
  public void execute(UserModel userModel) {
    String allPortfolioNames = userModel.getAllPortfolioNames();
    this.consumer.accept(allPortfolioNames);
  }
}
