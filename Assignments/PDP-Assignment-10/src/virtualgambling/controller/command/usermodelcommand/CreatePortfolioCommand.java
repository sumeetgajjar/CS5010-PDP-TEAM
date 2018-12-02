package virtualgambling.controller.command.usermodelcommand;

import virtualgambling.model.UserModel;

/**
 * This class represents a command to create a portfolio. It extends {@link
 * AbstractUserModelCommand} class.
 */
public class CreatePortfolioCommand extends AbstractUserModelCommand {

  private final String portfolioName;

  /**
   * Constructs a object of {@link CreatePortfolioCommand} with the given params.
   *
   * @param userModel     the user model
   * @param portfolioName the name of the portfolio
   */
  public CreatePortfolioCommand(UserModel userModel, String portfolioName) {
    super(userModel);
    this.portfolioName = portfolioName;
  }

  /**
   * Executes this command.
   */
  @Override
  public void execute() {
    userModel.createPortfolio(this.portfolioName);
  }
}
