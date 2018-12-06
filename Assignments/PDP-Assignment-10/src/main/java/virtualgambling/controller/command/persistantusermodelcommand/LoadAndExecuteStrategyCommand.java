package virtualgambling.controller.command.persistantusermodelcommand;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.function.Consumer;

import util.Constants;
import util.Utils;
import virtualgambling.model.PersistableUserModel;
import virtualgambling.model.persistence.StrategyLoader;
import virtualgambling.model.persistence.serdes.JSONSerDes;
import virtualgambling.model.strategy.Strategy;

/**
 * This class represents a command to Load and execute a Strategy on a Portfolio. It extends {@link
 * AbstractPersistableUserModelCommand}.
 */
public class LoadAndExecuteStrategyCommand extends AbstractPersistableUserModelCommand {

  private final String portfolioName;
  private final Path path;
  private final BigDecimal amountToInvest;
  private final double commission;
  private final Consumer<String> consumer;

  /**
   * Constructs the object of LoadAndExecuteStrategyCommand with the given params.
   *
   * @param persistableUserModel the persistable user model
   * @param portfolioName        the portfolio on which the strategy is to be executed
   * @param path                 the path of the strategy file to load
   * @param amountToInvest       the amount to invest
   * @param commission           the commission percentage per transaction
   * @param consumer             the consumer
   * @throws IllegalArgumentException if any of the given params are null
   */
  public LoadAndExecuteStrategyCommand(PersistableUserModel persistableUserModel,
                                       String portfolioName,
                                       Path path,
                                       BigDecimal amountToInvest,
                                       double commission,
                                       Consumer<String> consumer) throws IllegalArgumentException {
    super(persistableUserModel);
    this.portfolioName = Utils.requireNonNull(portfolioName);
    this.path = Utils.requireNonNull(path);
    this.amountToInvest = Utils.requireNonNull(amountToInvest);
    this.commission = commission;
    this.consumer = Utils.requireNonNull(consumer);
  }

  @Override
  public void execute() {


    try {
      JSONSerDes<Strategy> serDes = new JSONSerDes<>(path,
              Constants.RECURRING_STRATEGY_TYPE);
      this.persistableUserModel.loadIntoModel(new StrategyLoader(persistableUserModel, serDes,
              portfolioName,
              amountToInvest, commission));
      this.consumer.accept(Constants.STRATEGY_SUCCESSFULLY_LOADED_MESSAGE);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
