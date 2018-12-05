package virtualgambling.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import virtualgambling.model.bean.Portfolio;
import virtualgambling.model.bean.SharePurchaseOrder;

/**
 * Created by gajjar.s, on 3:38 AM, 12/5/18
 */
public interface Features {

  void createPortfolio(String portfolio);

  List<Portfolio> getAllPortfolios();

  Optional<BigDecimal> getPortfolioCostBasis(String portfolio, Date date);

  Optional<BigDecimal> getPortfolioValue(String portfolio, Date date);

  Optional<Portfolio> getPortfolioComposition(String portfolio);

  BigDecimal getRemainingCapital();

  Optional<SharePurchaseOrder> buyShares(String tickerName,
                                         String portfolioName,
                                         Date date,
                                         long quantity,
                                         double commissionPercentage);

  void quit();
}
