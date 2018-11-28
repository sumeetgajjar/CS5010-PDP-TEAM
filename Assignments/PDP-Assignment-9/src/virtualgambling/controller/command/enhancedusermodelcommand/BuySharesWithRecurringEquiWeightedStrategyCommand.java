package virtualgambling.controller.command.enhancedusermodelcommand;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import util.Utils;
import virtualgambling.model.EnhancedUserModel;

/**
 * This class represents a command which buys Shares with same weights on recurring basis. It
 * extends {@link BuySharesWithRecurringWeightedStrategyCommand} class.
 */
public class BuySharesWithRecurringEquiWeightedStrategyCommand extends
        BuySharesWithRecurringWeightedStrategyCommand {


  /**
   * Constructs a BuySharesWithRecurringEquiWeightedStrategyCommand object with given params.
   *
   * @param enhancedUserModel the enhanced user model
   * @param portfolioName     the portfolio to invest in
   * @param amountToInvest    the amount to invest in
   * @param stocks            the stocks to purchase
   * @param startDate         the start date for the recurring investment
   * @param endDate           the end date for the recurring investment
   * @param dayFrequency      the recurring interval
   * @param commission        the commission for each transaction
   * @throws IllegalArgumentException if any of the given params are null
   */
  public BuySharesWithRecurringEquiWeightedStrategyCommand(EnhancedUserModel enhancedUserModel,
                                                           String portfolioName,
                                                           BigDecimal amountToInvest,
                                                           Set<String> stocks,
                                                           Date startDate,
                                                           Date endDate,
                                                           int dayFrequency,
                                                           double commission)
          throws IllegalArgumentException {

    super(enhancedUserModel,
            portfolioName,
            amountToInvest,
            Utils.getStocksWithWeights(stocks),
            startDate,
            endDate,
            dayFrequency,
            commission);
  }

  /**
   * Constructs a BuySharesWithRecurringEquiWeightedStrategyCommand object with given params.
   *
   * @param enhancedUserModel the enhanced user model
   * @param portfolioName     the portfolio to invest in
   * @param amountToInvest    the amount to invest in
   * @param stocks            the stocks to purchase
   * @param startDate         the start date for the recurring investment
   * @param dayFrequency      the recurring interval
   * @param commission        the commission for each transaction
   * @throws IllegalArgumentException if any of the given params are null
   */
  public BuySharesWithRecurringEquiWeightedStrategyCommand(EnhancedUserModel enhancedUserModel,
                                                           String portfolioName,
                                                           BigDecimal amountToInvest,
                                                           Set<String> stocks,
                                                           Date startDate,
                                                           int dayFrequency,
                                                           double commission)
          throws IllegalArgumentException {

    super(enhancedUserModel,
            portfolioName,
            amountToInvest,
            Utils.getStocksWithWeights(stocks),
            startDate,
            dayFrequency,
            commission);
  }
}
