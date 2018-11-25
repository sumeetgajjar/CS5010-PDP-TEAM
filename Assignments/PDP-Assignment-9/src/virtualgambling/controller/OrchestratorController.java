package virtualgambling.controller;

import virtualgambling.model.SimpleUserModel;
import virtualgambling.model.stockdao.DAOV2;
import virtualgambling.model.stockdao.SimpleStockDAO;
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
    return "Please select data source, in-memory OR alpha-vantage-api";
  }

  @Override
  public void run() {
    while (true) {
      displayOnView(getWelcomeMessage());
      String inputFromView = this.getInputFromView();
      Controller tradingController = null;
      switch (inputFromView) {
        // todo use enhanced trading controller
        case "alpha-vantage-api":
          tradingController = new TradingController(new SimpleUserModel(new DAOV2()), view);
          break;
        case "in-memory":
          tradingController = new TradingController(new SimpleUserModel(new SimpleStockDAO(
                  new SimpleStockDataSource())), view);
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
