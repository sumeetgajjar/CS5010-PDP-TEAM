package virtualgambling;

import java.io.InputStreamReader;

import virtualgambling.controller.Controller;
import virtualgambling.controller.TradingController;
import virtualgambling.model.SimpleUserModel;
import virtualgambling.model.UserModel;
import virtualgambling.model.stockdatasource.SimpleStockExchange;
import virtualgambling.model.stockexchange.SimpleStockDataSource;
import virtualgambling.view.TextView;
import virtualgambling.view.View;

/**
 * Created by gajjar.s, on 9:41 PM, 11/12/18
 */
public class App {
  public static void main(String[] args) {
    View view = new TextView(new InputStreamReader(System.in), System.out);
    UserModel userModel = new SimpleUserModel(new SimpleStockExchange(new SimpleStockDataSource()));
    Controller controller = new TradingController(userModel, view);
    controller.go();
  }
}
