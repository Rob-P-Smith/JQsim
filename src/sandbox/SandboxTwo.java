package sandbox;

import complexClasses.ComplexGates;
import complexClasses.ComplexMatrix;
import complexClasses.ComplexQubit;

public class SandboxTwo {
    public static void main(String[] args) {
        ComplexQubit one = new ComplexQubit();
//        one.setState(ComplexGates.applyPauliX(one.getState()));
        System.out.println(one);
        ComplexQubit two = new ComplexQubit();
//        two.setState(ComplexGates.applyPauliX(two.getState()));
        System.out.println("\n"+ two);

        ComplexMatrix three = one.getTranspose(one.getState());
        ComplexMatrix four = one.getTranspose(two.getState());

        System.out.println("Transpose of one: \n" + three);
        System.out.println("Transpose of two: \n" + four);

        ComplexMatrix threeA = one.tensorMultiply(one.getState(), three);
        ComplexMatrix fourA = one.tensorMultiply(two.getState(), four);

        System.out.println("ThreeA is: \n" + threeA);
        System.out.println("FourA is: \n" + fourA);

        ComplexMatrix threeB= one.tensorMultiply(threeA, ComplexGates.getIdentity());
        ComplexMatrix fourB = one.tensorMultiply(fourA, ComplexGates.getPauliX());

        System.out.println("Tensor Product for Three: \n" + threeB);
        System.out.println("Tensor Product for Four: \n" + fourB);

        ComplexMatrix five = one.tensorMultiply(threeB, fourB);
//        ComplexMatrix four = one.multiplyMatrix(one.getState(), ComplexGates.getCNOT());
        System.out.println(five);
//        ComplexQubit three = ComplexGates.applyCNOT(one, two);
//        System.out.println("Resulting Target Qubit state: \n" + three);

//        ComplexQubit result = ComplexGates.applyCNOT(one, two);
//        System.out.println("Result of 00 CNOT application CX 0,1: \n" + result);
//
//        ComplexQubit invertedResult = ComplexGates.applyCNOT(two, one);
//        System.out.println("Result of 00 CNOT application CX 1,0: \n" + invertedResult);
    }
}
