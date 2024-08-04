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
        System.out.println("Onward and upward to QPE!");
        long totalRunsTime = 0;
        int runsCount = 5;
        int numQubits = 11;
        for(int r = 0; r < runsCount; r++) {
            long startTime = System.currentTimeMillis();


            jqs jqs = new jqs(numQubits);

            for (int i = 0; i < numQubits - 1; i++) {
                jqs.X(i);
            }
            jqs.getComputationalState();
//            System.out.println("\n" + jqs + "\n");
            jqs.CX(0, numQubits - 1);
            jqs.getComputationalState();
//            System.out.println("\n" + jqs + "\n");
            long endTime = System.currentTimeMillis();
            System.out.println("Run Time: " + (endTime-startTime));
            totalRunsTime += endTime - startTime;
        }
        System.out.println("Average execution time using Sparse: " + (totalRunsTime/runsCount) + "ms");
    }
}
