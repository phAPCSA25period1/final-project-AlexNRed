import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class GridSimulator {
    public static void main(String[] args) {
        // Create a grid with 5 rows and 5 columns
        Scanner scan = new Scanner(System.in);
        Random random = new Random();

        Grid grid = new Grid(5, 5);
        FaultDetector detector = new FaultDetector(grid, 90);
        FaultLog log = new FaultLog();

        System.out.print("How many seconds would you like the simulation to run? (30, 60, 90 seconds)");
        int seconds = scan.nextInt();

        System.out.println("Starting sim for " + seconds + " seconds: ");



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



        try {
            TimeUnit.SECONDS.sleep(2);
            for (int tick = 1; tick <= seconds; tick++) {
                simulation(grid, detector, log, random);
                TimeUnit.SECONDS.sleep(1);


            }


            System.out.println("\n=== Simulation complete ===");
            TimeUnit.SECONDS.sleep(1);
            System.out.println("Total ticks run: " + seconds);
            System.out.println();
            TimeUnit.SECONDS.sleep(1);
            log.printLog();


        } catch (InterruptedException  e) {
            System.out.print("sleep was interrupted");
        }


        scan.close();

    }

        // manually setting a fault
        //grid.getNode(2,3).setTheState(NodeState.FAULT);
        //double volt = grid.getNode(2,3).getVoltage();

        //System.out.println(volt);
        //grid.printGrid();


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


                double fault = (random.nextDouble() * (max - min)) + min;
                node.setVoltage(fault);
            }
        }
    }

    private static void simulation(Grid grid, FaultDetector detector, FaultLog faultLog, Random random) {

            grid.resetActiveNodes();

            voltrandomizer(grid, random);

            ArrayList<GridNode> faults = detector.detectFaults();

            for (GridNode fault : faults) {
                faultLog.logEvent(fault, 1);
                detector.getAdjacentNodes(fault);
                faultLog.addToTotalFaults();
                faultLog.addToTotalNodesRerouted();
            }

            grid.printGrid();

            System.out.println("Faults this tick: " + faults.size());


            System.out.println();


    }
}
