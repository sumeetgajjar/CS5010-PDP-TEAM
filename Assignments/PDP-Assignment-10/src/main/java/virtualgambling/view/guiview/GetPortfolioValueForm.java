package virtualgambling.view.guiview;

import java.awt.*;
import java.util.Date;

import virtualgambling.controller.Features;

/**
 * Created by gajjar.s, on 3:13 AM, 12/5/18
 */
public class GetPortfolioValueForm extends GetPortfolioCostBasisForm {

  public GetPortfolioValueForm(MainForm mainForm, Features features) throws HeadlessException {
    super(mainForm, features);
  }

  @Override
  protected void executeFeature(String portfolioName, Date date) {
    this.features.getPortfolioValue(portfolioName, date);
  }
}
