import java.time.Instant;

public class FaultEvent {
    // declared nodeId, timeStamp, severity, and description
    private String nodeId;
    private Instant timeStamp;
    private int severity;
    private String description;

    /**
     * creates a faultEvent object
     * @param nodeId the specific node the event occured at
     * @param timeStamp the exact timeStamp that the event occured at
     * @param severity the severity of the faultEvent
     * @param description describes what happened
     *
     */
    public FaultEvent(String nodeId, Instant timeStamp, int severity, String description) {
        this.nodeId = nodeId;
        this.timeStamp = timeStamp;
        this.severity = severity;
        this.description = description;
    }

    /**
     * gets the specific node that the event happened at
     * @return nodeId
     */
    public String getNodeId() {
        return nodeId;
    }

    /**
     * gets the exact timeStamp that the event happened at
     * @return timeStamp
     */
    public Instant getTimeStamp() {
        return timeStamp;
    }

    /**
     * gets the severity of the event
     * @return severity
     */
    public int getSeverity() {
        return severity;
    }

    /**
     * gets the description of the event
     * @return description
     */
    public String description() {
        return description;
    }





    // created my own toString that uses the Override Method
    @Override
    public String toString() {
        return "[T+" + timeStamp + "ms] Node " + nodeId
            + " | Severity " + severity
            + " | " + description;
    }
}
