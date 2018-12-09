package virtualgambling.model.persistence;

import java.io.IOException;
import java.util.Objects;

import util.Utils;
import virtualgambling.model.PersistableUserModel;
import virtualgambling.model.bean.Portfolio;
import virtualgambling.model.exceptions.PersistenceException;
import virtualgambling.model.factory.StockDAOType;
import virtualgambling.model.factory.StockDataSourceType;
import virtualgambling.model.persistence.serdes.SerDes;

/**
 * {@link PortfolioLoader} is a {@link Loader} that deserializes {@link Portfolio} and loads the
 * portfolio into the model that it has been instantiated with.
 *
 * <p>In case the Portfolio already exists, we override it and replace it's contents with the one
 * from the deserialized model.
 */
public class PortfolioLoader implements Loader {
  private final SerDes<Portfolio> serDes;

  /**
   * Constructs a {@link PortfolioLoader} in terms of the {@link PersistableUserModel} and a {@link
   * SerDes} of type Portfolio.
   *
   * @param serDes the serializer/deserializer that will be delegated the job of deserializing
   */
  public PortfolioLoader(SerDes<Portfolio> serDes) {
    this.serDes = Utils.requireNonNull(serDes);
  }

  @Override
  public void load(PersistableUserModel userModel) throws IOException, PersistenceException {
    Utils.requireNonNull(userModel);
    Portfolio portfolio = this.serDes.deserialize();
    if (Objects.isNull(portfolio)) {
      throw new PersistenceException("Could not deserialize portfolio");
    }
    userModel.createPortfolio(portfolio.getName());
    StockDAOType stockDAOType = userModel.getStockDAOType();
    StockDataSourceType stockDataSourceType = userModel.getStockDataSourceType();

    userModel.setStockDAOType(portfolio.getStockDAOType());
    userModel.setStockDataSourceType(portfolio.getStockDataSourceType());
    portfolio.getPurchases().forEach(sharePurchaseOrder ->
            userModel.buyShares(sharePurchaseOrder.getTickerName(),
                    portfolio.getName(),
                    sharePurchaseOrder.getStockPrice().getDate(),
                    sharePurchaseOrder.getQuantity(),
                    sharePurchaseOrder.getCommissionPercentage())
    );

    userModel.setStockDAOType(stockDAOType);
    userModel.setStockDataSourceType(stockDataSourceType);
  }
}
