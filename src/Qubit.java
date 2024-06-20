import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Qubit class that contains all the qubit related methods
 *
 * @author Robert P Smith
 * @version 0.1
 * @since 20 June 2024
 */
public class Qubit {

    // Define a function to check approximate equality to within 0.00001 and fix float calc errors
    private static boolean eEqual(double x, double y) {
        return Math.abs(x - y) < 0.00001;
    }

    // Define a function to create a state vector dynamically sized based on the input size
    private static ComplexNumber[][] colMatrix(List<ComplexNumber> listOfNumbers) {

        //TODO need to adjust to mate a state vector that can flex to the required size of the qubit load
        ComplexNumber[][] matrix = new ComplexNumber[listOfNumbers.size()][1];
        for (int i = 0; i < listOfNumbers.size(); i++) {
            matrix[i][0] = listOfNumbers.get(i);
        }
        return matrix;
    }

    // The qubit function to populate the qubit values after verifying they follow the qubit sum of squares = 1 rule
    static ComplexNumber[][] qubit(List<ComplexNumber> listOfNumbers) {
        double sumMagnitudesSquared = 0.0;
        for (ComplexNumber nums : listOfNumbers) {
            sumMagnitudesSquared += nums.magnitude() * nums.magnitude();
        }
        if (eEqual(sumMagnitudesSquared, 1.0)) {
            return colMatrix(listOfNumbers);
        } else {
            throw new IllegalArgumentException("Cannot create qubit, bad probabilities");
        }
    }

    static void measureMat(ComplexNumber[][] matrix) {

        int len = matrix.length;
        int blen = (int) Math.ceil(Math.log(len));
        List<List<Integer>> obs = new ArrayList<>(bitsOfLen(len));
        double[] ampls = new double[len];

        System.out.println("Blen is: " + blen);
        System.out.println("Matrix size is: " + matrix.length);

        for (int i = 0; i < len; i++) {
            double num = Double.valueOf(String.valueOf(matrix[i][0]));
            ampls[i] = num * num * 100;
        }

        for (double value : ampls) {
            System.out.println("Value from scaling: " + value);
        }
        for (List<Integer> entry : obs) {
            System.out.println(entry);
        }

        Random rand = new Random();
        int roll = rand.nextInt(99);

        List<Integer> result = rangeMap(0, ampls, obs, roll, 0, len);
        System.out.println("Result: " + result);
    }

    /**
     * Develops the range map and finds the corresponding value of the provided qubit based on a random probability
     *
     * @param start  The starting point as the lower bound the test the roll against
     * @param ampls  The amplitude of the probabilities after being scaled by 100
     * @param obs    The List<Integer> of all possible binary values for the qubit(s)
     * @param roll   The random roll generated
     * @param index  The current index being examined
     * @param length the length of the matrix of qubit(s)
     * @return a list of the current binary value located
     */
    private static List<Integer> rangeMap(double start, double[] ampls, List<List<Integer>> obs, int roll, int index, int length) {
        System.out.println("\n////////////////");
        System.out.println("LowerBound: " + start + ", UpperBound: " + (start + ampls[index]));
        System.out.println("Index: " + index +
                "\nBlen: " + ampls.length +
                "\nroll: " + roll +
                "\nampls: " + Arrays.toString(ampls)
        );

        if (index == ampls.length) {
            throw new IllegalStateException("No observation from circuit - invalid state");
        } else if (ampls[index] == 0) {
            return rangeMap(start, ampls, obs, roll, ++index, length);
        } else if (roll >= start && roll < start + ampls[index]) {
            return obs.get(index);
        } else {
            return rangeMap(start + ampls[index], ampls, obs, roll, ++index, length);
        }
    }

    //Generates a List of List<Integer> that contains all the possible binary values for the qubits
    private static List<List<Integer>> bitsOfLen(int n) {
        // Check for invalid length
        if (n < 1) {
            throw new IllegalArgumentException("Invalid bits length, must be >= 1");
        }

        // Base case for n = 1
        if (n == 1) {
            List<List<Integer>> result = new ArrayList<>();
            result.add(List.of(0));
            result.add(List.of( 1));
            return result;
        }

        // Recursive case: Generate bit combinations of length n
        List<List<Integer>> lowerBits = bitsOfLen(n - 1);
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
