package virtualgambling.model.persistence;

import java.io.IOException;

import virtualgambling.model.persistence.serdes.SerDes;
import virtualgambling.model.strategy.Strategy;

public class StrategyPersister implements Persister {
  private final SerDes<Strategy> serDes;
  private final Strategy strategy;

  public StrategyPersister(SerDes<Strategy> serDes, Strategy strategy) {
    this.serDes = serDes;
    this.strategy = strategy;
  }

  @Override
  public void persist() throws IOException {
    this.serDes.serialize(strategy);
  }
}
