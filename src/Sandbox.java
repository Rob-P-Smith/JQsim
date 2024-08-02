import complex_classes.ComplexMath;
import complex_classes.ComplexMatrix;
import complex_classes.ComplexNumber;
import interpreter.jqs;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static supportClasses.GreekEnums.PI;

/**
 * Just a class for debugging things while building the program.
 *
 * @author Robert Smith
 * @version 0.1
 * @since 11 July 2024
 */

public class Sandbox {
    private static int shots = 10000;

    /**
     * Main method for testing out quantum circuits while building the simulator.
     *
     * @param args none needed or accounted for.
     */
    public static void main(String[] args) {
        jqs jqs = new jqs(3);
        jqs.X(0);
        jqs.S(0);
        jqs.T(0);
//        jqs.RX(Math.PI/5, 0);
        jqs.X(1);
        jqs.T(1);
        jqs.X(2);
//        jqs.RY(3*Math.PI/2, 2);
        jqs.getComputationalState();
        System.out.println("Starting State: " + jqs);
        jqs.QFT();
        System.out.println("\nQFT State: "+ jqs);
        jqs.QFTi();
        System.out.println("\nAfter QFTi" +jqs);
    }
}
