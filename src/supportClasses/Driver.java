package supportClasses;

import supportClasses.FileIO.Reader;
import measurement.QubitWire;
import complexClasses.ComplexGates;
import complexClasses.ComplexQubit;
import supportClasses.FileIO.Writer;

import java.io.File;
import java.util.*;

/**
 * The main class that drives the JQsim quantum computing simulation.
 *
 * <p>
 * This class provides a text-based menu system for interacting with quantum qubits
 * and applying quantum gates. It allows users to initialize qubits, apply gates
 * (such as Pauli-X, Pauli-Y, Pauli-Z, Hadamard, and CNOT), display qubit states,
 * save and load system states to/from files.
 * </p>
 *
 * <p>
 * The menu system uses {@link Console} for user input and {@link ComplexGates} for
 * applying quantum gates to {@link ComplexQubit} objects.
 * </p>
 *
 * @author Robert Smith
 * @version 0.1
 * @since 25 June 2024
 */
public class Driver {
    private static final boolean DEBUG = false;

    /**
     * Displays the top-level menu for interacting with quantum qubits and gates.
     * Keeps looping until the user chooses to exit (selection 0).
     */
    public static void topMenu() {
        ComplexQubit[] workingQubits = new ComplexQubit[0];
        QubitWire[] workingQubitWires = new QubitWire[0];
        Set<Integer> qubitIDs = new HashSet<>();
        int selection = -1;

        while (selection != 0) {
            System.out.println("""
                    \n1. Initialize Wires
                    2. Add Gate to wire
                    3. Display Wire Gates
                    4. Run Simulation (pending full implementation)
                    5. Display executed work log
                    6. NOP
                    7. NOP
                    8. Save State To File
                    9. Load State From File
                                        
                    0. Exit
                    """);
            selection = Console.getInt();
            System.out.println("\033[H\033[2J");

            if (selection > 9) {
                System.out.println("Selection out of range, select a valid option.");
                selection = -1;
            }
            switch (selection) {
                case 0 -> {
                    System.out.println("Goodbye");
                    System.exit(0);
                }
                case 1 -> {
                    Object[] workingArrays = initializeWires(workingQubits, workingQubitWires);
                    // cast the returns into their correct types and assign them
                    workingQubits = (ComplexQubit[]) workingArrays[0];
                    workingQubitWires = (QubitWire[]) workingArrays[1];
                    for (ComplexQubit qubit : workingQubits) {
                        qubitIDs.add(qubit.getQubitID());
                    }
                    selection = -1;
                }
                case 2 -> {
                    workingQubitWires = addGateToWire(workingQubitWires, qubitIDs, selection);
                    selection = -1;
                }
                case 3 -> {
                    displayWireGates(workingQubitWires);
                    selection = -1;
                }
                case 4 -> {
                    runSimulation(workingQubitWires, workingQubits);
                    selection = -1;
                }
                case 5 -> {
                    for (QubitWire wire : workingQubitWires) {
                        List<String[]> workLog = wire.getWorkLog();
                        for (String[] workItem : workLog) {
                            System.out.println(Arrays.toString(workItem));
                        }
                    }
                }
                case 8 -> {
                    saveSystemStateToFile(workingQubits);
                    selection = -1;
                }
                case 9 -> {
                    workingQubits = loadSystemStateFromFile(workingQubits);
                    selection = -1;
                }
            }
        }
    }

    /**
     * Applies quantum gates to selected qubits based on user input.
     *
     * @param workingQubitWires The array of working QubitWires.
     * @param selection         The user's gate selection.
     * @return The updated array of working qubits after applying gates.
     */
    private static QubitWire[] addGateToWire(QubitWire[] workingQubitWires, Set<Integer> qubitIDs, int selection) {
        System.out.println("Selected: " + selection);
        if (workingQubitWires.length == 0) {
            System.out.println("Initialize wires before applying gates.");
            return null;
        } else {
            selection = -1;
            do {
                System.out.println("Which wire (number) do you want to apply a gate to?");
                //Display qubit selection by getting the qubit's ID instead of the workingQubits array location
                for (Integer qubitID : qubitIDs) {
                    System.out.println("Qubit #: " + qubitID);
                }
                selection = Console.getInt();
                if (!qubitIDs.contains(selection)) {
                    System.out.println("Please select a valid qubit to modify");
                    selection = -1;
                }
            } while (!qubitIDs.contains(selection));
        }

        int gateToApply = -1;
        while (gateToApply < 0) {
            System.out.println("Applying gates for qubit #" + selection);
            System.out.println("""
                    1. PauliX
                    2. PauliY
                    3. PauliZ
                    4. Hadamard
                    5. CNOT
                    6. S Gate
                    7. T Gate
                    8. NOP
                    9. Identity
                    
                    0. Back to Main Menu
                    """);
            gateToApply = Console.getInt();
            if (gateToApply > 10) {
                System.out.println("Please make a valid choice");
                gateToApply = -1;
            }
            switch (gateToApply) {
                case 0 -> {
                    return workingQubitWires;
                }
                case 1 -> {
                    workingQubitWires[selection].addGate(new String[]{"PAULI_X", "0"});
                    gateToApply = -1;
                }
                case 2 -> {
                    workingQubitWires[selection].addGate(new String[]{"PAULI_Y", "0"});
                    gateToApply = -1;
                }
                case 3 -> {
                    workingQubitWires[selection].addGate(new String[]{"PAULI_Z", "0"});
                    gateToApply = -1;
                }
                case 4 -> {
                    workingQubitWires[selection].addGate(new String[]{"HADAMARD", "0"});
                    gateToApply = -1;
                }
                case 5 -> {
                    workingQubitWires[selection].addGate(gatesForCNOT(qubitIDs, selection));
                    gateToApply = -1;
                }
                case 6 -> {
                    System.out.println("Case 6");
                    workingQubitWires[selection].addGate(new String[]{"SGate", "0"});
                    gateToApply = -1;
                }
                case 7 -> {
                    System.out.println("Case 7");
                    workingQubitWires[selection].addGate(new String[]{"TGate", "0"});
                    gateToApply = -1;
                }
                case 8 -> {
                    System.out.println("Case 8");
                    gateToApply = -1;
                }
                case 9 -> {
                    System.out.println("Case 9");
                    workingQubitWires[selection].addGate(new String[]{"IDENTITY", "0"});
                    gateToApply = -1;
                }
            }
        }
        return workingQubitWires;
    }

    private static void runSimulation(QubitWire[] workingQubitWires, ComplexQubit[] workingQubits) {
        int workLength = workingQubitWires[0].getWorkQueue().size();
        //Verify that each wire has an equal number of operations on it to ensure proper in-order execution of sim
        for (QubitWire workingQubitWire : workingQubitWires) {
            if (workingQubitWire.getWorkQueue().size() != workLength) {
                System.out.println("Uneven work length across wires, cannot execute simulation.");
                return;
            }
        }
        String[] currentOP;
        String currentGateType = "IDENTITY";
        int currentQubitID = -1;
        int controlQubitCNOT = -1;
        int targetQubitCNOT = -1;

        //for each wire get operation i and execute i for each wire
        //increment i, then execute each wire's 'i' gate again until done
        for (int i = 0; i < workLength; i++) {
            for (QubitWire activeWire : workingQubitWires) {
                for (ComplexQubit activeQubit : workingQubits) {
                    currentQubitID = activeQubit.getQubitID();
                    if (currentQubitID == activeWire.getWireNumber()) {
                        currentOP = activeWire.getNextGate(activeQubit);
                        currentGateType = currentOP[0];
                        if (currentGateType.equals("CNOT")) {
                            controlQubitCNOT = Integer.parseInt(currentOP[1]);
                            targetQubitCNOT = Integer.parseInt(currentOP[2]);
                        }
                        if (DEBUG)System.out.println("Dirver, Line 237, got \'" + Arrays.toString(currentOP) + "\' as gate to apply.");
                        gateExecutor(currentGateType, workingQubits, currentQubitID, controlQubitCNOT, targetQubitCNOT);
                        if(DEBUG) System.out.println("Current OP :"+currentGateType+" working qubit: "+currentQubitID);
                        if(DEBUG) displayQubitStates(workingQubits);
                    }
                }
            }
        }
        displayQubitStates(workingQubits);
    }

    private static void gateExecutor(String currentGateType,
                                     ComplexQubit[] workingQubits,
                                     int currentQubitID,
                                     int controlQubitCNOT,
                                     int targetQubitCNOT) {
        switch (currentGateType) {
            case "PAULI_X" -> {
                if (DEBUG)System.out.println("Applying PAULI_X");
                workingQubits[currentQubitID].setState(ComplexGates.applyPauliX(workingQubits[currentQubitID].getState()));
            }
            case "PAULI_Y" -> {
                if (DEBUG) System.out.println("Applying PAULI_Y");
                workingQubits[currentQubitID].setState(ComplexGates.applyPauliY(workingQubits[currentQubitID].getState()));
            }
            case "PAULI_Z" -> {
                if (DEBUG)System.out.println("Applying PAULI_Z");
                workingQubits[currentQubitID].setState(ComplexGates.applyPauliZ(workingQubits[currentQubitID].getState()));
            }
            case "HADAMARD" -> {
                if (DEBUG)System.out.println("Applying HADAMARD");
                workingQubits[currentQubitID].setState(ComplexGates.applyHadamard(workingQubits[currentQubitID].getState()));

            }
            case "CNOT" -> {
                if(DEBUG) System.out.println("Applying CNOT");
                ComplexQubit controlQubit = null, targetQubit = null;
                for (ComplexQubit qubit : workingQubits) {
                    if (qubit.getQubitID() == controlQubitCNOT) {
                        controlQubit = qubit;
                    }
                    if (qubit.getQubitID() == targetQubitCNOT) {
                        targetQubit = qubit;
                    }
                }
                for (ComplexQubit qubit : workingQubits) {
                    if (qubit.getQubitID() == targetQubitCNOT) {
                        ComplexGates.applyCNOT(controlQubit, targetQubit);
                        System.out.println("Resultant qubit: \n"+qubit);
                    }
                }
            }
            case "IDENTITY" -> {
                if (DEBUG)System.out.println("Applying IDENTITY");
                workingQubits[currentQubitID].setState(ComplexGates.applyIdentity(workingQubits[currentQubitID].getState()));
            }
            case "SGate" -> {
                if(DEBUG) System.out.println("Applying S Gate");
                workingQubits[currentQubitID].setState(ComplexGates.applySGate(workingQubits[currentQubitID].getState()));
            }
            case "TGate" -> {
                if(DEBUG) System.out.println("Applying T Gate");
                workingQubits[currentQubitID].setState(ComplexGates.applyTGate(workingQubits[currentQubitID].getState()));
            }
        }
    }

    private static void topMenuSelector() {
        //this will eventually move the switch out of the topMenu() into here to split getting a choice from
        //executing a choice tasks
    }

    private static void displayWireGates(QubitWire[] workingQubitWires) {
        for (QubitWire wire : workingQubitWires) {
            wire.viewLine();
            System.out.println();
        }
    }

    private static String[] gatesForCNOT(Set<Integer> qubitIDs, int control) {
        int target = -1;
        System.out.println("Qubit " + control + " selected as control qubit.");
        do {
            System.out.println("Which qubit (number) is the target qubit?");
            for (Integer qubitID : qubitIDs) {
                if (qubitID != control) System.out.println("Qubit #" + qubitID);
            }
            target = Console.getInt();
            if (!qubitIDs.contains(target) || control == target) {
                System.out.println("Please select a valid target qubit");
                target = -1;
            }
        } while (!qubitIDs.contains(target) || control == target);
        return new String[]{"CNOT", String.valueOf(control), String.valueOf(target)};
    }

    /**
     * Initializes all qubits to a zero state inside the {@code workingQubits} array.
     *
     * @param workingQubits The array of working qubits.
     * @return The initialized array of working qubits.
     */
    private static Object[] initializeWires(ComplexQubit[] workingQubits, QubitWire[] workingQubitWires) {
        System.out.println("How many wires do you want to initialize?");
        int quantityQubitsInitialized = Console.getInt();
        try {
            workingQubits = new ComplexQubit[quantityQubitsInitialized];
            workingQubitWires = new QubitWire[quantityQubitsInitialized];
            for (int i = 0; i < quantityQubitsInitialized; i++) {
                workingQubits[i] = new ComplexQubit();
                workingQubitWires[i] = new QubitWire(workingQubits[i].getQubitID());
            }
        } catch (Exception e) {
            System.out.println("Error initializing qubits and wires");
            throw new RuntimeException(e);
        }
        System.out.println(quantityQubitsInitialized + " initialized.");
        return new Object[]{workingQubits, workingQubitWires};
    }

    /**
     * Loads the system state from a file (pending implementation).
     *
     * @param workingQubits The array of working qubits.
     */
    private static ComplexQubit[] loadSystemStateFromFile(ComplexQubit[] workingQubits) {
        System.out.println("Please provide the file name in the savedStates folder to open: ");
        String[] contents = new File("./savedStates").list();
        System.out.println("savedStates currently contains: \n");
        for (String content : contents) {
            System.out.println(content);
        }
        String fileChoice = Console.getString();
        workingQubits = Reader.loadQubits("./savedStates/" + fileChoice);
        System.out.println("Loading state from file");
        return workingQubits;
    }

    /**
     * Saves the system state to a file (pending implementation).
     *
     * @param workingQubits The array of working qubits.
     */
    private static void saveSystemStateToFile(ComplexQubit[] workingQubits) {
        System.out.println("Provide a file name to save as:");
        String fileName = Console.getString();
        try {
            boolean saveAttempt = Writer.saveFile(fileName, workingQubits);
            if (saveAttempt) {
                System.out.println("Saving state to file.");
            } else {
                System.out.println("File not saved successfully.");
            }
        } catch (Exception e) {
            System.out.println("Error saving file, check file name and location.");
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }

    /**
     * Displays the current states of all qubits in the system.
     *
     * @param workingQubits The array of working qubits.
     */
    private static void displayQubitStates(ComplexQubit[] workingQubits) {
        System.out.println("Resulting Qubit state(s): \n");
        if (workingQubits.length == 0) {
            System.out.println("Initialize Qubits before displaying them.");
            return;
        }
        for (ComplexQubit qubit : workingQubits) {
            System.out.println(qubit + "\n");
        }
    }

    /**
     * Displays a welcome message when the simulation starts.
     */
    public static void welcomeMessage() {
        System.out.println("Welcome to JQsim");
    }

    /**
     * Main method that starts the simulation by displaying a welcome message
     * and launching the top-level menu.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        welcomeMessage();
        topMenu();
    }


}
