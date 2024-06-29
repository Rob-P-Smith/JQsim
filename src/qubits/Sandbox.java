package qubits;

import complexClasses.ComplexGates;
import complexClasses.ComplexMatrix;
import complexClasses.ComplexNumber;
import complexClasses.ComplexQubit;

import java.util.Random;

public class Sandbox {
    public static void main(String[] args) {
        ComplexQubit controlQubit = new ComplexQubit();
        ComplexQubit targetQubit = new ComplexQubit();
        ComplexMatrix control = new ComplexMatrix(2, 1);
        ComplexNumber none = new ComplexNumber(1,0);
        ComplexNumber ntwo = new ComplexNumber(0,0);
        ComplexMatrix target = new ComplexMatrix(2, 1);

        System.out.println("............|00>................................................");
        control.set(0,0,none);
        control.set(1,0,ntwo);
        target.set(0,0, none);
        target.set(1,0,ntwo);
        System.out.println("Control Initial a|0> :\n"+control);
        System.out.println("Target Initial b|0>:\n"+target);
        controlQubit.setState(control);
        targetQubit.setState(target);
        ComplexQubit testing = ComplexGates.applyCNOT(controlQubit, targetQubit);
        System.out.println(testing);
        System.out.println("................................................................");

        System.out.println("...............|01>.............................................");
        control.set(0,0,none);
        control.set(1,0,ntwo);
        target.set(0,0, ntwo);
        target.set(1,0,none);
        System.out.println("Control Initial a|0> :\n"+control);
        System.out.println("Target Initial b|1> :\n"+target);
        controlQubit.setState(control);
        targetQubit.setState(target);
        testing = ComplexGates.applyCNOT(controlQubit, targetQubit);
        System.out.println(testing);
        System.out.println("................................................................");

        System.out.println(".................|11>...........................................");
        control.set(0,0,ntwo);
        control.set(1,0,none);
        target.set(0,0, ntwo);
        target.set(1,0,none);
        System.out.println("Control Initial a|1> :\n"+control);
        System.out.println("Target Initial b|1> :\n"+target);
        controlQubit.setState(control);
        targetQubit.setState(target);
        testing = ComplexGates.applyCNOT(controlQubit, targetQubit);
        System.out.println(testing);
        System.out.println("................................................................");

        System.out.println("..................|10>..........................................");
        control.set(0,0,ntwo);
        control.set(1,0,none);
        target.set(0,0, none);
        target.set(1,0,ntwo);
        System.out.println("Control Initial a|1> :\n"+control);
        System.out.println("Target Initial b|0> :\n"+target);
        controlQubit.setState(control);
        targetQubit.setState(target);
        testing = ComplexGates.applyCNOT(controlQubit, targetQubit);
        System.out.println(testing);
        System.out.println("................................................................");

        final double PI = 3.14159;
        Random rand = new Random();
        ComplexMatrix testMatrix = new ComplexMatrix(3,3);
        for(int i = 0; i<testMatrix.getHeight(); i++){
            for(int j = 0; j<testMatrix.getWidth(); j++){
                int random = rand.nextInt(9);
                int sign = rand.nextInt(2);
                int phase = -1;
                if(sign == 0){
                    phase  = 1;
                }
                testMatrix.set(i,j,new ComplexNumber(phase*(i+random*PI),phase*-1*((random+j)*PI)));
            }
        }

        System.out.println(testMatrix);

//        testMatrix = testMatrix.getConjugateTranspose(testMatrix);
        testMatrix = testMatrix.getTranspose(testMatrix);

        System.out.println(testMatrix);

        ComplexMatrix testMatrixTwo = new ComplexMatrix(2,3);
        testMatrixTwo.set(0,0,new ComplexNumber(1, 0));
        testMatrixTwo.set(0,1,new ComplexNumber(2, 0));
        testMatrixTwo.set(0,2,new ComplexNumber(3, 0));
        testMatrixTwo.set(1,0,new ComplexNumber(4, 0));
        testMatrixTwo.set(1,1,new ComplexNumber(5, 0));
        testMatrixTwo.set(1,2,new ComplexNumber(6, 0));

        System.out.println("Original \n"+testMatrixTwo);

        testMatrixTwo = testMatrixTwo.getTranspose(testMatrixTwo);

        System.out.println(testMatrixTwo);
//        testBit = ComplexGates.applyHadamard(testBit);
//        System.out.println("Start State: "+testBit);
//        System.out.println("Start State: "+newBit);
//        newBit = ComplexGates.applyCNOT(testBit, newBit);

        //TODO: Remember that the Bell state is not applied because the 1/sqrt(2) is not resolved. A simulation
        // must be run to get the superposition result of the control qubit to set the state of the target qubit
        // at this point. Otherwise, the state always multiplies out as [0,1] for the target qubit in the tensor
        // multiplication.

//        System.out.println(newBit);
    }
}

