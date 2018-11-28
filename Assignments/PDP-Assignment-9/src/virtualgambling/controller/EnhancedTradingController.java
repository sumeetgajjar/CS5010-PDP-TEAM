package virtualgambling.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

import virtualgambling.controller.command.Command;
import virtualgambling.controller.command.enhancedusermodelcommand.BuyShareWithCommissionCommand;
import virtualgambling.controller.command.enhancedusermodelcommand.BuySharesEquiWeightedCommand;
import virtualgambling.controller.command.enhancedusermodelcommand.BuySharesWeightedCommand;
import virtualgambling.controller.command.enhancedusermodelcommand.BuySharesWithRecurringWeightedStrategyCommand;
import virtualgambling.model.EnhancedUserModel;
import virtualgambling.view.View;

public class EnhancedTradingController extends TradingController {

  private final EnhancedUserModel enhancedUserModel;

  /**
   * Constructs a object of {@link EnhancedTradingController} with the given params.
   *
   * @param enhancedUserModel the userModel
   * @param view              the view
   * @throws IllegalArgumentException if the given params are null
   */
  public EnhancedTradingController(EnhancedUserModel enhancedUserModel, View view) throws IllegalArgumentException {
    super(enhancedUserModel, view);
    this.enhancedUserModel = enhancedUserModel;
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

  private BiFunction<Supplier<String>, Consumer<String>, Command>
  getBuySharesWithRecurringDifferentWeightsCommand() {
    return (supplier, consumer) -> {
      String portfolioName = getPortfolioNameFromUser(supplier, consumer);
      BigDecimal amountToInvest = getBigDecimalInputFromUser(
              "Please enter the amount to invest", supplier, consumer);

      Date startDate = getDateFromUser("Please enter the start date for recurring investment",
              supplier, consumer);
      Date endDate = getDateFromUser("Please enter the end date for recurring investment",
              supplier, consumer);
      int recurringPeriod = getIntegerInputFromUser("Please the recurring interval",
              supplier, consumer);
      Map<String, Double> sharePercentage = getSharePercentageFromUser(supplier, consumer);
      double commission = getDoubleInputFromUser(
              "Please enter the commission per transaction", supplier, consumer);

      return new BuySharesWithRecurringWeightedStrategyCommand(
              this.enhancedUserModel,
              portfolioName,
              amountToInvest,
              sharePercentage,
              startDate,
              endDate,
              recurringPeriod,
              commission);
    };
  }

  private BiFunction<Supplier<String>, Consumer<String>, Command> getBuySharesWithCommissionCommand() {
    return (supplier, consumer) -> {
      String stockName = getStockNameFromUser(supplier, consumer);

      String portfolioName = getPortfolioNameFromUser(supplier, consumer);
      Date date = getDateFromUser("Please enter the date for investment", supplier, consumer);
      long quantity = getShareQuantityFromUser(supplier, consumer);
      double commission = getDoubleInputFromUser(
              "Please enter the commission per transaction", supplier, consumer);

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

  private BiFunction<Supplier<String>, Consumer<String>, Command> getBuySharesWithDifferentWeightsCommand() {
    return (supplier, consumer) -> {
      String portfolioName = getPortfolioNameFromUser(supplier, consumer);
      Date date = getDateFromUser("Please enter the date for investment", supplier, consumer);
      BigDecimal amountToInvest = getBigDecimalInputFromUser(
              "Please enter the amount to invest", supplier, consumer);
      Map<String, Double> sharePercentage = getSharePercentageFromUser(supplier, consumer);
      double commission = getDoubleInputFromUser(
              "Please enter the commission per transaction", supplier, consumer);

      return new BuySharesWeightedCommand(
              this.enhancedUserModel,
              portfolioName,
              amountToInvest,
              date,
              sharePercentage,
              commission);
    };
  }

  private BiFunction<Supplier<String>, Consumer<String>, Command> getBuySharesWithSameWeightsCommand() {
    return (supplier, consumer) -> {
      String portfolioName = getPortfolioNameFromUser(supplier, consumer);
      Date date = getDateFromUser("Please enter the date for investment", supplier, consumer);
      BigDecimal amountToInvest = getBigDecimalInputFromUser(
              "Please enter the amount to invest", supplier, consumer);
      Set<String> shares = getSharesFromUser(supplier, consumer);
      double commission = getDoubleInputFromUser(
              "Please enter the commission per transaction", supplier, consumer);

      return new BuySharesEquiWeightedCommand(
              this.enhancedUserModel,
              portfolioName,
              amountToInvest,
              date,
              shares,
              commission);
    };
  }

  private Set<String> getSharesFromUser(Supplier<String> supplier, Consumer<String> consumer) {
    int tickerNameCount = getIntegerInputFromUser(
            "Please enter the count of ticker names", supplier, consumer);

    Set<String> stocks = new LinkedHashSet<>();
    for (int i = 0; i < tickerNameCount; i++) {
      String stockName = getStockNameFromUser(supplier, consumer);
      stocks.add(stockName);
    }
    return stocks;
  }

  private Map<String, Double> getSharePercentageFromUser(Supplier<String> supplier,
                                                         Consumer<String> consumer) {

    int tickerNameCount = getIntegerInputFromUser(
            "Please enter the count of ticker names", supplier, consumer);

    Map<String, Double> stockWeights = new HashMap<>();

    for (int i = 0; i < tickerNameCount; i++) {
      String stockName = getStockNameFromUser(supplier, consumer);
      double stockPercentage = getDoubleInputFromUser(
              "Please enter the percentage of the stock", supplier, consumer);
      stockWeights.put(stockName, stockPercentage);
    }

    return stockWeights;
  }
}
