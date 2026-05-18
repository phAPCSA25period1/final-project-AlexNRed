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
            System.out.println("2. Medium");
            System.out.println("3. Hard");
            System.out.println("Choice (1-3): ");
            int choice  = scan.nextInt();
            double threshold;
            int repairTokens;
            double voltMin;

            switch (choice) {
                case 1:
                    threshold = 80.0;
                    repairTokens = 5;
                    voltMin = 76.0;
                    break;
                case 3:
                    threshold = 100.0;
                    repairTokens = 1;
                    voltMin = 84.0;
                    break;
                default:
                    threshold = 90.0;
                    repairTokens = 3;
                    voltMin = 80.0;
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
            for (int row = 0; row < gridSize; row++) {
                for (int col = 0; col < gridSize - 1; col++) {
                    grid.addPowerLine(grid.getNode(row, col), grid.getNode(row, col + 1), 50.0);
                }
            }

            for (int col = 0; col < gridSize; col++) {
                for (int row = 0; row < gridSize - 1; row++) {
                    grid.addPowerLine(grid.getNode(row, col), grid.getNode(row + 1, col), 50.0);
                }
            }



            try {

                TimeUnit.SECONDS.sleep(2);

                for (int tick = 1; tick <= totalTicks; tick++) {


                    simulation(grid, detector, log, random, tick, totalTicks, threshold, voltMin);

                    TimeUnit.SECONDS.sleep(1);

                    if (repairTokens > 0) {
                        System.out.println("Repair tokens remaining: " + repairTokens);
                        System.out.print("Use a repair token? (y/n): ");
                        System.out.flush();
                        String repairChoice = scan.nextLine();

                        if (repairChoice.equalsIgnoreCase("y")) {
                            System.out.print("Enter node to repair (row col): ");
                            int repairRow = scan.nextInt();
                            int repairCol = scan.nextInt();
                            scan.nextLine();

                            GridNode target = grid.getNode(repairRow, repairCol);

                            if (target.getState() == NodeState.FAULT) {
                                target.setTheState(NodeState.ACTIVE);
                                target.grantImmunity(tick);
                                repairTokens--;
                                System.out.println("Node R" + repairRow + "C" + repairCol + " repaired. Tokens left: " + repairTokens);
                                log.addToTotalNodesRerouted();
                            } else {
                                System.out.println("Node is not faulted");
                            }
                        }
                    } else {
                        System.out.println("You have no more repair Tokens");
                    }

                    TimeUnit.SECONDS.sleep(1);
                }


                System.out.println("\n=== Simulation complete ===");
                TimeUnit.SECONDS.sleep(2);
                clearScreen();
                endGame(gridName, log, totalTicks, grid);
                TimeUnit.SECONDS.sleep(2);
                log.printLog();


            } catch (InterruptedException  e) {
                System.out.print("sleep was interrupted");
            }


            scan.close();

        }





        /**
         * gets the difficulty set by the user
         * @param choice
         * @return difficulty choice
         */
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

        /**
         * randomizes the voltage of every node each tick, and different for each difficulty
         * @param grid the power grid where the node voltages are updated at
         * @param random use the random class to generate random voltages
         * @param voltMin the minimum voltage possible for each difficulty
         */
        private static void voltrandomizer(Grid grid, Random random, double voltMin) {
            double max = 130.0;

            GridNode[][] nodes = grid.getNodes();

            for (int row = 0; row < grid.getRows(); row++) {
                for (int col = 0; col < grid.getCols(); col++) {
                    GridNode node = nodes[row][col];

                    if (node.getState() == NodeState.ISOLATED ||
                        node.getState() == NodeState.FAULT ||
                        node.isImmune() ) {
                        continue;
                    }


                    double fault = (random.nextDouble() * (max - voltMin)) + voltMin;
                    node.setVoltage(fault);
                }
            }
        }

        /**
         * basically one full simulation tick
        * @param detector the fault detector that scans and reroutes the grid
        * @param faultLog the log that records every fault event
        * @param random uses random class for voltrandomizer
        * @param tickNum the current tick number
        * @param totalTicks the total number of ticks in this simulation run
        * @param threshold the voltage below which a node is considered faulted
        * @param voltMin the minimum voltage generated this tick set by difficulty

         */
        private static void simulation(Grid grid, FaultDetector detector, FaultLog faultLog, Random random, int tickNum, int totalTicks, double threshold, double voltMin) {



                System.out.println("=== Tick " + tickNum + " / " + totalTicks + " ===");

                grid.resetActiveNodes();
                grid.tickImmunity();

                voltrandomizer(grid, random, voltMin);

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

        /**
         * @param grid the power grid
         * 
         */
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

        private static void endGame(String gridName, FaultLog log, int totalTicks, Grid grid) {
            int health = getGridHealth(grid);
            int faults = log.getEventCount();

            int score = health;
            if (faults == 0) {
                score = 100;
            }

            String grade;

            if (score >= 90) {
                grade = "S";
            } else if (score >= 75) {
                grade = "A";
            } else if (score >= 60) {
                grade = "B";
            } else if (score >= 45) {
                grade = "C";
            } else {
                grade = "F";
            }

            System.out.println("=========================================");
            System.out.println("         " + gridName.toUpperCase() + " — FINAL REPORT");
            System.out.println("=========================================");
            System.out.println();
            System.out.println("  Ticks survived    : " + totalTicks);
            System.out.println("  Final grid health : " + health + "%");
            System.out.println("  Total faults      : " + faults);
            System.out.println();
            System.out.println("  Performance grade : " + grade);
            System.out.println();
            System.out.println("=========================================");
        }
    }
