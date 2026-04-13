import java.util.Random;
import java.util.Scanner;


public class GridSimulator {
    public static void main(String[] args) {
        // Create a grid with 5 rows and 5 columns
        Scanner scan = new Scanner(System.in);
        Random random = new Random();

        //System.out.print("How many seconds would you like the simulation to run? (30, 60, 90 seconds)");
        //int seconds = scan.nextInt();


        Grid grid = new Grid(5, 5);
        FaultDetector faultDetector = new FaultDetector(grid, 90);
        // Print the voltage of the node at (2, 3)
        //GridNode node = grid.getNode(2, 3);
        //System.out.println("Voltage at (2, 3): " + node.getVoltage() + " volts");

        // Set the state of the node at (2, 3) to FAULT
        //node.setTheState(NodeState.FAULT);
        //System.out.println("State at (2, 3): " + node.getState());

        // this essentially creates a powerline between all the nodes horizontally and vertically, connection each one together.
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 4 - 1; col++) {
                grid.addPowerLine(grid.getNode(row, col), grid.getNode(row, col + 1), 50.0);
            }
        }

        for (int col = 0; col < 5; col++) {
            for (int row = 0; row < 4; row++) {
                grid.addPowerLine(grid.getNode(row, col), grid.getNode(row + 1, col), 50.0);
            }
        }


        // manually setting a fault
        grid.getNode(2,3).setTheState(NodeState.FAULT);
        double volt = grid.getNode(2,3).getVoltage();

        System.out.println(volt);
        grid.printGrid();
    }

    private static void voltrandomizer(Grid grid, Random random) {
        double max = 130.0;
        double min = 85.0;

        GridNode[][] nodes = grid.getNodes();

        for (int row = 0; row < grid.getRows(); row++) {
            for (int col = 0; col < grid.getCols(); col++) {
                GridNode node = nodes[row][col];

                if (node.getState() == NodeState.ISOLATED ||
                    node.getState() == NodeState.FAULT) {
                    continue;
                }


                double voltage = (random.nextDouble() * (max - min)) + min;
                node.setVoltage(voltage);
            }
        }
    }
}
