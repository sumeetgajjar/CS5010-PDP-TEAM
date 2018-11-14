package virtualgambling.controller.command;

import java.util.function.Consumer;

import util.Utils;
import virtualgambling.model.UserModel;

public class RemainingCapitalCommand implements Command {
  private final Consumer<String> consumer;

  public RemainingCapitalCommand(Consumer<String> consumer) {
    this.consumer = consumer;
  }

  @Override
  public void execute(UserModel userModel) {
    this.consumer.accept(Utils.getFormattedCurrencyNumberString(userModel.getRemainingCapital()));
  }
}
