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
        jqs jqs = new jqs(4);
        jqs.X(0);
        jqs.CX(0,1);
        jqs.H(2);
        jqs.CX(2,3);

        jqs.getState();
        System.out.println("\nResulting state: " + ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));
    }
}

//Possible branches:
// 1. control is 0 or 1 for real or imaginary
// 2. control is not 0 or 1 for real or imaginary
// 3. target is 0 or 1 for real or imaginary
// 4. target is not 0 or 1 for real or imaginary
// 5. control and target are 0 or 1 for real or imaginary
// 6. control and target are not 0 or 1 for real or imaginary
