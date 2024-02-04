import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogEntry {
    private final String timestamp;
    private final String logLevel;
    private final String message;

    public LogEntry(String timestamp, String logLevel, String message) {
        this.timestamp = timestamp;
        this.logLevel = logLevel;
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }
    public String getMessage() {
        return message;
    }

    public boolean isFromUser() {
        // Implement your logic to determine if the log entry is from the user.
        // You may use specific log levels, keywords, or patterns to identify user messages.
        // For example, you might check if the log level is INFO and the message starts with "User:"
        return logLevel.equals("INFO") && message.startsWith("User:");
    }

    @Override
    public String toString() {
        return timestamp + " - " + logLevel + " - " + message;
    }

    public static LogEntry parseLogEntry(String logLine) {
        Pattern logPattern = Pattern.compile("(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2},\\d{3}) - (\\S+) - (.*)");
        Matcher matcher = logPattern.matcher(logLine);
        if (matcher.matches()) {
            String timestamp = matcher.group(1);
            String logLevel = matcher.group(2);
            String message = matcher.group(3);
            return new LogEntry(timestamp, logLevel, message);
        }
        return null;
    }
}
