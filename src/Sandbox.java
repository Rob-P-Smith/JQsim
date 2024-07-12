import complexClasses.ComplexMath;
import interpreter.jqs;

/**
 * Just a class for debugging things while building the program.
 * @author Robert Smith
 * @version 0.1
 * @since 11 July 2024
 */

public class Sandbox {
    public static void main(String[] args) {
        jqs jqs = new jqs(6);
        jqs.X(0);
        jqs.H(1);
        jqs.CX(0,5);
        jqs.H(3);
        jqs.CX(0,1);
        jqs.CX(3,4);
        jqs.getState();

        System.out.println(ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));
        System.out.println(jqs.getStateVec());
    }
}
