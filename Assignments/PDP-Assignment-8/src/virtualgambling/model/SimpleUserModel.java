package virtualgambling.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import virtualgambling.model.bean.Portfolio;
import virtualgambling.model.bean.PurchaseInfo;
import virtualgambling.model.bean.Share;
import virtualgambling.model.stockdatasource.StockExchange;

/**
 * Created by gajjar.s, on 9:45 PM, 11/12/18
 */
public class SimpleUserModel implements UserModel {

  public SimpleUserModel(StockExchange stockExchange) {

  }

  @Override
  public void createPortfolio(String portfolioName) {

  }

  @Override
  public Portfolio getPortfolio(String portfolioName) {
    return null;
  }

  @Override
  public List<Portfolio> getAllPortfolios() {
    return null;
  }

  @Override
  public PurchaseInfo buyShares(String tickerName, String portfolioName, Date date, long quantity) {
    return null;
  }

  @Override
  public BigDecimal getRemainingCapital() {
    return null;
  }

  @Override
  public void addShareData(Share share, Date date) {

  }
}
