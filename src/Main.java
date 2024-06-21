import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Driver for testing functionality of the simulator, will eventually migrate to a menu-driven system
 *
 * @author Robert P Smith
 * @version 0.1
 * @since 20 June 2024
 */
public class Main {

    /**
     * Simple driver for testing
     *
     * @param args none required or provisioned for
     */
    public static void main(String[] args) {
        Gates g = new Gates();
        //the number of times to run the sim
        final int shots = 4096;
        //testing case, input your arguments as the numbers in the ComplexNumber instantiates
        Double[] nums = {
                1.0,
                0.0
        };
        Double[][] matrix = buildMatrix(nums);
        if (matrix.length > 0) {
            HashMap<String, Integer> simResults = runShots(nums, matrix, shots);
            //print the results out
            System.out.println("\nResulting Outcomes: ");
            for (String key : simResults.keySet()) {
                System.out.println(key + ": " + simResults.get(key));
            }
        }


        //Starting to work on the quantum gates, functional but requires the previous work transition to using the
        //Qubit class and Matrix class to apply gates to user defined qubit start states and run the sim
        Qubit q0 = new Qubit(1, 0);

        // Apply gates
        Qubit qX = Gates.applyPauliX(q0);
        Qubit qZ = Gates.applyPauliZ(q0);
        Qubit qY = Gates.applyPauliY(q0);
        Qubit qH = Gates.applyHadamard(q0);

        // Print results
        System.out.println("Original Qubit |0>: " + q0);
        System.out.println("After applying Pauli-X: " + qX);
        System.out.println("After applying Pauli-Z: " + qZ);
        System.out.println("After applying Pauli-Y: " + qY);
        System.out.println("After applying Hadamard: " + qH);
    }

    /**
     * runs the simulation the 'shots' number of times and collects the results into a hashmap for greater accuracy
     *
     * @param nums   the numbers being tested in the qubit
     * @param matrix the 2d array of the qubits after they're built
     * @param shots  the number of times to run the sim
     * @return the hashmap of the results of the simulations
     */
    public static HashMap<String, Integer> runShots(Double[] nums, Double[][] matrix, int shots) {
        //Built the Hashmap that will collect the results
        System.out.println("Running " + shots + " simulations...");
        HashMap<String, Integer> result = new HashMap<>();
        //Built the list of possible outputs
        List<List<Integer>> possible = Qops.bitsOfLen(nums.length);
        //Load each possible solution into the hashmap with a zero value
        for (List<Integer> x : possible) {
            result.put(x.toString(), 0);
        }
        //run the sim and save the result
        for (int i = 0; i < shots; i++) {
            String outcome = Qops.measureMat(matrix);
            result.put(outcome, result.get(outcome) + 1);
        }
        return result;
    }

    private static Double[][] buildMatrix(Double[] nums) {
        List<Double> listOfValues = new ArrayList<>(List.of(nums));
        Double[][] result = new Double[0][];
        try {
            result = Qops.qubit(listOfValues);
            System.out.println("\nQubit created successfully:");
            for (int i = 0; i < result.length; i++) {
                for (int j = 0; j < result[i].length; j++) {
                    Double num = result[i][j];
                    System.out.println("[" + num + "]");
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }
}
