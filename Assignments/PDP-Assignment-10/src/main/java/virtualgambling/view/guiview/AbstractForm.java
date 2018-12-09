package virtualgambling.view.guiview;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;

import util.Utils;
import virtualgambling.model.bean.SharePurchaseOrder;

/**
 * This class represents a abstract class {@link AbstractForm} which extends {@link JFrame}. It
 * minimize the effort required to take input from the User and validate the input.
 */
public abstract class AbstractForm extends JFrame {

  protected static final String EMPTY_TICKER_NAME_MESSAGE = "Ticker Name cannot be empty";
  protected static final String EMPTY_PORTFOLIO_NAME_MESSAGE = "Portfolio Name cannot be empty";
  protected static final String EMPTY_DATE_STRING_MESSAGE = "Date cannot be empty";
  protected static final String EMPTY_QUANTITY_STRING_MESSAGE = "Quantity cannot be empty";
  protected static final String EMPTY_COMMISSION_STRING_MESSAGE = "Commission cannot be empty";
  protected static final String EMPTY_END_DATE_ERROR_MESSAGE = "End date cannot be empty";
  protected static final String EMPTY_RECURRING_INTERVAL_ERROR_MESSAGE = "Recurring Interval " +
          "cannot be empty";
  protected static final String EMPTY_AMOUNT_TO_INVEST_ERROR_MESSAGE = "Amount to invest cannot " +
          "be empty";
  protected static final String EMPTY_START_DATE_ERROR_MESSAGE = "Start date cannot be empty";
  protected static final String PORTFOLIO_FILE_LABEL_TEXT = "Portfolio File Path:";

  protected final JFrame previousJFrame;
  protected final JFrame currentJFrame;

  /**
   * Protected Constructor to be used by the Derived classes.
   *
   * @param previousJFrame the reference of the previous JFrame
   */
  protected AbstractForm(JFrame previousJFrame) {
    this.previousJFrame = previousJFrame;
    this.currentJFrame = this;
    this.initComponents();
    this.postInit();
  }

  protected abstract void initComponents();

  protected void postInit() {
    this.addJFrameClosingEvent();
    this.pack();
    this.centerThisJFrame();
  }

  protected JPanel getActionButtonJPanel(ActionListener executeButtonAction) {
    JPanel buttonJPanel = new JPanel();
    buttonJPanel.setLayout(new BoxLayout(buttonJPanel, BoxLayout.X_AXIS));

    JButton executeJButton = new JButton("Execute");
    executeJButton.addActionListener(executeButtonAction);

    JButton cancelJButton = new JButton("Cancel");
    cancelJButton.addActionListener(e -> this.showPrevious());

    buttonJPanel.add(executeJButton);
    buttonJPanel.add(cancelJButton);
    return buttonJPanel;
  }

  protected void showError(String message) {
    Utils.displayError(this, message);
  }

  protected void addJFrameClosingEvent() {

    this.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        showPrevious();
      }
    });
  }

  protected void showPrevious() {
    Utils.showPrevious(previousJFrame, currentJFrame);
  }

  protected void centerThisJFrame() {
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    int x = (int) (dim.getWidth() / 2 - this.getSize().getWidth() / 2);
    int y = (int) (dim.getHeight() / 2 - this.getSize().getHeight() / 2);
    this.setLocation(x, y);
  }

  protected boolean isTextFieldEmpty(JTextField jTextField) {
    String input = jTextField.getText();
    return input.isEmpty();
  }

  protected Date getDateFromTextField(JTextField jTextField,
                                      Consumer<String> displayErrorConsumer) {
    String dateString = jTextField.getText();
    Date date = null;
    try {
      date = Utils.getDateFromDefaultFormattedDateString(dateString);
    } catch (ParseException e1) {
      displayErrorConsumer.accept(String.format("Invalid Date Format: %s", dateString));
    }

    return date;
  }

  protected Long getLongFromTextField(JTextField jTextField,
                                      Consumer<String> displayErrorConsumer) {
    String longString = jTextField.getText();
    Long number = null;
    try {
      number = Long.parseLong(longString);
    } catch (NumberFormatException e) {
      displayErrorConsumer.accept(String.format("Unparseable Number, %s", e.getMessage()));
    }
    return number;
  }

  protected BigDecimal getBigDecimalFromTextField(JTextField jTextField,
                                                  Consumer<String> displayErrorConsumer) {
    String bigDecimalString = jTextField.getText();
    BigDecimal number = null;
    try {
      number = new BigDecimal(bigDecimalString);
    } catch (NumberFormatException e) {
      displayErrorConsumer.accept(String.format("Unparseable Number, %s", e.getMessage()));
    }
    return number;
  }

  protected Integer getIntegerFromTextField(JTextField jTextField,
                                            Consumer<String> displayErrorConsumer) {
    String integerString = jTextField.getText();
    Integer number = null;
    try {
      number = Integer.parseInt(integerString);
    } catch (NumberFormatException e) {
      displayErrorConsumer.accept(String.format("Unparseable Number, %s", e.getMessage()));
    }
    return number;
  }

  protected Double getDoubleFromTextField(JTextField jTextField,
                                          Consumer<String> displayErrorConsumer) {

    String commissionString = jTextField.getText();
    Double number = null;
    try {
      number = Double.parseDouble(commissionString);
    } catch (NumberFormatException e) {
      displayErrorConsumer.accept(String.format("Unparseable Decimal, %s", e.getMessage()));
    }
    return number;
  }

  protected File getFileToLoadUsingFileChooser() {
    JFileChooser jFileChooser =
            new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

    jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    jFileChooser.setApproveButtonText("Open");

    int returnValue = jFileChooser.showOpenDialog(this);

    if (returnValue == JFileChooser.APPROVE_OPTION) {
      return jFileChooser.getSelectedFile();
    }
    return null;
  }

  protected File getFileToSaveUsingFileChooser() {
    JFileChooser jFileChooser =
            new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

    jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    jFileChooser.setApproveButtonText("Save");

    int returnValue = jFileChooser.showSaveDialog(this);

    if (returnValue == JFileChooser.APPROVE_OPTION) {
      return jFileChooser.getSelectedFile();
    }
    return null;
  }

  protected boolean isPortfolioNameTextFieldEmpty(JTextField portfolioNameJTextField) {
    if (this.isTextFieldEmpty(portfolioNameJTextField)) {
      this.showError(AbstractForm.EMPTY_PORTFOLIO_NAME_MESSAGE);
      return true;
    }

    return false;
  }

  protected boolean isTickerNameTextFieldEmpty(JTextField tickerNameJTextField) {
    if (this.isTextFieldEmpty(tickerNameJTextField)) {
      this.showError(AbstractForm.EMPTY_TICKER_NAME_MESSAGE);
      return true;
    }

    return false;
  }

  protected boolean isDateTextFieldEmpty(JTextField dateJTextField) {
    if (this.isTextFieldEmpty(dateJTextField)) {
      this.showError(AbstractForm.EMPTY_DATE_STRING_MESSAGE);
      return true;
    }

    return false;
  }

  protected boolean isStartDateTextFieldEmpty(JTextField startDateJTextField) {
    if (this.isTextFieldEmpty(startDateJTextField)) {
      this.showError(AbstractForm.EMPTY_START_DATE_ERROR_MESSAGE);
      return true;
    }

    return false;
  }

  protected boolean isEndDateTextFieldEmpty(JTextField endDateJTextField) {
    if (this.isTextFieldEmpty(endDateJTextField)) {
      this.showError(AbstractForm.EMPTY_END_DATE_ERROR_MESSAGE);
      return true;
    }

    return false;
  }

  protected boolean isRecurringIntervalTextFieldEmpty(JTextField recurringIntervalJTextField) {
    if (this.isTextFieldEmpty(recurringIntervalJTextField)) {
      this.showError(AbstractForm.EMPTY_RECURRING_INTERVAL_ERROR_MESSAGE);
      return true;
    }

    return false;
  }

  protected boolean isAmountToInvestTextFieldEmpty(JTextField recurringIntervalJTextField) {
    if (this.isTextFieldEmpty(recurringIntervalJTextField)) {
      this.showError(AbstractForm.EMPTY_AMOUNT_TO_INVEST_ERROR_MESSAGE);
      return true;
    }

    return false;
  }

  protected boolean isQuantityTextFieldEmpty(JTextField quantityJTextField) {
    if (this.isTextFieldEmpty(quantityJTextField)) {
      this.showError(AbstractForm.EMPTY_QUANTITY_STRING_MESSAGE);
      return true;
    }

    return false;
  }

  protected boolean isCommissionTextFieldEmpty(JTextField commissionJTextField) {
    if (this.isTextFieldEmpty(commissionJTextField)) {
      this.showError(AbstractForm.EMPTY_COMMISSION_STRING_MESSAGE);
      return true;
    }

    return false;
  }

  protected void displaySharePurchaseOrder(Optional<List<SharePurchaseOrder>> sharePurchaseOrders
          , Consumer<String> consumer) {
    if (sharePurchaseOrders.isPresent()) {
      List<SharePurchaseOrder> list = sharePurchaseOrders.get();
      for (SharePurchaseOrder sharePurchaseOrder : list) {
        consumer.accept(sharePurchaseOrder.toString());
      }
      this.showPrevious();
    }
  }

  protected ActionListener getActionListenerForAddStockButtonForDifferentWeight(
          JTextArea stocksJTextArea,
          JTextField stockNameJTextField,
          JTextField stockPercentageJTextField,
          Map<String, Double> stockPercentageMap,
          Consumer<String> errorMessageConsumer) {

    return e -> {
      if (this.isTickerNameTextFieldEmpty(stockNameJTextField)) {
        return;
      }

      String tickerName = stockNameJTextField.getText();
      Double stockPercentage = getDoubleFromTextField(stockPercentageJTextField,
              errorMessageConsumer);
      if (Objects.isNull(stockPercentage)) {
        return;
      }

      stockPercentageMap.put(tickerName, stockPercentage);

      StringBuilder builder = new StringBuilder();
      for (Map.Entry<String, Double> entry : stockPercentageMap.entrySet()) {
        builder.append(String.format("%s:%s %%", entry.getKey(), entry.getValue()));
        builder.append(System.lineSeparator());
      }

      stocksJTextArea.setText(builder.toString());
    };
  }

  protected ActionListener getActionListenerForAddStockButtonForSameWeight(
          JTextArea stocksJTextArea,
          JTextField stockNameJTextField,
          Map<String, Double> stockPercentageMap) {

    return e -> {
      if (this.isTickerNameTextFieldEmpty(stockNameJTextField)) {
        return;
      }

      String tickerName = stockNameJTextField.getText();

      stockPercentageMap.put(tickerName, 0D);

      StringBuilder builder = new StringBuilder();
      for (Map.Entry<String, Double> entry : stockPercentageMap.entrySet()) {
        builder.append(entry.getKey());
        builder.append(System.lineSeparator());
      }

      stocksJTextArea.setText(builder.toString());
    };
  }
}
