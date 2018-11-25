package virtualgambling.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import virtualgambling.model.bean.SharePurchaseOrder;
import virtualgambling.model.exceptions.InsufficientCapitalException;
import virtualgambling.model.exceptions.StockDataNotFoundException;
import virtualgambling.model.stockdatasource.strategy.Strategy;

/**
 * Created by gajjar.s, on 1:12 PM, 11/25/18
 */
public interface UserModelV2 extends UserModel {

  SharePurchaseOrder buyShares(String tickerName,
                               String portfolioName,
                               Date date,
                               long quantity,
                               BigDecimal commissionPercentage) throws IllegalArgumentException,
          StockDataNotFoundException, InsufficientCapitalException;

  List<SharePurchaseOrder> buyShares(String portfolioName,
                                     Strategy strategy,
                                     BigDecimal commissionPercentage)
          throws IllegalArgumentException, StockDataNotFoundException, InsufficientCapitalException;
}