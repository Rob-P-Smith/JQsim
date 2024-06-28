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
        testBit = ComplexGates.applyPauliX(testBit);
        System.out.println(testBit);
        System.out.println(newBit);
        System.out.println(terdBit);
        ComplexMatrix cnoting = ComplexGates.applyCNOT(testBit, newBit).getState();

        testBit = new ComplexQubit(
                cnoting.get(0,0),
                cnoting.get(1,0),
                testBit.getQubitID()
        );

        newBit = new ComplexQubit(
                cnoting.get(2,0),
                cnoting.get(3,0),
                newBit.getQubitID()
        );

        ComplexMatrix resultBit = ComplexGates.applyCNOT(newBit, terdBit).getState();

        newBit = new ComplexQubit(
                resultBit.get(0,0),
                resultBit.get(1,0),
                newBit.getQubitID()
        );

        terdBit = new ComplexQubit(
                resultBit.get(2,0),
                resultBit.get(3, 0),
                terdBit.getQubitID()
        );

        System.out.println("\nTest Bit: \n"+testBit);
        System.out.println("\nNew Bit: \n"+newBit);
        System.out.println("\nTerd Bit: \n"+terdBit);
    }
}

