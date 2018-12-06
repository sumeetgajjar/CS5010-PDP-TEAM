package virtualgambling;

import java.io.InputStreamReader;

import virtualgambling.controller.Controller;
import virtualgambling.controller.GUITradingController;
import virtualgambling.controller.OrchestratorController;
import virtualgambling.controller.TradingController;
import virtualgambling.model.PersistableUserModelImpl;
import virtualgambling.model.SimpleUserModel;
import virtualgambling.model.UserModel;
import virtualgambling.model.factory.StockDAOType;
import virtualgambling.model.factory.StockDataSourceType;
import virtualgambling.view.TextView;
import virtualgambling.view.View;
import virtualgambling.view.guiview.GUIView;
import virtualgambling.view.guiview.MainForm;

/**
 * The class represents a Runner for the VirtualGambling MVC app.
 */
public class TradingApp {

  /**
   * Runner for the Virtual Trading App
   *
   * @param args args
   */
  public static void main(String[] args) {
    if (args.length == 2) {
      if (args[0].equals("-view")) {
        switch (args[1]) {
          case "console":
            runPersistableTradingApp();
            break;
          case "gui":
            runGUITradingApp();
            break;
          default:
            System.out.println("Invalid view options, correct usage is -view [gui|console]");
        }
      }
    } else {
      System.out.println("Invalid view options, correct usage is -view [gui|console]");
    }
  }

  private static void runGUITradingApp() {
    GUIView guiView = new MainForm();
    PersistableUserModelImpl persistableUserModel =
            new PersistableUserModelImpl(StockDAOType.SIMPLE,
                    StockDataSourceType.MOCK);
    Controller controller = new GUITradingController(persistableUserModel, guiView);
    controller.run();
  }

  private static void runPersistableTradingApp() {
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
