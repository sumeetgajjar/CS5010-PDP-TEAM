package virtualgambling.controller.command.persistantusermodelcommand;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.Objects;
import java.util.function.Consumer;

import virtualgambling.controller.command.enhancedusermodelcommand.BuySharesWithRecurringWeightedStrategyCommand;
import virtualgambling.model.PersistableUserModel;
import virtualgambling.model.persistence.StrategyPersister;
import virtualgambling.model.persistence.serdes.JSONSerDes;
import virtualgambling.model.strategy.RecurringWeightedInvestmentStrategy;
import virtualgambling.model.strategy.Strategy;

/**
 * Created by gajjar.s, on 10:34 PM, 12/5/18
 */
public class SaveAndExecuteBuySharesWithRecurringWeightedStrategyCommand extends
        BuySharesWithRecurringWeightedStrategyCommand {

  private final PersistableUserModel persistableUserModel;
  private final Path path;

  /**
   * Constructs a BuySharesWithRecurringWeightedStrategyCommand object with given params.
   *
   * @param persistableUserModel the enhanced user model
   * @param portfolioName        the portfolio to invest in
   * @param amountToInvest       the amount to invest in
   * @param strategy             the strategy
   * @param commission           the commission for each transaction
   * @param consumer             the consumer to consume output of command
   * @param path                 the path to save the strategy
   * @throws IllegalArgumentException if the given params are null
   */
  public SaveAndExecuteBuySharesWithRecurringWeightedStrategyCommand(PersistableUserModel persistableUserModel,
                                                                     String portfolioName,
                                                                     BigDecimal amountToInvest,
                                                                     Strategy strategy,
                                                                     double commission,
                                                                     Consumer<String> consumer,
                                                                     Path path)
          throws IllegalArgumentException {

    super(persistableUserModel, portfolioName, amountToInvest, strategy, commission, consumer);
    this.persistableUserModel = persistableUserModel;
    this.path = path;
  }

  /**
   * Constructs a BuySharesWithRecurringWeightedStrategyCommand object with given params.
   *
   * @param persistableUserModel the enhanced user model
   * @param portfolioName        the portfolio to invest in
   * @param amountToInvest       the amount to invest in
   * @param strategy             the strategy
   * @param commission           the commission for each transaction
   * @param consumer             the consumer to consume output of command
   * @throws IllegalArgumentException if the given params are null
   */
  public SaveAndExecuteBuySharesWithRecurringWeightedStrategyCommand(PersistableUserModel persistableUserModel,
                                                                     String portfolioName,
                                                                     BigDecimal amountToInvest,
                                                                     Strategy strategy,
                                                                     double commission,
                                                                     Consumer<String> consumer)
          throws IllegalArgumentException {

    this(persistableUserModel, portfolioName, amountToInvest, strategy, commission, consumer, null);
  }

  @Override
  public void execute() {
    if (Objects.nonNull(path)) {
      JSONSerDes<Strategy> serDes = new JSONSerDes<>(path,
              new TypeToken<RecurringWeightedInvestmentStrategy>() {
              }.getType());

      try {
        this.persistableUserModel.persistFromModel(new StrategyPersister(serDes,
                strategy));
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    super.execute();
  }
}
