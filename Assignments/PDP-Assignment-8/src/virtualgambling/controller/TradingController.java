package virtualgambling.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

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
  public void go() {


    Map<String, BiFunction<Supplier<String>, Consumer<String>, Command>> commandMap =
            this.getCommandMap();

    while (true) {
      try {
        String inputFromView = getInputFromView();
        Scanner scanner = new Scanner(inputFromView);
        String commandString = scanner.next();

        if (commandString.equalsIgnoreCase("q") ||
                commandString.equalsIgnoreCase("quit")) {
          return;
        }

        BiFunction<Supplier<String>, Consumer<String>, Command> biFunction =
                commandMap.get(commandString);

        if (Objects.nonNull(biFunction)) {
          Command command = biFunction.apply(scanner::next, this::displayOnView);
          command.execute(this.userModel);
        } else {
          this.displayOnView("Command not found, please try again");
        }
      } catch (NoSuchElementException e) {
        this.displayOnView("Invalid Command");
      } catch (IllegalArgumentException | InsufficientCapitalException | StockDataNotFoundException e) {
        this.displayOnView(e.getMessage());
      }
    }
  }

  private Map<String, BiFunction<Supplier<String>, Consumer<String>, Command>> getCommandMap() {
    Map<String, BiFunction<Supplier<String>, Consumer<String>, Command>> commandMap =
            new HashMap<>();

    commandMap.put("create_portfolio",
            (supplier, consumer) -> new CreatePortfolioCommand(supplier.get()));

    commandMap.put("get_all_portfolios",
            (supplier, consumer) -> new GetAllPortfolioCommand(consumer));

    commandMap.put("get_portfolio_cost_basis",
            (supplier, consumer) -> new CostBasisCommand(supplier.get(),
                    getDateFromString(supplier), consumer));

    commandMap.put("get_portfolio_value",
            (supplier, consumer) -> new PortfolioValueCommand(supplier.get(),
                    getDateFromString(supplier), consumer));

    commandMap.put("get_portfolio_composition",
            (supplier, consumer) -> new GetCompositionCommand(supplier.get(), consumer));

    commandMap.put("get_remaining_capital",
            (supplier, consumer) -> new RemainingCapitalCommand(consumer));

    commandMap.put("buy_shares", this::getBuySharesCommand);

    return commandMap;
  }

  private Command getBuySharesCommand(Supplier<String> supplier, Consumer<String> consumer) {
    String stockName = supplier.get();
    String portfolioName = supplier.get();
    Date date = getDateFromString(supplier);
    try {
      long quantity = Long.parseLong(supplier.get());
      return new BuyShareCommand(
              stockName,
              portfolioName,
              date,
              quantity,
              consumer);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Invalid quantity of shares");
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

  private static Date getDateFromString(Supplier<String> supplier) throws IllegalArgumentException {
    try {
      return Utils.getDateFromDefaultFormattedDateString(supplier.get());
    } catch (ParseException e) {
      throw new IllegalArgumentException("Invalid date format");
    }
  }
}
