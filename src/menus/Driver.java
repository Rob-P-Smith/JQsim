package menus;

public class Driver {
    public static void main(String[] args) {
        welcomeMessage();
    }

    public static void welcomeMessage() {
        System.out.println("Welcome to JQsim");
    }

    public static void topMenu() {
        System.out.println("""
                1. Initialize Qubits
                2. Apply Gates
                3. Run Simulation
                4. Save State To File
                5. Load State From File
                0. Exit
                """);
        int selection = Console.getInt();
        switch (selection) {
            case 0 -> {
                System.out.println("Goodbye");
                System.exit(0);
            }
            case 1 -> {

            }
            case 2 -> {

            }
            case 3 -> {

            }
            case 4 -> {

            }
            case 5 -> {

            }
        }
    }


}
