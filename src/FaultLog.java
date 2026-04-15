import java.time.Instant;
import java.util.ArrayList;

public class FaultLog {
    private ArrayList<FaultEvent> events;
    private int totalFaultsDetected;
    private int totalNodesRerouted;

    public FaultLog() {
        events = new ArrayList<>();
        totalFaultsDetected = 0;
        totalNodesRerouted = 0;
    }

    public void logEvent(GridNode node, int severity) {
        String nodeId = "R" + node.getRow() + " C" + node.getCol();
        long timeStamp = System.currentTimeMillis();
        Instant readableDate = Instant.ofEpochMilli(timeStamp);
        String description = "Voltage dropped to " + String.valueOf(node.getVoltage());

        FaultEvent event = new FaultEvent(nodeId, readableDate, severity, description);
        events.add(event);
    }

    public void printLog() {
        System.out.println("---- Fault Log ----");
        if (events.isEmpty()) {
            System.out.println("No faults detected.");
            return;
        } else {
            System.out.println("Total faults detected : " + totalFaultsDetected);
            System.out.println("Total nodes rerouted  : " + totalNodesRerouted);
        }

        System.out.println();

        for (FaultEvent event : events) {
            System.out.println(event.toString());
        }
    }

    public ArrayList<FaultEvent> getEvents() {
        return events;
    }

    public int getEventCount() {
        return events.size();
    }

    public void clearLog() {
        events.clear();
    }

    public void addToTotalFaults() {
        totalFaultsDetected++;
    }

    public void addToTotalNodesRerouted() {
        totalNodesRerouted++;
    }
}
