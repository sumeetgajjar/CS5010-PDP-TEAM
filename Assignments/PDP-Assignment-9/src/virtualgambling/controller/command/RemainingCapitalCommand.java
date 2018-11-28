package virtualgambling.controller.command;

import java.util.function.Consumer;

import util.Utils;
import virtualgambling.model.UserModel;

/**
 * This class represents a command to get the remaining capital of the user. It extends {@link
 * AbstractUserModelCommand} class.
 */
public class RemainingCapitalCommand extends AbstractUserModelCommand {
  private final Consumer<String> consumer;

  /**
   * Constructs a object of {@link RemainingCapitalCommand} with the given params.
   *
   * @param userModel the user model
   * @param consumer  the consumer to consume the result of command
   */
  public RemainingCapitalCommand(UserModel userModel, Consumer<String> consumer) {
    super(userModel);
    this.consumer = consumer;
  }

  /**
   * Executes this command and consumes the result of the command using the consumer.
   */
  @Override
  public void execute() {
    this.consumer.accept(Utils.getFormattedCurrencyNumberString(userModel.getRemainingCapital()));
  }
}
