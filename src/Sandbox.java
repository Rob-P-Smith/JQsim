import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Sandbox {
    public static void main(String[] args) {
        System.out.println("Testing applying gates to a qubit...");
        Qubit startQ = new Qubit(0,1);
        System.out.println("Initial State of Qubit: "+startQ);

        startQ = Gates.applyPauliX(startQ);
        System.out.println("Apply PauliX Gate: "+startQ);
        startQ = Gates.applyPauliX(startQ);
        System.out.println("Apply PauliX Gate Twice: "+startQ);

        startQ = Gates.applyPauliZ(startQ);
        System.out.println("Apply PauliZ Gate: "+startQ);
        startQ = Gates.applyPauliZ(startQ);
        System.out.println("Apply PauliZ Gate Twice: "+startQ);

        startQ = Gates.applyPauliY(startQ);
        System.out.println("Apply PauliY Gate: "+startQ);
        startQ = Gates.applyPauliY(startQ);
        System.out.println("Apply PauliY Gate Twice: "+startQ);
        
        startQ = Gates.applyHadamard(startQ);
        System.out.println("Apply Hadamard Gate: "+startQ);
        startQ = Gates.applyHadamard(startQ);
        System.out.println("Apply Hadamard Gate Twice: "+startQ);

        Qubit startQ1 = new Qubit(0,1);
        Qubit startQ2 = new Qubit(1,0);
        System.out.println("StartQ1 = "+startQ1+"\nStartQ2 = "+startQ2);

        System.out.println(counts(startQ1, startQ2));

    }

     /**
     * runs the simulation the 'shots' number of times and collects the results into a hashmap for greater accuracy
     *
     * @param a   the numbers being tested in the qubit
     * @param matrix the 2d array of the qubits after they're built
     * @param shots  the number of times to run the sim
     * @return the hashmap of the results of the simulations
     */
    public static HashMap<String, Integer> counts(Qubit a, Qubit b) {
        int shots = 1024;
        //Built the Hashmap that will collect the results
        System.out.println("Running " + shots + " simulations...");
        HashMap<String, Integer> result = new HashMap<>();
        //Built the list of possible outputs
        List<List<Integer>> possible = Qops.bitsOfLen(1);
        //Load each possible solution into the hashmap with a zero value
        for (List<Integer> x : possible) {
            result.put(x.toString(), 0);
        }
        //run the sim and save the result
        for (int i = 0; i < shots; i++) {
            String outcome = Qops.measureMat(a);
            result.put(outcome, result.get(outcome) + 1);
        }
        return result;
    }

    // private static Double[][] buildMatrix(Double[] nums) {
    //     List<Double> listOfValues = new ArrayList<>(List.of(nums));
    //     Double[][] result = new Double[0][];
    //     try {
    //         result = Qops.qubit(listOfValues);
    //         System.out.println("\nQubit created successfully:");
    //         for (int i = 0; i < result.length; i++) {
    //             for (int j = 0; j < result[i].length; j++) {
    //                 Double num = result[i][j];
    //                 System.out.println("[" + num + "]");
    //             }
    //         }
    //     } catch (IllegalArgumentException e) {
    //         System.out.println(e.getMessage());
    //     }
    //     return result;
    // }
}
