package virtualgambling.view.guiview;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.math.BigDecimal;
import java.util.Objects;

import javax.swing.*;

import virtualgambling.controller.Features;

/**
 * Created by gajjar.s, on 11:43 PM, 12/4/18
 */
public class LoadAndExecuteStrategyForm extends AbstractForm {

  protected final MainForm mainForm;
  protected final Features features;
  private JLabel filePathLabel;
  private File selectedFile;

  public LoadAndExecuteStrategyForm(MainForm mainForm, Features features) throws HeadlessException {
    super(mainForm);
    this.mainForm = mainForm;
    this.features = features;
  }

  @Override
  protected void initComponents() {
    JPanel container = new JPanel();
    container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

    JPanel jPanel = new JPanel();
    jPanel.setLayout(new GridLayout(4, 2));

    JLabel portfolioLabel = new JLabel("Portfolio Name:");
    jPanel.add(portfolioLabel);
    JTextField portfolioTextField = new JTextField(10);
    jPanel.add(portfolioTextField);

    JLabel amountToInvestLabel = new JLabel("Amount To Invest (Dollars):");
    jPanel.add(amountToInvestLabel);
    JTextField amountToInvestTextField = new JTextField(10);
    jPanel.add(amountToInvestTextField);

    JLabel commissionLabel = new JLabel("Commission percentage:");
    jPanel.add(commissionLabel);
    JTextField commissionTextField = new JTextField(10);
    jPanel.add(commissionTextField);

    jPanel.add(new JLabel("Strategy File:"));
    JButton selectPortfolioFileButton = new JButton("Select Strategy file");
    selectPortfolioFileButton.addActionListener(this.getSelectPortfolioFileActionListener());
    jPanel.add(selectPortfolioFileButton);

    container.add(jPanel);

    this.filePathLabel = new JLabel();
    filePathLabel.setPreferredSize(new Dimension(600, 20));
    JPanel filePathPanel = new JPanel();
    filePathPanel.add(this.filePathLabel);
    container.add(filePathPanel);

    ActionListener actionListener = getActionListenerForCreatePortfolio(portfolioTextField,
            amountToInvestTextField,
            commissionTextField);
    JPanel buttonJPanel = this.getActionButtonJPanel(actionListener);

    container.add(new JSeparator(SwingConstants.HORIZONTAL));
    container.add(buttonJPanel);

    this.add(container);
  }

  private ActionListener getSelectPortfolioFileActionListener() {
    return e -> {
      this.selectedFile = getFileToLoad();
      if (Objects.nonNull(this.selectedFile)) {
        this.filePathLabel.setText(String.format("Strategy File Path: %s",
                selectedFile.getAbsolutePath()));
      }
    };
  }

  private ActionListener getActionListenerForCreatePortfolio(JTextField portfolioTextField,
                                                             JTextField amountToInvestTextField,
                                                             JTextField commissionTextField) {
    return e -> {
      if (this.areInputsEmpty(portfolioTextField, amountToInvestTextField, commissionTextField)) {
        return;
      }

      String portfolioName = portfolioTextField.getText();
      BigDecimal amountToInvest = getBigDecimalFromTextField(amountToInvestTextField,
              this.mainForm::displayError);

      if (Objects.isNull(amountToInvest)) {
        return;
      }

      Double commissionPercentage = getDoubleFromTextField(commissionTextField,
              this.mainForm::displayError);
      if (Objects.isNull(commissionPercentage)) {
        return;
      }

      if (Objects.isNull(this.selectedFile)) {
        this.mainForm.displayError("Please select the file for loading Strategy");
        return;
      }

      boolean success = this.features.loadAndExecuteStrategy(portfolioName,
              selectedFile.getAbsolutePath(), amountToInvest, commissionPercentage);

      if (success) {
        this.showPrevious();
        this.mainForm.display("Strategy loaded Successfully");
      }
    };
  }

  private boolean areInputsEmpty(JTextField portfolioTextField,
                                 JTextField amountToInvestTextField,
                                 JTextField commissionTextField) {

    return isPortfolioNameTextFieldEmpty(portfolioTextField) ||
            isAmountToInvestTextFieldEmpty(amountToInvestTextField) ||
            isCommissionTextFieldEmpty(commissionTextField);
  }
}