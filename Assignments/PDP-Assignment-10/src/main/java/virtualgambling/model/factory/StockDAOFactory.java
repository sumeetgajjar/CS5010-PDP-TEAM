package virtualgambling.model.factory;

import virtualgambling.model.stockdao.StockDAO;

public interface StockDAOFactory {
  StockDAO fromStockDAOAndDataSource(StockDAOType stockDAOType,
                                     StockDataSourceType stockDataSourceType);
}
