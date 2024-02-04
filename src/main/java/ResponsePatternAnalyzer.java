import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.axis.CategoryLabelPositions;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResponsePatternAnalyzer {

    public static void analyzeResponsePatterns(List<LogEntry> logEntries) {
        SwingUtilities.invokeLater(() -> {
            // Map to store user input and corresponding system responses
            Map<String, Map<String, Integer>> responsePatterns = new HashMap<>();

            for (int i = 0; i < logEntries.size() - 1; i++) {
                LogEntry currentEntry = logEntries.get(i);
                LogEntry nextEntry = logEntries.get(i + 1);

                if (currentEntry.isFromUser()) {
                    String userInput = currentEntry.getMessage();
                    String systemResponse = nextEntry.getMessage();

                    // Update response patterns map
                    responsePatterns.computeIfAbsent(userInput, k -> new HashMap<>());
                    responsePatterns.get(userInput).put(systemResponse, responsePatterns.get(userInput).getOrDefault(systemResponse, 0) + 1);
                }
            }

            // Create dataset for the bar chart
            CategoryDataset dataset = createDataset(responsePatterns);

            // Create bar chart
            JFreeChart chart = ChartFactory.createBarChart(
                    "Response Patterns",
                    "User Input",
                    "Frequency",
                    dataset,
                    PlotOrientation.VERTICAL,
                    true,
                    true,
                    false
            );

            CategoryPlot plot = chart.getCategoryPlot();
            CategoryAxis xAxis = plot.getDomainAxis();
            xAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

            NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
            yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

            JFrame frame = new JFrame("Response Pattern Analysis");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            ChartPanel chartPanel = new ChartPanel(chart);
            frame.add(chartPanel, BorderLayout.CENTER);

            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    private static CategoryDataset createDataset(Map<String, Map<String, Integer>> responsePatterns) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Map.Entry<String, Map<String, Integer>> entry : responsePatterns.entrySet()) {
            String userInput = entry.getKey();
            Map<String, Integer> systemResponses = entry.getValue();

            for (Map.Entry<String, Integer> responseEntry : systemResponses.entrySet()) {
                String systemResponse = responseEntry.getKey();
                int frequency = responseEntry.getValue();

                dataset.addValue(frequency, systemResponse, userInput);
            }
        }

        return dataset;
    }

    public static void main(String[] args) {
        String filePath = "conversation.log";
        List<LogEntry> logEntries = LogAnalyzer.readAndParseLogs(filePath);

        // Analysis and Visualization of Response Patterns
        analyzeResponsePatterns(logEntries);
    }
}