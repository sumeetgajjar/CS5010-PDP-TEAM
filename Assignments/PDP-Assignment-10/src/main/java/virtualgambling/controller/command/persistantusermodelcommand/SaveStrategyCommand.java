package virtualgambling.controller.command.persistantusermodelcommand;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Consumer;

import util.Constants;
import util.Utils;
import virtualgambling.model.PersistableUserModel;
import virtualgambling.model.persistence.StrategyPersister;
import virtualgambling.model.persistence.serdes.JSONSerDes;
import virtualgambling.model.strategy.Strategy;

/**
 * This class represents a command to Save a Strategy to a file. It extends {@link
 * AbstractPersistableUserModelCommand}.
 */
public class SaveStrategyCommand extends AbstractPersistableUserModelCommand {

  private final Strategy strategy;
  private final Path path;
  private final Consumer<String> consumer;

  /**
   * Constructs an object of SaveStrategyCommand with the given params.
   *
   * @param persistableUserModel the persistable user model
   * @param strategy             the strategy to save
   * @param path                 the path at which the strategy is to be saved
   * @param consumer             the consumer
   * @throws IllegalArgumentException if any of the given params are null
   */
  public SaveStrategyCommand(PersistableUserModel persistableUserModel, Strategy strategy,
                             Path path, Consumer<String> consumer)
          throws IllegalArgumentException {

    super(persistableUserModel);
    this.strategy = Utils.requireNonNull(strategy);
    this.path = Utils.requireNonNull(path);
    this.consumer = Utils.requireNonNull(consumer);
  }

  @Override
  public void execute() {
    try {
      JSONSerDes<Strategy> serDes = new JSONSerDes<>(path,
              Constants.RECURRING_STRATEGY_TYPE);
      this.persistableUserModel.persistFromModel(new StrategyPersister(serDes, strategy));
      this.consumer.accept(Constants.STRATEGY_SUCCESSFULLY_SAVED_MESSAGE);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
