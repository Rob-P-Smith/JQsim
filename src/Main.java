import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Driver for testing functionality of the simulator, will eventually migrate to a menu driven system
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

        //testing case, input your arguments as the numbers in the ComplexNumber instantiates
        ComplexNumber[] nums = {
                new ComplexNumber(1 / (Math.sqrt(2))),
                new ComplexNumber(1 / (Math.sqrt(2)))
        };

        List<ComplexNumber> listOfValues = new ArrayList<>(List.of(nums));
        ComplexNumber[][] result = new ComplexNumber[0][];

        try {
            result = Qubit.qubit(listOfValues);
            System.out.println("\nQubit created successfully:");
            for (int i = 0; i < result.length; i++) {
                for (int j = 0; j < result[i].length; j++) {
                    ComplexNumber num = result[i][j];
                    System.out.println("[" + num.real + "]");
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        if (result.length > 0) {
            Qubit.measureMat(result);
        }

        runShots(nums, result);
    }

    public static HashMap<String, Integer> runShots(ComplexNumber[] nums, ComplexNumber[][] matrix){
        int shots = 1024;
        System.out.println("Running "+shots+" simulations...");        //Built the Hashmap that will collect the results
        HashMap<String, Integer> result = new HashMap<>();
        //Built the list of possible outputs
        List<List<Integer>> possible = Qubit.bitsOfLen(nums.length);
        //Load each possible solution into the hashamp with a zero value
        for(List<Integer> x : possible){
            result.put(x.toString(), 0);
        }
        //run the sim and save the result
        for(int i = 0; i < shots; i++) {
            String outcome = Qubit.measureMat(matrix);
            result.put(outcome, result.get(outcome)+1);
        }
        //print the results out
        System.out.println("\nResulting Outcomes: ");
        for(String key : result.keySet()){
            System.out.println(key + ": "+result.get(key));
        }
        return result;
    }
}
