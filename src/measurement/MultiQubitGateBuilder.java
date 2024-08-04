//TODO: Profile this class using
// https://bell-sw.com/announcements/2020/07/22/Hunting-down-code-hotspots-with-JDK-Flight-Recorder/
// and try to reduce memory and cpu usage.
package measurement;

import complex_classes.ComplexMath;
import complex_classes.ComplexMatrix;
import complex_classes.ComplexNumber;
import complex_classes.ComplexSparse;
import state.WorkItem;

/**
 * The GateBuilder class is responsible for constructing and applying quantum gates
 * to a quantum state vector.
 *
 * @author Robert Smith
 * @version 0.1
 * @since 16 July 2024
 */
public class MultiQubitGateBuilder {
    private GateDirector gateD;

    /**
     * Constructor for the MultiQubitGateBuilder class.
     *
     * @param gateD the active GateDirector
     */
    public MultiQubitGateBuilder(GateDirector gateD) {
        this.gateD = gateD;
    }

    /**
     * Returns a string representation of the final gate matrix.
     *
     * @return A string representation of the final gate.
     */
    @Override
    public String toString() {
        return gateD.finalGate.toString();
    }

    /**
     * Builds the operator matrix for a gate with one control qubit and one or many target qubits.
     * This method applies the single-qubit operator to each target qubit when the control qubit is not |0⟩.
     *
     * @param work           The WorkItem containing the gate information.
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
            if (controlQubit == targetQubit || controlQubit >= numQubits || targetQubit >= numQubits || controlQubit < 0 || targetQubit < 0) {
                throw new IllegalArgumentException("Invalid control or target qubit.");
            }
            applyDualQubitGate(work, numQubits);
        }
    }

    /**
     * Directly mutates the system state after applying the control gate in a single control single target gate.
     *
     * @param work  the WorkItem to evaluate and execute.
     * @param numQubits    the number of qubits, not the state vector size.
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
                        newStateVector.set(i, 0, thisMatrix.get(i,0));
                    } else {
                        newStateVector.set(i, 0, gateD.tracker.get(i,0));
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
                        gateD.tracker.getStateVec().get(i, 0).setReal(-1 * (gateD.tracker.getStateVec().get(i, 0).getReal()));
                        gateD.tracker.getStateVec().get(i, 0).setImag(-1 * (gateD.tracker.getStateVec().get(i, 0).getImag()));
                    }
                }
            }
            case "CY" -> {
                for (int i = 0; i < stateSize; i++) {
                    newStateVector.set(i, 0, new ComplexNumber());
                }
                for (int i = 0; i < stateSize; i++) {
                    int controlBit = (i >> controlQubit) & 1;
                    int targetBit = (i >> targetQubit) & 1;
                    if (controlBit == 1) {
                        // Apply Y rotation to target qubit
                        int flippedState = i ^ (1 << targetQubit);
                        if (targetBit == 0) {
                            // |0> -> i|1>
                            newStateVector.set(flippedState, 0, ComplexMath.multiplyComplexNumbers(new ComplexNumber(0, 1), gateD.tracker.getStateVec().get(i, 0)));
                        } else {
                            // |1> -> -i|0>
                            newStateVector.set(flippedState, 0, ComplexMath.multiplyComplexNumbers(new ComplexNumber(0, -1), gateD.tracker.getStateVec().get(i, 0)));
                        }
                    } else {
                        // Control qubit is |0>, do nothing
                        newStateVector.set(i, 0, gateD.tracker.get(i, 0));
                    }
                }
                gateD.tracker.setStateVec(newStateVector);
            }
            case "CS" -> {
                for (int i = 0; i < stateSize; i++) {
                    newStateVector.set(i, 0, new ComplexNumber());
                }
                for(int i = 0; i < stateSize; i++){
                    int controlBit = (i >> controlQubit) & 1;
                    if (controlBit == 1){
                        WorkItem applyS = new WorkItem("S",targetQubit);
                        ComplexSparse matrix = gateD.getGate(applyS);
                        ComplexSparse thisMatrix = ComplexMath.multiplyMatrix(matrix, gateD.tracker.getStateVec());
                        newStateVector.set(i, 0, thisMatrix.get(i,0));
                    } else {
                        newStateVector.set(i, 0, gateD.tracker.get(i,0));
                    }
                }
                gateD.tracker.setStateVec(newStateVector);
            }
            case "CT" -> {
                for (int i = 0; i < stateSize; i++) {
                    newStateVector.set(i, 0, new ComplexNumber());
                }
                for(int i = 0; i < stateSize; i++){
                    int controlBit = (i >> controlQubit) & 1;
                    if (controlBit == 1){
                        WorkItem applyT = new WorkItem("T",targetQubit);
                        ComplexSparse matrix = gateD.getGate(applyT);
                        ComplexSparse thisMatrix = ComplexMath.multiplyMatrix(matrix, gateD.tracker.getStateVec());
                        newStateVector.set(i, 0, thisMatrix.get(i,0));
                    } else {
                        newStateVector.set(i, 0, gateD.tracker.get(i,0));
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
                        newStateVector.set(i, 0, thisMatrix.get(i,0));
                    } else {
                        newStateVector.set(i, 0, gateD.tracker.get(i,0));
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
                        newStateVector.set(i, 0, thisMatrix.get(i,0));
                    } else {
                        newStateVector.set(i, 0, gateD.tracker.get(i,0));
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
                        newStateVector.set(i, 0, thisMatrix.get(i,0));
                    } else {
                        newStateVector.set(i, 0, gateD.tracker.get(i,0));
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
                        newStateVector.set(i, 0, gateD.tracker.getStateVec().get(swappedIndex, 0));
                    } else {
                        newStateVector.set(i,0,gateD.tracker.getStateVec().get(i,0));
                    }
                }
                gateD.tracker.setStateVec(newStateVector);
            }
        }
    }

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
                        newStateVector.set(newState, 0, gateD.tracker.get(i, 0));
                    } else {
                        // Keep the state as is
                        newStateVector.set(i, 0, gateD.tracker.get(i, 0));
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
                        newStateVector.set(newState, 0, gateD.tracker.get(i,0));
                    } else {
                        // Don't flip the target bits
                        newStateVector.set(i, 0, gateD.tracker.get(i,0));
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
                        newStateVector.set(i, 0, gateD.tracker.getStateVec().get(swappedIndex, 0));
                    } else {
                        newStateVector.set(i, 0, gateD.tracker.getStateVec().get(i,0));
                    }
                }
            }
        }
        gateD.tracker.setStateVec(newStateVector);
    }
}