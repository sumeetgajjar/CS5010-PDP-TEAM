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

  @Override
  String getWelcomeMessage() {
    return "Please enter the data source option" + System.lineSeparator()
            + "Enter 1 for 'in-memory" + System.lineSeparator()
            + "Enter 2 for 'alpha-vantage-api";
  }

  @Override
  public void run() {
    while (true) {
      displayOnView(getWelcomeMessage());
      String inputFromView = this.getInputFromView();
      Controller tradingController = null;
      switch (inputFromView) {
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