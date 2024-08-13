package measurement;

import complex_classes.ComplexMath;
import complex_classes.ComplexNumber;
import complex_classes.ComplexSparse;
import state.WorkItem;

/**
 * The MultiQubitGateBuilder class is responsible for constructing and applying multi-qubit quantum gates
 * to a quantum state vector. It handles both dual-qubit gates (one control, one target) and multi-qubit gates
 * (multiple controls and/or targets).
 *
 * This class works in conjunction with the GateDirector to manage the application of complex quantum operations.
 *
 * @author Robert Smith
 * @version 1.0
 * @since 11 August 2024
 * @see GateDirector
 * @see WorkItem
 * @see ComplexSparse
 */
public class MultiQubitGateBuilder {
    private GateDirector gateD;

    /**
     * Constructor for the MultiQubitGateBuilder class.
     *
     * @param gateD the active GateDirector used to manage gate operations and state tracking
     */
    public MultiQubitGateBuilder(GateDirector gateD) {
        this.gateD = gateD;
    }

    /**
     * Returns a string representation of the final gate matrix.
     * This method delegates to the toString method of the finalGate in the GateDirector.
     *
     * @return A string representation of the final gate matrix.
     */
    @Override
    public String toString() {
        return gateD.finalGate.toString();
    }

    /**
     * Builds the operator matrix for a gate with one or more control qubits and one or more target qubits.
     * This method determines whether the gate is a dual-qubit or multi-qubit operation and delegates to the
     * appropriate method for gate application.
     *
     * Internal functionality:
     * 1. Determines the number of qubits in the system.
     * 2. Validates control and target qubit indices.
     * 3. Calls either applyMultiQubitGate or applyDualQubitGate based on the WorkItem type.
     *
     * @param work The WorkItem containing the gate information.
     * @throws IllegalArgumentException if the control or target qubit indices are invalid.
     * @see WorkItem
     */
    void dualAndMultiGateSetup(WorkItem work) {
        //Setup basic variables
        int numQubits = (int) (Math.log(gateD.tracker.getStateVecSize()) / Math.log(2));
        int controlQubit, targetQubit;
        Integer[] controlQubits, targetQubits;
        //Determine which type of work item (gate) it is, dual or multi-qubit and test their values for viability.
        if (work.isMultiTarget()) {
            controlQubits = work.getControls();
            targetQubits = work.getTargets();
            for (Integer control : controlQubits) {
                for (Integer target : targetQubits) {
                    if (control == target || control >= numQubits || target >= numQubits || control < 0 || target < 0) {
                        throw new IllegalArgumentException("Invalid control or target qubit.");
                    }
                }
            }
            applyMultiQubitGate(work.getOperator(), controlQubits, targetQubits, numQubits);
        } else {
            controlQubit = work.getControl();
            targetQubit = work.getTarget();
            if (controlQubit >= numQubits || targetQubit >= numQubits || controlQubit < 0 || targetQubit < 0) {
//            if (controlQubit == targetQubit || controlQubit >= numQubits || targetQubit >= numQubits || controlQubit < 0 || targetQubit < 0) {
                throw new IllegalArgumentException("Invalid control or target qubit.");
            }
            applyDualQubitGate(work, numQubits);
        }
    }

    /**
     * Applies a dual-qubit gate (one control, one target) to the quantum state vector.
     * This method directly mutates the system state by applying the specified controlled gate.
     *
     * Internal functionality:
     * 1. Determines the gate type from the WorkItem.
     * 2. Creates a new state vector to store the result.
     * 3. Applies the appropriate controlled operation based on the gate type.
     * 4. Updates the system state with the new state vector.
     *
     * @param work The WorkItem specifying the gate operation.
     * @param numQubits The number of qubits in the system.
     * @see WorkItem
     * @see ComplexSparse
     * @see GateDirector#tracker
     */
    private void applyDualQubitGate(WorkItem work, int numQubits) {
        int controlQubit = work.getControl();
        int targetQubit = work.getTarget();
        int stateSize = 1 << numQubits;
        ComplexSparse newStateVector = new ComplexSparse(stateSize, 1);
        switch (work.getOperator()) {
            case "CX" -> {
                for (int i = 0; i < stateSize; i++) {
                    int controlBit = (i >> controlQubit) & 1;
                    if (controlBit == 1){
                        WorkItem applyX = new WorkItem("X",targetQubit);
                        ComplexSparse matrix = gateD.getGate(applyX);
                        ComplexSparse thisMatrix = ComplexMath.multiplyMatrix(matrix, gateD.tracker.getStateVec());
                        newStateVector.put(i, 0, thisMatrix.get(i,0));
                    } else {
                        newStateVector.put(i, 0, gateD.tracker.get(i,0));
                    }
                }
                gateD.tracker.setStateVec(newStateVector);
            }
            case "CZ" -> {
                for (int i = 0; i < stateSize; i++) {
                    int controlBit = (i >> controlQubit) & 1;
                    int targetBit = (i >> targetQubit) & 1;
                    if (controlBit == 1 && targetBit == 1) {
                        // Apply phase shift of -1
                        gateD.tracker.getStateVec().put(i, 0, new ComplexNumber((-1*gateD.tracker.getStateVec().get(i,0).getReal()), (-1*gateD.tracker.getStateVec().get(i,0).getImag())));
                    }
                }
            }
            case "CY" -> {
                for (int i = 0; i < stateSize; i++) {
                    newStateVector.put(i, 0, new ComplexNumber());
                }
                for (int i = 0; i < stateSize; i++) {
                    int controlBit = (i >> controlQubit) & 1;
                    int targetBit = (i >> targetQubit) & 1;
                    if (controlBit == 1) {
                        // Apply Y rotation to target qubit
                        int flippedState = i ^ (1 << targetQubit);
                        if (targetBit == 0) {
                            // |0> -> i|1>
                            newStateVector.put(flippedState, 0, ComplexMath.multiplyComplexNumbers(new ComplexNumber(0, 1), gateD.tracker.getStateVec().get(i, 0)));
                        } else {
                            // |1> -> -i|0>
                            newStateVector.put(flippedState, 0, ComplexMath.multiplyComplexNumbers(new ComplexNumber(0, -1), gateD.tracker.getStateVec().get(i, 0)));
                        }
                    } else {
                        // Control qubit is |0>, do nothing
                        newStateVector.put(i, 0, gateD.tracker.get(i, 0));
                    }
                }
                gateD.tracker.setStateVec(newStateVector);
            }
            case "CS" -> {
                for (int i = 0; i < stateSize; i++) {
                    newStateVector.put(i, 0, new ComplexNumber());
                }
                for(int i = 0; i < stateSize; i++){
                    int controlBit = (i >> controlQubit) & 1;
                    if (controlBit == 1){
                        WorkItem applyS = new WorkItem("S",targetQubit);
                        ComplexSparse matrix = gateD.getGate(applyS);
                        ComplexSparse thisMatrix = ComplexMath.multiplyMatrix(matrix, gateD.tracker.getStateVec());
                        newStateVector.put(i, 0, thisMatrix.get(i,0));
                    } else {
                        newStateVector.put(i, 0, gateD.tracker.get(i,0));
                    }
                }
                gateD.tracker.setStateVec(newStateVector);
            }
            case "CT" -> {
                for (int i = 0; i < stateSize; i++) {
                    newStateVector.put(i, 0, new ComplexNumber());
                }
                for(int i = 0; i < stateSize; i++){
                    int controlBit = (i >> controlQubit) & 1;
                    if (controlBit == 1){
                        WorkItem applyT = new WorkItem("T",targetQubit);
                        ComplexSparse matrix = gateD.getGate(applyT);
                        ComplexSparse thisMatrix = ComplexMath.multiplyMatrix(matrix, gateD.tracker.getStateVec());
                        newStateVector.put(i, 0, thisMatrix.get(i,0));
                    } else {
                        newStateVector.put(i, 0, gateD.tracker.get(i,0));
                    }
                }
                gateD.tracker.setStateVec(newStateVector);
            }
            case "CRZ" -> {
                for(int i = 0; i < stateSize; i++){
                    int controlBit = (i >> controlQubit) & 1;
                    if (controlBit == 1){
                        WorkItem applyRZ = new WorkItem("RZ",targetQubit, work.getTheta());
                        ComplexSparse matrix = gateD.getGate(applyRZ);
                        ComplexSparse thisMatrix = ComplexMath.multiplyMatrix(matrix, gateD.tracker.getStateVec());
                        newStateVector.put(i, 0, thisMatrix.get(i,0));
                    } else {
                        newStateVector.put(i, 0, gateD.tracker.get(i,0));
                    }
                }
                gateD.tracker.setStateVec(newStateVector);
            }
            case "CR1" -> {
                for(int i = 0; i < stateSize; i++){
                    int controlBit = (i >> controlQubit) & 1;
                    if (controlBit == 1){
                        WorkItem applyR1 = new WorkItem("R1",targetQubit, work.getTheta());
                        ComplexSparse matrix = gateD.getGate(applyR1);
                        ComplexSparse thisMatrix = ComplexMath.multiplyMatrix(matrix, gateD.tracker.getStateVec());
                        newStateVector.put(i, 0, thisMatrix.get(i,0));
                    } else {
                        newStateVector.put(i, 0, gateD.tracker.get(i,0));
                    }
                }
                gateD.tracker.setStateVec(newStateVector);
            }
            case "CR1i" -> {
                for(int i = 0; i < stateSize; i++){
                    int controlBit = (i >> controlQubit) & 1;
                    if (controlBit == 1){
                        WorkItem applyR1i = new WorkItem("R1i",targetQubit, work.getTheta());
                        ComplexSparse matrix = gateD.getGate(applyR1i);
                        ComplexSparse thisMatrix = ComplexMath.multiplyMatrix(matrix, gateD.tracker.getStateVec());
                        newStateVector.put(i, 0, thisMatrix.get(i,0));
                    } else {
                        newStateVector.put(i, 0, gateD.tracker.get(i,0));
                    }
                }
                gateD.tracker.setStateVec(newStateVector);
            }
            case "SWAP" -> {
                for (int i = 0; i < stateSize; i++){
                    int bit1 = (i >> controlQubit) & 1;
                    int bit2 = (i >> targetQubit) & 1;
                    if(bit1 != bit2){
                        int swappedIndex = i ^ (1 << controlQubit) ^ (1 << targetQubit);
                        newStateVector.put(i, 0, gateD.tracker.getStateVec().get(swappedIndex, 0));
                    } else {
                        newStateVector.put(i,0,gateD.tracker.getStateVec().get(i,0));
                    }
                }
                gateD.tracker.setStateVec(newStateVector);
            }
        }
    }

    /**
     * Applies a multi-qubit gate to the quantum state vector.
     * This method directly mutates the system state by applying the specified multi-qubit operation.
     *
     * Internal functionality:
     * 1. Determines the gate type from the operator string.
     * 2. Creates a new state vector to store the result.
     * 3. Applies the appropriate multi-qubit operation based on the gate type.
     * 4. Updates the system state with the new state vector.
     *
     * @param operator The string identifier for the multi-qubit gate operation.
     * @param controlQubits An array of indices for the control qubits.
     * @param targetQubits An array of indices for the target qubits.
     * @param numQubits The number of qubits in the system.
     * @see ComplexSparse
     * @see GateDirector#tracker
     */
    private void applyMultiQubitGate(String operator, Integer[] controlQubits, Integer[] targetQubits, int numQubits) {
        int stateSize = 1 << numQubits;
        ComplexSparse newStateVector = new ComplexSparse(gateD.tracker.getStateVecSize(), 1);
        switch(operator){
            case "TOFFOLI" -> {
                int targetQubit = targetQubits[0];
                for (int i = 0; i < stateSize; i++) {
                    int controlBitOne = (i >> controlQubits[0]) & 1;
                    int controlBitTwo = (i >> controlQubits[1]) & 1;

                    if (controlBitOne == 1 && controlBitTwo == 1) {
                        // Flip the target bit
                        int newState = i ^ (1 << targetQubit);
                        newStateVector.put(newState, 0, gateD.tracker.get(i, 0));
                    } else {
                        // Keep the state as is
                        newStateVector.put(i, 0, gateD.tracker.get(i, 0));
                    }
                }
                gateD.tracker.setStateVec(newStateVector);
            }
            case "CXX" -> {
                for (int i = 0; i < stateSize; i++){
                    int controlBit = (i >> controlQubits[0]) & 1;
                    int targetQubitOne = targetQubits[0];
                    int targetQubitTwo = targetQubits[1];

                    if (controlBit == 1){
                        //Flip the target bits
                        int newState = i ^ (1 << targetQubitOne) ^ (1 << targetQubitTwo);
                        newStateVector.put(newState, 0, gateD.tracker.get(i,0));
                    } else {
                        // Don't flip the target bits
                        newStateVector.put(i, 0, gateD.tracker.get(i,0));
                    }
                }
                gateD.tracker.setStateVec(newStateVector);
            }
            case "CSWAP" -> {
                int controlQubit = controlQubits[0];
                int targetQubitOne = targetQubits[0];
                int targetQubitTwo = targetQubits[1];
                for(int i =0; i < stateSize; i++){
                    int bit1 = (i >> controlQubit) & 1;
                    int bit2 = (i >> targetQubitOne) & 1;
                    int bit3 = (i >> targetQubitTwo) & 1;

                    if(bit1 == 1 && (bit2 != bit3)){
                        int swappedIndex = i ^ (1 << targetQubitOne) ^ (1 << targetQubitTwo);
                        newStateVector.put(i, 0, gateD.tracker.getStateVec().get(swappedIndex, 0));
                    } else {
                        newStateVector.put(i, 0, gateD.tracker.getStateVec().get(i,0));
                    }
                }
            }
        }
        gateD.tracker.setStateVec(newStateVector);
    }
}