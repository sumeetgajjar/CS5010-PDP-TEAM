package virtualgambling.controller.command;

import virtualgambling.model.UserModel;

/**
 * Created by gajjar.s, on 12:50 AM, 11/14/18
 */
public class CreatePortfolioCommand implements Command {

  private final String portfolioName;

  public CreatePortfolioCommand(String portfolioName) {
    this.portfolioName = portfolioName;
  }

  @Override
  public void execute(UserModel userModel) {
    userModel.createPortfolio(this.portfolioName);
  }
}
