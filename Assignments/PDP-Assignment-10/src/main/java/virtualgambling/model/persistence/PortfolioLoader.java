package virtualgambling.model.persistence;

import java.io.IOException;

import virtualgambling.model.PersistableUserModel;
import virtualgambling.model.bean.Portfolio;
import virtualgambling.model.persistence.serdes.SerDes;
import virtualgambling.model.stockdao.StockDAO;

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
    StockDAO originalStockDAO = userModel.getStockDAO();
    userModel.setStockDao(portfolio.getStockDAO());
    portfolio.getPurchases().forEach(sharePurchaseOrder ->
            userModel.buyShares(sharePurchaseOrder.getTickerName(),
                    this.portfolioName,
                    sharePurchaseOrder.getStockPrice().getDate(),
                    sharePurchaseOrder.getQuantity(),
                    sharePurchaseOrder.getCommissionPercentage())
    );
    userModel.setStockDao(originalStockDAO);
  }
}
