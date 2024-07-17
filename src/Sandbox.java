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
    public static void main(String[] args) {
        jqs jqs = new jqs(3);
        jqs.H(0);
        jqs.X(1);
        jqs.CY(1,0);
        jqs.getState();
        System.out.println("\nResulting state: " + ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));
    }
}
