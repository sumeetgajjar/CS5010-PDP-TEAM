package virtualgambling.view.guiview;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import util.Utils;
import virtualgambling.controller.Features;

/**
 * This class represents a GUI form to Persists a Strategy with different weights. It extends {@link
 * AbstractForm} to reduce the effort in implementing the common functionality.
 */
public class PersistRecurrentStrategyWithDifferentWeightsForm extends AbstractForm {

  protected final MainForm mainForm;
  protected final Features features;
  protected final Map<String, Double> stockPercentageMap;

  protected File selectedFile;
  protected JTextField stockPercentageJTextField;
  protected JLabel stockPercentageJLabel;

  private JLabel filePathLabel;
  private JButton addStockJButton;
  private JTextField stockNameJTextField;
  private JTextArea stocksJTextArea;

  /**
   * Constructs a object of PersistRecurrentStrategyWithDifferentWeightsForm with the given params.
   *
   * @param mainForm the mainForm
   * @param features the features
   * @throws IllegalArgumentException if the given params are null
   */
  public PersistRecurrentStrategyWithDifferentWeightsForm(MainForm mainForm, Features features)
          throws IllegalArgumentException {

    super(mainForm);
    this.mainForm = Utils.requireNonNull(mainForm);
    this.features = Utils.requireNonNull(features);
    this.stockPercentageMap = new LinkedHashMap<>();
    this.addActionListenerToAddStockButton();
    this.setTitle("Persist Strategy");
  }

  @Override
  protected void initComponents() {
    JPanel container = new JPanel();
    container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

    JPanel jPanel1 = new JPanel();
    jPanel1.add(new JLabel("Leave End date field blank if there is no end date"));
    container.add(jPanel1);

    JPanel jPanel = new JPanel();
    jPanel.setLayout(new GridLayout(3, 2));

    JLabel startDateLabel = new JLabel("Start date (YYYY-MM-DD):");
    jPanel.add(startDateLabel);
    JTextField startDateTextField = new JTextField(10);
    jPanel.add(startDateTextField);

    JLabel endDateLabel = new JLabel("End date (YYYY-MM-DD):");
    jPanel.add(endDateLabel);
    JTextField endDateTextField = new JTextField(10);
    jPanel.add(endDateTextField);

    JLabel recurringIntervalLabel = new JLabel("Recurring Interval (Days):");
    jPanel.add(recurringIntervalLabel);
    JTextField recurringIntervalDaysTextField = new JTextField(10);
    jPanel.add(recurringIntervalDaysTextField);

    container.add(jPanel);

    stocksJTextArea = new JTextArea();
    stocksJTextArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
    stocksJTextArea.setEditable(false);

    JPanel stocksAdditionPanel = new JPanel();
    stocksAdditionPanel.add(new JLabel("Ticker Name:"));
    stockNameJTextField = new JTextField(10);
    stocksAdditionPanel.add(stockNameJTextField);

    stockPercentageJLabel = new JLabel("Stock Percentage:");
    stocksAdditionPanel.add(stockPercentageJLabel);
    stockPercentageJTextField = new JTextField(10);
    stocksAdditionPanel.add(stockPercentageJTextField);

    addStockJButton = new JButton("Add Stock");

    stocksAdditionPanel.add(addStockJButton);
    container.add(stocksAdditionPanel);


    JButton selectStrategyFileButton = new JButton("Select Strategy file destination");
    selectStrategyFileButton.addActionListener(this.getSelectStrategyFileActionListener());

    JPanel fileChooserPanel = new JPanel();
    fileChooserPanel.setLayout(new GridLayout(2, 1));
    fileChooserPanel.add(selectStrategyFileButton);

    JScrollPane outputJPanel = new JScrollPane(stocksJTextArea);
    outputJPanel.setPreferredSize(new Dimension(400, 200));
    container.add(outputJPanel);

    this.filePathLabel = new JLabel(PORTFOLIO_FILE_LABEL_TEXT);

    filePathLabel.setPreferredSize(new Dimension(600, 20));
    fileChooserPanel.add(this.filePathLabel);
    container.add(fileChooserPanel);

    container.add(this.getActionButtonJPanel(
            getActionListenerForExecuteButton(
                    startDateTextField,
                    endDateTextField,
                    recurringIntervalDaysTextField)));

    this.add(container);
  }

  protected boolean executeFeature(Date startDate,
                                   JTextField endDateTextField,
                                   int dayFrequency) {

    boolean success;
    if (endDateTextField.getText().isEmpty()) {
      success = this.features.saveStrategy(this.selectedFile.getAbsolutePath(), startDate,
              dayFrequency,
              this.stockPercentageMap);
    } else {
      Date endDate = getDateFromTextField(endDateTextField, this.mainForm::displayError);
      if (Objects.isNull(endDate)) {
        return false;
      }
      success = this.features.saveStrategy(this.selectedFile.getAbsolutePath(), startDate,
              endDate,
              dayFrequency,
              this.stockPercentageMap);
    }
    return success;
  }

  protected ActionListener getActionListenerForAddStockButton(JTextArea stocksJTextArea,
                                                              JTextField stockNameJTextField,
                                                              JTextField stockPercentageJTextField) {
    return getActionListenerForAddStockButtonForDifferentWeight(
            stocksJTextArea,
            stockNameJTextField,
            stockPercentageJTextField,
            this.stockPercentageMap,
            this.mainForm::displayError);
  }

  private ActionListener getActionListenerForExecuteButton(
          JTextField startDateTextField,
          JTextField endDateTextField,
          JTextField recurringIntervalDaysTextField) {

    return e -> {
      if (this.areInputsEmpty(startDateTextField, recurringIntervalDaysTextField)) {
        return;
      }

      Date startDate = getDateFromTextField(startDateTextField, this.mainForm::displayError);
      if (Objects.isNull(startDate)) {
        return;
      }

      Integer dayFrequency = getIntegerFromTextField(recurringIntervalDaysTextField,
              this.mainForm::displayError);
      if (Objects.isNull(dayFrequency)) {
        return;
      }

      if (Objects.isNull(this.selectedFile)) {
        this.mainForm.displayError("Please select the file for saving strategy");
        return;
      }

      boolean success = executeFeature(startDate, endDateTextField, dayFrequency);
      if (success) {
        this.mainForm.display("Strategy saved successfully");
        this.showPrevious();
      }
    };
  }

  private ActionListener getSelectStrategyFileActionListener() {
    return e -> {
      this.selectedFile = getFileToSaveUsingFileChooser();
      if (Objects.nonNull(this.selectedFile)) {
        this.filePathLabel.setText(String.format("%s%s", PORTFOLIO_FILE_LABEL_TEXT,
                selectedFile.getAbsolutePath()));
      }
    };

  }

  private boolean areInputsEmpty(JTextField startDateTextField,
                                 JTextField recurringIntervalDaysTextField) {

    return isStartDateTextFieldEmpty(startDateTextField) ||
            isRecurringIntervalTextFieldEmpty(recurringIntervalDaysTextField);
  }

  private void addActionListenerToAddStockButton() {
    addStockJButton.addActionListener(
            this.getActionListenerForAddStockButton(
                    stocksJTextArea,
                    stockNameJTextField,
                    stockPercentageJTextField));
  }
}
