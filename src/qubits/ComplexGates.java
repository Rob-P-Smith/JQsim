package qubits;

import java.util.HashMap;
import java.util.Map;

/**
 * This class provides implementations of several quantum gates in complex form,
 * such as Pauli-X, Pauli-Y, Pauli-Z, and Hadamard gates. It also includes methods
 * to apply these gates to a {@link ComplexQubit} and to print these gates.
 *
 * <p>
 * Quantum gates are represented as {@link ComplexMatrix} objects, and the gates
 * provided here are static instances of predefined matrices.
 * </p>
 *
 * <p>
 * This class supports applying gates to {@link ComplexQubit} objects using matrix multiplication.
 * </p>
 *
 * <p>
 * Example usage:
 * <pre>{@code
 * ComplexQubit qubit = new ComplexQubit(new ComplexNumber(1, 0), new ComplexNumber(0, 0)); // Initialize qubit
 * ComplexQubit result = ComplexGates.applyPauliX(qubit); // Apply Pauli-X gate
 * }</pre>
 * </p>
 *
 * @author Robert Smith
 * @version 0.1
 * @since 25 June 2024
 */
public class ComplexGates {

    private static final double H_FACTOR = 1 / Math.sqrt(2);

    /**
     * Pauli-X gate matrix representation.
     */
    private static final ComplexMatrix PAULI_X = new ComplexMatrix(new ComplexNumber[][]{
            {new ComplexNumber(), new ComplexNumber(1)},
            {new ComplexNumber(1), new ComplexNumber()}
    });

    /**
     * Pauli-Z gate matrix representation.
     */
    private static final ComplexMatrix PAULI_Z = new ComplexMatrix(new ComplexNumber[][]{
            {new ComplexNumber(1), new ComplexNumber()},
            {new ComplexNumber(), new ComplexNumber(-1)}
    });

    /**
     * Pauli-Y gate matrix representation.
     */
    private static final ComplexMatrix PAULI_Y = new ComplexMatrix(new ComplexNumber[][]{
            {new ComplexNumber(), new ComplexNumber(0, -1)},
            {new ComplexNumber(0, 1), new ComplexNumber()}
    });

    /**
     * Hadamard gate matrix representation.
     */
    private static final ComplexMatrix HADAMARD = new ComplexMatrix(new ComplexNumber[][]{
            {new ComplexNumber(H_FACTOR), new ComplexNumber(H_FACTOR)},
            {new ComplexNumber(H_FACTOR), new ComplexNumber(-H_FACTOR)}
    });

    /**
     * CNOT gate matrix representation.
     */
    private static final ComplexMatrix CNOT = new ComplexMatrix(new ComplexNumber[][]{
            {new ComplexNumber(1), new ComplexNumber(), new ComplexNumber(), new ComplexNumber()},
            {new ComplexNumber(), new ComplexNumber(1), new ComplexNumber(), new ComplexNumber()},
            {new ComplexNumber(), new ComplexNumber(), new ComplexNumber(), new ComplexNumber(1)},
            {new ComplexNumber(), new ComplexNumber(), new ComplexNumber(1), new ComplexNumber()}
    });

    /**
     * Applies the Pauli-X gate to a {@link ComplexQubit}.
     *
     * @param target The input {@link ComplexQubit}.
     * @return The resulting {@link ComplexQubit} after applying the Pauli-X gate.
     */
    public static ComplexQubit applyPauliX(ComplexQubit target) {
        return applyGate(PAULI_X, target);
    }

    /**
     * Applies the Pauli-Z gate to a {@link ComplexQubit}.
     *
     * @param qubit The input {@link ComplexQubit}.
     * @return The resulting {@link ComplexQubit} after applying the Pauli-Z gate.
     */
    public static ComplexQubit applyPauliZ(ComplexQubit qubit) {
        return applyGate(PAULI_Z, qubit);
    }

    /**
     * Applies the Pauli-Y gate to a {@link ComplexQubit}.
     *
     * @param qubit The input {@link ComplexQubit}.
     * @return The resulting {@link ComplexQubit} after applying the Pauli-Y gate.
     */
    public static ComplexQubit applyPauliY(ComplexQubit qubit) {
        return applyGate(PAULI_Y, qubit);
    }

    /**
     * Applies the Hadamard gate to a {@link ComplexQubit}.
     *
     * @param qubit The input {@link ComplexQubit}.
     * @return The resulting {@link ComplexQubit} after applying the Hadamard gate.
     */
    public static ComplexQubit applyHadamard(ComplexQubit qubit) {
        return applyGate(HADAMARD, qubit);
    }

    /**
     * Applies a given gate represented by a {@link ComplexMatrix} to a {@link ComplexQubit}.
     *
     * @param gate The gate matrix to apply.
     * @param target    The input {@link ComplexQubit}.
     * @return The resulting {@link ComplexQubit} after applying the gate.
     */
    private static ComplexQubit applyGate(ComplexMatrix gate, ComplexQubit target) {
        ComplexMatrix result = gate.multiply(target.getState());
        target.setState(result);
        return target;
    }

    /**
     * Applies the CNOT gate to control and target qubits.
     *
     * @param controlQubit The control qubit.
     * @param targetQubit  The target qubit.
     * @return The resulting {@link ComplexQubit} after applying the CNOT gate.
     * @throws IllegalArgumentException If the qubits are not properly initialized or have incompatible dimensions.
     */
    public static ComplexQubit applyCNOT(ComplexQubit controlQubit, ComplexQubit targetQubit) {
        // Construct the CNOT matrix based on control and target qubit states
        ComplexMatrix cnotMatrix = CNOT;

        // Create a matrix representing the control qubit's state
        ComplexMatrix controlState = controlQubit.getState();

        // Ensure dimensions are compatible
        if (controlState.getRows() != 2 || controlState.getCols() != 1) {
            throw new IllegalArgumentException("Control qubit state must be a column vector of size 2x1.");
        }

        // Apply the CNOT gate by tensor multiplying control state with CNOT matrix
        ComplexMatrix resultMatrix = cnotMatrix.tensorMultiply(controlState);
        System.out.println("Result Matrix Of Tensor Multiply Applying a CNOT: "+resultMatrix);

        // Update the target qubit's state with the result
        targetQubit.setState(resultMatrix);

        return targetQubit;
    }

    /**
     * Prints the predefined quantum gates and their matrix representations.
     */
    public static void printGates() {
        Map<String, ComplexMatrix> gates = new HashMap<>();
        gates.put("PAULI_X", PAULI_X);
        gates.put("PAULI_Y", PAULI_Y);
        gates.put("PAULI_Z", PAULI_Z);
        gates.put("HADAMARD", HADAMARD);

        for (String gate : gates.keySet()) {
            System.out.println(gate);
            System.out.println(gates.get(gate));
        }
    }
}
