package virtualgambling.model.persistence;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import virtualgambling.model.UserModel;
import virtualgambling.model.bean.Portfolio;
import virtualgambling.model.bean.SharePurchaseOrder;
import virtualgambling.model.persistence.serdes.SerDes;

public class PortfolioLoader implements Loader {
  private final UserModel userModel;
  private final SerDes<Portfolio> serDes;
  private final String portfolioName;
  private final Path path;

  public PortfolioLoader(UserModel userModel, SerDes<Portfolio> serDes, String portfolioName,
                         Path path) {
    this.userModel = userModel;
    this.serDes = serDes;
    this.portfolioName = portfolioName;
    this.path = path;
  }

  @Override
  public void load() throws IOException {
    Portfolio portfolio = this.serDes.deserialize();
    List<SharePurchaseOrder> sharePurchaseOrders =
            getListOfOrdersFromExistingPortfolio(portfolioName);
    List<SharePurchaseOrder> distinctPurchaseOrders =
            findDistinctPurchaseOrders(portfolio.getPurchases(), sharePurchaseOrders);
    distinctPurchaseOrders.forEach(sharePurchaseOrder ->
            userModel.buyShares(sharePurchaseOrder.getTickerName(), this.portfolioName,
                    sharePurchaseOrder.getStockPrice().getDate(),
                    sharePurchaseOrder.getQuantity()));

  }

  private List<SharePurchaseOrder> findDistinctPurchaseOrders(List<SharePurchaseOrder> purchases,
                                                              List<SharePurchaseOrder> sharePurchaseOrders) {
    return null;
  }

  private List<SharePurchaseOrder> getListOfOrdersFromExistingPortfolio(String portfolioName) {
    return null;
  }
}
