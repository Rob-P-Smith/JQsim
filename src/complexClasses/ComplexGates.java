package complexClasses;

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
public class ComplexGates extends ComplexObject {
    private static final boolean CNOTDEBUG = false;
    private static final boolean DEBUG = false;
    private static final double H_FACTOR = 1 / Math.sqrt(2);

    /**
     * Pauli-X gate matrix.
     */
    private static final ComplexMatrix PAULI_X = new ComplexMatrix(new ComplexNumber[][]{
            {new ComplexNumber(0), new ComplexNumber(1)},
            {new ComplexNumber(1), new ComplexNumber(0)}
    });

    /**
     * Pauli-Z gate matrix.
     */
    private static final ComplexMatrix PAULI_Z = new ComplexMatrix(new ComplexNumber[][]{
            {new ComplexNumber(1), new ComplexNumber()},
            {new ComplexNumber(), new ComplexNumber(-1)}
    });

    /**
     * Pauli-Y gate matrix.
     */
    private static final ComplexMatrix PAULI_Y = new ComplexMatrix(new ComplexNumber[][]{
            {new ComplexNumber(), new ComplexNumber(0, -1)},
            {new ComplexNumber(0, 1), new ComplexNumber()}
    });

    /**
     * Hadamard gate matrix.
     */
    private static final ComplexMatrix HADAMARD = new ComplexMatrix(new ComplexNumber[][]{
            {new ComplexNumber(H_FACTOR), new ComplexNumber(H_FACTOR)},
            {new ComplexNumber(H_FACTOR), new ComplexNumber(-H_FACTOR)}
    });

    /**
     * Identity gate matrix.
     */
    private static final ComplexMatrix IDENTITY = new ComplexMatrix(new ComplexNumber[][]{
            {new ComplexNumber(1), new ComplexNumber()},
            {new ComplexNumber(), new ComplexNumber(1)}
    });

    /**
     * Identity Matrix Getter
     *
     * @return the identity matrix
     */
    public static ComplexMatrix getIdentity() {
        return IDENTITY;
    }

    /**
     * Pauli_X Matrix Getter
     *
     * @return the Pauli_X matrix
     */
    public static ComplexMatrix getPauliX() {
        return PAULI_X;
    }

    /**
     * Pauli_Y Matrix Getter
     *
     * @return the Pauli_Y matrix
     */
    public static ComplexMatrix getPauliY() {
        return PAULI_Y;
    }

    /**
     * Pauli_Z Matrix Getter
     *
     * @return the Pauli_Z matrix
     */
    public static ComplexMatrix getPauliZ() {
        return PAULI_Z;
    }

    /**
     * Applies the Pauli-X gate to a {@link ComplexQubit}.
     *
     * @param target The input {@link ComplexQubit}.
     * @return The resulting {@link ComplexQubit} after applying the Pauli-X gate.
     */
    public static ComplexMatrix applyPauliX(ComplexMatrix target) {
        return applyGate(PAULI_X, target);
    }

    /**
     * Applies the Pauli-Z gate to a {@link ComplexQubit}.
     *
     * @param qubit The input {@link ComplexQubit}.
     * @return The resulting {@link ComplexQubit} after applying the Pauli-Z gate.
     */
    public static ComplexMatrix applyPauliZ(ComplexMatrix qubit) {
        return applyGate(PAULI_Z, qubit);
    }

    /**
     * Applies the Pauli-Y gate to a {@link ComplexQubit}.
     *
     * @param qubit The input {@link ComplexQubit}.
     * @return The resulting {@link ComplexQubit} after applying the Pauli-Y gate.
     */
    public static ComplexMatrix applyPauliY(ComplexMatrix qubit) {
        return applyGate(PAULI_Y, qubit);
    }

    /**
     * Applies the Hadamard gate to a {@link ComplexQubit}.
     *
     * @param qubit The input {@link ComplexQubit}.
     * @return The resulting {@link ComplexQubit} after applying the Hadamard gate.
     */
    public static ComplexMatrix applyHadamard(ComplexMatrix qubit) {
        return applyGate(HADAMARD, qubit);
    }

    /**
     * Applies the Identity gate to a {@link ComplexQubit}.
     *
     * @param qubit The input {@link ComplexQubit}.
     * @return The resulting {@link ComplexQubit} after applying the Identity gate.
     */
    public static ComplexMatrix applyIdentity(ComplexMatrix qubit) {
        return applyGate(IDENTITY, qubit);
    }

    /**
     * Applies a given gate represented by a {@link ComplexMatrix} to a {@link ComplexQubit}.
     *
     * @param gate   The gate matrix to apply.
     * @param target The input {@link ComplexQubit}.
     * @return The resulting {@link ComplexQubit} after applying the gate.
     */
    private static ComplexMatrix applyGate(ComplexMatrix gate, ComplexMatrix target) {
        return gate.multiplyMatrix(gate, target);
    }

    /**
     * Applies the CNOT gate to control and target qubits.
     *
     * @param controlQubit The control qubit.
     * @param targetQubit  The target qubit.
     * @throws IllegalArgumentException If the qubits are not properly initialized or have incompatible dimensions.
     */

    public static void applyCNOT(ComplexQubit controlQubit, ComplexQubit targetQubit) {

        ComplexObject coj = new ComplexObject();
        ComplexMatrix CNOT = new ComplexMatrix(4, 4), controlProduct;
        ComplexMatrix resultMatrix = new ComplexMatrix(4, 1);
        ComplexMatrix controlState = controlQubit.getState();
        ComplexMatrix targetState = targetQubit.getState();
        // Ensure dimensions are compatible
        if ((controlState.getHeight() != 2 || controlState.getWidth() != 1) ||
                (targetState.getHeight() != 2 || targetState.getWidth() != 1)) {
            throw new IllegalArgumentException("Control qubit state must be a column vector of size 2x1.");
        }
        ComplexMatrix inputStateVector = coj.deriveStateVector(controlState, targetState);
        if (CNOTDEBUG) System.out.println("Input State Vecotr: \n" + inputStateVector);

        generateCNOT(CNOT);
        if (CNOTDEBUG) System.out.println("CNOT is \n" + CNOT);
        resultMatrix = coj.multiplyMatrix(CNOT, inputStateVector);
        if (CNOTDEBUG) System.out.println("Result Matrix is: \n" + resultMatrix);

        controlQubit.setState(deriveControlState(controlState, resultMatrix));
        targetQubit.setState(deriveTargetState(targetState, resultMatrix));
    }

    private static void generateCNOT(ComplexMatrix CNOT) {
        ComplexObject coj = new ComplexObject();
        ComplexMatrix qubitZero = new ComplexMatrix(new ComplexNumber[][]{
                {new ComplexNumber(1)},
                {new ComplexNumber()}
        });
        ComplexMatrix qubitOne = new ComplexMatrix(new ComplexNumber[][]{
                {new ComplexNumber()},
                {new ComplexNumber(1)}
        });
        //TODO Explainer: Using relocated inputs for CX 1,0 allows the use of the same CNOT gate to all
        // the application of the 1000, 0001, 0010, 0100 CNOT gate only applies if you feed it CX 0,1 but the desire
        // output is the flipped version. My version allows either to be supplied in user order and still get the right
        // result. If using the flipped input, you would need to flip control/target to use the above listed CNOT gate.

        ComplexMatrix zeroOuterProduct = coj.outerProduct(qubitZero);
        ComplexMatrix oneOuterProduct = coj.outerProduct(qubitOne);
        ComplexMatrix zeroProduct, oneProduct;

        zeroProduct = coj.tensorMultiply(zeroOuterProduct, ComplexGates.getIdentity());
        oneProduct = coj.tensorMultiply(oneOuterProduct, ComplexGates.getPauliX());

        if (CNOTDEBUG) System.out.println("zeroProduct: \n" + zeroProduct);
        if (CNOTDEBUG) System.out.println("oneProduct: \n" + oneProduct);
        ComplexMatrix temp = coj.addMatrix(oneProduct, zeroProduct);

        CNOT.setData(temp.getData());
    }

    private static ComplexMatrix deriveControlState(ComplexMatrix controlState, ComplexMatrix resultMatrix) {
        if (resultMatrix.get(0, 0).getReal() == 1.0) {
            controlState.setData(new ComplexNumber[][]{
                    {new ComplexNumber(1)},
                    {new ComplexNumber()}
            });
        } else if (resultMatrix.get(1, 0).getReal() == 1.0) {
            controlState.setData(new ComplexNumber[][]{
                    {new ComplexNumber(1)},
                    {new ComplexNumber()}
            });
        } else if (resultMatrix.get(2, 0).getReal() == 1.0) {
            controlState.setData(new ComplexNumber[][]{
                    {new ComplexNumber()},
                    {new ComplexNumber(1)}
            });
        } else if (resultMatrix.get(3, 0).getReal() == 1.0) {
            controlState.setData(new ComplexNumber[][]{
                    {new ComplexNumber()},
                    {new ComplexNumber(1)}
            });
        }
        return controlState;
    }

    private static ComplexMatrix deriveTargetState(ComplexMatrix targetState, ComplexMatrix resultMatrix) {

        if (resultMatrix.get(0, 0).getReal() == 1.0) {
            targetState.setData(new ComplexNumber[][]{
                    {new ComplexNumber(1)},
                    {new ComplexNumber()}
            });
        } else if (resultMatrix.get(1, 0).getReal() == 1.0) {
            targetState.setData(new ComplexNumber[][]{
                    {new ComplexNumber()},
                    {new ComplexNumber(1)}
            });
        } else if (resultMatrix.get(2, 0).getReal() == 1.0) {
            targetState.setData(new ComplexNumber[][]{
                    {new ComplexNumber(1)},
                    {new ComplexNumber()}
            });
        } else if (resultMatrix.get(3, 0).getReal() == 1.0) {
            targetState.setData(new ComplexNumber[][]{
                    {new ComplexNumber()},
                    {new ComplexNumber(1)}
            });
        }
        return targetState;
    }

    /**
     * Prints the predefined quantum gates and their matrix.
     */
    public static void printGates() {
        Map<String, ComplexMatrix> gates = new HashMap<>();
        gates.put("PAULI_X", PAULI_X);
        gates.put("PAULI_Y", PAULI_Y);
        gates.put("PAULI_Z", PAULI_Z);
        gates.put("HADAMARD", HADAMARD);
        gates.put("IDENTITY", IDENTITY);

        for (String gate : gates.keySet()) {
            System.out.println(gate);
            System.out.println(gates.get(gate));
        }
    }
}
