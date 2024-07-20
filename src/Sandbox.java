import complex_classes.ComplexMatrix;
import complex_classes.ComplexNumber;
import interpreter.jqs;

import java.util.HashMap;
import java.util.Map;

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
        quantumTeleportation();
        System.out.println("Test Simulate");
        testingSimulate();
    }
    public static void testingSimulate(){
        jqs jqs = new jqs(3);
        jqs.X(0);
        jqs.H(1);
        jqs.CX(1, 2);
        jqs.CX(0, 1);
        jqs.H(0);
        jqs.M(0);
        jqs.CX(1,2);
        jqs.CZ(0, 2);
        jqs.M(2);
        jqs.simulate();
    }

    public static void quantumTeleportation() {
        jqs jqs = new jqs(4);

        jqs.X(0);
        jqs.H(1);
        jqs.CX(1, 2);
        jqs.CX(0, 1);
        jqs.H(0);
        jqs.getComputationalState();
        System.out.println("Before measurements: " + jqs);
        jqs.measureQubit(0);
        System.out.println("After measuring qubit 0: " + jqs);
        jqs.measureQubit(1);
        System.out.println("After measuring qubit 1: " + jqs);
        jqs.CX(1, 2);
        jqs.CZ(0, 2);
        jqs.getComputationalState();
        jqs.M(2);
        System.out.println("Final teleported value of: " + jqs);
    }
}
