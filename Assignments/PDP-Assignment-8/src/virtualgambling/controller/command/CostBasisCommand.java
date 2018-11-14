package virtualgambling.controller.command;

import java.math.BigDecimal;
import java.util.Date;
import java.util.function.Consumer;

import util.Utils;
import virtualgambling.model.UserModel;

/**
 * Created by gajjar.s, on 12:51 AM, 11/14/18
 */
public class CostBasisCommand implements Command {
  private final String portfolioName;
  private final Date date;
  private final Consumer<String> consumer;

  public CostBasisCommand(String portfolioName, Date date, Consumer<String> consumer) {
    this.portfolioName = portfolioName;
    this.date = date;
    this.consumer = consumer;
  }

  @Override
  public void execute(UserModel userModel) {
    BigDecimal costBasisOfPortfolio = userModel.getCostBasisOfPortfolio(this.portfolioName,
            this.date);
    consumer.accept(Utils.getFormattedCurrencyNumberString(costBasisOfPortfolio));
  }
}
