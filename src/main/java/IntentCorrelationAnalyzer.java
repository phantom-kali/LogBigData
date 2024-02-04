import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class IntentCorrelationAnalyzer {

    public static Map<String, Map<String, Integer>> calculateIntentCorrelation(List<ExtendedLogEntry> logEntries) {
        Map<String, Map<String, Integer>> correlationMatrix = new HashMap<>();

        // Placeholder for storing the last intent seen for each timestamp
        Map<String, String> lastIntentMap = new HashMap<>();

        for (ExtendedLogEntry entry : logEntries) {
            String timestamp = entry.getTimestamp();
            String currentIntent = entry.getIntent();

            // Update correlation matrix
            if (!correlationMatrix.containsKey(timestamp)) {
                correlationMatrix.put(timestamp, new HashMap<>());
            }

            Map<String, Integer> timestampCorrelation = correlationMatrix.get(timestamp);

            // Check if there was a last intent for this timestamp
            if (lastIntentMap.containsKey(timestamp)) {
                String lastIntent = lastIntentMap.get(timestamp);

                // Update the correlation matrix based on the feedback loop
                String feedbackLoopKey = lastIntent + "->" + currentIntent;
                timestampCorrelation.put(feedbackLoopKey, timestampCorrelation.getOrDefault(feedbackLoopKey, 0) + 1);
            }

            // Update the last intent for this timestamp
            lastIntentMap.put(timestamp, currentIntent);
        }

        return correlationMatrix;
    }

    public static List<ExtendedLogEntry> readAndParseExtendedLogs(String filePath) {
        List<ExtendedLogEntry> logEntries = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                ExtendedLogEntry logEntry = ExtendedLogEntry.parseLogEntry(line);
                if (logEntry != null) {
                    logEntries.add(logEntry);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return logEntries;
    }

    public static void main(String[] args) {
        String filePath = "conversation.log";
        List<ExtendedLogEntry> logEntries = readAndParseExtendedLogs(filePath);

        // 1. Data Processing
        Map<String, Map<String, Integer>> intentCorrelation = calculateIntentCorrelation(logEntries);

        System.out.println(intentCorrelation);

        // 2. Visualization (if needed)
        // Implement visualization logic here using JFreeChart or any other visualization library
    }
}
