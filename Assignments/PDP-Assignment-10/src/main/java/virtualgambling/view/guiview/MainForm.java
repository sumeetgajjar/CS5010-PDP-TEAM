package virtualgambling.view.guiview;

import java.awt.*;

import javax.swing.*;

/**
 * Created by gajjar.s, on 11:27 PM, 12/4/18
 */
public class MainForm extends AbstractForm {

  private JTextArea jTextArea;

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

    JButton quitButton = GUIUtils.getQuitJButton();
    buttonJPanel.add(quitButton);
  }

  private JButton getPortfolioCostBasisButton() {
    JButton jButton = new JButton("Get Portfolios Cost Basis");
    jButton.addActionListener(e -> {
      GetPortfolioCostBasisForm getPortfolioCostBasisForm = new GetPortfolioCostBasisForm(this);
      GUIUtils.showPrevious(getPortfolioCostBasisForm, this);
    });
    return jButton;
  }

  private JButton getGetAllPortfolioJButton() {
    JButton jButton = new JButton("Get all Portfolios");
    jButton.addActionListener(e -> {
      this.appendOutput("YOLO");
      this.appendOutput("Was that an Earthquake or did I Just rocked your world?");
    });
    return jButton;
  }

  private JButton getCreatePortfolioJButton() {
    JButton createPortfolioButton = new JButton("Create Portfolio");

    createPortfolioButton.addActionListener(e -> {
      CreatePortfolioForm createPortfolioForm = new CreatePortfolioForm(this);
      GUIUtils.showPrevious(createPortfolioForm, this);
    });
    return createPortfolioButton;
  }

  public static void main(String[] args) {
    new MainForm();
    System.out.println();
  }
}
