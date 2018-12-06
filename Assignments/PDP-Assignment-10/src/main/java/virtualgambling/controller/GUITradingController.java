package virtualgambling.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import virtualgambling.model.EnhancedUserModel;
import virtualgambling.model.bean.Portfolio;
import virtualgambling.model.bean.SharePurchaseOrder;
import virtualgambling.model.exceptions.PortfolioNotFoundException;
import virtualgambling.view.guiview.GUIView;

/**
 * Created by gajjar.s, on 3:20 PM, 12/5/18
 */
public class GUITradingController implements Controller {
  private final EnhancedUserModel enhancedUserModel;
  private final GUIView guiView;

  public GUITradingController(EnhancedUserModel enhancedUserModel, GUIView guiView) {
    this.enhancedUserModel = enhancedUserModel;
    this.guiView = guiView;
  }


  @Override
  public void run() {
    this.guiView.addFeatures(new FeaturesImpl(enhancedUserModel, guiView));
  }

  private static class FeaturesImpl implements Features {

    private final EnhancedUserModel enhancedUserModel;
    private final GUIView guiView;

    private FeaturesImpl(EnhancedUserModel enhancedUserModel, GUIView guiView) {
      this.enhancedUserModel = enhancedUserModel;
      this.guiView = guiView;
    }

    @Override
    public boolean createPortfolio(String portfolio) {
      try {
        this.enhancedUserModel.createPortfolio(portfolio);
        return true;
      } catch (Exception e) {
        this.guiView.displayError(e.getMessage());
        return false;
      }
    }

    @Override
    public List<Portfolio> getAllPortfolios() {
      return this.enhancedUserModel.getAllPortfolios();
    }

    @Override
    public Optional<BigDecimal> getPortfolioCostBasis(String portfolio, Date date) {
      try {
        return Optional.of(this.enhancedUserModel
                .getPortfolio(portfolio).getCostBasisIncludingCommission(date));
      } catch (PortfolioNotFoundException e) {
        this.guiView.displayError(e.getMessage());
        return Optional.empty();
      }
    }

    @Override
    public Optional<BigDecimal> getPortfolioValue(String portfolio, Date date) {
      try {
        return Optional.of(this.enhancedUserModel.getPortfolio(portfolio).getValue(date));
      } catch (Exception e) {
        this.guiView.displayError(e.getMessage());
        return Optional.empty();
      }
    }

    @Override
    public Optional<Portfolio> getPortfolioComposition(String portfolio) {
      try {
        return Optional.of(this.enhancedUserModel.getPortfolio(portfolio));
      } catch (Exception e) {
        this.guiView.displayError(e.getMessage());
        return Optional.empty();
      }
    }

    @Override
    public BigDecimal getRemainingCapital() {
      return this.enhancedUserModel.getRemainingCapital();
    }

    @Override
    public Optional<SharePurchaseOrder> buyShares(String tickerName,
                                                  String portfolioName,
                                                  Date date,
                                                  long quantity,
                                                  double commissionPercentage) {
      try {
        return Optional.of(this.enhancedUserModel
                .buyShares(tickerName, portfolioName, date, quantity, commissionPercentage));

      } catch (Exception e) {
        this.guiView.displayError(e.getMessage());
        return Optional.empty();
      }
    }

    @Override
    public String loadPortfolio(String filePath) {
      try {
        //todo implement this
        return "asd";
      } catch (Exception e) {
        return null;
      }
    }

    @Override
    public boolean savePortfolio(String portfolioName, String filePath) {
      try {
        return true;
      } catch (Exception e) {
        return false;
      }
    }

    @Override
    public void quit() {
      System.exit(0);
    }
  }
}
