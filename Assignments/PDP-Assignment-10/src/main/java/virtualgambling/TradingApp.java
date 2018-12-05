package virtualgambling;

import java.io.InputStreamReader;

import virtualgambling.controller.Controller;
import virtualgambling.controller.OrchestratorController;
import virtualgambling.controller.TradingController;
import virtualgambling.model.SimpleUserModel;
import virtualgambling.model.UserModel;
import virtualgambling.model.factory.StockDAOType;
import virtualgambling.model.factory.StockDataSourceType;
import virtualgambling.view.TextView;
import virtualgambling.view.View;

/**
 * The class represents a Runner for the VirtualGambling MVC app.
 */
public class TradingApp {

  public static void main(String[] args) {
    runEnhancedTradingApp();
  }

  private static void runEnhancedTradingApp() {
    View view = new TextView(new InputStreamReader(System.in), System.out);
    Controller controller = new OrchestratorController(view);
    controller.run();
  }

  private static void runSimpleTradingApp() {
    View view = new TextView(new InputStreamReader(System.in), System.out);
    UserModel userModel = new SimpleUserModel(StockDAOType.SIMPLE, StockDataSourceType.SIMPLE);
    Controller controller = new TradingController(userModel, view);
    controller.run();
  }
}
