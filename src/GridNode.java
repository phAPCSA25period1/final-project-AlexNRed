
public class GridNode {
    // declared row, col, voltage, load, and a state for every GridNode
    private int row;
    private int col;
    private double voltage;
    private double load;
    private NodeState state; // An enum to represent the state of the node

    /**
    * the constructor to create a GridNode. Takes in row, col, the voltage, and the state of a gridNode
    */
    public GridNode(int row, int col, double voltage, NodeState state) {
        this.row = row;
        this.col = col;
        this.voltage = voltage;
        this.load = 0.0; // Default load
        this.state = state; // Default state
    }
    /**
     * returns the row at which the GridNode is at
     * @return row
     */
    public int getRow() {
        return row;
    }


    /**
     * returns the column at which the GridNode is at
     * @return col
     */
    public int getCol() {
        return col;
    }

    /**
     * returns the voltage that the GridNode is
     * @return voltage
     */
    public double getVoltage() {
        return voltage;
    }

    /**
     * changes the voltage of the GridNode
     * @param voltage sets the voltage of the GridNode
     */
    public void setVoltage(double voltage) {
        this.voltage = voltage;
    }

    /**
     * getter method to get the state of the GridNode
     * @return state
     */
    public NodeState getState() {
        return state;
    }

    /**
     * sets the state of the GridNode
     * @param state the state to which the GridNode will change into
     */
    public void setTheState(NodeState state) {
        this.state = state;

        switch (state) {
            case FAULT:
                this.voltage = 0.0;       // wire is broken — no voltage
                break;
            case ACTIVE:
                this.voltage = 120.0;     // restored to normal grid voltage
                break;
            case REROUTED:
                this.voltage = 108.0;     // slightly reduced — alternate path has resistance
                break;
            case ISOLATED:
                this.voltage = 0.0;       // deliberately cut off — also zero
                break;
        }
    }
}
