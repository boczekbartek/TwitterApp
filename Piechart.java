package twitterApp;

import java.util.List;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;

public class PieChart extends JFrame {
	private ChartPanel chartPanel;
	private JFreeChart chart;
	private PieDataset dataset;
	private static final long serialVersionUID = 1L;

	public PieChart(String applicationTitle, String chartTitle, List<Value> list) {
		super(applicationTitle);
		// This will create the dataset
		dataset = createDataset(list);
		// based on the dataset we create the chart
		chart = createChart(dataset, chartTitle);
		// we put the chart into a panel
		chartPanel = new ChartPanel(chart);
		// default size
		chartPanel.setPreferredSize(new java.awt.Dimension(800, 400));
		chartPanel.setLocation(300, 300);

		// add it to our application
		setContentPane(chartPanel);

	}

	public ChartPanel getChartPanel(){
		return chartPanel;
	}
	
	public JFreeChart getChart(){
		return chart;
	}
	/**
	 * Creates a sample dataset
	 */

	public PieDataset createDataset(List<Value> list) {
		DefaultPieDataset result = new DefaultPieDataset();
		for (Value v : list) {
			result.setValue(v.str, v.intValue);
		}
		return result;

	}

	/**
	 * Creates a chart
	 */

	private JFreeChart createChart(PieDataset dataset, String title) {

		JFreeChart chart = ChartFactory.createPieChart3D(title, // chart title
				dataset, // data
				true, // include legend
				true, false);

		PiePlot3D plot = (PiePlot3D) chart.getPlot();
		plot.setStartAngle(290);
		plot.setDirection(Rotation.CLOCKWISE);
		plot.setForegroundAlpha(0.5f);
		return chart;

	}
}