package virtualgambling.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

import util.Constants;
import util.Utils;
import virtualgambling.controller.command.Command;
import virtualgambling.controller.command.enhancedusermodelcommand.BuyShareWithCommissionCommand;
import virtualgambling.controller.command.enhancedusermodelcommand.BuySharesEquiWeightedCommand;
import virtualgambling.controller.command.enhancedusermodelcommand.BuySharesWeightedCommand;
import virtualgambling.controller.command.enhancedusermodelcommand.BuySharesWithRecurringWeightedStrategyCommand;
import virtualgambling.model.EnhancedUserModel;
import virtualgambling.model.exceptions.StrategyExecutionException;
import virtualgambling.model.strategy.RecurringWeightedInvestmentStrategy;
import virtualgambling.model.strategy.Strategy;
import virtualgambling.view.View;

/**
 * {@link EnhancedTradingController} represents a controller in the MVC pattern that extends a
 * {@link TradingController} and adds new tradingFeatures with the possibility of using
 * commission and
 * strategies in purchasing stock.
 */
public class EnhancedTradingController extends TradingController {

  private final EnhancedUserModel enhancedUserModel;

  /**
   * Constructs a object of {@link EnhancedTradingController} with the given params.
   *
   * @param enhancedUserModel the userModel
   * @param view              the view
   * @throws IllegalArgumentException if the given params are null
   */
  public EnhancedTradingController(EnhancedUserModel enhancedUserModel, View view)
          throws IllegalArgumentException {
    super(enhancedUserModel, view);
    this.enhancedUserModel = Utils.requireNonNull(enhancedUserModel);
  }

  @Override
  protected void executeCommand(String commandString) {
    try {
      super.executeCommand(commandString);
    } catch (StrategyExecutionException e) {
      this.displayOnView(e.getMessage());
    }
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

    return commandMap;
  }

  protected BiFunction<Supplier<String>,
          Consumer<String>, Command> getBuySharesWithRecurringSameWeightsCommand() {
    return (supplier, consumer) -> {
      String portfolioName = getPortfolioNameFromUser(supplier, consumer);

      Strategy strategy = getRecurringWeightedInvestmentStrategyWithSameWeights(supplier, consumer);
      BigDecimal amountToInvest = getBigDecimalInputFromUser(
              Constants.RECURRING_INVESTMENT_AMOUNT_MESSAGE, supplier, consumer);
      double commission = getDoubleInputFromUser(Constants.COMMISSION_MESSAGE, supplier, consumer);

      return new BuySharesWithRecurringWeightedStrategyCommand(
              this.enhancedUserModel,
              portfolioName,
              amountToInvest,
              strategy,
              commission,
              consumer);

    };
  }

  protected Strategy getRecurringWeightedInvestmentStrategyWithSameWeights(
          Supplier<String> supplier,
          Consumer<String> consumer) {

    Strategy strategy;
    Date startDate = getDateFromUser(Constants.START_DATE_MESSAGE, supplier, consumer);
    Date endDate = getEndDateFromUser(Constants.END_DATE_MESSAGE, supplier, consumer);
    int recurringPeriod = getIntegerInputFromUser(Constants.RECURRING_INTERVAL_MESSAGE,
            supplier, consumer);
    Set<String> shares = getSharesFromUser(supplier, consumer);
    if (Objects.nonNull(endDate)) {
      strategy = new RecurringWeightedInvestmentStrategy(
              startDate,
              Utils.getStocksWithWeights(shares),
              recurringPeriod,
              endDate);
    } else {
      strategy = new RecurringWeightedInvestmentStrategy(
              startDate,
              Utils.getStocksWithWeights(shares),
              recurringPeriod);
    }
    return strategy;
  }

  protected BiFunction<Supplier<String>,
          Consumer<String>, Command> getBuySharesWithRecurringDifferentWeightsCommand() {
    return (supplier, consumer) -> {
      String portfolioName = getPortfolioNameFromUser(supplier, consumer);

      Strategy strategy = getRecurringWeightedInvestmentStrategyWithDifferentWeights(supplier,
              consumer);

      BigDecimal amountToInvest = getBigDecimalInputFromUser(
              Constants.RECURRING_INVESTMENT_AMOUNT_MESSAGE, supplier, consumer);
      double commission = getDoubleInputFromUser(Constants.COMMISSION_MESSAGE, supplier, consumer);


      return new BuySharesWithRecurringWeightedStrategyCommand(
              this.enhancedUserModel,
              portfolioName,
              amountToInvest,
              strategy,
              commission,
              consumer);

    };
  }

  protected Strategy getRecurringWeightedInvestmentStrategyWithDifferentWeights(
          Supplier<String> supplier, Consumer<String> consumer) {

    Date startDate = getDateFromUser(Constants.START_DATE_MESSAGE, supplier, consumer);
    Date endDate = getEndDateFromUser(Constants.END_DATE_MESSAGE, supplier, consumer);
    int recurringPeriod = getIntegerInputFromUser(Constants.RECURRING_INTERVAL_MESSAGE,
            supplier, consumer);
    Map<String, Double> sharePercentage = getSharePercentageFromUser(supplier, consumer);
    Strategy strategy;
    if (Objects.nonNull(endDate)) {
      strategy = new RecurringWeightedInvestmentStrategy(
              startDate,
              sharePercentage,
              recurringPeriod,
              endDate);
    } else {
      strategy = new RecurringWeightedInvestmentStrategy(
              startDate,
              sharePercentage,
              recurringPeriod);
    }
    return strategy;
  }

  protected BiFunction<Supplier<String>,
          Consumer<String>, Command> getBuySharesWithCommissionCommand() {
    return (supplier, consumer) -> {
      String stockName = getStockNameFromUser(supplier, consumer);

      String portfolioName = getPortfolioNameFromUser(supplier, consumer);
      Date date = getDateFromUser(Constants.INVESTMENT_DATE_MESSAGE, supplier, consumer);
      long quantity = getShareQuantityFromUser(supplier, consumer);
      double commission = getDoubleInputFromUser(
              Constants.COMMISSION_MESSAGE, supplier, consumer);

      return new BuyShareWithCommissionCommand(
              this.enhancedUserModel,
              stockName,
              portfolioName,
              date,
              quantity,
              commission,
              consumer);
    };
  }

  protected BiFunction<Supplier<String>,
          Consumer<String>, Command> getBuySharesWithDifferentWeightsCommand() {
    return (supplier, consumer) -> {
      String portfolioName = getPortfolioNameFromUser(supplier, consumer);
      Date date = getDateFromUser(Constants.INVESTMENT_DATE_MESSAGE, supplier, consumer);
      BigDecimal amountToInvest = getBigDecimalInputFromUser(
              Constants.INVESTMENT_AMOUNT_MESSAGE, supplier, consumer);
      Map<String, Double> sharePercentage = getSharePercentageFromUser(supplier, consumer);
      double commission = getDoubleInputFromUser(
              Constants.COMMISSION_MESSAGE, supplier, consumer);

      return new BuySharesWeightedCommand(
              this.enhancedUserModel,
              portfolioName,
              amountToInvest,
              date,
              sharePercentage,
              commission,
              consumer);
    };
  }

  protected BiFunction<Supplier<String>,
          Consumer<String>, Command> getBuySharesWithSameWeightsCommand() {
    return (supplier, consumer) -> {
      String portfolioName = getPortfolioNameFromUser(supplier, consumer);
      Date date = getDateFromUser(Constants.INVESTMENT_DATE_MESSAGE, supplier, consumer);
      BigDecimal amountToInvest = getBigDecimalInputFromUser(
              Constants.INVESTMENT_AMOUNT_MESSAGE, supplier, consumer);
      Set<String> shares = getSharesFromUser(supplier, consumer);
      double commission = getDoubleInputFromUser(Constants.COMMISSION_MESSAGE, supplier, consumer);

      return new BuySharesEquiWeightedCommand(
              this.enhancedUserModel,
              portfolioName,
              amountToInvest,
              date,
              shares,
              commission,
              consumer);
    };
  }

  protected Set<String> getSharesFromUser(Supplier<String> supplier, Consumer<String> consumer) {
    int tickerNameCount = getIntegerInputFromUser(Constants.STOCK_COUNT_MESSAGE, supplier,
            consumer);

    Set<String> stocks = new LinkedHashSet<>();
    for (int i = 0; i < tickerNameCount; i++) {
      String stockName = getStockNameFromUser(supplier, consumer);
      stocks.add(stockName);
    }
    return stocks;
  }

  protected Map<String, Double> getSharePercentageFromUser(Supplier<String> supplier,
                                                           Consumer<String> consumer) {

    int tickerNameCount = getIntegerInputFromUser(Constants.STOCK_COUNT_MESSAGE, supplier,
            consumer);
    Map<String, Double> stockWeights = new HashMap<>();

    for (int i = 0; i < tickerNameCount; i++) {
      String stockName = getStockNameFromUser(supplier, consumer);
      double stockPercentage = getDoubleInputFromUser(Constants.STOCK_PERCENTAGE_MESSAGE,
              supplier, consumer);
      stockWeights.put(stockName, stockPercentage);
    }

    return stockWeights;
  }
}
