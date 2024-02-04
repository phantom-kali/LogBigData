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
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;

public class LogAnalyzer {
    public static List<LogEntry> readAndParseLogs(String filePath) {
        List<LogEntry> logEntries = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                LogEntry logEntry = LogEntry.parseLogEntry(line);
                if (logEntry != null) {
                    logEntries.add(logEntry);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return logEntries;
    }

    public static Map<String, Integer> analyzeMessageFrequency(List<LogEntry> logEntries) {
        Map<String, Integer> messageFrequency = new HashMap<>();
        for (LogEntry entry : logEntries) {
            String timestamp = entry.getTimestamp(); // You can extract hour, day, etc., as needed
            messageFrequency.put(timestamp, messageFrequency.getOrDefault(timestamp, 0) + 1);
        }
        return messageFrequency;
    }

    public static void visualizeMessageFrequency(Map<String, Integer> messageFrequency) {
        XYSeries series = new XYSeries("Message Frequency");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");

        for (Map.Entry<String, Integer> entry : messageFrequency.entrySet()) {
            try {
                Date timestamp = dateFormat.parse(entry.getKey());
                series.add(timestamp.getTime(), entry.getValue());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        XYSeriesCollection dataset = new XYSeriesCollection(series);
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Message Frequency Over Time",
                "Timestamp",
                "Message Count",
                dataset
        );

        XYPlot plot = chart.getXYPlot();
        DateAxis dateAxis = new DateAxis("Timestamp");
        dateAxis.setDateFormatOverride(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS"));
        dateAxis.setTickUnit(new DateTickUnit(DateTickUnitType.HOUR, 1));
        plot.setDomainAxis(dateAxis);

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        JFrame frame = new JFrame("Message Frequency Visualization");
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
        List<LogEntry> logEntries = readAndParseLogs(filePath);

        // 1. Data Processing
        Map<String, Integer> messageFrequency = analyzeMessageFrequency(logEntries);

        System.out.println(messageFrequency);

        // 2. Visualization
        visualizeMessageFrequency(messageFrequency);
    }
}
