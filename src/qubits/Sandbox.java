package qubits;

public class Sandbox {
    public static void main(String[] args) {
        System.out.println("Start State 1: ");
        ComplexQubit testBit = new ComplexQubit(new ComplexNumber(0,0),new ComplexNumber(1,0));
        ComplexQubit newBit = new ComplexQubit(new ComplexNumber(0,0), new ComplexNumber(1,0));
        System.out.println(testBit);
        System.out.println(newBit);
        testBit = ComplexGates.applyCNOT(testBit, newBit);

        System.out.println("Start State 2: ");
        testBit = new ComplexQubit(new ComplexNumber(1,0),new ComplexNumber(0,0));
        newBit = new ComplexQubit(new ComplexNumber(1,0), new ComplexNumber(0,0));
        System.out.println(testBit);
        System.out.println(newBit);

        testBit = ComplexGates.applyCNOT(testBit, newBit);

        System.out.println("Start State 3: ");
        testBit = new ComplexQubit(new ComplexNumber(0,0),new ComplexNumber(1,0));
        newBit = new ComplexQubit(new ComplexNumber(1,0), new ComplexNumber(0,0));
        System.out.println(testBit);
        System.out.println(newBit);

        testBit = ComplexGates.applyCNOT(testBit, newBit);

        System.out.println("Start State 4: ");
        testBit = new ComplexQubit(new ComplexNumber(1,0),new ComplexNumber(0,0));
        newBit = new ComplexQubit(new ComplexNumber(0,0), new ComplexNumber(1,0));
        System.out.println(testBit);
        System.out.println(newBit);

        testBit = ComplexGates.applyCNOT(testBit, newBit);
    }

}

