package virtualgambling.controller.command;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

import virtualgambling.model.UserModel;
import virtualgambling.view.View;

/**
 * Created by gajjar.s, on 12:51 AM, 11/14/18
 */
public class PortfolioValueCommand implements Command {

  private final String portfolioName;
  private final Date date;
  private final View view;

  public PortfolioValueCommand(String portfolioName, Date date, View view) {
    this.portfolioName = portfolioName;
    this.date = date;
    this.view = view;
  }

  @Override
  public void execute(UserModel userModel) throws IOException {
    BigDecimal portfolioValue = userModel.getPortfolioValue(portfolioName, date);
    view.display(portfolioValue.toPlainString());
  }
}
