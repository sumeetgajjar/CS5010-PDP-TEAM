package virtualgambling.controller.command;

import java.util.function.Consumer;

import virtualgambling.model.UserModel;

/**
 * Created by gajjar.s, on 12:50 AM, 11/14/18
 */
public class GetAllPortfolioCommand implements Command {

  private final Consumer<String> consumer;

  public GetAllPortfolioCommand(Consumer<String> consumer) {
    this.consumer = consumer;
  }

  @Override
  public void execute(UserModel userModel) {
    String allPortfolioNames = userModel.getAllPortfolioNames();
    this.consumer.accept(allPortfolioNames);
  }
}
