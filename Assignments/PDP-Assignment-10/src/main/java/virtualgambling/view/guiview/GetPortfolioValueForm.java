package virtualgambling.view.guiview;

import java.awt.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import virtualgambling.controller.Features;

/**
 * Created by gajjar.s, on 3:13 AM, 12/5/18
 */
public class GetPortfolioValueForm extends GetPortfolioCostBasisForm {

  public GetPortfolioValueForm(MainForm mainForm, Features features) throws HeadlessException {
    super(mainForm, features);
  }

  @Override
  protected Optional<BigDecimal> executeFeature(String portfolioName, Date date) {
    return this.features.getPortfolioValue(portfolioName, date);
  }
}
