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
 * This class represents a Trading Controller for our VirtualGambling MVC app. It implements {@link
 * Controller} interface.
 */
public class TradingController implements Controller {

  private final UserModel userModel;
  private final View view;

  /**
   * Constructs a object of {@link TradingController} with the given params.
   *
   * @param userModel the userModel
   * @param view      the view
   * @throws IllegalArgumentException if the given params are null
   */
  public TradingController(UserModel userModel, View view) throws IllegalArgumentException {
    this.userModel = Utils.requireNonNull(userModel);
    this.view = Utils.requireNonNull(view);
  }

  @Override
  public void run() {
    Map<String, BiFunction<Supplier<String>, Consumer<String>, Command>> commandMap =
            this.getCommandMap();
    this.displayOnView(getWelcomeMessage());

    while (true) {
      try {
        String inputFromView = getInputFromView();
        Scanner scanner = new Scanner(inputFromView);
        String commandString = scanner.next();

        if (commandString.equalsIgnoreCase("q")
                || commandString.equalsIgnoreCase("quit")) {
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
        this.displayOnView("Incomplete Command, please enter valid parameters");
      } catch (IllegalArgumentException
              | InsufficientCapitalException | StockDataNotFoundException e) {
        this.displayOnView(e.getMessage());
      }
    }
  }

  private Map<String, BiFunction<Supplier<String>, Consumer<String>, Command>> getCommandMap() {
    Map<String, BiFunction<Supplier<String>, Consumer<String>, Command>> commandMap =
            new HashMap<>();

    commandMap.put("create_portfolio", (supplier, consumer) ->
            new CreatePortfolioCommand(supplier.get()));

    commandMap.put("get_all_portfolios", (supplier, consumer) ->
            new GetAllPortfolioCommand(consumer));

    commandMap.put("get_portfolio_cost_basis", (supplier, consumer) ->
            new CostBasisCommand(supplier.get(),
                    getDateFromString(supplier), consumer));

    commandMap.put("get_portfolio_value", (supplier, consumer) ->
            new PortfolioValueCommand(supplier.get(),
                    getDateFromString(supplier), consumer));

    commandMap.put("get_portfolio_composition", (supplier, consumer) ->
            new GetCompositionCommand(supplier.get(), consumer));

    commandMap.put("get_remaining_capital", (supplier, consumer) ->
            new RemainingCapitalCommand(consumer));

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
            + System.lineSeparator()
            + "=================================================================================="
            + System.lineSeparator()
            + "You can use the following example commands where the first word is the "
            + System.lineSeparator() + "command and the remaining are it's parameters"
            + System.lineSeparator()
            + "=================================================================================="
            + System.lineSeparator()
            + "create_portfolio portfolioName (portfolioName should be one word)."
            + System.lineSeparator()
            + "get_all_portfolios"
            + System.lineSeparator()
            + "get_portfolio_cost_basis portfolioName date"
            + System.lineSeparator()
            + "get_portfolio_value portfolioName date"
            + System.lineSeparator()
            + "get_portfolio_composition portfolioName"
            + System.lineSeparator()
            + "get_remaining_capital"
            + System.lineSeparator()
            + "buy_shares tickerName portfolioName date quantity"
            + System.lineSeparator()
            + "q or quit"
            + System.lineSeparator()
            + "=================================================================================="
            + System.lineSeparator()
            + "All dates must be in this format 'yyyy-MM-DD' and the date should not be a weekend."
            + System.lineSeparator()
            + "=================================================================================="
            + System.lineSeparator()
            + System.lineSeparator();
  }
}
