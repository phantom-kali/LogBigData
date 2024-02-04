import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtendedLogEntry extends LogEntry {

    private final String intent;

    public ExtendedLogEntry(String timestamp, String logLevel, String message, String intent) {
        super(timestamp, logLevel, message);
        this.intent = intent;
    }

    public String getIntent() {
        return intent;
    }

    public static ExtendedLogEntry parseLogEntry(String logLine) {
        Pattern logPattern = Pattern.compile("(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2},\\d{3}) - (\\S+) - (.*)(?: - Intent: (.*))?");
        Matcher matcher = logPattern.matcher(logLine);
        if (matcher.matches()) {
            String timestamp = matcher.group(1);
            String logLevel = matcher.group(2);
            String message = matcher.group(3);
            String intent = matcher.group(4); // This may be null if there's no Intent group

            return new ExtendedLogEntry(timestamp, logLevel, message, intent);
        }
        return null;
    }
}
