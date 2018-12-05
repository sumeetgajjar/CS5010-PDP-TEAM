package virtualgambling.view.guiview;

import java.awt.*;

import javax.swing.*;

/**
 * Created by gajjar.s, on 11:27 PM, 12/4/18
 */
public class MainForm extends JFrame {

  private final JScrollPane outputJPanel;
  //  private final JPanel outputJPanel;
  private final JTextArea jTextArea;
  private final MainForm mainForm;

  public MainForm() throws HeadlessException {
    this.mainForm = this;

    this.setLocation(200, 200);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    this.setLayout(new FlowLayout());

    JPanel buttonJPanel = new JPanel();
    buttonJPanel.setLayout(new BoxLayout(buttonJPanel, BoxLayout.Y_AXIS));


    JButton createPortfolioButton = getCreatePortfolioJButton(this.mainForm);

    JButton quitButton = GUIUtils.getQuitJButton();

    buttonJPanel.add(createPortfolioButton);
    buttonJPanel.add(quitButton);

    buttonJPanel.setBackground(Color.BLUE);
    buttonJPanel.setPreferredSize(new Dimension(300, 800));
    this.add(buttonJPanel);

    this.add(new JSeparator(SwingConstants.VERTICAL));

    this.jTextArea = getJTextArea();

    this.outputJPanel = new JScrollPane(this.jTextArea);
    this.outputJPanel.setPreferredSize(new Dimension(600, 800));

    this.add(this.outputJPanel);

    this.pack();
    this.setLocationRelativeTo(null);
    this.setVisible(true);
  }

  private JButton getCreatePortfolioJButton(MainForm mainForm) {
    JButton createPortfolioButton = new JButton("Create Portfolio");

    createPortfolioButton.addActionListener(e -> {
      CreatePortfolioForm createPortfolioForm = new CreatePortfolioForm(mainForm);
      GUIUtils.showPrevious(createPortfolioForm, mainForm);
    });
    return createPortfolioButton;
  }

  private JTextArea getJTextArea() {
    JTextArea jTextArea = new JTextArea("Welcome to Virtual Trading");
    jTextArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
    jTextArea.setEditable(false);
    return jTextArea;
  }

  public void appendOutput(String message) {
    this.jTextArea.append(System.lineSeparator());
    this.jTextArea.append(message);
  }

  public static void main(String[] args) {
    new MainForm();
    System.out.println();
  }
}
