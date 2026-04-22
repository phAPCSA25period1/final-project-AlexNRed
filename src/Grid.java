import java.util.ArrayList;

public class Grid {
    // declared a 2D array of GridNodes called nodes and an arraylist of Powerline Objects called lines.
    private GridNode[][] nodes;
    private ArrayList<PowerLine> lines;
    // the default voltage will be set to 120.0 volts
    /**
     * creates a constructor called Grid that takes in rows and columns. Initalizes the 2D array of GridNodes and an ArrayList,
     * and fills each value of Grid with a GridNode. Think of a grid as a neighborhood from the top view and a gridNode is each house.
     *
     */
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

    /**
     * gets the GridNode at a current position
     * @param row the row that the gridNode is at
     * @param col the col that the gridNode is at
     * @return nodes[row][col]
     */
    public GridNode getNode(int row, int col) {
        return nodes[row][col];
    }

    /**
     * return the nodes
     * @return nodes
     */
    public GridNode[][] getNodes() {
        return nodes;
    }

    /**
     * returns the amount of rows that are in the Grid
     * @return nodes.length
     */
    public int getRows() {
        return nodes.length;
    }

    /**
     * returns the amount of cols that are in the Grid
     * @return nodes.[0]length
     */
    public int getCols() {
        return nodes[0].length;
    }

    /**
     * creates a powerline between nodeA to nodeB (one house to another house)
     * @param nodeA the house where you want to start adding a powerline to
     * @param nodeB the 2nd house to connect the powerline to
     * @param capacity the capacity that the powerline can handle.
     */
    public void addPowerLine(GridNode nodeA, GridNode nodeB, double capacity) {
        lines.add(new PowerLine(nodeA, nodeB, capacity));
        // PowerLine line = new PowerLine(nodeA, nodeB, capacity);
        // lines.add(line);
    }

    /**
     * gets the ArrayList of lines
     * @return lines
     */
    public ArrayList<PowerLine> getPowerLines() {
        return lines;
    }

    /**
     * prints the entire Grid
     */
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

    /**
     * if the GridNode is not purposely cut off (ISOLATED), then after each cycle of the simulation,
     * the nodes are reset back to Active.
     */
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
