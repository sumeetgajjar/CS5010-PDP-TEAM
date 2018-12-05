package virtualgambling.view.guiview;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Optional;

import javax.swing.*;

import virtualgambling.controller.Features;
import virtualgambling.model.bean.Portfolio;

/**
 * Created by gajjar.s, on 11:43 PM, 12/4/18
 */
public class GetPorfolioCompositionForm extends CreatePortfolioForm {

  public GetPorfolioCompositionForm(MainForm mainForm, Features features) throws HeadlessException {
    super(mainForm, features);
  }

  @Override
  protected ActionListener getActionListenerForCreatePortfolio(JTextField portfolioNameJTextField) {
    return e -> {
      if (this.isPortfolioNameTextFieldEmpty(portfolioNameJTextField)) {
        return;
      }

      String portfolioName = portfolioNameJTextField.getText();
      Optional<Portfolio> optional = this.features.getPortfolioComposition(portfolioName);
      if (optional.isPresent()) {
        this.mainForm.display(optional.get().toString());
        this.showPrevious();
      }
    };

  }
}
