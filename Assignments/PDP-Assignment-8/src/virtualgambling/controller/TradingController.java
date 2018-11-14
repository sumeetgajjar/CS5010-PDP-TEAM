package virtualgambling.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Objects;

import util.Utils;
import virtualgambling.controller.command.BuyShareCommand;
import virtualgambling.controller.command.Command;
import virtualgambling.controller.command.CostBasisCommand;
import virtualgambling.controller.command.CreatePortfolioCommand;
import virtualgambling.controller.command.GetAllPortfolioCommand;
import virtualgambling.controller.command.GetCompositionCommand;
import virtualgambling.controller.command.PortfolioValueCommand;
import virtualgambling.controller.command.RemainingCapitalCommand;
import virtualgambling.model.UserModel;
import virtualgambling.model.exceptions.InsufficientCapitalException;
import virtualgambling.model.exceptions.StockDataNotFoundException;
import virtualgambling.view.View;

/**
 * Created by gajjar.s, on 9:45 PM, 11/12/18
 */
public class TradingController implements Controller {

  private final UserModel userModel;
  private final View view;

  public TradingController(UserModel userModel, View view) {
    this.userModel = userModel;
    this.view = view;
  }

  @Override
  public void go() throws IllegalStateException {
    try {
      while (true) {
        Command command = null;
        String portfolioName;
        String commandString = getInputFromView();
        switch (commandString) {
          case "q":
          case "quit":
            return;
          case "create_portfolio":
            command = new CreatePortfolioCommand(this.getInputFromView());
            break;
          case "get_all_portfolios":
            command = new GetAllPortfolioCommand(this::displayOnView);
            break;
          case "get_portfolio_cost_basis":
            portfolioName = this.getInputFromView();
            command = new CostBasisCommand(
                    portfolioName,
                    this.getDateFromView(),
                    this::displayOnView);
            break;
          case "get_portfolio_value":
            portfolioName = this.getInputFromView();
            command = new PortfolioValueCommand(
                    portfolioName,
                    getDateFromView(),
                    this::displayOnView);
            break;
          case "get_portfolio_composition":
            portfolioName = this.getInputFromView();
            command = new GetCompositionCommand(portfolioName, this::displayOnView);
            break;
          case "get_remaining_capital":
            command = new RemainingCapitalCommand(this::displayOnView);
            break;
          case "buy_shares":
            String stockName = getInputFromView();
            portfolioName = getInputFromView();
            long quantity = Long.parseLong(getInputFromView());
            command = new BuyShareCommand(
                    stockName,
                    portfolioName,
                    this.getDateFromView(),
                    quantity,
                    this::displayOnView);
            break;
          default:
            this.displayOnView("Command not found");
        }
        if (Objects.nonNull(command)) {
          command.execute(userModel);
        }
      }
      //todo check this
    } catch (IllegalArgumentException | InsufficientCapitalException | StockDataNotFoundException e) {
      this.displayOnView(e.getMessage());
    }
  }

  private Date getDateFromView() throws IllegalArgumentException {
    String dateString = getInputFromView();
    try {
      return Utils.getDateFromDefaultFormattedDateString(dateString);
    } catch (ParseException e) {
      throw new IllegalArgumentException("Invalid date format");
    }
  }

  private void displayOnView(String text) throws IllegalStateException {
    try {
      this.view.display(text);
      this.view.display(System.lineSeparator());
    } catch (IOException e) {
      throw new IllegalStateException("Cannot display data on view");
    }
  }

  private String getInputFromView() throws IllegalStateException {
    try {
      return view.getInput();
    } catch (IOException e) {
      throw new IllegalStateException("Cannot get data from view");
    }
  }
}
