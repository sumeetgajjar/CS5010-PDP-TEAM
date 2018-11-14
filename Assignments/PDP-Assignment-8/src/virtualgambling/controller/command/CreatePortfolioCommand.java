package virtualgambling.controller.command;

import virtualgambling.model.UserModel;

/**
 * This class represents a command to create a portfolio. It implements the {@link Command}
 * interface.
 */
public class CreatePortfolioCommand implements Command {

  private final String portfolioName;

  /**
   * Constructs a object of {@link CreatePortfolioCommand} with the given params.
   *
   * @param portfolioName the name of the portfolio
   */
  public CreatePortfolioCommand(String portfolioName) {
    this.portfolioName = portfolioName;
  }

  /**
   * Executes this command.
   *
   * @param userModel the userModel
   */
  @Override
  public void execute(UserModel userModel) {
    userModel.createPortfolio(this.portfolioName);
  }
}
