package virtualgambling.controller;

import java.util.Date;
import java.util.List;

/**
 * Created by gajjar.s, on 3:38 AM, 12/5/18
 */
public interface Features {

  void createPortfolio(String portfolio);

  List<String> getAllPortfolios();

  String getPortfolioCostBasis(String portfolio, Date date);

  String getPortfolioValue(String portfolio, Date date);

  String getRemainingCapital();

  String buyShares(String tickerName,
                   String portfolioName,
                   Date date,
                   long quantity,
                   double commissionPercentage);

  void quit();
}
