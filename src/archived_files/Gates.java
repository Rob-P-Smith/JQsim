package archived_files;

/**
 * Represents the quantum gates to be called in adjusting the state when setting up the simulation
 *
 * @author Robert P Smith
 * @version 0.1
 * @since 20 June 2024
 */
public class Gates {
    private static final double H_FACTOR = 1 / Math.sqrt(2);
    static final Matrix PAULI_X = new Matrix(new double[][]{{0, 1}, {1, 0}});
    private static final Matrix PAULI_Z = new Matrix(new double[][]{{1, 0}, {0, -1}});
    private static final Matrix PAULI_Y = new Matrix(new double[][]{{0, -1}, {1, 0}});
    private static final Matrix HADAMARD = new Matrix(new double[][]{{H_FACTOR, H_FACTOR}, {H_FACTOR, -H_FACTOR}});

    public static Qubit applyPauliX(Qubit q) {
        return applyGate(PAULI_X, q);
    }

    public static Qubit applyPauliZ(Qubit q) {
        return applyGate(PAULI_Z, q);
    }

    public static Qubit applyPauliY(Qubit q) {
        return applyGate(PAULI_Y, q);
    }

    public static Qubit applyHadamard(Qubit q) {
        return applyGate(HADAMARD, q);
    }

    private static Qubit applyGate(Matrix gate, Qubit q) {
        Matrix result = gate.multiply(q.getState());
        return new Qubit(result.get(0, 0), result.get(1, 0));
    }
}
