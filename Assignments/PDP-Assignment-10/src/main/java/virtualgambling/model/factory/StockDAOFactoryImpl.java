package virtualgambling.model.factory;

import virtualgambling.model.stockdao.SimpleStockDAO;
import virtualgambling.model.stockdao.StockDAO;
import virtualgambling.model.stockdatasource.AlphaVantageAPIStockDataSource;
import virtualgambling.model.stockdatasource.SimpleStockDataSource;
import virtualgambling.model.stockdatasource.StockDataSource;

public class StockDAOFactoryImpl implements StockDAOFactory {
  @Override
  public StockDAO fromStockDAOAndDataSource(StockDAOType stockDAOType,
                                            StockDataSourceType stockDataSourceType) {
    StockDataSource stockDataSource;
    switch (stockDataSourceType) {
      case SIMPLE: {
        stockDataSource = new SimpleStockDataSource();
        break;
      }
      case ALPHA_VANTAGE: {
        stockDataSource = AlphaVantageAPIStockDataSource.getInstance();
        break;
      }
      default:
        throw new IllegalArgumentException("Data source not found");
    }

    switch (stockDAOType) {
      case SIMPLE:
        return new SimpleStockDAO(stockDataSource);
      default:
        throw new IllegalArgumentException("stock DAO not found");
    }
  }
}

