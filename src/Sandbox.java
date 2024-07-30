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
        quantumTeleportation();
//        rotations();
    }

    public static void rotations(){
        System.out.println("PI over 2: "+Math.PI/2*(180/Math.PI));
        System.out.println("PI over 4: "+Math.PI/4*(180/Math.PI));
        System.out.println("PI over 8: "+Math.PI/8*(180/Math.PI));
        System.out.println("PI over 16: "+Math.PI/16*(180/Math.PI));
    }

    public static void testQFT() {
        jqs jqs = new jqs(3, 10000);
        jqs.H(0);
        //This is right, but it's wrong, because the user has to manually determine the correct phase applied to the CGate involved.
        //Need to turn this into a "black-box" that does the QFT automatically based on user input state provided and act as a controlled U for
        //the ancilla bits to transform them based on the wave form from QFT.
        //Then reverse the QFT to inverse QFT to decode the ancilla bits at the end, and get the state of the system after all the controlled U QFT steps
        //to get QPE.
        jqs.X(1);
        jqs.X(2);
        jqs.CGate("RZ", 1, 0, Math.PI / 2);
        jqs.CGate("RZ", 2, 0, Math.PI / 4);
        jqs.H(1);
        jqs.CGate("RZ", 2, 1, Math.PI / 2);
        jqs.H(2);
        jqs.SWAP(0,2);
        jqs.getComputationalState();
        System.out.println(jqs);
    }


    public static void quantumTeleportation() {
        jqs jqs = new jqs(3, 1000);

//        jqs.X(0);
        jqs.H(1);
        jqs.CX(1, 2);
        jqs.H(0);
        jqs.RZ(Math.PI/4, 0);
        jqs.H(0);
        jqs.CX(0, 1);
        jqs.H(0);
        jqs.CX(1, 2);
        jqs.CZ(0, 2);
        jqs.H(2);
        jqs.RZ(-Math.PI/4, 2);
        jqs.H(2);
        jqs.M(2);
        jqs.getComputationalState();
        System.out.println(jqs+"\n");
        jqs.simulate();
    }
}
