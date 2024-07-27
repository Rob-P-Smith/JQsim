import complex_classes.ComplexMath;
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
//        smallTest();
//        testingSimulate();
//        quantumTeleportation();
//        hadmardTestForKickBack();
//        testKickBackMore();
//        testKickBackMoreTwo();
        testQFT();
     }


    public static void testQFT(){
        //https://quantum.ibm.com/composer/files/new?initial=N4IgdghgtgpiBcICKAxAKgWjTAzgFwwHUBLPACwHsBXAgWRghyoCdcQAaEAR0agRADyABQCiAOSQBBAMq0ABACYAdAAYA3AB0wxMAGMANlQAmMORu4x9xAEYBGJTt3nNYLV1YBzOVwDaAVgBdF11POV0fABYgrQAPbx8VaLA43wUklMiksnjE4OYALwAKAAdiOQB6RQBKHID2eNskkKLSiura%2BtSshqaCkrLKhRrfRs6fNLyWgfbfRLGAZl6ptqGO%2BKjJ-pXhn1H4xc3WwZ293w2tbK6XYiMew%2BnVroWkm7utZq3j%2BLSx8%2B1bq4XfYvAG7EFvPR9I4zHyLX7dM5JHAAdwgxR6zxcKLRa0RLg4IBMOBCxGKeGIFDA-BAAF8gA
        jqs jqs = new jqs(5, 3);
        jqs.X(0);
        jqs.X(2);
        jqs.X(4);
        jqs.H(0);
        jqs.CGate("RZ", 0,1, Math.PI/2);
        jqs.CGate("RZ", 0,2, Math.PI/2);
        jqs.H(1);
        jqs.CGate("RZ", 0,3, Math.PI/2);
        jqs.CGate("RZ", 1,2, Math.PI/2);
        jqs.CGate("RZ", 0,4, Math.PI/2);
        jqs.CGate("RZ", 1,3, Math.PI/2);
        jqs.H(2);
        jqs.CGate("RZ", 1,4, Math.PI/2);
        jqs.CGate("RZ", 2,3, Math.PI/2);
        jqs.CGate("RZ", 2,4, Math.PI/2);
        jqs.H(3);
        jqs.CGate("RZ", 3,4, Math.PI/2);
//        jqs.M(2);
        jqs.SWAP(1,3);
//        jqs.H(4);
        jqs.SWAP(0,4);
//        jqs.M(1);
//        jqs.M(3);
//        jqs.M(0);
//        jqs.M(4);
        jqs.getComputationalState();
        System.out.println(jqs);
        System.out.println(jqs.getStateVec());
//        jqs.simulate();
    }


    public static void testKickBackMore(){
        jqs jqs = new jqs(4);
        jqs.H(0);
        jqs.CX(0,1);
        jqs.H(1);
        jqs.RZ(Math.PI/2,1);
        jqs.S(1);
        jqs.CX(1,2);
        jqs.H(2);
        jqs.getComputationalState();
        System.out.println(jqs);
    }

    public static void testKickBackMoreTwo(){
        jqs jqs = new jqs(4);
        jqs.H(0);
        jqs.CX(0,1);
        jqs.H(1);
        jqs.RZ(Math.PI/2,1);
        jqs.S(1);
        jqs.CX(1,2);
        jqs.H(2);
        //debug simulate, phase being lost and not represented correctly in the console
        jqs.simulate();
    }

    public static void hadmardTestForKickBack(){
        jqs jqs = new jqs(4,10000);
        jqs.H(0);
//        jqs.RX(Math.PI/2, 2);
        jqs.H(1);
        jqs.T(1);
        jqs.CX(0,1);
        jqs.H(0);
        jqs.CX(0,2);
//        jqs.getComputationalState();\
        jqs.simulate();
    }

    public static void testingSimulate() {
        jqs jqs = new jqs(3);

        jqs.H(1);
        jqs.CX(1, 2);
        jqs.CX(0, 1);
        jqs.H(0);
        jqs.M(0);
        jqs.CX(1, 2);
        jqs.CZ(0, 2);
        jqs.M(2);
        jqs.simulate();
    }

    public static void smallTest() {
        jqs jqs = new jqs(2, 10000);
        jqs.H(0);
        jqs.CX(0, 1);
        jqs.simulate();
//        System.out.println(ComplexMath.complexMatrixToBasisStates(jqs.getStateVec()));
    }

    public static void quantumTeleportation() {
        jqs jqs = new jqs(3, 100000);

        jqs.X(0);
        jqs.H(1);
        jqs.CX(1, 2);
        jqs.CX(0, 1);
        jqs.H(0);
        jqs.M(0);
        jqs.M(1);
        jqs.CX(1, 2);
        jqs.CZ(0, 2);
        jqs.M(2);
        jqs.simulate();
//        System.out.println("Final teleported value of: " + jqs);
    }
}
