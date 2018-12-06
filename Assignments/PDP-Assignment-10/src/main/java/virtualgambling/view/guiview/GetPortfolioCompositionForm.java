package virtualgambling.view.guiview;

import java.awt.event.ActionListener;
import java.util.Optional;

import javax.swing.JTextField;

import virtualgambling.controller.Features;
import virtualgambling.model.bean.Portfolio;

/**
 * This class represents a GUI form to get the composition of a portfolio. It extends {@link
 * CreatePortfolioForm} to reduce the effort in implementing the common functionality.
 */
public class GetPortfolioCompositionForm extends CreatePortfolioForm {

  /**
   * Constructs a object of GetPortfolioCompositionForm with the given params.
   *
   * @param mainForm the mainForm
   * @param features the features
   * @throws IllegalArgumentException if the given params are null
   */
  public GetPortfolioCompositionForm(MainForm mainForm, Features features)
          throws IllegalArgumentException {

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
