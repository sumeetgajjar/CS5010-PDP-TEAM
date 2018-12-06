package virtualgambling.model.persistence;

import java.io.IOException;

import util.Utils;
import virtualgambling.model.bean.Portfolio;
import virtualgambling.model.persistence.serdes.SerDes;

/**
 * {@link PortfolioPersister} is a {@link Persister} that serializes the given {@link Portfolio}
 * using the provided {@link SerDes}
 *
 * <p>If a serialized version of {@link Portfolio} already exists in the location determined by
 * the {@link SerDes}, it will be overwritten.
 */
public class PortfolioPersister implements Persister {
  private final SerDes<Portfolio> serDes;
  private final Portfolio portfolio;

  /**
   * Constructs a {@link PortfolioPersister} in terms of {@link SerDes} and a {@link Portfolio}.
   *
   * @param serDes    the serializer/deserializer that will be delegated the job of serializing
   * @param portfolio the portfolio that needs to be serialized
   */
  public PortfolioPersister(SerDes<Portfolio> serDes, Portfolio portfolio) {
    this.serDes = Utils.requireNonNull(serDes);
    this.portfolio = Utils.requireNonNull(portfolio);
  }

  @Override
  public void persist() throws IOException {
    this.serDes.serialize(Utils.requireNonNull(portfolio));
  }
}
