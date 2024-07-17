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
        jqs.RY(Math.PI/5, 0);
        jqs.RX(Math.PI/9, 0);
        jqs.CX(0,1);
        jqs.H(1);
        jqs.CX(1,2);
        jqs.getState();
        System.out.println("\nResulting state: " + ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));
    }
}
