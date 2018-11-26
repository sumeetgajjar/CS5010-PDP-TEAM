package virtualgambling.model;

import java.util.Date;
import java.util.List;

import virtualgambling.model.bean.SharePurchaseOrder;
import virtualgambling.model.exceptions.InsufficientCapitalException;
import virtualgambling.model.exceptions.StockDataNotFoundException;
import virtualgambling.model.stockdatasource.strategy.Strategy;

/**
 * Created by gajjar.s, on 1:12 PM, 11/25/18
 */
public interface EnhancedUserModel extends UserModel {

  SharePurchaseOrder buyShares(String tickerName,
                               String portfolioName,
                               Date date,
                               long quantity,
                               double commissionPercentage) throws IllegalArgumentException,
          StockDataNotFoundException, InsufficientCapitalException;

  List<SharePurchaseOrder> buyShares(String portfolioName,
                                     Strategy strategy,
                                     double commissionPercentage)
          throws IllegalArgumentException, StockDataNotFoundException, InsufficientCapitalException;
}