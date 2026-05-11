import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class GridSimulator {
        public static void main(String[] args) {
            // Create a grid with 5 rows and 5 columns
            Scanner scan = new Scanner(System.in);
            Random random = new Random();

            clearScreen();
            System.out.println("=========================================");
            System.out.println("      SMART GRID FAULT SIMULATOR");
            System.out.println("=========================================");
            System.out.println();
            System.out.println("A city's power grid is in your hands.");
            System.out.println("Keep the lights on as long as you can.");
            System.out.println();

            // name the grid
            System.out.print("Name your power grid: ");
            String gridName = scan.nextLine();
            System.out.println();

            // grid size
            System.out.println("Select a grid Size: ");
            System.out.println("1. Small  (4x4) — easier");
            System.out.println("2. Medium (5x5) — normal");
            System.out.println("3. Large  (7x7) — hard");
            System.out.print("Choice (1-3): ");
            int sizeChoice = scan.nextInt();
            int gridSize;
            switch (sizeChoice) {
                case 1:
                    gridSize = 4;
                    break;
                case 3:
                    gridSize = 7;
                    break;
                default:
                    gridSize = 5;
                    break;
            }
            System.out.println();

            // difficulty
            System.out.println("Select Difficulty");
            System.out.println("1. Easy");
            System.out.println("1. Medium");
            System.out.println("1. Hard");
            System.out.println("Choice (1-3): ");
            int choice  = scan.nextInt();
            double threshold;
            int repairTokens;
            switch (choice) {
                case 1:
                    threshold = 80.0;
                    repairTokens = 5;
                    break;
                case 3:
                    threshold = 100.0;
                    repairTokens = 1;
                    break;
                default:
                    threshold = 90.0;
                    repairTokens = 3;
                    break;
            }
            System.out.println();

            // set the time
            System.out.print("How many ticks? (10 = fast, 30 = medium, 60 = Long): ");
            int totalTicks = scan.nextInt();
            scan.nextLine(); // consume leftover newline
            System.out.println();

            // confirm the settings
            System.out.println("-----------------------------------------");
            System.out.println("Grid name  : " + gridName);
            System.out.println("Grid size  : " + gridSize + "x" + gridSize);
            System.out.println("Difficulty : " + getDifficulty(choice));
            System.out.println("Duration   : " + totalTicks + " ticks");
            System.out.println("Repairs    : " + repairTokens + " tokens");
            System.out.println("-----------------------------------------");
            System.out.print("Start simulation? (y/n): ");
            String confirm = scan.nextLine();
            if (!confirm.equalsIgnoreCase("y")) {
                System.out.println("Simulation cancelled.");
                scan.close();
                return;
            }

            Grid grid = new Grid (gridSize, gridSize);
            FaultDetector detector = new FaultDetector(grid, threshold);
            FaultLog log = new FaultLog();


            // this essentially creates a powerline between all the nodes horizontally and vertically, connection each one together.
            for (int row = 0; row < 5; row++) {
                for (int col = 0; col < 4; col++) {
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

                for (int tick = 1; tick <= totalTicks; tick++) {
                    simulation(grid, detector, log, random, tick, totalTicks);
                    TimeUnit.SECONDS.sleep(1);


                }


                System.out.println("\n=== Simulation complete ===");
                TimeUnit.SECONDS.sleep(1);
                System.out.println("Total ticks run: " + totalTicks);
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

        private static String getDifficulty(int choice) {
            switch (choice) {
                case 1:
                    return "Easy";
                case 3:
                    return "Hard";
                default:
                    return "medium";
            }
        }

        private static void clearScreen() {
            System.out.print("\033[H\033[2J");
            System.out.flush();
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


                    double fault = (random.nextDouble() * (max - min)) + min;
                    node.setVoltage(fault);
                }
            }
        }

        private static void simulation(Grid grid, FaultDetector detector, FaultLog faultLog, Random random, int tickNum, int totalTicks) {

                clearScreen();

                System.out.println("=== Tick " + tickNum + " / " + totalTicks + " ===");

                grid.resetActiveNodes();

                voltrandomizer(grid, random);

                ArrayList<GridNode> faults = detector.detectFaults();

                for (GridNode fault : faults) {
                    faultLog.logEvent(fault, 1);
                    faultLog.addToTotalFaults();

                    int rerouted = detector.rerouteAround(fault);
                    if (rerouted > 0) {
                        faultLog.addToTotalNodesRerouted();
                        System.out.println("  Rerouted " + rerouted + " nodes around R" + fault.getRow() + "C" + fault.getCol());
                    }
                }

                grid.printGrid();



                System.out.println("Faults this tick: " + faults.size());
                System.out.println("Grid Health: " + getGridHealth(grid) + "%");

                System.out.println();


        }

        private static int getGridHealth(Grid grid) {
            int active = 0;
            int total = grid.getRows() * grid.getCols();
            GridNode[][] nodes = grid.getNodes();

            for (int r = 0; r < grid.getRows(); r++) {
                for (int c = 0; c < grid.getCols(); c++) {
                    NodeState s = nodes[r][c].getState();
                    if (s == NodeState.ACTIVE || s == NodeState.REROUTED) {
                        active++;
                    }
                }
            }

            return (int)((active /  (double) total ) * 100);
        }
    }
