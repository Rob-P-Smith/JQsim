package measurement;

import complexClasses.ComplexQubit;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * original_files.Qubit class that contains all the qubit related methods
 *
 * @author Robert P Smith
 * @version 0.1
 * @since 20 June 2024
 */
public class Qops {

    static String measureMat(ComplexQubit a) {

//        int len = matrix.length;
//        List<List<Integer>> obs = new ArrayList<>(bitsOfLen(len));
//        double[] amplitudes = new double[len];
//
//        for (int i = 0; i < len; i++) {
//            double num = Double.valueOf(String.valueOf(matrix[i][0]));
//            amplitudes[i] = num * num * 100;
//        }

        Random rand = new Random();
        int roll = rand.nextInt(99);

//        List<Integer> result = rangeMap(0, amplitudes, obs, roll, 0);
//        return result.toString();
        return null;
    }


    static String measureMat(Double[][] matrix) {

        int len = matrix.length;
        List<List<Integer>> obs = new ArrayList<>(bitsOfLen(len));
        double[] amplitudes = new double[len];

        for (int i = 0; i < len; i++) {
            double num = Double.valueOf(String.valueOf(matrix[i][0]));
            amplitudes[i] = num * num * 100;
        }

        Random rand = new Random();
        int roll = rand.nextInt(99);

        List<Integer> result = rangeMap(0, amplitudes, obs, roll, 0);
        return result.toString();
    }

    /**
     * Develops the range map and finds the corresponding value of the provided qubit based on a random probability
     *
     * @param start  The starting point as the lower bound the test the roll against
     * @param amplitudes  The amplitude of the probabilities after being scaled by 100
     * @param obs    The List<Integer> of all possible binary values for the qubit(s)
     * @param roll   The random roll generated
     * @param index  The current index being examined
     * @return a list of the current binary value located
     */
    public static List<Integer> rangeMap(double start, double[] amplitudes, List<List<Integer>> obs, int roll, int index) {
        if (index == amplitudes.length) {
            throw new IllegalStateException("No observation from circuit - invalid state");
        } else if (amplitudes[index] == 0) {
            return rangeMap(start, amplitudes, obs, roll, ++index);
        } else if (roll >= start && roll < start + amplitudes[index]) {
            return obs.get(index);
        } else {
            return rangeMap(start + amplitudes[index], amplitudes, obs, roll, ++index);
        }
    }

    //Generates a List of List<Integer> that contains all the possible binary values for the qubits
    public static List<List<Integer>> bitsOfLen(int num) {
        // Check for invalid length
        if (num < 1) {
            throw new IllegalArgumentException("Invalid bits length, must be >= 1");
        }

        // Base case for num = 1
        if (num == 1) {
            List<List<Integer>> result = new ArrayList<>();
            result.add(List.of(0));
            result.add(List.of( 1));
            return result;
        }

        // Recursive case: Generate bit combinations of length num
        List<List<Integer>> lowerBits = bitsOfLen(num - 1);
        List<List<Integer>> result = new ArrayList<>();

        for (int i = 0; i <= 1; i++) {
            for (List<Integer> x : lowerBits) {
                List<Integer> newList = new ArrayList<>();
                newList.add(i);
                newList.addAll(x);
                result.add(newList);
            }
        }
        return result;
    }
}
