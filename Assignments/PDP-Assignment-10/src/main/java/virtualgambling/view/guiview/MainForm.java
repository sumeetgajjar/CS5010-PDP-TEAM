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


    JButton createPortfolioButton = getCreatePortfolioJButton();
    JButton quitButton = GUIUtils.getQuitJButton();

    JPanel buttonJPanel = new JPanel();
    buttonJPanel.setLayout(new BoxLayout(buttonJPanel, BoxLayout.Y_AXIS));
    buttonJPanel.add(createPortfolioButton);
    buttonJPanel.add(quitButton);
    buttonJPanel.setBackground(Color.BLUE);
    buttonJPanel.setPreferredSize(new Dimension(300, 800));
    this.add(buttonJPanel);

    this.add(new JSeparator(SwingConstants.VERTICAL));

    this.jTextArea = getJTextArea();
    JScrollPane outputJPanel = new JScrollPane(this.jTextArea);
    outputJPanel.setPreferredSize(new Dimension(600, 800));
    this.add(outputJPanel);
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
