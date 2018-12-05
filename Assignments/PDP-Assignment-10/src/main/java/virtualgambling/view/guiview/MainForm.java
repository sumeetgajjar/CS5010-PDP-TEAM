package virtualgambling.view.guiview;

import java.awt.*;

import javax.swing.*;

import virtualgambling.controller.Features;

/**
 * Created by gajjar.s, on 11:27 PM, 12/4/18
 */
public class MainForm extends AbstractForm implements GuiView {

  private JTextArea jTextArea;
  private Features features;

  public MainForm() throws HeadlessException {
    super(null);
    this.setVisible(true);
  }

  private JTextArea getJTextArea() {
    JTextArea jTextArea = new JTextArea("Welcome to Virtual Trading");
    jTextArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
    jTextArea.setEditable(false);
    return jTextArea;
  }

  @Override
  public void addFeatures(Features features) {
    this.features = features;
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

  private void addButtonsToPanel(JPanel buttonJPanel) {
    JButton createPortfolioButton = getCreatePortfolioJButton();
    buttonJPanel.add(createPortfolioButton);

    JButton getAllPortfoliosButton = getGetAllPortfolioJButton();
    buttonJPanel.add(getAllPortfoliosButton);

    JButton getPortfolioCostBasisButton = getPortfolioCostBasisButton();
    buttonJPanel.add(getPortfolioCostBasisButton);

    JButton getPortfolioValueButton = getPortfolioValueButton();
    buttonJPanel.add(getPortfolioValueButton);

    JButton getRemainingCapitalButton = getRemainingCapital();
    buttonJPanel.add(getRemainingCapitalButton);

    JButton drawPortfolioPerformanceButton = getDrawPortfolioPerformanceButton();

    JButton quitButton = getQuitJButton();
    buttonJPanel.add(quitButton);
  }

  private JButton getDrawPortfolioPerformanceButton() {
    JButton jButton = new JButton("Plot Portfolio Performance");
    jButton.addActionListener(e -> {
      DrawPortfolioPerformance drawPortfolioPerformance =
              new DrawPortfolioPerformance(this, features);
      GUIUtils.showPrevious(drawPortfolioPerformance, this);
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

//      List<String> allPortfolios = features.getAllPortfolios();
//      allPortfolios.forEach(this::display);

      this.appendOutput("YOLO");
      this.appendOutput("Was that an Earthquake or did I Just rocked your world?");
      //todo insert command here
    });
    return jButton;
  }

  private JButton getRemainingCapital() {
    JButton jButton = new JButton("Get Remaining Capital");
    jButton.addActionListener(e -> {

//      this.display(this.features.getRemainingCapital());

      this.appendOutput("You have infinite money, jao jee lo apni zindagi");
      //todo insert command here
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

  public static void main(String[] args) {
    new MainForm();
    System.out.println();
  }
}