package virtualgambling.model.persistence;

import java.io.IOException;

import util.Utils;
import virtualgambling.model.persistence.serdes.SerDes;
import virtualgambling.model.strategy.Strategy;

/**
 * {@link StrategyPersister} is a {@link Persister} that serializes the given {@link Strategy} using
 * the provided {@link SerDes}
 *
 * <p>If a serialized version of {@link Strategy} already exists in the location determined by
 * the {@link SerDes}, it will be overwritten.
 */
public class StrategyPersister implements Persister {
  private final SerDes<Strategy> serDes;
  private final Strategy strategy;

  /**
   * Constructs a {@link StrategyPersister} from {@link SerDes} and the strategy instance that needs
   * to be persisted.
   *
   * @param serDes   the serializer/deserializer that will be delegated the job of serializing
   * @param strategy the strategy that needs to be serialized
   */
  public StrategyPersister(SerDes<Strategy> serDes, Strategy strategy) {
    this.serDes = Utils.requireNonNull(serDes);
    this.strategy = Utils.requireNonNull(strategy);
  }

  @Override
  public void persist() throws IOException {
    this.serDes.serialize(strategy);
  }
}
