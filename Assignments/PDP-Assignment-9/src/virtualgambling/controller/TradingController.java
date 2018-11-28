package virtualgambling.controller;

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
import virtualgambling.model.exceptions.PortfolioNotFoundException;
import virtualgambling.model.exceptions.StockDataNotFoundException;
import virtualgambling.view.View;

/**
 * This class represents a Trading Controller for our VirtualGambling MVC app. It implements {@link
 * Controller} interface.
 */
public class TradingController extends AbstractController {
  private final UserModel userModel;

  /**
   * Constructs a object of {@link TradingController} with the given params.
   *
   * @param userModel the userModel
   * @param view      the view
   * @throws IllegalArgumentException if the given params are null
   */
  public TradingController(UserModel userModel, View view) throws IllegalArgumentException {
    super(view);
    this.userModel = Utils.requireNonNull(userModel);
  }

  /**
   * The controller starts by displaying a welcome message on the view. It then waits for the input
   * from the user. On receiving the input from the user if the input corresponds to a valid command
   * it executes the respective command and displays the output of the command on view if there is
   * any. If the input does not corresponds to a valid command or if its not possible to execute the
   * command successfully then it displays the error message on view and again waits for the input
   * from the user. The controller returns the control to the caller if input from the view is "q"
   * or "quit". At any point during the execution if it is not possible for the controller to read
   * input from the view or display text on the view then it throws {@link IllegalStateException}.
   *
   * @throws IllegalStateException if unable to read from view or unable to display on view
   */
  @Override
  public void run() throws IllegalStateException {
    Map<String, BiFunction<Supplier<String>, Consumer<String>, Command>> commandMap =
            this.getCommandMap();
    this.displayOnView(getWelcomeMessage());

    while (true) {
      try {
        this.displayOnView(getMenuString());
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
          Command command = biFunction.apply(this::getInputFromView, this::displayOnView);
          command.execute(this.userModel);
        } else {
          this.displayOnView("Command not found, please try again");
        }
      } catch (NoSuchElementException e) {
        this.displayOnView("Incomplete Command, please enter valid parameters");
      } catch (IllegalArgumentException | InsufficientCapitalException |
              StockDataNotFoundException | PortfolioNotFoundException e) {
        this.displayOnView(e.getMessage());
      }
    }
  }

  @Override
  protected String getWelcomeMessage() {
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

  protected String getMenuString() {
    return "=================================================================================="
            + System.lineSeparator()
            + "1 => to create a portfolio"
            + System.lineSeparator()
            + "2 => to list all portfolios"
            + System.lineSeparator()
            + "3 => to get the cost basis of a portfolio"
            + System.lineSeparator()
            + "3 => to get the value of a portfolio"
            + System.lineSeparator()
            + "4 => to get the composition of a portfolio"
            + System.lineSeparator()
            + "5 => to get the remaining capital"
            + System.lineSeparator()
            + "6 => to buy shares"
            + System.lineSeparator()
            + "q or quit"
            + System.lineSeparator()
            + "Please enter a choice"
            + System.lineSeparator()
            + "=================================================================================="
            + System.lineSeparator()
            + "All dates must be in this format 'yyyy-MM-DD' and the date should not be a weekend."
            + System.lineSeparator()
            + "=================================================================================="
            + System.lineSeparator();
  }

  private Map<String, BiFunction<Supplier<String>, Consumer<String>, Command>> getCommandMap() {
    Map<String, BiFunction<Supplier<String>, Consumer<String>, Command>> commandMap =
            new HashMap<>();

    commandMap.put("1", getCreatePortfolioCommand());
    commandMap.put("2", getGetAllPortfolioCommand());
    commandMap.put("3", getPortfolioValueCommand());
    commandMap.put("4", getGetCompositionCommand());
    commandMap.put("5", getCostBasisCommand());
    commandMap.put("6", getRemainingCapitalCommand());
    commandMap.put("7", getBuySharesCommand());

    return commandMap;
  }

  private BiFunction<Supplier<String>, Consumer<String>, Command> getRemainingCapitalCommand() {
    return (supplier, consumer) ->
            new RemainingCapitalCommand(consumer);
  }

  private BiFunction<Supplier<String>, Consumer<String>, Command> getCostBasisCommand() {
    return (supplier, consumer) -> {
      String portfolioName = getPortfolioNameFromUser(supplier, consumer);
      Date date = getDateFromUser(supplier, consumer);
      return new CostBasisCommand(portfolioName, date, consumer);
    };
  }

  private BiFunction<Supplier<String>, Consumer<String>, Command> getGetCompositionCommand() {
    return (supplier, consumer) -> {
      String portfolioName = getPortfolioNameFromUser(supplier, consumer);
      return new GetCompositionCommand(portfolioName, consumer);
    };
  }

  private BiFunction<Supplier<String>, Consumer<String>, Command> getPortfolioValueCommand() {
    return (supplier, consumer) -> {
      String portfolioName = getPortfolioNameFromUser(supplier, consumer);
      Date date = getDateFromUser(supplier, consumer);
      return new PortfolioValueCommand(portfolioName, date, consumer);
    };
  }

  private BiFunction<Supplier<String>, Consumer<String>, Command> getGetAllPortfolioCommand() {
    return (supplier, consumer) -> new GetAllPortfolioCommand(consumer);
  }

  private BiFunction<Supplier<String>, Consumer<String>, Command> getCreatePortfolioCommand() {
    return (supplier, consumer) -> {
      String portfolioName = getPortfolioNameFromUser(supplier, consumer);
      return new CreatePortfolioCommand(portfolioName);
    };
  }

  private BiFunction<Supplier<String>, Consumer<String>, Command> getBuySharesCommand() {
    return (supplier, consumer) -> {
      String stockName = getStockNameFromUser(supplier, consumer);
      String portfolioName = getPortfolioNameFromUser(supplier, consumer);
      Date date = getDateFromUser(supplier, consumer);
      long quantity = getShareQuantityFromUser(supplier, consumer);
      return new BuyShareCommand(stockName, portfolioName, date, quantity, consumer);
    };
  }

  private String getStockNameFromUser(Supplier<String> supplier, Consumer<String> consumer) {
    consumer.accept("Please enter the stock name to purchase");
    return supplier.get();
  }

  private long getShareQuantityFromUser(Supplier<String> supplier, Consumer<String> consumer) {
    while (true) {
      try {
        consumer.accept("Please enter the quantity of shares to purchase");
        return Long.parseLong(supplier.get());
      } catch (NumberFormatException e) {
        consumer.accept(e.getMessage());
      }
    }
  }

  private String getPortfolioNameFromUser(Supplier<String> supplier, Consumer<String> consumer) {
    consumer.accept("Please enter the portfolio name");
    return supplier.get();
  }

  private Date getDateFromUser(Supplier<String> supplier, Consumer<String> consumer) {
    while (true) {
      try {
        consumer.accept("Please enter the date");
        return Utils.getDateFromStringSupplier(supplier);
      } catch (IllegalArgumentException e) {
        consumer.accept(e.getMessage());
      }
    }
  }
}
