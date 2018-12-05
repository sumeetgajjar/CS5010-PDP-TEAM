package virtualgambling.view.guiview;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Objects;

import javax.swing.*;

import virtualgambling.controller.Features;

/**
 * Created by gajjar.s, on 4:06 AM, 12/5/18
 */
public class PortfolioPerformanceForm extends AbstractForm {

  private final MainForm mainForm;
  private final Features features;

  protected PortfolioPerformanceForm(MainForm mainForm, Features features) {
    super(mainForm);
    this.mainForm = mainForm;
    this.features = features;
  }

  @Override
  protected void initComponents() {

    this.setLayout(new FlowLayout());

    JPanel leftPanel = new JPanel(new GridLayout(3, 2));

    JLabel portfolioLabel = new JLabel("Portfolio Name");
    leftPanel.add(portfolioLabel);

    JTextField portfolioTextField = new JTextField(10);
    leftPanel.add(portfolioTextField);

    JLabel startDate = new JLabel("Start date (YYYY-MM-DD)");
    leftPanel.add(startDate);

    JTextField startDateTextField = new JTextField(10);
    leftPanel.add(startDateTextField);

    JLabel endDate = new JLabel("End date (YYYY-MM-DD)");
    leftPanel.add(endDate);

    JTextField endDateTextField = new JTextField(10);
    leftPanel.add(endDateTextField);

    ActionListener actionListener = getActionListenerForCreatePortfolio(
            portfolioTextField,
            startDateTextField,
            endDateTextField);

    JPanel buttonJPanel = this.getActionButtonJPanel(actionListener);

    JPanel rightPanel = new JPanel();
    this.add(leftPanel);
    this.add(rightPanel);
    this.add(buttonJPanel);
  }

  private ActionListener getActionListenerForCreatePortfolio(JTextField portfolioTextField,
                                                             JTextField startDateTextField,
                                                             JTextField endDateTextField) {
    return e -> {

      if (this.areInputsEmpty(portfolioTextField, startDateTextField, endDateTextField)) {
        return;
      }

      String portfolioName = portfolioTextField.getText();
      Date startDate = this.getDateFromTextField(startDateTextField, this::showError);
      if (Objects.isNull(startDate)) {
        return;
      }

      Date endDate = this.getDateFromTextField(endDateTextField, this::showError);
      if (Objects.isNull(endDate)) {
        return;
      }

      this.plotGraph(portfolioName);
      //todo insert command here
    };
  }

  private boolean areInputsEmpty(JTextField portfolioTextField,
                                 JTextField startDateTextField,
                                 JTextField endDateTextField) {

    return isPortfolioNameTextFieldEmpty(portfolioTextField) ||
            isStartDateTextFieldEmpty(startDateTextField) ||
            isEndDateTextFieldEmpty(endDateTextField);
  }

  private void plotGraph(String portfolioName) {
    SwingUtilities.invokeLater(() -> {
      PlotJFrame ex = new PlotJFrame(this, portfolioName);
      ex.setVisible(true);
    });
  }

  private static class PlotJFrame extends AbstractForm {

    private final String portfolioName;
    private JFreeChart jFreeChart;

    private PlotJFrame(JFrame previousFrame, String portfolioName) {
      super(previousFrame);
      this.portfolioName = portfolioName;
      this.setTitles();
    }

    @Override
    protected void initComponents() {

      XYDataset dataset = createDataSet();
      this.jFreeChart = createChart(dataset);
      ChartPanel chartPanel = new ChartPanel(jFreeChart);
      chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
      chartPanel.setBackground(Color.white);
      add(chartPanel);
    }

    private void setTitles() {
      this.setTitle(String.format("Performance of '%s' portfolio", portfolioName));
      this.jFreeChart.setTitle(
              new TextTitle(
                      String.format("Performance of '%s' portfolio", portfolioName),
                      new Font(Font.SANS_SERIF, Font.BOLD, 20)));
    }

    private XYDataset createDataSet() {

      XYSeries series1 = new XYSeries("2014");
      series1.add(18, 530);
      series1.add(20, 580);
      series1.add(25, 740);
      series1.add(30, 901);
      series1.add(40, 1300);
      series1.add(50, 2219);

      XYSeries series2 = new XYSeries("2016");
      series2.add(18, 567);
      series2.add(20, 612);
      series2.add(25, 800);
      series2.add(30, 980);
      series2.add(40, 1210);
      series2.add(50, 2350);

      XYSeriesCollection dataset = new XYSeriesCollection();
      dataset.addSeries(series1);
      dataset.addSeries(series2);

      return dataset;
    }

    private JFreeChart createChart(final XYDataset dataset) {

      JFreeChart chart = ChartFactory.createXYLineChart(
              String.format("Performance of '%s' portfolio", portfolioName),
              "Value",
              "Time",
              dataset,
              PlotOrientation.VERTICAL,
              true,
              true,
              false
      );

      XYPlot plot = chart.getXYPlot();

      XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

      renderer.setSeriesPaint(0, Color.RED);
      renderer.setSeriesStroke(0, new BasicStroke(2.0f));

      renderer.setSeriesPaint(1, Color.BLUE);
      renderer.setSeriesStroke(1, new BasicStroke(2.0f));

      plot.setRenderer(renderer);
      plot.setBackgroundPaint(Color.white);

      plot.setRangeGridlinesVisible(true);
      plot.setRangeGridlinePaint(Color.BLACK);

      plot.setDomainGridlinesVisible(true);
      plot.setDomainGridlinePaint(Color.BLACK);

      chart.getLegend().setFrame(BlockBorder.NONE);

      return chart;
    }
  }
}
