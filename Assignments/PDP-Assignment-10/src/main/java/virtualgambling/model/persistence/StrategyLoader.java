package virtualgambling.model.persistence;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Objects;

import util.Utils;
import virtualgambling.model.PersistableUserModel;
import virtualgambling.model.exceptions.PersistenceException;
import virtualgambling.model.persistence.serdes.SerDes;
import virtualgambling.model.strategy.Strategy;

/**
 * {@link StrategyLoader} is a {@link Loader} that deserializes {@link Strategy} and loads the
 * strategy (executes it) into the model that it has been instantiated with.
 *
 * <p>In case a portfolio already exists, execution of this strategy adds to it's transactions.
 */
public class StrategyLoader implements Loader {
  private final SerDes<Strategy> serDes;
  private final String portfolioName;
  private final BigDecimal amountToInvest;
  private final double commissionPercentage;

  /**
   * Constructs an instance of {@link StrategyLoader} with the given parameters that are required in
   * order to execute the strategy.
   *
   * @param serDes               the serializer/deserializer that will be delegated the job of
   *                             deserialization
   * @param portfolioName        the name of the portfolio in which to execute this strategy
   * @param amountToInvest       the amount that needs to be invested
   * @param commissionPercentage the commission percentage per transaction
   */
  public StrategyLoader(SerDes<Strategy> serDes,
                        String portfolioName, BigDecimal amountToInvest,
                        double commissionPercentage) {
    this.serDes = Utils.requireNonNull(serDes);
    this.portfolioName = Utils.requireNonNull(portfolioName);
    this.amountToInvest = Utils.requireNonNull(amountToInvest);
    this.commissionPercentage = Utils.requireNonNull(commissionPercentage);
  }

  @Override
  public void load(PersistableUserModel userModel) throws IOException, PersistenceException {
    Utils.requireNonNull(userModel);
    Strategy strategy = this.serDes.deserialize();
    if (Objects.isNull(strategy)) {
      throw new PersistenceException("Could not deserialize strategy");
    }
    userModel.buyShares(portfolioName, amountToInvest, strategy, commissionPercentage);
  }
}
