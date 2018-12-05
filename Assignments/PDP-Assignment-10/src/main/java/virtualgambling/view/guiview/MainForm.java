package virtualgambling.view.guiview;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.*;

import util.Utils;
import virtualgambling.controller.Features;
import virtualgambling.model.bean.Portfolio;

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
  }

  private JTextArea getJTextArea() {
    JTextArea jTextArea = new JTextArea();
    jTextArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
    jTextArea.setEditable(false);
    jTextArea.setLineWrap(true);
    return jTextArea;
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
    this.appendOutput(text);
  }

  @Override
  protected void appendOutput(String message) {
    this.jTextArea.append(System.lineSeparator());
    this.jTextArea.append(message);
    this.jTextArea.append(System.lineSeparator());
    this.jTextArea.setCaretPosition(jTextArea.getDocument().getLength());
  }

  @Override
  protected void initComponents() {
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLayout(new FlowLayout());


    JPanel buttonJPanel = new JPanel();
    buttonJPanel.setLayout(new BoxLayout(buttonJPanel, BoxLayout.Y_AXIS));
    this.addButtonsToPanel(buttonJPanel);
    buttonJPanel.setBackground(Color.BLUE);
    buttonJPanel.setPreferredSize(new Dimension(300, 800));
    this.add(buttonJPanel);

    this.add(new JSeparator(SwingConstants.VERTICAL));

    this.jTextArea = getJTextArea();
    JScrollPane outputJPanel = new JScrollPane(this.jTextArea);
    outputJPanel.setPreferredSize(new Dimension(600, 800));
    this.add(outputJPanel);
  }

  @Override
  protected void addJFrameClosingEvent() {
    //closing event should not be added to this MainForm since there is no previous form to go to.
  }

  private void addButtonsToPanel(JPanel buttonJPanel) {
    JButton createPortfolioButton = getCreatePortfolioJButton();
    buttonJPanel.add(createPortfolioButton);

    JButton getAllPortfoliosButton = getGetAllPortfolioJButton();
    buttonJPanel.add(getAllPortfoliosButton);

    JButton getPortfolioCostBasisButton = getPortfolioCostBasisButton();
    buttonJPanel.add(getPortfolioCostBasisButton);

    JButton getPortfolioCompositionButton = getPortfolioCompositionButton();
    buttonJPanel.add(getPortfolioCompositionButton);

    JButton getPortfolioValueButton = getPortfolioValueButton();
    buttonJPanel.add(getPortfolioValueButton);

    JButton getRemainingCapitalButton = getRemainingCapital();
    buttonJPanel.add(getRemainingCapitalButton);

    JButton plotPortfolioPerformanceButton = getDrawPortfolioPerformanceButton();
    buttonJPanel.add(plotPortfolioPerformanceButton);

    JButton buySharesButton = getBuySharesButton();
    buttonJPanel.add(buySharesButton);

    JButton quitButton = getQuitJButton();
    buttonJPanel.add(quitButton);
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
}
