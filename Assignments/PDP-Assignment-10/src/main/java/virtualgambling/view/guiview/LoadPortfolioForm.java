package virtualgambling.view.guiview;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Objects;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;

import virtualgambling.controller.Features;

/**
 * Created by gajjar.s, on 11:43 PM, 12/4/18
 */
public class LoadPortfolioForm extends AbstractForm {

  protected final MainForm mainForm;
  protected final Features features;
  private JLabel filePathLabel;
  private File selectedFile;

  public LoadPortfolioForm(MainForm mainForm, Features features) throws HeadlessException {
    super(mainForm);
    this.mainForm = mainForm;
    this.features = features;
  }

  @Override
  protected void initComponents() {
    JPanel outerPanel = new JPanel();
    outerPanel.setLayout(new BoxLayout(outerPanel, BoxLayout.Y_AXIS));

    JPanel fileChooserPanel = new JPanel();
    fileChooserPanel.setLayout(new BoxLayout(fileChooserPanel, BoxLayout.Y_AXIS));

    JLabel jLabel = new JLabel("Please enter the name of the Portfolio");
    fileChooserPanel.add(jLabel);

    JTextField portfolioNameJTextField = new JTextField(10);
    fileChooserPanel.add(portfolioNameJTextField);

    JButton selectPortfolioFileButton = new JButton("Select portfolio file");
    selectPortfolioFileButton.addActionListener(getSelectPortfolioFileActionListener());
    fileChooserPanel.add(selectPortfolioFileButton);

    this.filePathLabel = new JLabel();
    filePathLabel.setPreferredSize(new Dimension(400, 20));
    filePathLabel.setBackground(Color.red);
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
      JFileChooser jFileChooser =
              new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

      int returnValue = jFileChooser.showOpenDialog(this);

      if (returnValue == JFileChooser.APPROVE_OPTION) {
        this.selectedFile = jFileChooser.getSelectedFile();
        this.filePathLabel.setText(String.format("Portfolio File Path: %s",
                selectedFile.getAbsolutePath()));
      }
    };
  }

  private ActionListener getActionListenerForCreatePortfolio(JTextField portfolioNameJTextField) {
    return e -> {
      if (this.isPortfolioNameTextFieldEmpty(portfolioNameJTextField)) {
        return;
      }

      if (Objects.isNull(this.selectedFile)) {
        this.mainForm.displayError("Please select the file for loading portfolio");
        return;
      }

      String portfolioName = portfolioNameJTextField.getText();
      boolean success = this.features.loadPortfolio(portfolioName, selectedFile);

      if (success) {
        this.showPrevious();
        this.mainForm.display(String.format("Portfolio '%s' loaded Successfully",
                portfolioName));
      }
    };
  }
}
