package measurement;

import complex_classes.ComplexMath;
import complex_classes.ComplexMatrix;
import state.WorkItem;

/**
 * This class implements the Quantum Fourier Transform (QFT) algorithm.
 * It uses a GateDirector to apply the necessary quantum gates.
 *
 *  * @author Robert Smith
 *  * @version 0.1
 *  * @since 28 July 2024
 *
 * @see GateDirector
 * @see complex_classes.ComplexMath
 * @see complex_classes.ComplexMatrix
 * @see state.WorkItem
 */
public class QFTBuilder {
    private final GateDirector gateD;
    private final int numQubits;
    private final int stateSize;

    /**
     * Applies the Quantum Fourier Transform to the current state vector.
     */
    public void applyQFT() {

        for (int i = 0; i < numQubits; i++) {
            applyHadamard(numQubits - 1 - i);
            int k = 1;
            for (int j = i + 1; j < numQubits; j++) {
                applyRk(i,j,k++);
            }
        }
        swapQubits();
    }

    /**
     * Applies the controlled rotation gate Rk.
     *<p>
     * theta below examples:
     * 2*Math.PI/Math.pow(2,2) = Math.PI/2
     * 2*Math.PI/Math.pow(2,3) = Math.PI/4
     *</p>
     * k starts at 2 because the hadamard applied already is the R1 version of Rk and RZ already
     * cuts theta in half during construction each iteration through the method cuts the degree
     * of rotation in half by exponentially increasing the divisor for 2 * Math.PI for each
     * subsequent application of Rk to a given qubit in register having QFT applied.
     * @param controlQubit the control qubit
     * @param targetQubit  the target qubit
     * @see WorkItem
     * @see ComplexMatrix
     * @see ComplexMath#multiplyMatrix(ComplexMatrix, ComplexMatrix)
     */
    private void applyRk(int controlQubit, int targetQubit, int k) {
        double theta = (2 * Math.PI / Math.pow(2,k));
        WorkItem Rk = new WorkItem("CRZ", numQubits-1-controlQubit, numQubits-1-targetQubit, theta);
        gateD.getGate(Rk);
    }

    /**
     * Swaps the order of qubits in the state vector.
     */
    private void swapQubits() {
        for (int i = 0; i < numQubits / 2; i++) {
            WorkItem applySwap = new WorkItem("SWAP", i, numQubits - 1 - i);
            gateD.getGate(applySwap);
        }
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


}