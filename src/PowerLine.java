public class PowerLine {
    private GridNode nodeA;
    private GridNode nodeB;
    private double capacity;
    private boolean active;

    public PowerLine(GridNode nodeA, GridNode nodeB, double capacity) {
        this.nodeA = nodeA;
        this.nodeB = nodeB;
        this.capacity = capacity;
        this.active = true; // Power line is active by default
    }

    public GridNode getNodeA() {
        return nodeA;
    }

    public GridNode getNodeB() {
        return nodeB;
    }

    public double getCapacity() {
        return capacity;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
