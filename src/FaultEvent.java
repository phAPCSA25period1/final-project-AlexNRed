import java.time.Instant;

public class FaultEvent {
    private String nodeId;
    private Instant timeStamp;
    private int severity;
    private String description;

    public FaultEvent(String nodeId, Instant timeStamp, int severity, String description) {
        this.nodeId = nodeId;
        this.timeStamp = timeStamp;
        this.severity = severity;
        this.description = description;
    }

    public String getNodeId() {
        return nodeId;
    }

    public Instant getTimeStamp() {
        return timeStamp;
    }

    public int getSeverity() {
        return severity;
    }

    public String description() {
        return description;
    }






    @Override
    public String toString() {
        return "[T+" + timeStamp + "ms] Node " + nodeId
            + " | Severity " + severity
            + " | " + description;
    }
}
