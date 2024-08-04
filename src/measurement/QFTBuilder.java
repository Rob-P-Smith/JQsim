package measurement;

import complex_classes.ComplexMath;
import complex_classes.ComplexSparse;
import state.WorkItem;

/**
 * This class implements the Quantum Fourier Transform (QFT) algorithm and its counterpart QFT inverse.
 * It uses a GateDirector to apply the necessary quantum gates.
 * In the formula
 * e^2PIijk/2^n for QFT
 * <ul>
 * <li>e^2PIi is solved with the cos/sin math in the R1 gate builder</li>
 * <li>j=i in the outer loop of applyQFT and applyQFTi</li>
 * <li>k=j in the inner loop of applyQFT and applyQFTi</li>
 * <li>n=k as the 3rd parameter passed to the applyRk and applyRki methods</li>
 * </ul>
 * Functionally, this represents rows as i and columns as j with k being the scalar on rotation of the Rk gate. For each
 * increment of k, the rotation scalar, the subsequent rotations are 1/2 the distance of the last, which reflects the
 * requirement in QFT to reduce the influence of each subsequent bit on transformed state by 50%.
 * @author Robert Smith
 * @version 0.1
 * @since 28 July 2024
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
                applyRk(i, j, k++);
            }
        }
        swapQubits();
    }

    /**
     * Applies the controlled rotation gate Rk.
     * <p>
     * theta below examples:
     * 2*Math.PI/Math.pow(2,2) = Math.PI/2
     * 2*Math.PI/Math.pow(2,3) = Math.PI/4
     * </p>
     * k starts at 1 because the hadamard applied already as the power 0 increment.
     *
     * @param controlQubit the control qubit
     * @param targetQubit  the target qubit
     * @see WorkItem
     * @see ComplexSparse
     * @see ComplexMath#multiplyMatrix(ComplexSparse, ComplexSparse)
     */
    private void applyRk(int controlQubit, int targetQubit, int k) {
//        System.out.println("In QFT.");
        double theta = (2 * Math.PI / Math.pow(2, k) / 2);
        WorkItem Rk = new WorkItem("CR1", numQubits - 1 - controlQubit, numQubits - 1 - targetQubit, theta);
        gateD.getGate(Rk);
    }

    /**
     * Applies the Quantum Fourier Transform Inverse to the current state vector.
     */
    public void applyQFTi() {
        swapQubits();
        for (int i = numQubits-1; i >=0 ; i--) {
            applyHadamard(numQubits - i-1);
            int k = 1;
            for (int j = i - 1; j >=0; j--) {
                applyRki(j, i, k++);
            }
        }
    }

    /**
     * Applies the controlled rotation gate inverse, Rki.
     * k starts at 1 because the hadamard applied already as the 0 exponent.
     *
     * @param controlQubit the control qubit
     * @param targetQubit  the target qubit
     * @see WorkItem
     * @see ComplexSparse
     * @see ComplexMath#multiplyMatrix(ComplexSparse, ComplexSparse)
     */
    private void applyRki(int controlQubit, int targetQubit, int k) {
//        System.out.println("In QFT inverse.");
        double theta = (2 * Math.PI / Math.pow(2, k) / 2);
        WorkItem Rki = new WorkItem("CR1i", numQubits - 1 - controlQubit, numQubits - 1 - targetQubit, theta);
        gateD.getGate(Rki);
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
     * @see ComplexSparse
     * @see ComplexMath#multiplyMatrix(ComplexSparse, ComplexSparse)
     */
    private void applyHadamard(int targetQubit) {
        WorkItem applyHadamard = new WorkItem("H", targetQubit);
        ComplexSparse matrix = gateD.getGate(applyHadamard);
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