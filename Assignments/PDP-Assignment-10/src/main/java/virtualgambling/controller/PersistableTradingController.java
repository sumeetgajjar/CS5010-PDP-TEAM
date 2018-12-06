package virtualgambling.controller;

import java.math.BigDecimal;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

import util.Constants;
import virtualgambling.controller.command.Command;
import virtualgambling.controller.command.persistantusermodelcommand.LoadAndExecuteStrategyCommand;
import virtualgambling.controller.command.persistantusermodelcommand.LoadPortfolioCommand;
import virtualgambling.controller.command.persistantusermodelcommand.SavePortfolioCommand;
import virtualgambling.controller.command.persistantusermodelcommand.SaveStrategyCommand;
import virtualgambling.model.PersistableUserModel;
import virtualgambling.model.strategy.Strategy;
import virtualgambling.view.View;

public class PersistableTradingController extends EnhancedTradingController {

  private final PersistableUserModel persistableUserModel;

  /**
   * Constructs a object of {@link EnhancedTradingController} with the given params.
   *
   * @param persistableUserModel the userModel
   * @param view                 the view
   * @throws IllegalArgumentException if the given params are null
   */
  public PersistableTradingController(PersistableUserModel persistableUserModel, View view)
          throws IllegalArgumentException {
    super(persistableUserModel, view);
    this.persistableUserModel = persistableUserModel;
  }

  @Override
  protected Map<String, BiFunction<Supplier<String>, Consumer<String>, Command>> getCommandMap() {
    Map<String, BiFunction<Supplier<String>, Consumer<String>, Command>> commandMap =
            new HashMap<>();

    commandMap.put("1", getCreatePortfolioCommand());
    commandMap.put("2", getGetAllPortfolioCommand());
    commandMap.put("3", getCostBasisCommand());
    commandMap.put("4", getPortfolioValueCommand());
    commandMap.put("5", getGetCompositionCommand());
    commandMap.put("6", getRemainingCapitalCommand());
    commandMap.put("7", getBuySharesWithCommissionCommand());
    commandMap.put("8", getBuySharesWithDifferentWeightsCommand());
    commandMap.put("9", getBuySharesWithSameWeightsCommand());
    commandMap.put("10", getBuySharesWithRecurringDifferentWeightsCommand());
    commandMap.put("11", getBuySharesWithRecurringSameWeightsCommand());
    commandMap.put("12", getLoadPortfolioFromFileCommand());
    commandMap.put("13", getSavePortfolioToFileCommand());
    commandMap.put("14", getLoadStrategyFromFileAndExecuteCommand());
    commandMap.put("15", getSaveStrategyToFileCommand());

    return commandMap;
  }

  private BiFunction<Supplier<String>, Consumer<String>, Command> getSaveStrategyToFileCommand() {
    return (supplier, consumer) -> {

      String filePath = getStringInputFromUser(Constants.STRATEGY_FILE_SAVE_NAME_MESSAGE,
              supplier,
              consumer);

      long option;

      while (true) {
        option = getLongInputFromUser(Constants.SELECT_STRATEGY_TO_CREATE_MESSAGE,
                supplier, consumer);

        if (option == 1 || option == 2) {
          break;
        } else {
          consumer.accept(Constants.INVALID_OPTION_FOR_STRATEGY_CREATION_MESSAGE);
        }
      }

      Strategy strategy = null;
      if (option == 1) {
        strategy = this.getRecurringWeightedInvestmentStrategyWithSameWeights(supplier, consumer);
      } else if (option == 2) {
        strategy = this.getRecurringWeightedInvestmentStrategyWithDifferentWeights(supplier,
                consumer);
      }

      return new SaveStrategyCommand(persistableUserModel, strategy, Paths.get(filePath), consumer);
    };

  }

  private BiFunction<Supplier<String>,
          Consumer<String>, Command> getLoadStrategyFromFileAndExecuteCommand() {
    return (supplier, consumer) -> {
      String portfolioName = this.getPortfolioNameFromUser(supplier, consumer);
      String filePath = getStringInputFromUser(Constants.LOAD_STRATEGY_FROM_FILE,
              supplier,
              consumer);
      BigDecimal amountToInvest = getBigDecimalInputFromUser(
              Constants.INVESTMENT_AMOUNT_MESSAGE, supplier, consumer);
      double commission = getDoubleInputFromUser(Constants.COMMISSION_MESSAGE, supplier, consumer);

      return new LoadAndExecuteStrategyCommand(
              this.persistableUserModel,
              portfolioName,
              Paths.get(filePath),
              amountToInvest,
              commission,
              consumer);
    };
  }

  private BiFunction<Supplier<String>, Consumer<String>, Command> getSavePortfolioToFileCommand() {
    return (supplier, consumer) -> {
      String portfolioName = this.getPortfolioNameFromUser(supplier, consumer);
      String filePath = getStringInputFromUser(Constants.PORTFOLIO_FILE_SAVE_NAME_MESSAGE,
              supplier,
              consumer);

      return new SavePortfolioCommand(
              this.persistableUserModel,
              portfolioName,
              Paths.get(filePath),
              consumer);
    };
  }

  private BiFunction<Supplier<String>, Consumer<String>, Command> getLoadPortfolioFromFileCommand() {
    return (supplier, consumer) -> {
      String filePath = getStringInputFromUser(Constants.LOAD_PORTFOLIO_FROM_FILE,
              supplier,
              consumer);

      return new LoadPortfolioCommand(this.persistableUserModel, Paths.get(filePath), consumer);
    };
  }

  @Override
  protected String getMenuString() {
    return System.lineSeparator()
            + "=================================================================================="
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
            + "7 => to buy shares of same stock"
            + System.lineSeparator()
            + "8 => to buy shares of various stocks with different individual weights"
            + System.lineSeparator()
            + "9 => to buy shares of various stocks with equal weights"
            + System.lineSeparator()
            + "10 => to recurrently buy shares of various stocks with different individual weights"
            + System.lineSeparator()
            + "11 => to recurrently buy shares of various stocks with same individual weights"
            + System.lineSeparator()
            + "12 => to load a portfolio from a file"
            + System.lineSeparator()
            + "13 => to save a portfolio to a file"
            + System.lineSeparator()
            + "14 => to load a strategy from a file and apply it to a portfolio"
            + System.lineSeparator()
            + "15 => to create and save a strategy to a file"
            + System.lineSeparator()
            + "q or quit => to quit"
            + System.lineSeparator()
            + "Please enter a choice"
            + System.lineSeparator()
            + "=================================================================================="
            + System.lineSeparator()
            + "All dates must be in this format 'yyyy-MM-DD'"
            + System.lineSeparator()
            + "While buying multiple stocks, if same stock is entered multiple times then the " +
            "latest input will considered"
            + System.lineSeparator()
            + "=================================================================================="
            + System.lineSeparator();
  }
}
