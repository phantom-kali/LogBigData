import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.DateTickUnitType;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TurnTakingVisualization {

    public static void visualizeTurnTaking(List<LogEntry> logEntries) {
        SwingUtilities.invokeLater(() -> {
            XYSeries userSeries = new XYSeries("User Messages");
            XYSeries systemResponseSeries = new XYSeries("System Responses");

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");

            double frequency = 0.1;  // Adjust the frequency of the sine wave
            double amplitude = 1.0;  // Adjust the amplitude of the sine wave

            for (LogEntry entry : logEntries) {
                try {
                    Date timestamp = dateFormat.parse(entry.getTimestamp());
                    double sineValue = amplitude * Math.sin(frequency * timestamp.getTime());

                    if (entry.isFromUser()) {
                        userSeries.add(timestamp.getTime(), sineValue);
                    } else {
                        systemResponseSeries.add(timestamp.getTime(), sineValue);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            XYSeriesCollection dataset = new XYSeriesCollection();
            dataset.addSeries(userSeries);
            dataset.addSeries(systemResponseSeries);

            JFreeChart chart = ChartFactory.createXYLineChart(
                    "Turn-Taking Visualization",
                    "Timestamp",
                    "Sine Wave",
                    dataset
            );

            XYPlot plot = chart.getXYPlot();
            DateAxis dateAxis = new DateAxis("Timestamp");
            dateAxis.setDateFormatOverride(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS"));
            dateAxis.setTickUnit(new DateTickUnit(DateTickUnitType.HOUR, 1));
            plot.setDomainAxis(dateAxis);

            NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
            rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

            JFrame frame = new JFrame("Turn-Taking Visualization");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            ChartPanel chartPanel = new ChartPanel(chart);
            frame.add(chartPanel, BorderLayout.CENTER);

            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    public static void main(String[] args) {
        String filePath = "conversation.log";
        List<LogEntry> logEntries = LogAnalyzer.readAndParseLogs(filePath);

        // Visualization of Turn-Taking with Sine Wave
        visualizeTurnTaking(logEntries);
    }
}
