package virtualgambling;

import java.io.InputStreamReader;

import virtualgambling.controller.Controller;
import virtualgambling.controller.OrchestratorController;
import virtualgambling.view.TextView;
import virtualgambling.view.View;

/**
 * The class represents a Runner for the VirtualGambling MVC app.
 */
public class TradingApp {

  /**
   * Initializes the Model, View and Controller and start the application. Starts the
   * SimpleTradingApp if the args[0] is equal to "1", else starts EnhancedTradingApp by default.
   *
   * @param args the args
   */
  public static void main(String[] args) {
    runEnhancedTradingApp();
  }

  private static void runEnhancedTradingApp() {
    View view = new TextView(new InputStreamReader(System.in), System.out);
    Controller controller = new OrchestratorController(view);
    controller.run();
  }
}
