package virtualgambling.view.guiview;

import java.awt.*;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.Date;

import javax.swing.*;

import util.Utils;
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

    this.addJFrameClosingEvent();

    JPanel rightPanel = new JPanel();
    this.add(leftPanel);
    this.add(rightPanel);
    this.add(buttonJPanel);
  }

  private ActionListener getActionListenerForCreatePortfolio(JTextField portfolioTextField,
                                                             JTextField startDateTextField,
                                                             JTextField endDateTextField) {
    return e -> {
      if (this.isTextFieldEmpty(portfolioTextField)) {
        this.showError("Portfolio Name cannot be empty");
        return;
      }

      if (this.isTextFieldEmpty(startDateTextField)) {
        this.showError("Date cannot be empty");
        return;
      }

      if (this.isTextFieldEmpty(endDateTextField)) {
        this.showError("Date cannot be empty");
        return;
      }

      String startDateString = startDateTextField.getText();
      Date startDate;
      try {
        startDate = Utils.getDateFromDefaultFormattedDateString(startDateString);
      } catch (ParseException e1) {
        this.showError(String.format("Invalid Start Date: %s", startDateString));
        return;
      }

      String endDateString = startDateTextField.getText();
      Date endDate;
      try {
        endDate = Utils.getDateFromDefaultFormattedDateString(endDateString);
      } catch (ParseException e1) {
        this.showError(String.format("Invalid End Date: %s", endDateString));
        return;
      }

      this.plotGraph();
      //todo insert command here
    };
  }

  private void plotGraph() {

  }
}
