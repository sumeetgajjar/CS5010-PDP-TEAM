package virtualgambling.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import virtualgambling.model.EnhancedUserModel;
import virtualgambling.model.bean.Portfolio;
import virtualgambling.model.bean.SharePurchaseOrder;
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
    this.guiView.addFeatures(new FeaturesImpl(enhancedUserModel));
  }

  private static class FeaturesImpl implements Features {

    private final EnhancedUserModel enhancedUserModel;

    private FeaturesImpl(EnhancedUserModel enhancedUserModel) {
      this.enhancedUserModel = enhancedUserModel;
    }

    @Override
    public void createPortfolio(String portfolio) {
      this.createPortfolio(portfolio);
    }

    @Override
    public List<Portfolio> getAllPortfolios() {
      return this.enhancedUserModel.getAllPortfolios();
    }

    @Override
    public BigDecimal getPortfolioCostBasis(String portfolio, Date date) {
      return this.enhancedUserModel.getPortfolio(portfolio).getCostBasisIncludingCommission(date);
    }

    @Override
    public BigDecimal getPortfolioValue(String portfolio, Date date) {
      return this.enhancedUserModel.getPortfolio(portfolio).getValue(date);
    }

    @Override
    public BigDecimal getRemainingCapital() {
      return this.enhancedUserModel.getRemainingCapital();
    }

    @Override
    public SharePurchaseOrder buyShares(String tickerName,
                                        String portfolioName,
                                        Date date,
                                        long quantity,
                                        double commissionPercentage) {

      return this.enhancedUserModel.buyShares(tickerName, portfolioName, date, quantity,
              commissionPercentage);
    }

    @Override
    public void quit() {
      System.exit(0);
    }
  }
}
