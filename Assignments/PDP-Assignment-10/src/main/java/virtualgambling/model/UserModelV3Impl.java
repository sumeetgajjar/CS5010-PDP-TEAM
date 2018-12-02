package virtualgambling.model;

import java.io.IOException;
import java.nio.file.Path;

import util.Utils;
import virtualgambling.model.bean.Portfolio;
import virtualgambling.model.stockdao.StockDAO;

/**
 * Created by gajjar.s, on 12:20 PM, 12/2/18
 */
public class UserModelV3Impl extends EnhancedUserModelImpl implements UserModelV3 {

  /**
   * Constructs a {@link UserModelV3Impl} object with given params.
   *
   * @param stockDAO the stockDAO
   * @throws IllegalArgumentException if the given stockDAO is null
   */
  public UserModelV3Impl(StockDAO stockDAO) throws IllegalArgumentException {
    super(stockDAO);
  }

  @Override
  public void savePortfolioToFile(String portfolioName, Path path) throws IOException {
    Portfolio portfolio = getPortfolio(portfolioName);
    String serializedString = portfolio.serialize();
    Utils.saveToFile(path, serializedString);
  }

  @Override
  public void loadPortfolioFromFileInModel(Path path) throws IOException, IllegalStateException {
    String serializedString = Utils.readStringFromFile(path);
    Portfolio portfolio = Portfolio.deserialize(serializedString);
    if (this.portfolios.containsKey(portfolio.getName())) {
      throw new IllegalStateException("Cannot load portfolio into model");
    }
    this.portfolios.put(portfolio.getName(), portfolio);
  }
}
