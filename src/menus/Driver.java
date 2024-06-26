package menus;

import qubits.ComplexGates;
import qubits.ComplexQubit;

public class Driver {
    public static void main(String[] args) {
        welcomeMessage();
        topMenu();
    }

    public static void welcomeMessage() {
        System.out.println("Welcome to JQsim");
    }

    public static void topMenu() {
        ComplexQubit[] workingQubits = new ComplexQubit[0];
        int selection = -1;

        while (selection != 0) {
            System.out.println("""
                    \n1. Initialize Qubits
                    2. Apply Gates
                    3. Display Qubit States
                    4. Run Simulation
                    5. Save State To File
                    6. Load State From File
                    0. Exit
                    """);
            selection = Console.getInt();
            System.out.println("\033[H\033[2J");

            if (selection > 6) {
                System.out.println("Selection ouf of range, select a valid option.");
                selection = -1;
            }
            switch (selection) {
                //Exit JQsim selection
                case 0 -> {
                    System.out.println("Goodbye");
                    System.exit(0);
                }
                //Initialize Qubits
                case 1 -> {
                    workingQubits = initializeQubits(workingQubits);
                    selection = -1;
                }
                //Apply gates to qubits
                case 2 -> {
                    workingQubits = applyQubitGates(workingQubits, selection);
                    selection = -1;
                }
                //Display current state of all qubits
                case 3 -> {
                    displayQubitStates(workingQubits);
                    selection = -1;
                }
                //Runs the simulation and returns the expectation values
                case 4 -> {
                    System.out.println("Selected: "+selection);
                    selection = -1;
                }
                //Save system state to file
                case 5 -> {
                    System.out.println("Selected: " + selection);
                    saveSystemStateToFile(workingQubits);
                    selection = -1;
                }
                //Load system state from file
                case 6 -> {
                    System.out.println("Selected: " + selection);
                    loadSystemSateFromFile(workingQubits);
                    selection = -1;
                }
            }
        }
    }

    private static void loadSystemSateFromFile(ComplexQubit[] workingQubits) {
        System.out.println("Loading state from file");
    }

    private static void saveSystemStateToFile(ComplexQubit[] workingQubits) {
        System.out.println("Saving state to file.");
    }

    private static void displayQubitStates(ComplexQubit[] workingQubits) {
        System.out.println("Current Qubit state: ");
        if (workingQubits.length == 0) {
            System.out.println("Initialize Qubits before displaying them.");
            return;
        }
        int count = 0;
        for (ComplexQubit qubit : workingQubits) {
            System.out.println("Qubit #" + count + ": " + qubit);
        }
    }

    private static ComplexQubit[] applyQubitGates(ComplexQubit[] workingQubits, int selection) {
        System.out.println("Selected: " + selection);
        if (workingQubits.length == 0) {
            System.out.println("Initialize Qubits before applying gates.");
            return null;
        } else {
            selection = -1;
             do{
                System.out.println("Which qubit (number) do you want to apply a gate to?");
                for (int i = 0; i < workingQubits.length; i++) {
                    System.out.println("Qubit #" + i);
                }
                selection = Console.getInt();
                //check the qubit selected is in the array of cubits
                if (selection < 0 || selection > workingQubits.length-1) {
                    System.out.println("Please select a valid qubit to modify");
                    selection = -1;
                }
            }while (selection < 0 || selection > workingQubits.length-1);
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
            if (gateToApply > 6) {
                System.out.println("Please make a valid choice");
                gateToApply = -1;
            }
            switch (gateToApply) {
                case 0 -> {
                    return workingQubits;
                }
                case 1 -> {
                    System.out.println("Selected: " +gateToApply);
                    workingQubits[selection] = ComplexGates.applyPauliX(workingQubits[selection]);
                }
                case 2 -> {
                    System.out.println("Selected: " +gateToApply);
                    workingQubits[selection] = ComplexGates.applyPauliY(workingQubits[selection]);
                }
                case 3 -> {
                    System.out.println("Selected: " +gateToApply);
                    workingQubits[selection] = ComplexGates.applyPauliZ(workingQubits[selection]);
                }
                case 4 -> {
                    System.out.println("Selected: " +gateToApply);
                    workingQubits[selection] = ComplexGates.applyHadamard(workingQubits[selection]);
                }
                case 5 -> {
                    System.out.println("Selected: " +gateToApply);
                    System.out.println("Cannot Apply CNOT yet...");
                }
            }
        }
        return workingQubits;
    }

    //Initializes all qubits to a zero state inside workingQubits array
    private static ComplexQubit[] initializeQubits(ComplexQubit[] workingQubits) {
        System.out.println("How many qubits do you want to initialize?");
        int quantityQubitsInitialized = Console.getInt();
        try {
            workingQubits = new ComplexQubit[quantityQubitsInitialized];
            for (int i = 0; i < quantityQubitsInitialized; i++) {
                workingQubits[i] = new ComplexQubit();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println(quantityQubitsInitialized + " initialized.");
        return workingQubits;
    }

    public static void selectQubitMenu(int workingQubitsLength) {

    }
}
