package virtualgambling.model.persistence;

import java.io.IOException;
import java.nio.file.Path;

import virtualgambling.model.persistence.serdes.SerDes;
import virtualgambling.model.strategy.Strategy;

public class StrategyPersister implements Persister {
  private final SerDes<Strategy> serDes;
  private final Strategy strategy;
  private final Path path;

  public StrategyPersister(SerDes<Strategy> serDes, Strategy strategy, Path path) {
    this.serDes = serDes;
    this.strategy = strategy;
    this.path = path;
  }

  @Override
  public void persist() throws IOException {
    this.serDes.serialize(strategy);
  }
}
