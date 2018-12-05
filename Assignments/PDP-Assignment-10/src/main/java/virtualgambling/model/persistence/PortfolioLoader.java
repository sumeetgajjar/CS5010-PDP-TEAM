package virtualgambling.model.persistence;

import java.io.IOException;

import virtualgambling.model.PersistableUserModel;
import virtualgambling.model.bean.Portfolio;
import virtualgambling.model.factory.StockDAOType;
import virtualgambling.model.factory.StockDataSourceType;
import virtualgambling.model.persistence.serdes.SerDes;

public class PortfolioLoader implements Loader {
  private final PersistableUserModel userModel;
  private final SerDes<Portfolio> serDes;
  private final String portfolioName;

  public PortfolioLoader(PersistableUserModel userModel, SerDes<Portfolio> serDes,
                         String portfolioName) {
    this.userModel = userModel;
    this.serDes = serDes;
    this.portfolioName = portfolioName;
  }

  @Override
  public void load() throws IOException {
    Portfolio portfolio = this.serDes.deserialize();
    userModel.createPortfolio(portfolio.getName());
    StockDAOType stockDAOType = userModel.getStockDAOType();
    StockDataSourceType stockDataSourceType = userModel.getStockDataSourceType();

    userModel.setStockDAOType(portfolio.getStockDAOType());
    userModel.setStockDataSourceType(portfolio.getStockDataSourceType());
    portfolio.getPurchases().forEach(sharePurchaseOrder ->
            userModel.buyShares(sharePurchaseOrder.getTickerName(),
                    this.portfolioName,
                    sharePurchaseOrder.getStockPrice().getDate(),
                    sharePurchaseOrder.getQuantity(),
                    sharePurchaseOrder.getCommissionPercentage())
    );

    userModel.setStockDAOType(stockDAOType);
    userModel.setStockDataSourceType(stockDataSourceType);
  }
}
