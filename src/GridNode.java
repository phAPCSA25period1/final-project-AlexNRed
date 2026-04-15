
public class GridNode {
    private int row;
    private int col;
    private double voltage;
    private double load;
    private NodeState state; // An enum to represent the state of the node

    public GridNode(int row, int col, double voltage, NodeState state) {
        this.row = row;
        this.col = col;
        this.voltage = voltage;
        this.load = 0.0; // Default load
        this.state = state; // Default state
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public double getVoltage() {
        return voltage;
    }

    public void setVoltage(double voltage) {
        this.voltage = voltage;
    }

    public NodeState getState() {
        return state;
    }

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
