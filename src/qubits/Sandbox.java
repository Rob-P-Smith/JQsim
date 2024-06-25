package qubits;

public class Sandbox {
    public static void main(String[] args) {

        //TODO When this complex version is functional move double version into archive pacakge
        System.out.println("\nTesting applying gates to a qubit with [1,0] using ComplexNumbers...\n");

        ComplexQubit cQubit = new ComplexQubit();
        System.out.println("Test initialize qubit: \n"+cQubit);
        cQubit = ComplexGates.applyHadamard(cQubit);
        System.out.println("Hardamard applied: \n"+cQubit);

        cQubit = new ComplexQubit();
        cQubit = ComplexGates.applyPauliX(cQubit);
        System.out.println("Palui X applied: \n"+cQubit);

        cQubit = new ComplexQubit();
        cQubit = ComplexGates.applyPauliY(cQubit);
        System.out.println("Pauli Y applied: \n"+cQubit);

        cQubit = new ComplexQubit();
        cQubit = ComplexGates.applyPauliZ(cQubit);
        System.out.println("Pauli Z applied: \n"+cQubit);

        //ComplexGates.printGates();
    }

}

