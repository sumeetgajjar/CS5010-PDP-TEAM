package virtualgambling.model.persistence;

import java.io.IOException;
import java.nio.file.Path;

import virtualgambling.model.bean.Portfolio;
import virtualgambling.model.persistence.serdes.SerDes;

public class PortfolioPersister implements Persister {
  private final SerDes<Portfolio> serDes;
  private final Portfolio portfolio;
  private final Path path;

  public PortfolioPersister(SerDes<Portfolio> serDes, Portfolio portfolio, Path path) {
    this.serDes = serDes;
    this.portfolio = portfolio;
    this.path = path;
  }

  @Override
  public void persist() throws IOException {
    this.serDes.serialize(portfolio);
  }
}
