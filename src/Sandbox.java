import interpreter.jqs;

/**
 * Just a class for debugging things while building the program.
 *
 * @author Robert Smith
 * @version 0.1
 * @since 11 July 2024
 */

public class Sandbox {

    /**
     * Main method for testing out quantum circuits while building the simulator.
     *
     * @param args none needed or accounted for.
     */
    public static void main(String[] args) {
        benchmark();
    }

    public static void benchmark() {
        int maxQubits = 10;
        int runsCount = 5;

        for (int qubits = 2; qubits <= maxQubits; qubits++) {
            long totalRunsTime = 0;
            System.out.println("\nTesting "+qubits+" qubits.");
            for (int r = 0; r < runsCount; r++) {
                long startTime = System.currentTimeMillis();

                jqs jqs = new jqs(qubits);
                for (int i = 0; i < qubits - 1; i++) {
                    jqs.X(i);
                }
                jqs.getComputationalState();
                jqs.CX(0, qubits - 1);
                jqs.getComputationalState();

                long endTime = System.currentTimeMillis();
                System.out.println("Run Time: " + (endTime - startTime));
                totalRunsTime += endTime - startTime;
            }

            System.out.println("Average execution time using Sparse: " + (totalRunsTime / runsCount) + "ms");
        }
    }
}

