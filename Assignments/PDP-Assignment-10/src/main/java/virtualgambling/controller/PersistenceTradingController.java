package virtualgambling.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

import virtualgambling.controller.command.Command;
import virtualgambling.model.PersistableUserModel;
import virtualgambling.view.View;

public class PersistenceTradingController extends EnhancedTradingController {
  /**
   * Constructs a object of {@link EnhancedTradingController} with the given params.
   *
   * @param enhancedUserModel the userModel
   * @param view              the view
   * @throws IllegalArgumentException if the given params are null
   */
  public PersistenceTradingController(PersistableUserModel enhancedUserModel, View view)
          throws IllegalArgumentException {
    super(enhancedUserModel, view);
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
    commandMap.put("14", getLoadStrategyFromFileCommand());
    commandMap.put("15", getSaveStrategyToFileCommand());

    return commandMap;
  }

  @Override
  protected BiFunction<Supplier<String>, Consumer<String>, Command> getBuySharesWithRecurringSameWeightsCommand() {
    BiFunction<Supplier<String>, Consumer<String>, Command> biFunction =
            super.getBuySharesWithRecurringSameWeightsCommand();
    return null;
  }

  @Override
  protected BiFunction<Supplier<String>, Consumer<String>, Command> getBuySharesWithRecurringDifferentWeightsCommand() {
    return super.getBuySharesWithRecurringDifferentWeightsCommand();
  }

  private BiFunction<Supplier<String>, Consumer<String>, Command> getSaveStrategyToFileCommand() {
    return null;
  }

  private BiFunction<Supplier<String>, Consumer<String>, Command> getLoadStrategyFromFileCommand() {
    return null;
  }

  private BiFunction<Supplier<String>, Consumer<String>, Command> getSavePortfolioToFileCommand() {
    return null;
  }

  private BiFunction<Supplier<String>, Consumer<String>, Command> getLoadPortfolioFromFileCommand() {
    return null;
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
