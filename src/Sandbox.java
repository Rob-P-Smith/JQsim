import complex_classes.ComplexMath;
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
     * @param args none needed or accounted for.
     */
    public static void main(String[] args) {
        jqs jqs = new jqs(3);
        jqs.H(0);
        jqs.S(0);
        jqs.H(1);
        jqs.getState();
        System.out.println("\nResulting state: " + ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));
    }
}
