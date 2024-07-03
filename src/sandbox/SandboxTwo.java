package sandbox;

import complexClasses.ComplexGates;
import complexClasses.ComplexMatrix;
import complexClasses.ComplexObject;
import complexClasses.ComplexQubit;

public class SandboxTwo {
    public static void main(String[] args) {

        ComplexObject coj = new ComplexObject();
        ComplexQubit one = new ComplexQubit();
        ComplexQubit two = new ComplexQubit();
//        one.setState(ComplexGates.applyPauliX(two.getState()));

        System.out.println(one);
        System.out.println(two);

        System.out.println("\nApplying CNOT 0,1\n");
        ComplexGates.applyCNOT(two, one);

//        System.out.println("Results");
//        System.out.println(one);
//        System.out.println(two);
//
//        System.out.println("Applying CNOT 1, 0");
//        ComplexGates.applyCNOT(two, one);
//
//        System.out.println("Results");
//        System.out.println(one);
//        System.out.println(two);
    }
}
