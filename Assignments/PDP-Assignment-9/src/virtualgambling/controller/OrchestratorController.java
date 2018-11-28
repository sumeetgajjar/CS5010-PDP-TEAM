package virtualgambling.controller;

import virtualgambling.model.EnhancedUserModelImpl;
import virtualgambling.model.stockdao.DAOV2;
import virtualgambling.model.stockdao.SimpleStockDAO;
import virtualgambling.model.stockdatasource.AlphaVantageAPIStockDataSource;
import virtualgambling.model.stockdatasource.SimpleStockDataSource;
import virtualgambling.view.View;

/**
 * Represents an orchestrator that initializes the model correctly based on the input data source.
 */
public class OrchestratorController extends AbstractController {
  /**
   * Constructs a new Orchestrator Controller by taking in a view as input.
   *
   * @param view view that will give input and accept output
   */
  public OrchestratorController(View view) {
    super(view);
  }

  private String getWelcomeMessage() {
    return "" + System.lineSeparator() + ""
            + "__        __   _                            _____      __     ___      _            "
            + "   _   _____              _ _             " + System.lineSeparator() + ""
            + "\\ \\      / /__| | ___ ___  _ __ ___   ___  |_   _|__   \\ \\   / (_)_ __| |_ _   _"
            + "  __ _| | |_   _| __ __ _  __| (_)_ __   __ _ " + System.lineSeparator() + ""
            + " \\ \\ /\\ / / _ \\ |/ __/ _ \\| '_ ` _ \\ / _ \\   | |/ _ \\   \\ \\ / /| | '__| "
            + "__| | | |/ _` | |   | || '__/ _` |/ _` | | '_ \\ / _` |" + System.lineSeparator()
            + ""
            + "  \\ V  V /  __/ | (_| (_) | | | | | |  __/   | | (_) |   \\ V / | | |  | |_| |_| | "
            + "(_| | |   | || | | (_| | (_| | | | | | (_| |" + System.lineSeparator() + ""
            + "   \\_/\\_/ \\___|_|\\___\\___/|_| |_| |_|\\___|   |_|\\___/     \\_/  |_|_|   "
            + "\\__|\\__,_|\\__,_|_|   |_||_|  \\__,_|\\__,_|_|_| |_|\\__, |"
            + System.lineSeparator() + ""
            + "                                                                                    "
            + "                                    |___/ "
            + System.lineSeparator();

  }

  private String getMenuOptions() {
    return "Please enter the data source option" + System.lineSeparator()
            + "Enter 1 for 'in-memory" + System.lineSeparator()
            + "Enter 2 for 'alpha-vantage-api";
  }

  @Override
  public void run() {
    this.displayOnView(this.getWelcomeMessage());
    while (true) {
      this.displayOnView(this.getMenuOptions());
      String commandString = this.getInputFromView();
      Controller tradingController = null;

      if (isQuitCommand(commandString)) {
        return;
      }

      switch (commandString) {
        case "1":
          tradingController = new TradingController(
                  new EnhancedUserModelImpl(
                          new SimpleStockDAO(
                                  new SimpleStockDataSource())), view);
          break;
        case "2":
          tradingController =
                  new EnhancedTradingController(new EnhancedUserModelImpl(
                          new DAOV2(
                                  AlphaVantageAPIStockDataSource.getInstance())), view);
          break;
        default:
          this.displayOnView("Please type one of in-memory OR alpha-vantage-api");
      }

      if (tradingController != null) {
        tradingController.run();
        break; // exit this controller as well if tradingController exits
      }
    }
  }

}
