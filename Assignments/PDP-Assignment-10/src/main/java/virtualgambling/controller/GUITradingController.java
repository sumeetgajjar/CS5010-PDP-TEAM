package virtualgambling.controller;

import java.math.BigDecimal;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import util.Constants;
import util.Utils;
import virtualgambling.model.PersistableUserModel;
import virtualgambling.model.bean.Portfolio;
import virtualgambling.model.bean.SharePurchaseOrder;
import virtualgambling.model.factory.StockDataSourceType;
import virtualgambling.model.persistence.PortfolioLoader;
import virtualgambling.model.persistence.PortfolioPersister;
import virtualgambling.model.persistence.StrategyLoader;
import virtualgambling.model.persistence.StrategyPersister;
import virtualgambling.model.persistence.serdes.JSONSerDes;
import virtualgambling.model.strategy.RecurringWeightedInvestmentStrategy;
import virtualgambling.model.strategy.Strategy;
import virtualgambling.view.guiview.GUIView;

/**
 * This class represents a GUI Trading Controller. It implements {@link Controller} interface. This
 * is a Asynchronous Controller which wait for the callback from {@link GUIView} to perform
 * operations.
 */
public class GUITradingController implements Controller {

  private final PersistableUserModel persistableUserModel;
  private final GUIView guiView;

  /**
   * Constructs a {@link GUITradingController} object with the given params.
   *
   * @param persistableUserModel the persistable user model
   * @param guiView              the gui view
   * @throws IllegalArgumentException if any of the given paramas are null
   */
  public GUITradingController(PersistableUserModel persistableUserModel,
                              GUIView guiView) throws IllegalArgumentException {
    this.persistableUserModel = Utils.requireNonNull(persistableUserModel);
    this.guiView = Utils.requireNonNull(guiView);
  }

  @Override
  public void run() {
    this.guiView.addFeatures(new TradingFeaturesImpl(persistableUserModel, guiView));
  }

  private static class TradingFeaturesImpl implements TradingFeatures {

    private final PersistableUserModel userModel;
    private final GUIView guiView;

    private TradingFeaturesImpl(PersistableUserModel userModel, GUIView guiView) {
      this.userModel = userModel;
      this.guiView = guiView;
    }

    @Override
    public boolean createPortfolio(String portfolio) {
      try {
        this.userModel.createPortfolio(portfolio);
        return true;
      } catch (Exception e) {
        this.guiView.displayError(e.getMessage());
        return false;
      }
    }

    @Override
    public Optional<Portfolio> getPortfolio(String portfolioName) {
      try {
        return Optional.of(this.userModel.getPortfolio(portfolioName));
      } catch (Exception e) {
        this.guiView.displayError(e.getMessage());
        return Optional.empty();
      }
    }

    @Override
    public List<Portfolio> getAllPortfolios() {
      return this.userModel.getAllPortfolios();
    }

    @Override
    public Optional<BigDecimal> getPortfolioCostBasis(String portfolio, Date date) {
      try {
        return Optional.of(this.userModel
                .getPortfolio(portfolio).getCostBasisIncludingCommission(date));
      } catch (Exception e) {
        this.guiView.displayError(e.getMessage());
        return Optional.empty();
      }
    }

    @Override
    public Optional<BigDecimal> getPortfolioValue(String portfolio, Date date) {
      try {
        return Optional.of(this.userModel.getPortfolio(portfolio).getValue(date));
      } catch (Exception e) {
        this.guiView.displayError(e.getMessage());
        return Optional.empty();
      }
    }

    @Override
    public BigDecimal getRemainingCapital() {
      return this.userModel.getRemainingCapital();
    }

    @Override
    public Optional<SharePurchaseOrder> buyShares(String tickerName,
                                                  String portfolioName,
                                                  Date date,
                                                  long quantity,
                                                  double commissionPercentage) {
      try {
        return Optional.of(this.userModel
                .buyShares(tickerName, portfolioName, date, quantity, commissionPercentage));

      } catch (Exception e) {
        this.guiView.displayError(e.getMessage());
        return Optional.empty();
      }
    }

    @Override
    public Optional<List<SharePurchaseOrder>> buyShares(String portfolioName, Date date,
                                                        Set<String> tickerNames,
                                                        BigDecimal amountToInvest,
                                                        double commission) {
      return Optional.empty();
    }

    @Override
    public Optional<List<SharePurchaseOrder>> buyShares(String portfolioName, Date startDate,
                                                        Map<String, Double> stockWeights,
                                                        BigDecimal amountToInvest,
                                                        double commission) {
      return Optional.empty();
    }

    @Override
    public Optional<List<SharePurchaseOrder>> buyShares(String portfolioName, Date startDate,
                                                        int dayFrequency, Set<String> tickerNames
            , BigDecimal amountToInvest, double commission) {
      try {
        Strategy strategy =
                new RecurringWeightedInvestmentStrategy(startDate,
                        Utils.getStocksWithWeights(tickerNames), dayFrequency);

        return Optional.of(this.userModel.buyShares(portfolioName, amountToInvest, strategy,
                commission));
      } catch (Exception e) {
        this.guiView.displayError(e.getMessage());
        return Optional.empty();
      }
    }

    @Override
    public Optional<List<SharePurchaseOrder>> buyShares(String portfolioName, Date startDate,
                                                        Date endDate, int dayFrequency,
                                                        Set<String> tickerNames,
                                                        BigDecimal amountToInvest,
                                                        double commission) {
      try {
        Strategy strategy =
                new RecurringWeightedInvestmentStrategy(startDate,
                        Utils.getStocksWithWeights(tickerNames), dayFrequency,
                        endDate);

        return Optional.of(this.userModel.buyShares(portfolioName, amountToInvest, strategy,
                commission));
      } catch (Exception e) {
        this.guiView.displayError(e.getMessage());
        return Optional.empty();
      }
    }

    @Override
    public Optional<List<SharePurchaseOrder>> buyShares(String portfolioName, Date startDate,
                                                        int dayFrequency,
                                                        Map<String, Double> stockWeights,
                                                        BigDecimal amountToInvest,
                                                        double commission) {
      try {
        Strategy strategy =
                new RecurringWeightedInvestmentStrategy(startDate, stockWeights, dayFrequency);

        return Optional.of(this.userModel.buyShares(portfolioName, amountToInvest, strategy,
                commission));
      } catch (Exception e) {
        this.guiView.displayError(e.getMessage());
        return Optional.empty();
      }
    }

    @Override
    public Optional<List<SharePurchaseOrder>> buyShares(String portfolioName, Date startDate,
                                                        Date endDate, int dayFrequency,
                                                        Map<String, Double> stockWeights,
                                                        BigDecimal amountToInvest,
                                                        double commission) {

      try {
        Strategy strategy =
                new RecurringWeightedInvestmentStrategy(startDate, stockWeights, dayFrequency,
                        endDate);

        return Optional.of(this.userModel.buyShares(portfolioName, amountToInvest, strategy,
                commission));
      } catch (Exception e) {
        this.guiView.displayError(e.getMessage());
        return Optional.empty();
      }
    }

    @Override
    public boolean loadAndExecuteStrategy(String portfolioName, String filePath,
                                          BigDecimal amountToInvest, double commissionPercentage) {

      try {
        JSONSerDes<Strategy> serDes = new JSONSerDes<>(Paths.get(filePath),
                Constants.RECURRING_STRATEGY_TYPE);
        userModel.loadIntoModel(new StrategyLoader(userModel, serDes, portfolioName,
                amountToInvest, commissionPercentage));
        return true;
      } catch (Exception e) {
        this.guiView.displayError(e.getMessage());
        return false;
      }
    }

    @Override
    public boolean saveStrategy(String filePath, Date startDate, int dayFrequency,
                                Set<String> tickerNames) {
      try {
        JSONSerDes<Strategy> jsonSerDes = new JSONSerDes<>(Paths.get(filePath),
                Constants.RECURRING_STRATEGY_TYPE);
        Strategy strategy = new RecurringWeightedInvestmentStrategy(startDate,
                Utils.getStocksWithWeights(tickerNames), dayFrequency);

        userModel.persistFromModel(new StrategyPersister(jsonSerDes, strategy));

        return true;
      } catch (Exception e) {
        this.guiView.displayError(e.getMessage());
        return false;
      }
    }

    @Override
    public boolean saveStrategy(String filePath, Date startDate, Date endDate, int dayFrequency,
                                Set<String> tickerNames) {
      try {
        JSONSerDes<Strategy> jsonSerDes = new JSONSerDes<>(Paths.get(filePath),
                Constants.RECURRING_STRATEGY_TYPE);
        Strategy strategy = new RecurringWeightedInvestmentStrategy(startDate,
                Utils.getStocksWithWeights(tickerNames), dayFrequency, endDate);

        userModel.persistFromModel(new StrategyPersister(jsonSerDes, strategy));

        return true;
      } catch (Exception e) {
        this.guiView.displayError(e.getMessage());
        return false;
      }
    }

    @Override
    public boolean saveStrategy(String filePath, Date startDate, int dayFrequency, Map<String,
            Double> stockWeights) {
      try {
        JSONSerDes<Strategy> jsonSerDes = new JSONSerDes<>(Paths.get(filePath),
                Constants.RECURRING_STRATEGY_TYPE);
        Strategy strategy = new RecurringWeightedInvestmentStrategy(startDate,
                stockWeights, dayFrequency);

        userModel.persistFromModel(new StrategyPersister(jsonSerDes, strategy));

        return true;
      } catch (Exception e) {
        this.guiView.displayError(e.getMessage());
        return false;
      }
    }

    @Override
    public boolean saveStrategy(String filePath, Date startDate, Date endDate, int dayFrequency,
                                Map<String, Double> stockWeights) {
      try {
        JSONSerDes<Strategy> jsonSerDes = new JSONSerDes<>(Paths.get(filePath),
                Constants.RECURRING_STRATEGY_TYPE);
        Strategy strategy = new RecurringWeightedInvestmentStrategy(startDate,
                stockWeights, dayFrequency, endDate);

        userModel.persistFromModel(new StrategyPersister(jsonSerDes, strategy));

        return true;
      } catch (Exception e) {
        this.guiView.displayError(e.getMessage());
        return false;
      }
    }

    @Override
    public boolean loadPortfolio(String filePath) {
      try {
        JSONSerDes<Portfolio> jsonSerDes = new JSONSerDes<>(Paths.get(filePath),
                Constants.PORTFOLIO_TYPE);
        userModel.loadIntoModel(new PortfolioLoader(userModel, jsonSerDes));
        return true;
      } catch (Exception e) {
        this.guiView.displayError(e.getMessage());
        return false;
      }
    }

    @Override
    public boolean savePortfolio(String portfolioName, String filePath) {
      try {
        JSONSerDes<Portfolio> jsonSerDes = new JSONSerDes<>(Paths.get(filePath),
                Constants.PORTFOLIO_TYPE);
        userModel.persistFromModel(new PortfolioPersister(jsonSerDes,
                userModel.getPortfolio(portfolioName)));
        return true;
      } catch (Exception e) {
        this.guiView.displayError(e.getMessage());
        return false;
      }
    }

    @Override
    public boolean setDataSource(StockDataSourceType stockDataSourceType) {
      try {
        userModel.setStockDataSourceType(stockDataSourceType);
        return true;
      } catch (Exception e) {
        this.guiView.displayError(e.getMessage());
        return false;
      }
    }

    @Override
    public void quit() {
      System.exit(0);
    }
  }
}
