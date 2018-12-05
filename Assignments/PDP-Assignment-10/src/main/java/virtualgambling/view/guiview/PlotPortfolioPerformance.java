package virtualgambling.view.guiview;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Objects;

import javax.swing.*;

import virtualgambling.controller.Features;

/**
 * Created by gajjar.s, on 4:06 AM, 12/5/18
 */
public class PlotPortfolioPerformance extends AbstractForm {

  private final Features features;

  protected PlotPortfolioPerformance(JFrame previousJFrame, Features features) {
    super(previousJFrame);
    this.features = features;
  }

  @Override
  protected void initComponents() {

    this.setLayout(new FlowLayout());

    JPanel leftPanel = new JPanel(new GridLayout(3, 2));

    JLabel portfolioLabel = new JLabel("Portfolio Name");
    leftPanel.add(portfolioLabel);

    JTextField portfolioTextField = new JTextField(10);
    leftPanel.add(portfolioTextField);

    JLabel startDate = new JLabel("Start date (YYYY-MM-DD)");
    leftPanel.add(startDate);

    JTextField startDateTextField = new JTextField(10);
    leftPanel.add(startDateTextField);

    JLabel endDate = new JLabel("End date (YYYY-MM-DD)");
    leftPanel.add(endDate);

    JTextField endDateTextField = new JTextField(10);
    leftPanel.add(endDateTextField);

    ActionListener actionListener = getActionListenerForCreatePortfolio(
            portfolioTextField,
            startDateTextField,
            endDateTextField);

    JPanel buttonJPanel = this.getActionButtonJPanel(actionListener);

    JPanel rightPanel = new JPanel();
    this.add(leftPanel);
    this.add(rightPanel);
    this.add(buttonJPanel);
  }

  private ActionListener getActionListenerForCreatePortfolio(JTextField portfolioTextField,
                                                             JTextField startDateTextField,
                                                             JTextField endDateTextField) {
    return e -> {

      if (this.areInputsEmpty(portfolioTextField, startDateTextField, endDateTextField)) {
        return;
      }

      String portfolioName = portfolioTextField.getText();
      Date startDate = this.getDateFromTextField(startDateTextField, this::showError);
      if (Objects.isNull(startDate)) {
        return;
      }

      Date endDate = this.getDateFromTextField(endDateTextField, this::showError);
      if (Objects.isNull(endDate)) {
        return;
      }

      this.plotGraph();
      //todo insert command here
    };
  }

  private boolean areInputsEmpty(JTextField portfolioTextField,
                                 JTextField startDateTextField,
                                 JTextField endDateTextField) {

    return isPortfolioNameTextFieldEmpty(portfolioTextField) ||
            isStartDateTextFieldEmpty(startDateTextField) ||
            isEndDateTextFieldEmpty(endDateTextField);
  }

  private void plotGraph() {

  }
}
