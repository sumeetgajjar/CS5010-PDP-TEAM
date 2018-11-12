package virtualgambling.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by gajjar.s, on 8:11 PM, 11/11/18
 */
public interface User {

  void createPortfolio(String portfolioName);

  Portfolio getPortfolio(String portfolioName);

  List<Portfolio> getPortfolios();

  OrderInfo buyShares(String tickerName,
                      String portfolioName,
                      LocalDateTime localDateTime,
                      BigDecimal amount);
}
