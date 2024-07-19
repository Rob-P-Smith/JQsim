import complex_classes.ComplexMath;
import interpreter.jqs;

import java.util.*;

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
     *
     * @param args none needed or accounted for.
     */
    public static void main(String[] args) {
        jqs jqs = new jqs(3);
        jqs.X(0);
        jqs.H(1);
        jqs.CX(1,2);
        jqs.CX(0,1);
        jqs.H(0);
        jqs.getState();
        System.out.println(ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));
        jqs.measureQubit(0);
        jqs.measureQubit(1);
        jqs.CX(1,2);
        jqs.getState();
        jqs.measureQubit(2);
        System.out.println("Final Outcome: "+ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));
    }
}
