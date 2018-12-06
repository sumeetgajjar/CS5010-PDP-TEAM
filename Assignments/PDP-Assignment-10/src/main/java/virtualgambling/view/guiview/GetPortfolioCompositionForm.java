package virtualgambling.view.guiview;

import java.awt.HeadlessException;
import java.awt.event.ActionListener;
import java.util.Optional;

import javax.swing.JTextField;

import virtualgambling.controller.Features;
import virtualgambling.model.bean.Portfolio;

/**
 * Created by gajjar.s, on 11:43 PM, 12/4/18
 */
public class GetPortfolioCompositionForm extends CreatePortfolioForm {

  public GetPortfolioCompositionForm(MainForm mainForm, Features features) throws HeadlessException {
    super(mainForm, features);
    this.setTitle("Portfolio Composition");
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
        this.mainForm.display(
                String.format("Portfolio Composition of portfolio '%s'", portfolioName));
        this.mainForm.display(optional.get().toString());
        this.showPrevious();
      }
    };

  }
}
