package virtualgambling.controller.command.usermodelcommand;

import java.util.function.Consumer;
import java.util.stream.Collectors;

import virtualgambling.model.UserModel;
import virtualgambling.model.bean.Portfolio;

/**
 * This class represents a command to get list of all portfolios. It extends {@link
 * AbstractUserModelCommand} class.
 */
public class GetAllPortfolioCommand extends AbstractUserModelCommand {

  private final Consumer<String> consumer;

  /**
   * Constructs a object of {@link GetAllPortfolioCommand} with the given params.
   *
   * @param userModel the user model
   * @param consumer  the consumer to consume the result of command
   */
  public GetAllPortfolioCommand(UserModel userModel, Consumer<String> consumer) {
    super(userModel);
    this.consumer = consumer;
  }

  /**
   * Executes this command and consumes the result of the command using the consumer.
   */
  @Override
  public void execute() {
    String allPortfolioNames =
            userModel.getAllPortfolios().stream().map(Portfolio::getName)
                    .collect(Collectors.joining(System.lineSeparator()));
    this.consumer.accept(allPortfolioNames);
  }
}
