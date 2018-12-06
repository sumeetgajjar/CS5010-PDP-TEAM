package virtualgambling.model.persistence;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Objects;

import util.Utils;
import virtualgambling.model.EnhancedUserModel;
import virtualgambling.model.exceptions.PersistenceException;
import virtualgambling.model.persistence.serdes.SerDes;
import virtualgambling.model.strategy.Strategy;

public class StrategyLoader implements Loader {
  private final SerDes<Strategy> serDes;
  private final EnhancedUserModel enhancedUserModel;
  private final String portfolioName;
  private final BigDecimal amountToInvest;
  private final double commissionPercentage;

  public StrategyLoader(SerDes<Strategy> serDes, EnhancedUserModel enhancedUserModel,
                        String portfolioName, BigDecimal amountToInvest,
                        double commissionPercentage) {
    this.serDes = Utils.requireNonNull(serDes);
    this.enhancedUserModel = Utils.requireNonNull(enhancedUserModel);
    this.portfolioName = Utils.requireNonNull(portfolioName);
    this.amountToInvest = Utils.requireNonNull(amountToInvest);
    this.commissionPercentage = Utils.requireNonNull(commissionPercentage);
  }

  @Override
  public void load() throws IOException, PersistenceException {
    Strategy strategy = this.serDes.deserialize();
    if (Objects.isNull(strategy)) {
      throw new PersistenceException("Could not deserialize strategy");
    }
    this.enhancedUserModel.buyShares(portfolioName, amountToInvest, strategy, commissionPercentage);
  }
}
