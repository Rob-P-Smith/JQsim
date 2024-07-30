package measurement;

import complex_classes.ComplexMath;
import complex_classes.ComplexMatrix;
import state.WorkItem;

/**
 * This class implements the Quantum Fourier Transform (QFT) algorithm.
 * It uses a GateDirector to apply the necessary quantum gates.
 *
 * @see GateDirector
 * @see complex_classes.ComplexMath
 * @see complex_classes.ComplexMatrix
 * @see state.WorkItem
 */
public class QFTBuilder {
    private GateDirector gateD;
    private int numQubits;
    private int stateSize;

    /**
     * Constructor for the QFTBuilder class.
     *
     * @param gateD the active GateDirector
     * @see GateDirector
     */
    public QFTBuilder(GateDirector gateD) {
        this.gateD = gateD;
        this.stateSize = gateD.tracker.getStateVecSize();
        this.numQubits = (int) (Math.log(stateSize) / Math.log(2));
    }

    /**
     * Returns a string representation of the current quantum state.
     *
     * @return a string representation of the quantum state
     */
    @Override
    public String toString() {
        return gateD.tracker.toString();
    }

    /**
     * Applies the Quantum Fourier Transform to the current state vector.
     */
    public void applyQFT() {
        for (int i = 0; i < numQubits; i++) {
            applyHadamard(i);
            for (int j = i + 1; j < numQubits; j++) {
                applyRk(i, j, j - i);
            }
        }
        swapQubits();
    }

    /**
     * Swaps the order of qubits in the state vector.
     */
    private void swapQubits() {
        for (int i = 0; i < numQubits / 2; i++) {
            WorkItem applySwap = new WorkItem("SWAP", numQubits - 1 - i, i);
            gateD.getGate(applySwap);
        }
    }

    /**
     * Applies the controlled rotation gate Rk.
     *
     * @param controlQubit the control qubit
     * @param targetQubit  the target qubit
     * @param k            the qubit limit while iterating through
     * @see WorkItem
     * @see ComplexMatrix
     * @see ComplexMath#multiplyMatrix(ComplexMatrix, ComplexMatrix)
     */
    private void applyRk(int controlQubit, int targetQubit, int k) {
        double theta = 2 * Math.PI / Math.pow(2, k);
        WorkItem Rk = new WorkItem("CRZ", controlQubit, targetQubit, theta);
        ComplexMatrix matrix = gateD.getGate(Rk);
        gateD.tracker.setStateVec(ComplexMath.multiplyMatrix(matrix, gateD.tracker.getStateVec()));
    }

    /**
     * Applies the Hadamard gate to the specified qubit.
     *
     * @param targetQubit the qubit to apply the Hadamard gate to
     * @see WorkItem
     * @see ComplexMatrix
     * @see ComplexMath#multiplyMatrix(ComplexMatrix, ComplexMatrix)
     */
    private void applyHadamard(int targetQubit) {
        WorkItem applyHadamard = new WorkItem("H", targetQubit);
        ComplexMatrix matrix = gateD.getGate(applyHadamard);
        gateD.tracker.setStateVec(ComplexMath.multiplyMatrix(matrix, gateD.tracker.getStateVec()));
    }
}