package menus;

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

            if (selection >= 7) {
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
                    workingQubits = initializeQubits(workingQubits, selection);
                    selection = -1;
                }
                //Apply gates to qubits
                case 2 -> {
                    workingQubits = applyQubitGates(workingQubits, selection);
                    selection = -1;
                }
                //Display current state of all qubits
                case 3 -> {
                    System.out.println("Selected: " + selection);
                    displayQubitStates(workingQubits);
                    selection = -1;
                }
                //Save system state to file
                case 4 -> {
                    System.out.println("Selected: " + selection);
                    saveSystemStateToFile(workingQubits);
                    selection = -1;
                }
                //Load system state from file
                case 5 -> {
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
        if(workingQubits.length == 0) {
            System.out.println("Initialize Qubits before displaying them.");
            return;
        }
        int count = 0;
        for(ComplexQubit qubit : workingQubits) {
            System.out.println("Qubit #"+count+": "+qubit);
        }
    }

    private static ComplexQubit[] applyQubitGates(ComplexQubit[] workingQubits, int selection) {
        System.out.println("Selected: " + selection);
        if (workingQubits.length == 0) {
            System.out.println("Initialize Qubits before applying gates.");
            return null;
        } else {
            System.out.println("Which qubit do you want to apply a gate to?");
            for (int i = 0; i < workingQubits.length; i++) {
                System.out.println("Qubit #"+i);
            }
            int qubitToModify = Console.getInt();
            //check the qubit selected is in the array of cubits
            if(qubitToModify < 0 || qubitToModify > workingQubits.length-1){
                System.out.println("Please select a valid qubit to modify");
                qubitToModify = Console.getInt();
            }
            System.out.println("""
                                1. PauliX
                                2. PauliY
                                3. PauliZ
                                4. Hadamard
                                5. CNOT
                                6. Back to Main Menu
                                """);
            int gateToApply = Console.getInt();
            if(gateToApply > 6){
                System.out.println("Please make a valid choice");
                Console.getInt();
            }
        }
        return workingQubits;
    }

    //Initializes all qubits to a zero state inside workingQubits array
    private static ComplexQubit[] initializeQubits(ComplexQubit[] workingQubits, int selection) {
        System.out.println("Selected: " + selection);
        System.out.println("How many qubits do you want to initialize?");
        int quantityQubitsInitialized = Console.getInt();
        try {
            workingQubits = new ComplexQubit[quantityQubitsInitialized];
            for(int i = 0; i < quantityQubitsInitialized; i++) {
                workingQubits[i] = new ComplexQubit();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println(quantityQubitsInitialized+" initialized.");
        return workingQubits;
    }

    public static void selectQubitMenu(int workingQubitsLength){

    }


}
