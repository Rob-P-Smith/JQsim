package sandbox;

import complexClasses.*;

public class Sandbox {
    public static void main(String[] args) {
        ComplexObject coj = new ComplexObject();
        ComplexQubit zeroQubit = new ComplexQubit();
        zeroQubit.setState(ComplexGates.applyPauliX(zeroQubit.getState()));
        ComplexQubit oneQubit = new ComplexQubit();

        oneQubit.setState(ComplexGates.applyPauliX(oneQubit.getState()));

        System.out.println("Deriving the CNOT matrix from a CX of 0,1:");
        ComplexMatrix zeroDot = coj.outerProduct(zeroQubit.getState());
        System.out.println(zeroQubit + "\ndot Product is: \n" + zeroDot);
        ComplexMatrix oneDot = coj.outerProduct(oneQubit.getState());
        System.out.println(oneQubit + "\ndot Product is: \n" + oneDot);


        ComplexMatrix stepOne = coj.tensorMultiply(zeroDot, ComplexGates.getIdentity());
        System.out.println("Multiplying zeroDot against Identity returns: ");
        System.out.println(stepOne);

        ComplexMatrix stepTwo = coj.tensorMultiply(oneDot, ComplexGates.getPauliX());
        System.out.println("Multiplying oneDot against PauliX returns: ");
        System.out.println(stepTwo);

        ComplexMatrix stepThree = coj.addMatrix(stepOne, stepTwo);
        System.out.println("Adding zeroDot and oneDot results in: ");
        System.out.println(stepThree);

        System.out.println("/////////////////////////////////////");

        System.out.println("Deriving the CNOT matrix from a CX of 1,0:");
        ComplexMatrix rZeroDot = coj.outerProduct(zeroQubit.getState());
        System.out.println(zeroQubit + "\ndot Product is: \n" + zeroDot);
        ComplexMatrix rOneDot = coj.outerProduct(oneQubit.getState());
        System.out.println(oneQubit + "\ndot Product is: \n" + oneDot);

        ComplexMatrix rStepOne = coj.tensorMultiply(ComplexGates.getPauliX(), rOneDot);
        System.out.println("Multiplying rOneDot against Pauli X returns: ");
        System.out.println(rStepOne);

        ComplexMatrix rStepTwo = coj.tensorMultiply(ComplexGates.getIdentity(), rZeroDot);
        System.out.println("Multiplying rZeroDot against Identity returns: ");
        System.out.println(rStepTwo);

        ComplexMatrix rStepThree = coj.addMatrix(rStepOne, rStepTwo);
        System.out.println("Adding zeroDot and oneDot results in: ");
        System.out.println(rStepThree);

        ComplexMatrix finals = coj.multiplyMatrix(rStepThree, rStepThree.deriveStateVector(zeroQubit.getState(), oneQubit.getState()));
        System.out.println("Resultant Matrix of finals: \n" + finals);
    }
}