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
        quantumKickback();
    }

    public static void quantumKickback(){
        jqs jqs = new jqs(3);
        jqs jqs2 = new jqs(3);
        jqs.H(0);
        jqs.X(1);
        //test initial state that doesn't have kickback
        jqs.CX(0,1);
        jqs.getState();
        System.out.println("Final Outcome: "+ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));
        //test that kickback occurs on control qubit
        jqs2.H(0);
        jqs2.X(1);
        jqs2.CZ(0,1);
        jqs2.getState();
        System.out.println("Final Outcome: "+ComplexMath.complexMatrixToDiracNotation(jqs2.getStateVec()));
    }
}
