package view;

import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * This class implements a simple JFrame which display the data extracted from
 * the data file in a previous run in a series of ctarts as done in
 * {@link GraphTelemetryView}. In this particular class we have broken the
 * model-view-controller structure and everything is done into the constructor.
 * The class autonomosly parse data from file and displays them in the graphs.
 */
public class GraphsFrame extends JFrame {
	private static final long serialVersionUID = 1801638182066987430L;
	private Map<String, XYSeries> dataSeries;

	public GraphsFrame(File logFile) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(logFile));
		dataSeries = new HashMap<String, XYSeries>();

		try {
			// parsing dell'header
			String header = reader.readLine();
			String[] parameters = header.split(",");
			for (int i = 1; i < parameters.length; i++)
				dataSeries.put(parameters[i], new XYSeries(parameters[i]));

			// creazione dei grafici
			this.setLayout(new GridLayout(2, (int) Math.ceil(parameters.length / 2.0)));
			for (XYSeries xys : dataSeries.values()) {
				JFreeChart chart = ChartFactory.createXYLineChart(xys.getDescription(), null, null,
						new XYSeriesCollection(xys), PlotOrientation.VERTICAL, true, true, false);
				XYItemRenderer r = ((XYPlot) chart.getPlot()).getRenderer();
				if (r instanceof XYLineAndShapeRenderer) {
					XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
					renderer.setBaseShapesVisible(true);
					renderer.setBaseShapesFilled(true);
				}
				ChartPanel panel = new ChartPanel(chart);
				this.add(panel);
			}
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			this.pack();
			this.setVisible(true);

			// lettura e aggiunta dei dati
			String line;
			while ((line = reader.readLine()) != null) {
				String[] values = line.split(",");
				for (int i = 1; i < values.length; i++)
					try {
						Double timestamp = Double.valueOf(values[0]);
						Double value = Double.valueOf(values[i]);
						dataSeries.get(parameters[i]).add(timestamp, value);
					} catch (ArrayIndexOutOfBoundsException | NumberFormatException ignore) {
						System.err.println("Errore alla riga: " + line);
						ignore.printStackTrace();
					}
			}
		} catch (NullPointerException e) {
			throw new IOException();
		} finally {
			reader.close();
		}
	}

}
