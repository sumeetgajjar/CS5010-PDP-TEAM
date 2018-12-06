package virtualgambling.view.guiview;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.*;

import util.Utils;
import virtualgambling.controller.Features;
import virtualgambling.model.bean.Portfolio;
import virtualgambling.model.factory.StockDataSourceType;

/**
 * Created by gajjar.s, on 11:27 PM, 12/4/18
 */
public class MainForm extends AbstractForm implements GUIView {

  private JTextArea jTextArea;
  private Features features;

  public MainForm() throws HeadlessException {
    super(null);
    this.display("Welcome to Virtual Trading");
    this.setVisible(true);
    this.setTitle("Virtual Trading");
  }

  @Override
  public void addFeatures(Features features) {
    this.features = features;
  }

  @Override
  public void displayError(String message) {
    this.showError(message);
  }

  @Override
  public String getInput() {
    return GUIUtils.getInput(this);
  }

  @Override
  public void display(String text) {
    this.jTextArea.append(System.lineSeparator());
    this.jTextArea.append(text);
    this.jTextArea.append(System.lineSeparator());
    this.jTextArea.setCaretPosition(jTextArea.getDocument().getLength());
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
    scrollPane.setPreferredSize(new Dimension(800, 750));
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

      this.features.setDataSource(stockDataSourceType);
      this.display(String.format("Stock Data Source selected: %s", stockDataSourceType.getName()));
    });
    return jButton;
  }

  private JButton getCreateStrategyAndPersistButton() {
    JButton jButton = new JButton("Create and Save Strategy");
    jButton.addActionListener(e -> {
      CreateStrategyAndPersistForm createStrategyAndPersistForm =
              new CreateStrategyAndPersistForm(this, features);
      GUIUtils.showPrevious(createStrategyAndPersistForm, this);
    });
    return jButton;
  }

  private JButton getLoadAndExecuteStrategyButton() {
    JButton jButton = new JButton("Load and Execute Strategy");
    jButton.addActionListener(e -> {
      LoadAndExecuteStrategyForm loadAndExecuteStrategyForm =
              new LoadAndExecuteStrategyForm(this, features);
      GUIUtils.showPrevious(loadAndExecuteStrategyForm, this);
    });
    return jButton;
  }

  private JButton getBuySharesUsingStrategyButton() {
    JButton jButton = new JButton("Buy Shares using Strategy");
    jButton.addActionListener(e -> {
      SelectStrategyToBuySharesForm selectStrategyToBuySharesForm =
              new SelectStrategyToBuySharesForm(this, features);
      GUIUtils.showPrevious(selectStrategyToBuySharesForm, this);
    });
    return jButton;
  }

  private JButton getSavePortfolioButton() {
    JButton jButton = new JButton("Save Portfolio");
    jButton.addActionListener(e -> {
      SavePortfolioForm savePortfolioForm =
              new SavePortfolioForm(this, features);
      GUIUtils.showPrevious(savePortfolioForm, this);
    });
    return jButton;
  }

  private JButton getLoadPortfolioButton() {
    JButton jButton = new JButton("Load Portfolio");
    jButton.addActionListener(e -> {
      LoadPortfolioForm loadPortfolioForm =
              new LoadPortfolioForm(this, features);
      GUIUtils.showPrevious(loadPortfolioForm, this);
    });
    return jButton;
  }

  private JButton getPortfolioCompositionButton() {
    JButton jButton = new JButton("Get Portfolio Composition");
    jButton.addActionListener(e -> {
      GetPortfolioCompositionForm getPortfolioCompositionForm =
              new GetPortfolioCompositionForm(this, features);
      GUIUtils.showPrevious(getPortfolioCompositionForm, this);
    });
    return jButton;
  }

  private JButton getBuySharesButton() {
    JButton jButton = new JButton("Buy Shares");
    jButton.addActionListener(e -> {
      BuySharesForm buySharesForm =
              new BuySharesForm(this, features);
      GUIUtils.showPrevious(buySharesForm, this);
    });
    return jButton;
  }

  private JButton getDrawPortfolioPerformanceButton() {
    JButton jButton = new JButton("Plot Portfolio Performance");
    jButton.addActionListener(e -> {
      PortfolioPerformanceForm portfolioPerformanceForm =
              new PortfolioPerformanceForm(this, features);
      GUIUtils.showPrevious(portfolioPerformanceForm, this);
    });
    return jButton;
  }

  private JButton getQuitJButton() {
    JButton quitButton = new JButton("Quit");
    quitButton.addActionListener(e -> features.quit());
    return quitButton;
  }


  private JButton getPortfolioCostBasisButton() {
    JButton jButton = new JButton("Get Portfolios Cost Basis");
    jButton.addActionListener(e -> {
      GetPortfolioCostBasisForm getPortfolioCostBasisForm =
              new GetPortfolioCostBasisForm(this, features);
      GUIUtils.showPrevious(getPortfolioCostBasisForm, this);
    });
    return jButton;
  }

  private JButton getPortfolioValueButton() {
    JButton jButton = new JButton("Get Portfolios Value");
    jButton.addActionListener(e -> {
      GetPortfolioCostBasisForm getPortfolioValueForm = new GetPortfolioValueForm(this, features);
      GUIUtils.showPrevious(getPortfolioValueForm, this);
    });
    return jButton;
  }

  private JButton getGetAllPortfolioJButton() {
    JButton jButton = new JButton("Get all Portfolios");
    jButton.addActionListener(e -> {

      List<Portfolio> allPortfolios = this.features.getAllPortfolios();
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
              Utils.getFormattedCurrencyNumberString(this.features.getRemainingCapital());
      this.display(String.format("Remaining Capital: %s", formattedRemainingCapital));
    });
    return jButton;
  }

  private JButton getCreatePortfolioJButton() {
    JButton createPortfolioButton = new JButton("Create Portfolio");

    createPortfolioButton.addActionListener(e -> {
      CreatePortfolioForm createPortfolioForm = new CreatePortfolioForm(this, features);
      GUIUtils.showPrevious(createPortfolioForm, this);
    });
    return createPortfolioButton;
  }

  private JTextArea getJTextArea() {
    JTextArea jTextArea = new JTextArea();
    jTextArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
    jTextArea.setEditable(false);
    jTextArea.setLineWrap(true);
    return jTextArea;
  }
}
