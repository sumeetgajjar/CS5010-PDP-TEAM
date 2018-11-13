package virtualgambling.controller;

import virtualgambling.model.SimpleUserModel;
import virtualgambling.model.UserModel;
import virtualgambling.model.stockdatasource.SimpleStockExchange;
import virtualgambling.model.stockdatasource.StockExchange;
import virtualgambling.model.stockexchange.SimpleStockDataSource;
import virtualgambling.model.stockexchange.StockDataSource;
import virtualgambling.view.View;

/**
 * Created by gajjar.s, on 9:42 PM, 11/12/18
 */
public class InitializationController implements Controller {

  private final View view;

  public InitializationController(View view) {
    this.view = view;
  }

  @Override
  public void go() {
    StockDataSource stockDataSource = new SimpleStockDataSource();
    StockExchange stockExchange = new SimpleStockExchange(stockDataSource);
    UserModel userModel = new SimpleUserModel(stockExchange);

    Controller controller = new TradingController(userModel, this.view);
    controller.go();
  }
}
