import java.util.ArrayList;

public class Grid {
    private GridNode[][] nodes;
    private ArrayList<PowerLine> lines;
    // the default voltage will be set to 120.0 volts
    public Grid(int rows, int cols) {
        nodes = new GridNode[rows][cols];
        lines = new ArrayList<>();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                // Constructor call
                nodes[row][col] = new GridNode(row, col, 120.0, NodeState.ACTIVE);
            }
        }
    }


    public GridNode getNode(int row, int col) {
        return nodes[row][col];
    }

    public GridNode[][] getNodes() {
        return nodes;
    }

    public int getRows() {
        return nodes.length;
    }

    public int getCols() {
        return nodes[0].length;
    }

    public void addPowerLine(GridNode nodeA, GridNode nodeB, double capacity) {
        lines.add(new PowerLine(nodeA, nodeB, capacity));
        // PowerLine line = new PowerLine(nodeA, nodeB, capacity);
        // lines.add(line);
    }

    public ArrayList<PowerLine> getPowerLines() {
        return lines;
    }

    public void printGrid() {
        System.out.println("---- Grid State ----");
        for (int row = 0; row < nodes.length; row++) {
            for (int col = 0; col < nodes[row].length; col++) {
                NodeState state = nodes[row][col].getState();
                switch (state) {
                    case ACTIVE:
                        System.out.print("[ ] ");
                        break;
                    case FAULT:
                        System.out.print("[!] ");
                        break;
                    case ISOLATED:
                        System.out.print("[X] ");
                        break;
                    case REROUTED:
                        System.out.print("[~] ");
                        break;
                }
            }
            System.out.println();
        }
        System.out.println();
        System.out.println("[ ]=active  [!]=fault  [X]=isolated  [~]=rerouted\n");
    }

    public void resetActiveNodes() {
        for (int row = 0; row < getRows(); row++) {
            for (int col = 0; col < getCols(); col++) {
                GridNode node = nodes[row][col];

                if (node.getState() != NodeState.ISOLATED) {
                    node.setTheState(NodeState.ACTIVE);
                }
            }
        }
    }
}
