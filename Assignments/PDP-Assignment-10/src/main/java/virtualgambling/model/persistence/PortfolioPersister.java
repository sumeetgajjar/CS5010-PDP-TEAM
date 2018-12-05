package virtualgambling.model.persistence;

import java.io.IOException;

import virtualgambling.model.bean.Portfolio;
import virtualgambling.model.persistence.serdes.SerDes;

public class PortfolioPersister implements Persister {
  private final SerDes<Portfolio> serDes;
  private final Portfolio portfolio;

  public PortfolioPersister(SerDes<Portfolio> serDes, Portfolio portfolio) {
    this.serDes = serDes;
    this.portfolio = portfolio;
  }

  @Override
  public void persist() throws IOException {
    this.serDes.serialize(portfolio);
  }
}
