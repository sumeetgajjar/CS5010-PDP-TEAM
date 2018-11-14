package virtualgambling.controller.command;

import java.io.IOException;

import virtualgambling.model.UserModel;
import virtualgambling.view.View;

/**
 * Created by gajjar.s, on 12:51 AM, 11/14/18
 */
public class GetCompositionCommand implements Command {

  private final String portfolioName;
  private final View view;

  public GetCompositionCommand(String portfolioName, View view) {
    this.portfolioName = portfolioName;
    this.view = view;
  }

  @Override
  public void execute(UserModel userModel) throws IOException {
    String portfolioComposition = userModel.getPortfolioComposition(portfolioName);
    view.display(portfolioComposition);
  }
}
