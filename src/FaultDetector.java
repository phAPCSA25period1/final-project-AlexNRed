import java.util.ArrayList;

public class FaultDetector {
    private Grid grid;
    private double voltageThreshold;

    public FaultDetector(Grid grid, double voltageThreshold) {
        this.grid = grid;
        this.voltageThreshold = voltageThreshold;
    }

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
