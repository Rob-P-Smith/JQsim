package menus;

import fileIO.Reader;
import fileIO.Writer;
import measurement.QubitWire;
import qubits.ComplexGates;
import qubits.ComplexQubit;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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

    /**
     * Displays a welcome message when the simulation starts.
     */
    public static void welcomeMessage() {
        System.out.println("Welcome to JQsim");
    }

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
                    4. Run Simulation (pending implementation)
                    5. Save State To File
                    6. Load State From File
                    9. Display workingQubitWires
                    0. Exit
                    """);
            selection = Console.getInt();
            System.out.println("\033[H\033[2J");

            if (selection > 99) {
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
                    for(ComplexQubit qubit : workingQubits) {
                        qubitIDs.add(qubit.getQubitID());
                    }
                    selection = -1;
                }
                case 2 -> {
                    workingQubitWires = applyQubitGates(workingQubitWires, qubitIDs, selection);
                    selection = -1;
                }
                case 3 -> {
                    displayWireGates(workingQubitWires);
                    selection = -1;
                }
                case 4 -> {
                    System.out.println("Selected: " + selection);
                    selection = -1;
                }
                case 5 -> {
                    System.out.println("Selected: " + selection);
                    saveSystemStateToFile(workingQubits);
                    selection = -1;
                }
                case 6 -> {
                    System.out.println("Selected: " + selection);
                    workingQubits = loadSystemSateFromFile(workingQubits);
                    selection = -1;
                }
                case 9 -> {
                    System.out.println(Arrays.deepToString(workingQubitWires));
                }
                case 99 -> {
                    displayQubitStates(workingQubits);
                    selection = -1;
                }
            }
        }
    }

    private static void displayWireGates(QubitWire[] workingQubitWires) {
        for(QubitWire wire : workingQubitWires){
            wire.viewLine();
            System.out.println();
        }
    }

    /**
     * Applies quantum gates to selected qubits based on user input.
     *
     * @param workingQubitWires The array of working QubitWires.
     * @param selection     The user's gate selection.
     * @return The updated array of working qubits after applying gates.
     */
    private static QubitWire[] applyQubitGates(QubitWire[] workingQubitWires,Set<Integer> qubitIDs, int selection) {
        System.out.println("Selected: " + selection);
        if (workingQubitWires.length == 0) {
            System.out.println("Initialize Wires before applying gates.");
            return null;
        } else {
            selection = -1;
            do {
                System.out.println("Which wire (number) do you want to apply a gate to?");
                //Display qubit selection by getting the qubit's ID instead of the workingQubits array location
                for(Integer qubitID : qubitIDs) {
                    System.out.println("Qubit #: "+qubitID);
                }
                selection = Console.getInt();
                if(!qubitIDs.contains(selection)){
                    System.out.println("Please select a valid qubit to modify");
                    selection = -1;
                }
            } while (selection < 0 || !qubitIDs.contains(selection));
        }

        int gateToApply = -1;
        while (gateToApply < 0) {
            System.out.println("""
                    1. PauliX
                    2. PauliY
                    3. PauliZ
                    4. Hadamard
                    5. CNOT
                    0. Back to Main Menu
                    """);
            gateToApply = Console.getInt();
            if (gateToApply > 5) {
                System.out.println("Please make a valid choice");
                gateToApply = -1;
            }
            switch (gateToApply) {
                case 0 -> {
                    return workingQubitWires;
                }
                case 1 -> {
                    workingQubitWires[selection].addGate(new String[]{"PAULI_X", "0"});
                }
                case 2 -> {
                    workingQubitWires[selection].addGate(new String[]{"PAULI_Y", "0"});
                }
                case 3 -> {
                    workingQubitWires[selection].addGate(new String[]{"PAULI_Z", "0"});
                }
                case 4 -> {
                    workingQubitWires[selection].addGate(new String[]{"HADAMARD", "0"});
                }
                case 5 -> {
                    workingQubitWires[selection].addGate(gatesForCNOT(qubitIDs, selection));
                }
            }
        }
        return workingQubitWires;
    }

    private static String[] gatesForCNOT(Set<Integer> qubitIDs, int control){
        int target = -1;
        System.out.println("Qubit " + control + " selected as control qubit.");
        do {
            System.out.println("Which qubit (number) is the target qubit?");
            for(Integer qubitID : qubitIDs){
                System.out.println("Qubit #" + qubitID);
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
     * Applies a Controlled-NOT (CNOT) gate to selected qubits.
     *
     * @param workingQubits The array of working qubits.
     * @param control       The index of the control qubit.
     * @return The updated array of working qubits after applying the CNOT gate.
     */
    private static ComplexQubit[] applyCNOT(ComplexQubit[] workingQubits, int control) {
        int target = -1;
        System.out.println("Qubit " + control + " selected as control.");
        do {
            System.out.println("Which qubit (number) is the target qubit?");
            for (int i = 0; i < workingQubits.length; i++) {
                if (i != control) {
                    System.out.println("Qubit #" + i);
                }
            }
            target = Console.getInt();
            if (target < 0 || target > workingQubits.length - 1 || control == target) {
                System.out.println("Please select a valid target qubit");
                control = -1;
            }
        } while ((target < 0 || target > workingQubits.length - 1) && control != target);
//        workingQubits[control].setEntangledQubit(workingQubits[target]); //I think I can get rid of this now...

        System.out.println("Control Qubit is: " + workingQubits[control].getState()); //TODO remove this trace
        System.out.println("Target Qubit is: " + workingQubits[target].getState());//TODO remove this trace
        try {
            workingQubits[target] = ComplexGates.applyCNOT(workingQubits[control], workingQubits[target]);
        } catch (Exception e) {
            System.out.println("Exception in applying CNOT gate.");
            e.printStackTrace();
        }
        return workingQubits;
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
    private static ComplexQubit[] loadSystemSateFromFile(ComplexQubit[] workingQubits) {
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
        System.out.println("Current Qubit state: ");
        if (workingQubits.length == 0) {
            System.out.println("Initialize Qubits before displaying them.");
            return;
        }
        for (ComplexQubit qubit : workingQubits) {
            System.out.println(qubit + "\n");
        }
    }
}
