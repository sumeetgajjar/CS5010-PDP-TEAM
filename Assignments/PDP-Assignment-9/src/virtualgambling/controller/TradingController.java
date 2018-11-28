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
import virtualgambling.controller.command.Command;
import virtualgambling.controller.command.usermodelcommand.BuyShareCommand;
import virtualgambling.controller.command.usermodelcommand.CostBasisCommand;
import virtualgambling.controller.command.usermodelcommand.CreatePortfolioCommand;
import virtualgambling.controller.command.usermodelcommand.GetAllPortfolioCommand;
import virtualgambling.controller.command.usermodelcommand.GetCompositionCommand;
import virtualgambling.controller.command.usermodelcommand.PortfolioValueCommand;
import virtualgambling.controller.command.usermodelcommand.RemainingCapitalCommand;
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

    while (true) {
      try {
        this.displayOnView(getMenuString());
        String inputFromView = getInputFromView();
        Scanner scanner = new Scanner(inputFromView);
        String commandString = scanner.next();

        if (isQuitCommand(commandString)) {
          return;
        }

        BiFunction<Supplier<String>, Consumer<String>, Command> biFunction =
                commandMap.get(commandString);

        if (Objects.nonNull(biFunction)) {
          Command command = biFunction.apply(this::getInputFromView, this::displayOnView);
          command.execute();
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

  protected String getMenuString() {
    return "=================================================================================="
            + System.lineSeparator()
            + "1 => to create a portfolio"
            + System.lineSeparator()
            + "2 => to list all portfolios"
            + System.lineSeparator()
            + "3 => to get the cost basis of a portfolio"
            + System.lineSeparator()
            + "4 => to get the value of a portfolio"
            + System.lineSeparator()
            + "5 => to get the composition of a portfolio"
            + System.lineSeparator()
            + "6 => to get the remaining capital"
            + System.lineSeparator()
            + "7 => to buy shares"
            + System.lineSeparator()
            + "q or quit => to quit"
            + System.lineSeparator()
            + "Please enter a choice"
            + System.lineSeparator()
            + "=================================================================================="
            + System.lineSeparator()
            + "All dates must be in this format 'yyyy-MM-DD'"
            + System.lineSeparator()
            + "=================================================================================="
            + System.lineSeparator();
  }

  protected Map<String, BiFunction<Supplier<String>, Consumer<String>, Command>> getCommandMap() {
    Map<String, BiFunction<Supplier<String>, Consumer<String>, Command>> commandMap =
            new HashMap<>();

    commandMap.put("1", getCreatePortfolioCommand());
    commandMap.put("2", getGetAllPortfolioCommand());
    commandMap.put("3", getCostBasisCommand());
    commandMap.put("4", getPortfolioValueCommand());
    commandMap.put("5", getGetCompositionCommand());
    commandMap.put("6", getRemainingCapitalCommand());
    commandMap.put("7", getBuySharesCommand());

    return commandMap;
  }

  protected BiFunction<Supplier<String>, Consumer<String>, Command> getRemainingCapitalCommand() {
    return (supplier, consumer) ->
            new RemainingCapitalCommand(this.userModel, consumer);
  }

  protected BiFunction<Supplier<String>, Consumer<String>, Command> getCostBasisCommand() {
    return (supplier, consumer) -> {
      String portfolioName = getPortfolioNameFromUser(supplier, consumer);
      Date date = getDateFromUser(Constants.INVESTMENT_DATE_MESSAGE, supplier, consumer);
      return new CostBasisCommand(this.userModel, portfolioName, date, consumer);
    };
  }

  protected BiFunction<Supplier<String>, Consumer<String>, Command> getGetCompositionCommand() {
    return (supplier, consumer) -> {
      String portfolioName = getPortfolioNameFromUser(supplier, consumer);
      return new GetCompositionCommand(this.userModel, portfolioName, consumer);
    };
  }

  protected BiFunction<Supplier<String>, Consumer<String>, Command> getPortfolioValueCommand() {
    return (supplier, consumer) -> {
      String portfolioName = getPortfolioNameFromUser(supplier, consumer);
      Date date = getDateFromUser(Constants.INVESTMENT_DATE_MESSAGE, supplier, consumer);
      return new PortfolioValueCommand(this.userModel, portfolioName, date, consumer);
    };
  }

  protected BiFunction<Supplier<String>, Consumer<String>, Command> getGetAllPortfolioCommand() {
    return (supplier, consumer) -> new GetAllPortfolioCommand(this.userModel, consumer);
  }

  protected BiFunction<Supplier<String>, Consumer<String>, Command> getCreatePortfolioCommand() {
    return (supplier, consumer) -> {
      String portfolioName = getPortfolioNameFromUser(supplier, consumer);
      return new CreatePortfolioCommand(this.userModel, portfolioName);
    };
  }

  protected BiFunction<Supplier<String>, Consumer<String>, Command> getBuySharesCommand() {
    return (supplier, consumer) -> {
      String stockName = getStockNameFromUser(supplier, consumer);

      String portfolioName = getPortfolioNameFromUser(supplier, consumer);
      Date date = getDateFromUser(Constants.INVESTMENT_DATE_MESSAGE, supplier, consumer);
      long quantity = getShareQuantityFromUser(supplier, consumer);
      return new BuyShareCommand(this.userModel, stockName, portfolioName, date, quantity,
              consumer);
    };
  }

  protected long getShareQuantityFromUser(Supplier<String> supplier, Consumer<String> consumer) {
    return getLongInputFromUser(Constants.SHARE_QUANTITY_MESSAGE, supplier, consumer);
  }

  protected String getPortfolioNameFromUser(Supplier<String> supplier, Consumer<String> consumer) {
    return getStringInputFromUser(Constants.PORTFOLIO_NAME_MESSAGE, supplier, consumer);
  }

  protected String getStockNameFromUser(Supplier<String> supplier, Consumer<String> consumer) {
    return getStringInputFromUser(Constants.STOCK_NAME_MESSAGE, supplier, consumer);
  }

}
