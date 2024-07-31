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
//        quantumTeleportation();
        testQFT();
        System.out.println("\n Testing Auto Version \n");
        testAutoQFT();
//        rotations();
        System.out.println();
        double[] radians = {2 * Math.PI / 8 * 2, 2 * Math.PI / 8 * 3, 2 * Math.PI / 8 * 4, 2 * Math.PI / 8 * 5, 2 * Math.PI / 8 * 6};
        System.out.println("R2: radians:" + radians[0] + ", degrees: " + 180 / Math.PI * radians[0]);
        System.out.println("R3: radians:" + radians[1] + ", degrees: " + 180 / Math.PI * radians[1]);
        System.out.println("R4: radians:" + radians[2] + ", degrees: " + 180 / Math.PI * radians[2]);
        System.out.println("R5: radians:" + radians[3] + ", degrees: " + 180 / Math.PI * radians[3]);
        System.out.println("R6: radians:" + radians[4] + ", degrees: " + 180 / Math.PI * radians[4]);
    }

    //This is taken from https://algassert.com/quirk#circuit={%22cols%22:[[%22H%22,%22X%22,%22X%22],[%22%E2%80%A2%22,%22Z^%C2%BD%22],[%22%E2%80%A2%22,1,%22Z^%C2%BC%22],[1,%22H%22],[1,%22%E2%80%A2%22,%22Z^%C2%BD%22],[1,1,%22H%22],[%22Swap%22,1,%22Swap%22]]}
    //The output of my program agrees with their output state amplitudes per computational basis state.
    public static void testQFT() {
        jqs jqs = new jqs(3);
        jqs.X(0);
        jqs.X(1);
        jqs.X(2);
        jqs.H(2);
        jqs.CGate("S", 2, 1);
        jqs.CGate("T", 2, 0);
        jqs.H(1);
        jqs.CGate("S", 1, 0);
        jqs.H(0);
        jqs.SWAP(0,2);
        jqs.getComputationalState();
        System.out.println(jqs);
    }

    public static void testAutoQFT() {
        jqs jqs = new jqs(3, 1000, "QFT-TEST");
        jqs.X(0);
        jqs.X(1);
        jqs.X(2);
        jqs.getComputationalState();
        jqs.QFT();
        System.out.println(jqs);
    }

    public static void quantumTeleportation() {
        jqs jqs = new jqs(3, 1000);

//        jqs.X(0);
        jqs.H(1);
        jqs.CX(1, 2);
        jqs.H(0);
        jqs.RZ(Math.PI / 4, 0);
        jqs.H(0);
        jqs.CX(0, 1);
        jqs.H(0);
        jqs.CX(1, 2);
        jqs.CZ(0, 2);
        jqs.H(2);
        jqs.RZ(-Math.PI / 4, 2);
        jqs.H(2);
        jqs.M(2);
        jqs.getComputationalState();
        System.out.println(jqs + "\n");
        jqs.simulate();
    }
}
