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
        System.out.println("Simple case QFT black box: ");
        testAutoQFT();
        System.out.println("\nSimple case Constructed QFT using CS and CT");
        testQFT();
        System.out.println("\nSimple case Constructed QFT using CRZ");
        testQFTWithRZ();
        System.out.println("Complex case QFT black box: ");
    }

    public static void testAutoQFT() {
        jqs jqs = new jqs(3, 1000, "QFT-TEST");
//        jqs.X(0);
        jqs.X(1);
        jqs.X(2);
        jqs.getComputationalState();
        jqs.QFT();
        System.out.println(jqs);
    }

    //This is taken from https://algassert.com/quirk#circuit={%22cols%22:[[%22H%22,%22X%22,%22X%22],[%22%E2%80%A2%22,%22Z^%C2%BD%22],[%22%E2%80%A2%22,1,%22Z^%C2%BC%22],[1,%22H%22],[1,%22%E2%80%A2%22,%22Z^%C2%BD%22],[1,1,%22H%22],[%22Swap%22,1,%22Swap%22]]}
    //The output of my program agrees with their output state amplitudes per computational basis state.
    public static void testQFT() {
        jqs jqs = new jqs(3);
//        jqs.X(0);
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

    //This is taken from https://algassert.com/quirk#circuit={%22cols%22:[[%22H%22,%22X%22,%22X%22],[%22%E2%80%A2%22,%22Z^%C2%BD%22],[%22%E2%80%A2%22,1,%22Z^%C2%BC%22],[1,%22H%22],[1,%22%E2%80%A2%22,%22Z^%C2%BD%22],[1,1,%22H%22],[%22Swap%22,1,%22Swap%22]]}
    //The output of my program agrees with their output state amplitudes per computational basis state.
    public static void testQFTWithRZ() {
        jqs jqs = new jqs(3);
//        jqs.X(0);
        jqs.X(1);
        jqs.X(2);
        jqs.H(2);
        jqs.CGate("RZ", 2, 1, Math.PI);
        jqs.CGate("RZ", 2, 0, Math.PI/2);
        jqs.H(1);
        jqs.CGate("RZ", 1, 0, Math.PI);
        jqs.H(0);
        jqs.SWAP(0,2);
        jqs.getComputationalState();
        System.out.println(jqs);
    }

    public static void debuggingRk(){
        jqs jqs = new jqs(2);
        jqs jqs2 = new jqs(2);
        jqs jqs3 = new jqs(2);
        jqs jqs4 = new jqs(2);

        jqs.X(0);
        jqs.S(0);
        jqs.S(0);
        jqs.getComputationalState();
        System.out.println("S Gate");
        System.out.println(jqs);

        jqs3.X(0);
        jqs3.T(0);
        jqs3.getComputationalState();
        System.out.println("T Gate");
        System.out.println(jqs3);

        jqs2.X(0);
        jqs2.RZ(Math.PI/4, 0);
        jqs2.getComputationalState();
        System.out.println("RZ Gate");
        System.out.println(jqs2);

        jqs4.X(0);
        jqs4.R1(Math.PI, 0);
        jqs4.getComputationalState();
        System.out.println("R1 Gate");
        System.out.println(jqs4);
    }

    public static void printRotations(){
        System.out.println();
        double[] radians = {2 * Math.PI / 8 * 2, 2 * Math.PI / 8 * 3, 2 * Math.PI / 8 * 4, 2 * Math.PI / 8 * 5, 2 * Math.PI / 8 * 6};
        System.out.println("R2: radians: " + radians[0] + ", degrees: " + 180 / Math.PI * radians[0]);
        System.out.println("R3: radians: " + radians[1] + ", degrees: " + 180 / Math.PI * radians[1]);
        System.out.println("R4: radians: " + radians[2] + ", degrees: " + 180 / Math.PI * radians[2]);
        System.out.println("R5: radians: " + radians[3] + ", degrees: " + 180 / Math.PI * radians[3]);
        System.out.println("R6: radians: " + radians[4] + ", degrees: " + 180 / Math.PI * radians[4]);
        System.out.println();
    }
}
