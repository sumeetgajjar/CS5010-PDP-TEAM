package virtualgambling.view.guiview;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;

/**
 * Created by gajjar.s, on 11:43 PM, 12/4/18
 */
public class GetPortfolioCostBasisForm extends AbstractForm {

  private final MainForm mainForm;

  public GetPortfolioCostBasisForm(MainForm mainForm) throws HeadlessException {
    super(mainForm);
    this.mainForm = mainForm;
  }

  @Override
  protected void initComponents() {

    this.setLayout(new GridLayout(3, 1));

    JPanel portfolioPanel = new JPanel();
    JLabel portfolioLabel = new JLabel("Please enter the name of the Portfolio");
    portfolioPanel.add(portfolioLabel);

    JTextField portfolioTextField = new JTextField(10);
    portfolioPanel.add(portfolioTextField);

    JPanel datePanel = new JPanel();
    JLabel dateLabel = new JLabel("Please enter the date (YYYY-MM-DD)");
    datePanel.add(dateLabel);

    JTextField dateTextField = new JTextField(10);
    datePanel.add(dateTextField);

    ActionListener actionListener = getActionListenerForCreatePortfolio(
            portfolioTextField,
            dateTextField);

    JPanel buttonJPanel = this.getActionButtonJPanel(actionListener);

    this.addJFrameClosingEvent();

    this.add(portfolioPanel);
    this.add(datePanel);
    this.add(buttonJPanel);
  }

  @Override
  protected void appendOutput(String message) {
    this.mainForm.appendOutput(message);
  }

  private ActionListener getActionListenerForCreatePortfolio(JTextField portfolioTextField,
                                                             JTextField dateTextField) {
    return e -> {
      String portfolioName = portfolioTextField.getText();
      if (portfolioName.isEmpty()) {
        this.showError("Portfolio Name cannot be empty");
        return;
      }

      String date = dateTextField.getText();
      if (date.isEmpty()) {
        this.showError("Date cannot be empty");
        return;
      }

      //todo insert command here

      this.appendOutput(portfolioName);
      this.appendOutput(date);
      this.showPrevious();
    };
  }

  public static void main(String[] args) {
    new GetPortfolioCostBasisForm(null).setVisible(true);
  }
}
