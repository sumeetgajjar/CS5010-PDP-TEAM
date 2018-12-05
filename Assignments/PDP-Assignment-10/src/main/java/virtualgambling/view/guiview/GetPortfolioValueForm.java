package virtualgambling.view.guiview;

import java.awt.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import util.Utils;
import virtualgambling.controller.Features;

/**
 * Created by gajjar.s, on 3:13 AM, 12/5/18
 */
public class GetPortfolioValueForm extends GetPortfolioCostBasisForm {

  private final MainForm mainForm;

  public GetPortfolioValueForm(MainForm mainForm, Features features) throws HeadlessException {
    super(mainForm, features);
    this.mainForm = mainForm;
  }

  @Override
  protected void executeFeature(String portfolioName, Date date) {
    Optional<BigDecimal> portfolioValue = this.features.getPortfolioValue(portfolioName, date);
    portfolioValue.ifPresent(bigDecimal -> {
      String numberString = Utils.getFormattedCurrencyNumberString(bigDecimal);
      this.mainForm.display(numberString);
    });
  }
}
