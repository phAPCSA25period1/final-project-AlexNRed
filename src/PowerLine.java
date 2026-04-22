public class PowerLine {
    // declared a specific GridNode called nodeA and nodeB and declared capacity and active.
    private GridNode nodeA;
    private GridNode nodeB;
    private double capacity;
    private boolean active;

    /**
     * creates a powerline object between nodeA to nodeB (one house to another house)
     * @param nodeA the house where you want to start adding a powerline to
     * @param nodeB the 2nd house to connect the powerline to
     * @param capacity the capacity that the powerline can handle.
     */
    public PowerLine(GridNode nodeA, GridNode nodeB, double capacity) {
        this.nodeA = nodeA;
        this.nodeB = nodeB;
        this.capacity = capacity;
        this.active = true; // Power line is active by default
    }

    /**
     * gets NodeA in the Grid
     * @return nodeA
     */
    public GridNode getNodeA() {
        return nodeA;
    }

    /**
     * gets NodeB in the Grid
     * @return nodeB
     */
    public GridNode getNodeB() {
        return nodeB;
    }

    /**
     * gets the capacity of the specific powerline in the Grid
     * @return capacity
     */
    public double getCapacity() {
        return capacity;
    }

    /**
     * returns a boolean to check if the powerline is active or not
     * @return active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * sets the powerline to a state
     * @return active
     */
    public void setActive(boolean active) {
        this.active = active;
    }

}
