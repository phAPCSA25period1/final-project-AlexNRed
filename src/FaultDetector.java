import java.util.ArrayList;

public class FaultDetector {
    // declared a Grid calling it grid, and a voltageThreshold
    private Grid grid;
    private double voltageThreshold;

    /**
     * creates a faultDetector object
     * @param grid the power grid network
     * @param voltageThreshold the voltage limit to detect a fault
     */
    public FaultDetector(Grid grid, double voltageThreshold) {
        this.grid = grid;
        this.voltageThreshold = voltageThreshold;
    }

    /**
     * method that detectsFaults
     * creates a arrayList that holds GridNodes called Faults and creates a 2D array of GridNodes called nodes.
     * @return ArrayList<GridNode>
     */
    public ArrayList<GridNode> detectFaults() {
        ArrayList<GridNode> faults = new ArrayList<>();
        GridNode[][] nodes = grid.getNodes();

        for (int row = 0; row < grid.getRows(); row++) {
            for (int col = 0; col < grid.getCols(); col++) {
                GridNode node = nodes[row][col];

                if (node.getState() == NodeState.ACTIVE) {

                    if (node.getVoltage() < voltageThreshold) {

                        // this triggers a fault -> Voltage below threshold = fault
                        node.setTheState(NodeState.FAULT);
                        faults.add(node);
                    }

                }
            }
        }
        return faults;
    }

    /**
     * finds all the adjacent Nodes that are right next to the node. for example if the nodes is at posiiton (1,1), it will get all the adjacent nodes, so up down, left and right.
     * @param node the node that we want to find adjacent nodes near it.
     * @return ArrayList<GridNode>
     */
    public ArrayList<GridNode> getAdjacentNodes(GridNode node) {
        ArrayList<GridNode> neighbors = new ArrayList<>();
        int row = node.getRow();
        int col = node.getCol();

        if (row > 0) {
            neighbors.add(grid.getNode(row - 1, col));
        }

        if (row < grid.getRows() - 1 ) {
            neighbors.add(grid.getNode(row + 1, col));
        }

        if (col > 0) {
            neighbors.add(grid.getNode(row, col - 1));
        }

        if (col < grid.getCols() - 1) {
            neighbors.add(grid.getNode(row, col + 1));
        }
        return neighbors;
    }
}
