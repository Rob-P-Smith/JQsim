package qubits;

import qubits.ComplexQubit;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Robert Smith
 * @version 0.1
 * @since 25 June 2024
 *
 */
public class ComplexGates {
    private static final double H_FACTOR = 1 / Math.sqrt(2);
    private static final ComplexMatrix PAULI_X = new ComplexMatrix(new ComplexNumber[][]{
            {new ComplexNumber(), new ComplexNumber(1)},
            {new ComplexNumber(1), new ComplexNumber()}});
    private static final ComplexMatrix PAULI_Z = new ComplexMatrix(new ComplexNumber[][]{
            {new ComplexNumber(1), new ComplexNumber()},
            {new ComplexNumber(), new ComplexNumber(-1)}});
    private static final ComplexMatrix PAULI_Y = new ComplexMatrix(new ComplexNumber[][]{
            {new ComplexNumber(), new ComplexNumber(0,-1)},
            {new ComplexNumber(0, 1), new ComplexNumber()}});
    private static final ComplexMatrix HADAMARD = new ComplexMatrix(new ComplexNumber[][]{
            {new ComplexNumber(H_FACTOR), new ComplexNumber(H_FACTOR)},
            {new ComplexNumber(H_FACTOR), new ComplexNumber(-H_FACTOR)}});

    public static ComplexQubit applyPauliX(ComplexQubit q) {
        return applyGate(PAULI_X, q);
    }

    public static ComplexQubit applyPauliZ(ComplexQubit q) {
        return applyGate(PAULI_Z, q);
    }

    public static ComplexQubit applyPauliY(ComplexQubit q) {
        return applyGate(PAULI_Y, q);
    }

    public static ComplexQubit applyHadamard(ComplexQubit q) {
        return applyGate(HADAMARD, q);
    }

    private static ComplexQubit applyGate(ComplexMatrix gate, ComplexQubit q) {
        ComplexMatrix result = gate.multiply(q.getState());
        return new ComplexQubit(result.get(0, 0), result.get(1, 0));
    }

    public static void printGates(){
        Map<String, ComplexMatrix> gates = new HashMap<>();
        gates.put("PAULI_X", PAULI_X);
        gates.put("PAULI_Y", PAULI_Y);
        gates.put("PAULI_Z", PAULI_Z);
        gates.put("HADAMAR", HADAMARD);

        for(String gate : gates.keySet()){
            System.out.println(gate+"");
            System.out.println(gates.get(gate));
        }
    }
}
