package virtualgambling.view.guiview;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import util.Utils;
import virtualgambling.controller.TradingFeatures;

/**
 * This class represents a GUI form to get the value of a portfolio. It extends {@link
 * GetPortfolioCostBasisForm} to reduce the effort in implementing the common functionality.
 */
public class GetPortfolioValueForm extends GetPortfolioCostBasisForm {

  /**
   * Constructs a object of GetPortfolioValueForm with the given params.
   *
   * @param mainForm the mainForm
   * @param tradingFeatures the tradingFeatures
   * @throws IllegalArgumentException if the given params are null
   */
  public GetPortfolioValueForm(MainForm mainForm, TradingFeatures tradingFeatures)
          throws IllegalArgumentException {

    super(mainForm, tradingFeatures);
    this.setTitle("Portfolio Value");
  }

  @Override
  protected String getPrefix(String portfolioName, Date date) {
    return "Value of portfolio '" + portfolioName + "' on '"
            + Utils.getDefaultFormattedDateStringFromDate(date) + "'";
  }

  @Override
  protected Optional<BigDecimal> executeFeature(String portfolioName, Date date) {
    return this.tradingFeatures.getPortfolioValue(portfolioName, date);
  }
}
