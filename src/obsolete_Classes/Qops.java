package obsolete_Classes;

import complexClasses.ComplexMatrix;
import complexClasses.ComplexNumber;
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
    private static final boolean DEBUG = false;

    public static ComplexMatrix measureMat(ComplexMatrix matrix) {
        int len = 2;
        ComplexMatrix result = new ComplexMatrix(2, 1);

        for (int i = 0; i < len; i++) {
            ComplexNumber bitOne = new ComplexNumber(matrix.get(0, 0).getReal(), matrix.get(0, 0).getImag());
            ComplexNumber bitTwo = new ComplexNumber(matrix.get(1, 0).getReal(), matrix.get(1, 0).getImag());
            result.set(0, 0, new ComplexNumber(
                    (bitOne.getReal() * bitOne.getReal()) * 100.0,
                    (bitOne.getImag() * bitOne.getImag()) * 100.0)
            );
            result.set(1, 0, new ComplexNumber(
                    (bitTwo.getReal() * bitTwo.getReal()) * 100.0,
                    (bitTwo.getImag() * bitTwo.getImag()) * 100.0)
            );
        }
        Random rand = new Random();
        int roll = rand.nextInt(100);
        if (DEBUG) {
            System.out.println("bitOne Amplitude: \nReal: " +
                    result.get(0,0).getReal() +
                    "\nImag: " +
                    result.get(0,0).getImag());
            System.out.println("bitTwo Amplitude: \nReal: " + result.get(1,0) +
                    "\nImag: " +
                    result.get(1,0).getImag());
            System.out.println("\nRoll: \n"+roll);
        }
        List<Integer> returned = new ArrayList<Integer>();
        double[] values = {result.get(0,0).getReal(), result.get(1,0).getReal()};
        returned = rangeMap(0.0, values, roll, 0);
        if(returned.get(0) == 0){
            result.set(0,0, new ComplexNumber(0,0));
            result.set(1,0, new ComplexNumber(1,0));
        } else {
            result.set(0,0, new ComplexNumber(1,0));
            result.set(1,0, new ComplexNumber(0,0));
        }
        return result;
    }

    /**
     * Develops the range map and finds the corresponding value of the provided qubit based on a random probability
     *
     * @param start      The starting point as the lower bound the test the roll against
     * @param amplitudes The amplitude of the probabilities after being scaled by 100
     * @param roll       The random roll generated
     * @param index      The current index being examined
     * @return a list of the current binary value located
     */
    public static List<Integer> rangeMap(double start, double[] amplitudes, int roll, int index) {
        if (index == amplitudes.length) {
            throw new IllegalStateException("No observation from circuit - invalid state");
        } else if (amplitudes[index] == 0) {
            return rangeMap(start, amplitudes, roll, ++index);
        } else if (roll >= start && roll < start + amplitudes[index]) {
            List<Integer> result = new ArrayList<>();
            if (index == 0) {
                result.add(1);
                result.add(0);
            } else {
                result.add(0);
                result.add(1);
            }
            return result;
        } else {
            return rangeMap(start + amplitudes[index], amplitudes, roll, ++index);
        }
    }
}
