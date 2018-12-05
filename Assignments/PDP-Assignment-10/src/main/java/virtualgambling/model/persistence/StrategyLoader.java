package virtualgambling.model.persistence;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;

import virtualgambling.model.EnhancedUserModel;
import virtualgambling.model.persistence.serdes.SerDes;
import virtualgambling.model.strategy.Strategy;

public class StrategyLoader implements Loader {
  private final SerDes<Strategy> serDes;
  private final EnhancedUserModel enhancedUserModel;
  private final String portfolioName;
  private final BigDecimal amountToInvest;
  private final double commissionPercentage;
  private final Path path;

  public StrategyLoader(SerDes<Strategy> serDes, EnhancedUserModel enhancedUserModel,
                        String portfolioName, BigDecimal amountToInvest,
                        double commissionPercentage, Path path) {
    this.serDes = serDes;
    this.enhancedUserModel = enhancedUserModel;
    this.portfolioName = portfolioName;
    this.amountToInvest = amountToInvest;
    this.commissionPercentage = commissionPercentage;
    this.path = path;
  }

  @Override
  public void load() throws IOException {
    Strategy strategy = this.serDes.deserialize();
    this.enhancedUserModel.buyShares(portfolioName, amountToInvest, strategy, commissionPercentage);
  }
}
