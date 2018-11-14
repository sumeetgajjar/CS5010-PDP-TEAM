package virtualgambling.controller.command;

import java.util.Date;

import virtualgambling.model.UserModel;
import virtualgambling.model.bean.SharePurchaseInfo;
import virtualgambling.view.View;

public class BuyShareCommand implements Command {
  private final String tickerName;
  private final String portfolioName;
  private final Date date;
  private final long quantity;
  private final View view;

  public BuyShareCommand(String tickerName, String portfolioName, Date date, long quantity,
                         View view) {
    this.tickerName = tickerName;
    this.portfolioName = portfolioName;
    this.date = date;
    this.quantity = quantity;
    this.view = view;
  }

  @Override
  public void execute(UserModel userModel) {
    SharePurchaseInfo sharePurchaseInfo = userModel.buyShares(tickerName, portfolioName, date,
            quantity);

    //todo format sharePurchaseInfo and send it to view output
  }
}
