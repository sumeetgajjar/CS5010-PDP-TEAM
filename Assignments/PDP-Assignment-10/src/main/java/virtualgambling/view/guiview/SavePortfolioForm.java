package virtualgambling.view.guiview;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Objects;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import util.Utils;
import virtualgambling.controller.Features;

/**
 * This class represents a GUI form to save a portfolio to a file. It extends {@link AbstractForm}
 * to reduce the effort in implementing the common functionality.
 */
public class SavePortfolioForm extends AbstractForm {

  private final MainForm mainForm;
  private final Features features;
  private JLabel filePathLabel;
  private File selectedFile;

  /**
   * Constructs a object of SavePortfolioForm with the given params.
   *
   * @param mainForm the mainForm
   * @param features the features
   * @throws IllegalArgumentException if the given params are null
   */
  public SavePortfolioForm(MainForm mainForm, Features features) throws IllegalArgumentException {
    super(mainForm);
    this.mainForm = Utils.requireNonNull(mainForm);
    this.features = Utils.requireNonNull(features);
    this.setTitle("Save Portfolio");
  }

  @Override
  protected void initComponents() {
    JPanel outerPanel = new JPanel();
    outerPanel.setLayout(new BoxLayout(outerPanel, BoxLayout.Y_AXIS));

    JPanel fileChooserPanel = new JPanel();
    fileChooserPanel.setLayout(new GridLayout(3, 1));

    JPanel jPanel1 = new JPanel();
    jPanel1.add(new JLabel("Please enter the name of the Portfolio to save"));
    JTextField portfolioNameJTextField = new JTextField(10);
    jPanel1.add(portfolioNameJTextField);
    fileChooserPanel.add(jPanel1);

    JButton selectPortfolioFileButton = new JButton("Select portfolio file destination");
    selectPortfolioFileButton.addActionListener(this.getSelectPortfolioFileActionListener());
    fileChooserPanel.add(selectPortfolioFileButton);

    this.filePathLabel = new JLabel(PORTFOLIO_FILE_LABEL_TEXT);

    filePathLabel.setPreferredSize(new Dimension(600, 20));
    fileChooserPanel.add(this.filePathLabel);

    ActionListener actionListener = getActionListenerForCreatePortfolio(portfolioNameJTextField);
    JPanel buttonJPanel = this.getActionButtonJPanel(actionListener);

    outerPanel.add(fileChooserPanel);
    outerPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
    outerPanel.add(buttonJPanel);

    this.add(outerPanel);
  }

  private ActionListener getSelectPortfolioFileActionListener() {
    return e -> {
      this.selectedFile = getFileToSaveUsingFileChooser();
      if (Objects.nonNull(this.selectedFile)) {
        this.filePathLabel.setText(String.format("%s%s", PORTFOLIO_FILE_LABEL_TEXT,
                selectedFile.getAbsolutePath()));
      }
    };
  }

  private ActionListener getActionListenerForCreatePortfolio(JTextField portfolioNameJTextField) {
    return e -> {

      if (this.isPortfolioNameTextFieldEmpty(portfolioNameJTextField)) {
        return;
      }

      String portfolioName = portfolioNameJTextField.getText();

      if (Objects.isNull(this.selectedFile)) {
        this.mainForm.displayError("Please select the file for saving portfolio");
        return;
      }

      boolean success = this.features.savePortfolio(portfolioName, selectedFile.getAbsolutePath());
      if (success) {
        this.mainForm.display(String.format("Portfolio '%s' saved successfully",
                portfolioName));
        this.showPrevious();
      }
    };
  }
}
