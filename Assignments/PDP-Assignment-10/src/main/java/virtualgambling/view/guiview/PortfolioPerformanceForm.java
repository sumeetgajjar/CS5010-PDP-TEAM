package virtualgambling.view.guiview;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

import javax.swing.*;

import util.Utils;
import virtualgambling.controller.Features;
import virtualgambling.model.bean.Portfolio;

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

      Optional<Portfolio> optional = this.features.getPortfolio(portfolioName);
      if (optional.isPresent()) {
        Portfolio portfolio = optional.get();
        this.plotGraph(portfolio, startDate, endDate);
      }
    };
  }

  private boolean areInputsEmpty(JTextField portfolioTextField,
                                 JTextField startDateTextField,
                                 JTextField endDateTextField) {

    return isPortfolioNameTextFieldEmpty(portfolioTextField) ||
            isStartDateTextFieldEmpty(startDateTextField) ||
            isEndDateTextFieldEmpty(endDateTextField);
  }

  private void plotGraph(Portfolio portfolio, Date startDate, Date endDate) {
    SwingUtilities.invokeLater(() -> {
      PortfolioPlotJFrame ex = new PortfolioPlotJFrame(this, portfolio, startDate, endDate);
      ex.setVisible(true);
    });
  }

  private static class PortfolioPlotJFrame extends JFrame {

    private final JFrame previousJFrame;
    private final Portfolio portfolio;
    private final Date startDate;
    private final Date endDate;
    private JFreeChart jFreeChart;

    private PortfolioPlotJFrame(JFrame previousJFrame, Portfolio portfolio,
                                Date startDate, Date endDate) {
      super();
      this.previousJFrame = previousJFrame;
      this.portfolio = portfolio;
      this.startDate = startDate;
      this.endDate = endDate;
      this.initComponents();
      this.setTitles();
      this.addJFrameClosingEvent();
      this.pack();
      this.centerThisJFrame();
    }

    private void initComponents() {
      XYDataset dataset = createDataSet();
      this.jFreeChart = createChart(dataset);
      ChartPanel chartPanel = new ChartPanel(jFreeChart);
      chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
      chartPanel.setBackground(Color.white);
      add(chartPanel);
    }

    private void addJFrameClosingEvent() {
      this.addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
          showPrevious();
        }
      });
    }

    private void showPrevious() {
      GUIUtils.showPrevious(previousJFrame, this);
    }

    private void centerThisJFrame() {
      Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
      int x = (int) (dim.getWidth() / 2 - this.getSize().getWidth() / 2);
      int y = (int) (dim.getHeight() / 2 - this.getSize().getHeight() / 2);
      this.setLocation(x, y);
    }

    private void setTitles() {
      this.setTitle(String.format("Performance of '%s' sharePurchaseOrders", portfolio.getName()));
      this.jFreeChart.setTitle(
              new TextTitle(
                      String.format("Performance of '%s' sharePurchaseOrders", portfolio.getName()),
                      new Font(Font.SANS_SERIF, Font.BOLD, 20)));
    }

    private XYDataset createDataSet() {

      TimeSeriesCollection dataSet = new TimeSeriesCollection();

      TimeSeries portfolioValue = new TimeSeries("Portfolio Value");
      TimeSeries portfolioCostBasis = new TimeSeries("Portfolio Cost Basis");

      Date startDate = this.startDate;
      Date endDate = this.endDate;
      Calendar calendar = Utils.getCalendarInstance();
      while (startDate.compareTo(endDate) != 0) {

        portfolioCostBasis.add(new Day(startDate),
                portfolio.getCostBasisIncludingCommission(startDate));

        portfolioValue.add(new Day(startDate),
                portfolio.getValue(startDate));

        calendar.setTime(startDate);
        calendar.add(Calendar.DATE, 1);
        startDate = calendar.getTime();
      }

      dataSet.addSeries(portfolioValue);
      dataSet.addSeries(portfolioCostBasis);

      return dataSet;
    }

    private JFreeChart createChart(final XYDataset dataset) {

      JFreeChart chart = ChartFactory.createTimeSeriesChart(
              "Portfolio Performance",
              "Time",
              "Value",
              dataset,
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
