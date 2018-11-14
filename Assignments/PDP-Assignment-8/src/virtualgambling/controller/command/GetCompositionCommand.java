package virtualgambling.controller.command;

import java.util.function.Consumer;

import virtualgambling.model.UserModel;

/**
 * Created by gajjar.s, on 12:51 AM, 11/14/18
 */
public class GetCompositionCommand implements Command {

  private final String portfolioName;
  private final Consumer<String> consumer;

  public GetCompositionCommand(String portfolioName, Consumer<String> consumer) {
    this.portfolioName = portfolioName;
    this.consumer = consumer;
  }

  @Override
  public void execute(UserModel userModel) {
    String portfolioComposition = userModel.getPortfolioComposition(portfolioName);
    this.consumer.accept(portfolioComposition);
  }
}
