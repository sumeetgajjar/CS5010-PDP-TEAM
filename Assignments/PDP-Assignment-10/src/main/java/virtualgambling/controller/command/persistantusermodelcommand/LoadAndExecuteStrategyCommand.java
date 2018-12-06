package virtualgambling.controller.command.persistantusermodelcommand;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.function.Consumer;

import util.Constants;
import virtualgambling.model.PersistableUserModel;
import virtualgambling.model.persistence.StrategyLoader;
import virtualgambling.model.persistence.serdes.JSONSerDes;
import virtualgambling.model.strategy.RecurringWeightedInvestmentStrategy;
import virtualgambling.model.strategy.Strategy;

/**
 * Created by gajjar.s, on 11:31 PM, 12/5/18
 */
public class LoadAndExecuteStrategyCommand extends AbstractPersistableUserModelCommand {

  private final String portfolioName;
  private final Path path;
  private final BigDecimal amountToInvest;
  private final double commission;
  private final Consumer<String> consumer;

  public LoadAndExecuteStrategyCommand(PersistableUserModel persistableUserModel,
                                       String portfolioName,
                                       Path path,
                                       BigDecimal amountToInvest,
                                       double commission,
                                       Consumer<String> consumer) throws IllegalArgumentException {
    super(persistableUserModel);
    this.portfolioName = portfolioName;
    this.path = path;
    this.amountToInvest = amountToInvest;
    this.commission = commission;
    this.consumer = consumer;
  }

  @Override
  public void execute() {

    JSONSerDes<Strategy> serDes = new JSONSerDes<>(path,
            new TypeToken<RecurringWeightedInvestmentStrategy>() {
            }.getType());

    try {
      this.persistableUserModel.loadIntoModel(new StrategyLoader(persistableUserModel, serDes,
              portfolioName,
              amountToInvest, commission));
      this.consumer.accept(Constants.STRATEGY_SUCCESSFULLY_LOADED_MESSAGE);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
