package virtualgambling.controller.command.persistantusermodelcommand;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Consumer;

import util.Constants;
import virtualgambling.model.PersistableUserModel;
import virtualgambling.model.persistence.StrategyPersister;
import virtualgambling.model.persistence.serdes.JSONSerDes;
import virtualgambling.model.strategy.RecurringWeightedInvestmentStrategy;
import virtualgambling.model.strategy.Strategy;

/**
 * Created by gajjar.s, on 12:18 AM, 12/6/18
 */
public class SaveStrategyCommand extends AbstractPersistableUserModelCommand {

  private final Strategy strategy;
  private final Path path;
  private final Consumer<String> consumer;

  public SaveStrategyCommand(PersistableUserModel persistableUserModel, Strategy strategy,
                             Path path, Consumer<String> consumer) throws IllegalArgumentException {
    super(persistableUserModel);
    this.strategy = strategy;
    this.path = path;
    this.consumer = consumer;
  }

  @Override
  public void execute() {
    JSONSerDes<Strategy> serDes = new JSONSerDes<>(path,
            new TypeToken<RecurringWeightedInvestmentStrategy>() {
            }.getType());

    try {
      this.persistableUserModel.persistFromModel(new StrategyPersister(serDes, strategy));
      this.consumer.accept(Constants.STRATEGY_SUCCESSFULLY_SAVED_MESSAGE);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
