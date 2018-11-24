package virtualgambling.controller.command;

import java.util.function.Consumer;

import util.Utils;
import virtualgambling.model.UserModel;

/**
 * This class represents a command to get the remaining capital of the user. It implements the
 * {@link Command} interface.
 */
public class RemainingCapitalCommand implements Command {
  private final Consumer<String> consumer;

  /**
   * Constructs a object of {@link RemainingCapitalCommand} with the given params.
   *
   * @param consumer the consumer to consume the result of command
   */
  public RemainingCapitalCommand(Consumer<String> consumer) {
    this.consumer = consumer;
  }

  /**
   * Executes this command and consumes the result of the command using the consumer.
   *
   * @param userModel the userModel
   */
  @Override
  public void execute(UserModel userModel) {
    this.consumer.accept(Utils.getFormattedCurrencyNumberString(userModel.getRemainingCapital()));
  }
}
