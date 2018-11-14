package virtualgambling.controller.command;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

import virtualgambling.model.UserModel;
import virtualgambling.view.View;

/**
 * Created by gajjar.s, on 12:51 AM, 11/14/18
 */
public class CostBasisCommand implements Command {

  private final String portfolioName;
  private final Date date;
  private final View view;

  public CostBasisCommand(String portfolioName, Date date, View view) {
    this.portfolioName = portfolioName;
    this.date = date;
    this.view = view;
  }

  @Override
  public void execute(UserModel userModel) throws IOException {
    BigDecimal costBasisOfPortfolio = userModel.getCostBasisOfPortfolio(this.portfolioName,
            this.date);
    view.display(costBasisOfPortfolio.toPlainString());
  }
}
