package virtualgambling.controller;

import util.Constants;
import virtualgambling.model.EnhancedUserModelImpl;
import virtualgambling.model.stockdao.SimpleStockDAO;
import virtualgambling.model.stockdatasource.AlphaVantageAPIStockDataSource;
import virtualgambling.model.stockdatasource.SimpleStockDataSource;
import virtualgambling.view.View;

/**
 * Represents an orchestrator that initializes the model correctly based on the input data source.
 *
 * <p>Note: AlphaVantage as a source leads to expensive IO operations and can block the terminal
 * for some time.
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

  @Override
  public void run() {
    this.displayOnView(this.getWelcomeMessage());
    while (true) {
      this.displayOnView(this.getMenuOptions());
      String commandString = this.getInputFromView();
      Controller controller = null;

      if (isQuitCommand(commandString)) {
        return;
      }

      switch (commandString) {
        case "1":
          controller = new EnhancedTradingController(
                  new EnhancedUserModelImpl(
                          new SimpleStockDAO(
                                  new SimpleStockDataSource())), view);
          break;
        case "2":
          controller =
                  new EnhancedTradingController(new EnhancedUserModelImpl(
                          new SimpleStockDAO(
                                  AlphaVantageAPIStockDataSource.getInstance())), view);
          break;
        default:
          this.displayOnView(Constants.INVALID_CHOICE_MESSAGE);
      }

      if (controller != null) {
        controller.run();
        break; // exit this controller as well if controller exits
      }
    }
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
            + "Enter 1 for 'in-memory'" + System.lineSeparator()
            + "Enter 2 for 'alpha-vantage-api'" + System.lineSeparator()
            + "Note: Option 1 provides stock prices for the last 100 days of the following "
            + "tickers [\"AAPL\", \"GOOG\", \"GE\", \"BAC\", \"ORCL\", \"VZ\", \"MS\", \"T\"]"
            + System.lineSeparator()
            + "Note: Option 2 may lead to longer running time for operations like buying stocks, " +
            "querying composition or value of a portfolio"
            + System.lineSeparator()
            + "Note: If any given day is a holiday, Option 2 uses the next available working day " +
            "and Option 1 will be unable to find stock data."
            + System.lineSeparator()
            + "Note: All the stock prices are the closing price for the given date."
            + System.lineSeparator();
  }
}
