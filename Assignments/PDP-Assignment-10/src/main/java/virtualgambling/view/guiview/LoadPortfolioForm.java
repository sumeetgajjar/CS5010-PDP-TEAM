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
import javax.swing.SwingConstants;

import util.Utils;
import virtualgambling.controller.TradingFeatures;

/**
 * This class represents a GUI form to Load the portfolio from the file into the model. It extends
 * {@link LoadPortfolioForm} to reduce the effort in implementing the common functionality.
 */
public class LoadPortfolioForm extends AbstractForm {

  private static final String PORTFOLIO_FILE_PATH = "Portfolio File Path: ";
  protected final MainForm mainForm;
  protected final TradingFeatures tradingFeatures;
  private JLabel filePathLabel;
  private File selectedFile;

  /**
   * Constructs a object of LoadPortfolioForm with the given params.
   *
   * @param mainForm        the mainForm
   * @param tradingFeatures the tradingFeatures
   * @throws IllegalArgumentException if the given params are null
   */
  public LoadPortfolioForm(MainForm mainForm, TradingFeatures tradingFeatures) throws IllegalArgumentException {
    super(mainForm);
    this.mainForm = Utils.requireNonNull(mainForm);
    this.tradingFeatures = Utils.requireNonNull(tradingFeatures);
    this.setTitle("Load Portfolio");
  }

  @Override
  protected void initComponents() {
    JPanel outerPanel = new JPanel();
    outerPanel.setLayout(new BoxLayout(outerPanel, BoxLayout.Y_AXIS));

    JPanel fileChooserPanel = new JPanel();
    fileChooserPanel.setLayout(new GridLayout(2, 1));

    JButton selectPortfolioFileButton = new JButton("Select portfolio file");
    selectPortfolioFileButton.addActionListener(this.getSelectPortfolioFileActionListener());
    fileChooserPanel.add(selectPortfolioFileButton);

    this.filePathLabel = new JLabel(PORTFOLIO_FILE_PATH);

    filePathLabel.setPreferredSize(new Dimension(600, 20));
    fileChooserPanel.add(this.filePathLabel);

    ActionListener actionListener = getActionListenerForCreatePortfolio();
    JPanel buttonJPanel = this.getActionButtonJPanel(actionListener);

    outerPanel.add(fileChooserPanel);
    outerPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
    outerPanel.add(buttonJPanel);

    this.add(outerPanel);
  }

  private ActionListener getSelectPortfolioFileActionListener() {
    return e -> {
      this.selectedFile = getFileToLoadUsingFileChooser();
      if (Objects.nonNull(this.selectedFile)) {
        this.filePathLabel.setText(String.format("%s%s", PORTFOLIO_FILE_PATH,
                selectedFile.getAbsolutePath()));
      }
    };
  }

  private ActionListener getActionListenerForCreatePortfolio() {
    return e -> {
      if (Objects.isNull(this.selectedFile)) {
        this.mainForm.displayError("Please select the file for loading portfolio");
        return;
      }

      boolean success = this.tradingFeatures.loadPortfolio(selectedFile.getAbsolutePath());

      if (success) {
        this.showPrevious();
        this.mainForm.display("Portfolio loaded Successfully");
      }
    };
  }
}
