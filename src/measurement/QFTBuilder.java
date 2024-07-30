package measurement;

import complex_classes.ComplexMath;
import complex_classes.ComplexMatrix;
import complex_classes.ComplexNumber;
import state.WorkItem;

/**
 * This class implements the Quantum Fourier Transform (QFT) algorithm.
 * It uses a GateDirector to apply the necessary quantum gates.
 *
 * @author Robert Smith
 * @version 0.01
 * @since 29 July 2024
 */
public class QFTBuilder {
    private GateDirector gateD;
    private int numQubits;
    private int stateSize;

    /**
     * Constructor for the QFTBuilder class.
     *
     * @param gateD the active GateDirector
     */
    public QFTBuilder(GateDirector gateD) {
        this.gateD = gateD;
        this.stateSize = gateD.tracker.getStateVecSize();
        this.numQubits = (int) (Math.log(stateSize) / Math.log(2));

    }

    /**
     * Overridden toString() so the linter can s t f u.
     *
     * @return the string from the called toString().
     */
    @Override
    public String toString() {
        return gateD.tracker.toString();
    }

    /**
     * Applies the Quantum Fourier Transform to the current state vector.
     */
    public void applyQFT() {
        for (int i = numQubits - 1; i >= 0; i--) {
            applyHadamard(i);
            for (int j = i - 1; j >= 0; j--) {
                applyRk(j, i, numQubits - i);
            }
        }
        swapQubits();
    }

    /**
     * Swaps the order of qubits in the state vector.
     */
    private void swapQubits() {
        int idx = 0;
        int jdx = numQubits - 1;
        while (idx < jdx) {
            WorkItem applySwap = new WorkItem("SWAP", idx++, jdx--);
            ComplexMatrix matrix = gateD.getGate(applySwap);
        }
    }

    /**
     * Applies the controlled rotation gate Rk.
     *
     * @param controlQubit the control qubit
     * @param targetQubit  the target qubit
     * @param k the qubit limit while iterating through
     */
    private void applyRk(int controlQubit, int targetQubit, int k) {
        for (int i = 0; i < stateSize; i++) {
            int controlBit = (i >> controlQubit) & 1;
            int targetBit = (i >> targetQubit) & 1;
            if (controlBit == 1 && targetBit == 1) {
                double theta = 2 * Math.PI * targetBit / Math.pow(2, k);
                WorkItem Rk = new WorkItem("CRZ", controlQubit, targetQubit, theta);
//                ComplexNumber phase = new ComplexNumber(Math.cos(theta), Math.sin(theta));
//                gateD.tracker.getStateVec().set(i, 0, ComplexMath.multiplyComplexNumbers(gateD.tracker.getStateVec().get(i, 0), phase));
            }
        }
    }

    /**
     * Applies the Hadamard gate to the specified qubit.
     *
     * @param targetQubit the qubit to apply the Hadamard gate to
     */
    private void applyHadamard(int targetQubit) {
        WorkItem applyHadamard = new WorkItem("H", targetQubit);
        ComplexMatrix matrix = gateD.getGate(applyHadamard);
        gateD.tracker.setStateVec(ComplexMath.multiplyMatrix(matrix, gateD.tracker.getStateVec()));
    }
}

