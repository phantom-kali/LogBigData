import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IntentDistributionAnalyzer {

    public static Map<String, Integer> calculateIntentDistribution(List<ExtendedLogEntry> logEntries) {
        // Count occurrences of different intents
        Map<String, Integer> intentDistribution = new HashMap<>();

        for (ExtendedLogEntry entry : logEntries) {
            String intent = entry.getIntent();
            intentDistribution.put(intent, intentDistribution.getOrDefault(intent, 0) + 1);
        }

        return intentDistribution;
    }

    public static void visualizeIntentDistribution(Map<String, Integer> intentDistribution) {
        SwingUtilities.invokeLater(() -> {
            DefaultPieDataset dataset = createDataset(intentDistribution);
            JFreeChart chart = createChart(dataset);

            JFrame frame = new JFrame("Intent Distribution Chart");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            ChartPanel chartPanel = new ChartPanel(chart);
            frame.add(chartPanel, BorderLayout.CENTER);

            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    private static DefaultPieDataset createDataset(Map<String, Integer> intentDistribution) {
        DefaultPieDataset dataset = new DefaultPieDataset();

        for (Map.Entry<String, Integer> entry : intentDistribution.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }

        return dataset;
    }

    private static JFreeChart createChart(DefaultPieDataset dataset) {
        return ChartFactory.createPieChart(
                "Intent Distribution",
                dataset,
                true,
                true,
                false
        );
    }

    public static void main(String[] args) {
        String filePath = "conversation.log";
        List<ExtendedLogEntry> logEntries = IntentCorrelationAnalyzer.readAndParseExtendedLogs(filePath);

        // 1. Data Processing
        Map<String, Integer> intentDistribution = calculateIntentDistribution(logEntries);

        System.out.println(intentDistribution);

        // 2. Visualization
        visualizeIntentDistribution(intentDistribution);
    }
}
