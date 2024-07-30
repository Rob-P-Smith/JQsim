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
    private ComplexMatrix inputMatrix;
    private int numQubits;
    private int stateSize;

    /**
     * Constructor for the QFTBuilder class.
     *
     * @param gateD the active GateDirector
     */
    public QFTBuilder(GateDirector gateD) {
        this.gateD = gateD;
        this.inputMatrix = gateD.tracker.getStateVec();
        this.numQubits = (int) (Math.log(inputMatrix.getHeight()) / Math.log(2));
        this.stateSize = inputMatrix.getHeight();
    }

    @Override
    public String toString() {
        return gateD.tracker.toString();
    }

    /**
     * Applies the Quantum Fourier Transform to the current state vector.
     */
    public void applyQFT(){
        for(int i = numQubits - 1; i >= 0; i--){
            applyHadamard(i);
            for(int j = i - 1; j >= 0; j--){
                applyRk(j, i, numQubits - i);
            }
        }
        swapQubits();
    }

    /**
     * Swaps the order of qubits in the state vector.
     * 32 - numQubits only works for a system with 32 or fewer qubits, going over 32 requires implementation of long for the qubit size to allow more bits.
     * The 32 qubit limit will not be an issue due to memory constraints, even when sparse storage is implemented.
     */
    private void swapQubits(){
        ComplexMatrix reversedMatrix = new ComplexMatrix(1<<numQubits, 1);
        final int BITCOUNT = 32;
        for (int i = 0; i < stateSize; i++) {
            int reversedIndex = Integer.reverse(i) >>> (BITCOUNT - numQubits);
            reversedMatrix.set(reversedIndex, 0, inputMatrix.get(i, 0));
        }
        inputMatrix = reversedMatrix;
    }

    /**
     * Applies the controlled rotation gate Rk.
     * @param controlQubit the control qubit
     * @param targetQubit the target qubit
     * @param limit the rotation parameter
     */
    private void applyRk(int controlQubit, int targetQubit, int limit){

        for (int i = 0; i < stateSize; i++) {
            int controlBit = (i >> controlQubit) & 1;
            int targetBit = (i >> targetQubit) & 1;

            if (controlBit == 1) {
                double theta = 2 * Math.PI * targetBit / Math.pow(2, limit);
                ComplexNumber phase = new ComplexNumber(Math.cos(theta), Math.sin(theta));
                inputMatrix.set(i, 0, ComplexMath.multiplyComplexNumbers(inputMatrix.get(i, 0) , phase));
            }
        }
    }

    /**
     * Applies the Hadamard gate to the specified qubit.
     * @param targetQubit the qubit to apply the Hadamard gate to
     */
    private void applyHadamard(int targetQubit){
        WorkItem applyHadamard = new WorkItem("H", targetQubit);
        ComplexMatrix matrix = gateD.getGate(applyHadamard);
        inputMatrix = ComplexMath.multiplyMatrix(matrix, inputMatrix);
    }
}

