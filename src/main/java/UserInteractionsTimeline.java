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

public class UserInteractionsTimeline {

    public static void visualizeUserInteractionsTimeline(List<LogEntry> logEntries) {
        XYSeries userSeries = new XYSeries("User Messages");
        XYSeries systemResponseSeries = new XYSeries("System Responses");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");

        Date earliestTimestamp = new Date();
        Date latestTimestamp = new Date(0); // Set to the epoch as the starting point

        for (LogEntry entry : logEntries) {
            try {
                Date timestamp = dateFormat.parse(entry.getTimestamp());
                if (timestamp.before(earliestTimestamp)) {
                    earliestTimestamp = timestamp;
                }
                if (timestamp.after(latestTimestamp)) {
                    latestTimestamp = timestamp;
                }

                if (entry.isFromUser()) {
                    userSeries.add(timestamp.getTime(), 1);
                } else {
                    systemResponseSeries.add(timestamp.getTime(), 1);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(userSeries);
        dataset.addSeries(systemResponseSeries);

        JFreeChart chart = ChartFactory.createXYBarChart(
                "User Interactions Over Time",
                "Timestamp",
                false,
                "Count",
                dataset
        );

        XYPlot plot = chart.getXYPlot();
        DateAxis dateAxis = new DateAxis("Timestamp");
        dateAxis.setDateFormatOverride(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS"));
        dateAxis.setTickUnit(new DateTickUnit(DateTickUnitType.HOUR, 1));

        // Set the X-axis range based on the earliest and latest timestamps
        dateAxis.setRange(earliestTimestamp.getTime(), latestTimestamp.getTime());

        plot.setDomainAxis(dateAxis);

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        JFrame frame = new JFrame("User Interactions Timeline");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        ChartPanel chartPanel = new ChartPanel(chart);
        frame.add(chartPanel, BorderLayout.CENTER);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        String filePath = "conversation.log";
        List<LogEntry> logEntries = LogAnalyzer.readAndParseLogs(filePath);

        // Visualization of User Interactions Timeline
        visualizeUserInteractionsTimeline(logEntries);
    }
}
