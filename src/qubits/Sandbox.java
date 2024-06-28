package qubits;

public class Sandbox {
    public static void main(String[] args) {
//        System.out.println("Start State 1: ");
//        ComplexQubit testBit = new ComplexQubit(new ComplexNumber(0,0),new ComplexNumber(1,0));
//        ComplexQubit newBit = new ComplexQubit(new ComplexNumber(0,0), new ComplexNumber(1,0));
//        System.out.println(testBit);
//        System.out.println(newBit);
//        testBit = ComplexGates.applyCNOT(testBit, newBit);
//
//        System.out.println("Start State 2: ");
//        testBit = new ComplexQubit(new ComplexNumber(1,0),new ComplexNumber(0,0));
//        newBit = new ComplexQubit(new ComplexNumber(1,0), new ComplexNumber(0,0));
//        System.out.println(testBit);
//        System.out.println(newBit);
//
//        testBit = ComplexGates.applyCNOT(testBit, newBit);
//
//        System.out.println("Start State 3: ");
//        testBit = new ComplexQubit(new ComplexNumber(0,0),new ComplexNumber(1,0));
//        newBit = new ComplexQubit(new ComplexNumber(1,0), new ComplexNumber(0,0));
//        System.out.println(testBit);
//        System.out.println(newBit);
//
//        testBit = ComplexGates.applyCNOT(testBit, newBit);
//
//        System.out.println("Start State 4: ");
//        testBit = new ComplexQubit(new ComplexNumber(1,0),new ComplexNumber(0,0));
//        newBit = new ComplexQubit(new ComplexNumber(0,0), new ComplexNumber(1,0));
//        System.out.println(testBit);
//        System.out.println(newBit);
//
//        testBit = ComplexGates.applyCNOT(testBit, newBit);

        ComplexQubit testBit = new ComplexQubit(new ComplexNumber(1,0),new ComplexNumber(0,0));
        ComplexQubit newBit = new ComplexQubit(new ComplexNumber(1,0), new ComplexNumber(0,0));
        ComplexQubit terdBit = new ComplexQubit(new ComplexNumber(1, 0), new ComplexNumber(0,0));
        testBit = ComplexGates.applyHadamard(testBit);
        System.out.println("Start State: "+testBit);
        System.out.println("Start State: "+newBit);
        newBit = ComplexGates.applyCNOT(testBit, newBit);

        //TODO: Remember that the Bell state is not applied because the 1/sqrt(2) is not resolved. A simulation
        // must be run to get the superposition result of the control qubit to set the state of the target qubit
        // at this point. Otherwise, the state always multiplies out as [0,1] for the target qubit in the tensor
        // multiplication.

        System.out.println(newBit);
    }
}

