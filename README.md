[![Open in Codespaces](https://classroom.github.com/assets/launch-codespace-2972f46106e565e64193e422d61a12cf1da4916b45550586e14ef0a7c637dd04.svg)](https://classroom.github.com/open-in-codespaces?assignment_repo_id=23542247)

Smart Grid Fault Simulator

What This Project does
The Smart Grid Fault Simulator is a terminal based simulation that models a city's power grid through ticks. Each Tick randomizes node voltages across the grid and when the voltage drops below a certain threshold, the node goes into a FAULT state. This triggers for it's neighboring nodes to be rerouted and the event is logged. The player can spend repair tokens to restore faulted nodes, but use them wisely because they are limited.

Who It's For
the project is intended for anyone who is curious and wants to learn more about how power-grid fault detection and rerouting logic works. 

How to Run the Program
to run the program, you would simply have to run it through the terminal

Technical Overview:
Main classes:
GridSimulator — Main which handles all user input, drives the simulation loop, and calls endGame() to display the final report

Grid — Owns the 2D array of GridNode objects and the ArrayList<PowerLine> and responsible for resetting active nodes and ticking immunity each round

GridNode — Represents a single node (house) in the grid and tracks position, voltage, state, and immunity ticks. Voltage is automatically adjusted when state changes through setTheState()

NodeState — Enum with four values: ACTIVE, FAULT, ISOLATED, REROUTED

FaultDetector — Scans the grid for under-threshold active nodes, promotes them to FAULT, and reroutes their active neighbors

FaultLog — Records FaultEvent objects, tracks total faults and reroutes, and prints the end-of-session log

FaultEvent — record of a single fault: node ID, timestamp, severity, and description

PowerLine — Connects two GridNode objects with a capacity value and can be changed to active/inactive

Key data structures:

GridNode[][] nodes inside Grid — the core 2D array that represents the city grid

ArrayList<PowerLine> lines — adjacent list of power connections

ArrayList<FaultEvent> events inside FaultLog which are ordered by event history

Program logic per tick:

Reset all non-isolated nodes to ACTIVE
Tick down immunity counters
Randomize voltages within the difficulty-defined range
FaultDetector.detectFaults() flags under-threshold nodes
For each fault, log the event and reroute active neighbors
Print the grid and offer the player a repair token

Class Diagram:

<img width="673" height="1032" alt="Untitled Diagram drawio" src="https://github.com/user-attachments/assets/db5f524f-a9d7-48c3-9d43-96c18711a30a" />


Future Improvements
If I had more time, I would've definitely made a tutorial and more game like since it's kind of bare bones and I would've liked to add a weather system that affects the nodes like it might rain which would increase the chance of the node going faulty.

