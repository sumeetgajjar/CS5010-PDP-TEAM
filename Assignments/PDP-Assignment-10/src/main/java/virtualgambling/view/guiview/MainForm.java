package virtualgambling.view.guiview;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import util.Utils;
import virtualgambling.controller.TradingFeatures;
import virtualgambling.model.bean.Portfolio;
import virtualgambling.model.factory.StockDataSourceType;

/**
 * This class represents a the GUI view of this MVC application. It extends {@link AbstractForm} to
 * reduce the effort in implementing the common functionality and implements {@link GUIView}
 * interface.
 */
public class MainForm extends AbstractForm implements GUIView {

  private JTextArea jTextArea;
  private TradingFeatures tradingFeatures;

  /**
   * Constructs a object of MainForm.
   */
  public MainForm() {
    super(null);
    this.display("Welcome to Virtual Trading");
    this.setVisible(true);
    this.setTitle("Virtual Trading");
  }

  @Override
  public void addFeatures(TradingFeatures tradingFeatures) {
    this.tradingFeatures = tradingFeatures;
  }

  @Override
  public void displayError(String message) {
    this.showError(message);
  }

  @Override
  public String getInput() {
    return Utils.getInput(this);
  }

  @Override
  public void display(String text) {
    StringBuilder separator = new StringBuilder();
    for (int i = 0; i < 123; i++) {
      separator.append("-");
    }

    this.jTextArea.append(text);
    this.jTextArea.append(System.lineSeparator());
    this.jTextArea.append(separator.toString());
    this.jTextArea.append(System.lineSeparator());
    this.jTextArea.setCaretPosition(jTextArea.getDocument().getLength());
    this.jTextArea.append(System.lineSeparator());
  }

  @Override
  protected void initComponents() {
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLayout(new FlowLayout());

    JPanel outputPanel = new JPanel();
    outputPanel.setLayout(new BoxLayout(outputPanel, BoxLayout.Y_AXIS));

    JPanel buttonJPanel = new JPanel();
    this.addButtonsToPanel(buttonJPanel);
    buttonJPanel.setPreferredSize(new Dimension(300, 800));
    this.add(buttonJPanel);

    this.add(new JSeparator(SwingConstants.VERTICAL));

    this.jTextArea = getJTextArea();
    JScrollPane scrollPane = new JScrollPane(this.jTextArea);
    scrollPane.setPreferredSize(new Dimension(1000, 750));
    scrollPane.add(new JSeparator(JSeparator.HORIZONTAL));
    outputPanel.add(scrollPane);

    JPanel clearInputPanel = new JPanel();
    JButton clearButton = new JButton("Clear Output");
    clearButton.addActionListener(e -> this.jTextArea.setText(null));
    clearInputPanel.add(clearButton);

    outputPanel.add(clearInputPanel);

    this.add(outputPanel);
  }

  @Override
  protected void addJFrameClosingEvent() {
    //closing event should not be added to this MainForm since there is no previous form to go to.
  }

  private void addButtonsToPanel(JPanel buttonJPanel) {
    List<JButton> buttons = new ArrayList<>();
    buttons.add(getCreatePortfolioJButton());
    buttons.add(getGetAllPortfolioJButton());
    buttons.add(getPortfolioCostBasisButton());
    buttons.add(getPortfolioCompositionButton());
    buttons.add(getPortfolioValueButton());
    buttons.add(getRemainingCapital());
    buttons.add(getDrawPortfolioPerformanceButton());
    buttons.add(getBuySharesButton());
    buttons.add(getBuySharesUsingStrategyButton());
    buttons.add(getLoadPortfolioButton());
    buttons.add(getSavePortfolioButton());
    buttons.add(getLoadAndExecuteStrategyButton());
    buttons.add(getCreateStrategyAndPersistButton());
    buttons.add(getSelectDataSourceButton());
    buttons.add(getQuitJButton());

    buttonJPanel.setLayout(new GridLayout(buttons.size(), 1));
    buttons.forEach(buttonJPanel::add);
  }

  private JButton getSelectDataSourceButton() {
    JButton jButton = new JButton("Select DataSource");
    jButton.addActionListener(e -> {

      Object[] options = {StockDataSourceType.SIMPLE.getName(),
              StockDataSourceType.ALPHA_VANTAGE.getName()};

      int n = JOptionPane.showOptionDialog(this,
              "Please Select the Stock Data Source",
              "Stock Data Source Selection",
              JOptionPane.YES_NO_OPTION,
              JOptionPane.QUESTION_MESSAGE,
              null,
              options,
              options[0]);

      StockDataSourceType stockDataSourceType = n == 0 ? StockDataSourceType.SIMPLE :
              StockDataSourceType.ALPHA_VANTAGE;

      this.tradingFeatures.setDataSource(stockDataSourceType);
      this.display(String.format("Stock Data Source selected: %s", stockDataSourceType.getName()));
    });
    return jButton;
  }

  private JButton getCreateStrategyAndPersistButton() {
    JButton jButton = new JButton("Create and Save Strategy");
    jButton.addActionListener(e -> {
      CreateStrategyAndPersistForm createStrategyAndPersistForm =
              new CreateStrategyAndPersistForm(this, tradingFeatures);
      Utils.showPrevious(createStrategyAndPersistForm, this);
    });
    return jButton;
  }

  private JButton getLoadAndExecuteStrategyButton() {
    JButton jButton = new JButton("Load and Execute Strategy");
    jButton.addActionListener(e -> {
      LoadAndExecuteStrategyForm loadAndExecuteStrategyForm =
              new LoadAndExecuteStrategyForm(this, tradingFeatures);
      Utils.showPrevious(loadAndExecuteStrategyForm, this);
    });
    return jButton;
  }

  private JButton getBuySharesUsingStrategyButton() {
    JButton jButton = new JButton("Buy Shares using Strategy");
    jButton.addActionListener(e -> {
      SelectStrategyToBuySharesForm selectStrategyToBuySharesForm =
              new SelectStrategyToBuySharesForm(this, tradingFeatures);
      Utils.showPrevious(selectStrategyToBuySharesForm, this);
    });
    return jButton;
  }

  private JButton getSavePortfolioButton() {
    JButton jButton = new JButton("Save Portfolio");
    jButton.addActionListener(e -> {
      SavePortfolioForm savePortfolioForm =
              new SavePortfolioForm(this, tradingFeatures);
      Utils.showPrevious(savePortfolioForm, this);
    });
    return jButton;
  }

  private JButton getLoadPortfolioButton() {
    JButton jButton = new JButton("Load Portfolio");
    jButton.addActionListener(e -> {
      LoadPortfolioForm loadPortfolioForm =
              new LoadPortfolioForm(this, tradingFeatures);
      Utils.showPrevious(loadPortfolioForm, this);
    });
    return jButton;
  }

  private JButton getPortfolioCompositionButton() {
    JButton jButton = new JButton("Get Portfolio Composition");
    jButton.addActionListener(e -> {
      GetPortfolioCompositionForm getPortfolioCompositionForm =
              new GetPortfolioCompositionForm(this, tradingFeatures);
      Utils.showPrevious(getPortfolioCompositionForm, this);
    });
    return jButton;
  }

  private JButton getBuySharesButton() {
    JButton jButton = new JButton("Buy Shares");
    jButton.addActionListener(e -> {
      BuySharesForm buySharesForm =
              new BuySharesForm(this, tradingFeatures);
      Utils.showPrevious(buySharesForm, this);
    });
    return jButton;
  }

  private JButton getDrawPortfolioPerformanceButton() {
    JButton jButton = new JButton("Plot Portfolio Performance");
    jButton.addActionListener(e -> {
      PortfolioPerformanceForm portfolioPerformanceForm =
              new PortfolioPerformanceForm(this, tradingFeatures);
      Utils.showPrevious(portfolioPerformanceForm, this);
    });
    return jButton;
  }

  private JButton getQuitJButton() {
    JButton quitButton = new JButton("Quit");
    quitButton.addActionListener(e -> tradingFeatures.quit());
    return quitButton;
  }


  private JButton getPortfolioCostBasisButton() {
    JButton jButton = new JButton("Get Portfolios Cost Basis");
    jButton.addActionListener(e -> {
      GetPortfolioCostBasisForm getPortfolioCostBasisForm =
              new GetPortfolioCostBasisForm(this, tradingFeatures);
      Utils.showPrevious(getPortfolioCostBasisForm, this);
    });
    return jButton;
  }

  private JButton getPortfolioValueButton() {
    JButton jButton = new JButton("Get Portfolios Value");
    jButton.addActionListener(e -> {
      GetPortfolioCostBasisForm getPortfolioValueForm = new GetPortfolioValueForm(this,
              tradingFeatures);
      Utils.showPrevious(getPortfolioValueForm, this);
    });
    return jButton;
  }

  private JButton getGetAllPortfolioJButton() {
    JButton jButton = new JButton("Get all Portfolios");
    jButton.addActionListener(e -> {

      List<Portfolio> allPortfolios = this.tradingFeatures.getAllPortfolios();
      if (allPortfolios.isEmpty()) {
        this.display("There are not portfolios present");
      } else {
        String portfolioNames = allPortfolios.stream()
                .map(Portfolio::getName).collect(Collectors.joining(System.lineSeparator()));

        this.display(
                String.format("All the portfolios:%s%s", System.lineSeparator(), portfolioNames));
      }
    });
    return jButton;
  }

  private JButton getRemainingCapital() {
    JButton jButton = new JButton("Get Remaining Capital");
    jButton.addActionListener(e -> {

      String formattedRemainingCapital =
              Utils.getFormattedCurrencyNumberString(this.tradingFeatures.getRemainingCapital());
      this.display(String.format("Remaining Capital: %s", formattedRemainingCapital));
    });
    return jButton;
  }

  private JButton getCreatePortfolioJButton() {
    JButton createPortfolioButton = new JButton("Create Portfolio");

    createPortfolioButton.addActionListener(e -> {
      CreatePortfolioForm createPortfolioForm = new CreatePortfolioForm(this, tradingFeatures);
      Utils.showPrevious(createPortfolioForm, this);
    });
    return createPortfolioButton;
  }

  private JTextArea getJTextArea() {
    JTextArea jTextArea = new JTextArea();
    jTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
    jTextArea.setEditable(false);
    jTextArea.setLineWrap(true);
    jTextArea.setMargin(new Insets(4, 4, 4, 4));
    return jTextArea;
  }
}
