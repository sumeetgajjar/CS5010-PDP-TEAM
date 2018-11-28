package virtualgambling;

import java.io.InputStreamReader;

import virtualgambling.controller.Controller;
import virtualgambling.controller.TradingController;
import virtualgambling.model.SimpleUserModel;
import virtualgambling.model.UserModel;
import virtualgambling.model.stockdao.SimpleStockDAO;
import virtualgambling.model.stockdatasource.SimpleStockDataSource;
import virtualgambling.view.TextView;
import virtualgambling.view.View;

/**
 * The class represents a Runner for the VirtualGambling MVC app.
 */
public class SimpleTradingApp {

  /**
   * Initializes the Model, View and Controller and start the application.
   *
   * @param args the args
   */
  public static void main(String[] args) {
    runSimpleTradingApp();
  }

  private static void runSimpleTradingApp() {
    View view = new TextView(new InputStreamReader(System.in), System.out);
    UserModel userModel = new SimpleUserModel(new SimpleStockDAO(new SimpleStockDataSource()));
    Controller controller = new TradingController(userModel, view);
    controller.run();
  }
}
