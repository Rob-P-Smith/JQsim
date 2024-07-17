//TODO: Profile this class using
// https://bell-sw.com/announcements/2020/07/22/Hunting-down-code-hotspots-with-JDK-Flight-Recorder/
// and try to reduce memory and cpu usage.
package measurement;

import complex_classes.ComplexMath;
import complex_classes.ComplexMatrix;
import complex_classes.ComplexNumber;
import state.WorkItem;

/**
 * The GateBuilder class is responsible for constructing and applying quantum gates
 * to a quantum state vector.
 *
 * @author Robert Smith
 * @version 0.1
 * @since 5 July 2024
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
     * @param singleOperator The single-qubit operator matrix to be applied to target qubits.
     */
    void dualAndMultiGateSetup(WorkItem work, ComplexMatrix singleOperator) {
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
            applyDualQubitGate(work.getOperator(), controlQubit, targetQubit, numQubits);
        }
    }

    /**
     * Directly mutates the system state after applying the control gate in a single control single target gate.
     *
     * @param controlQubit the qubit to set as control in control gate operation
     * @param targetQubit  the qubit to use as the target based on the control qubit value
     * @param numQubits    the number of qubits, not the state vector size
     */
    private void applyDualQubitGate(String operator, int controlQubit, int targetQubit, int numQubits) {
        switch (operator) {
            case "CX" -> {
                int stateSize = 1 << numQubits;
                ComplexMatrix newStateVector = new ComplexMatrix(gateD.tracker.getStateVecSize(), 1);
                for (int i = 0; i < stateSize; i++) {
                    int controlBit = (i >> controlQubit) & 1;

                    if (controlBit == 1) {
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
            case "CZ" -> {
                int stateSize = 1 << numQubits;
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
                int stateSize = 1 << numQubits;
                ComplexMatrix newStateVector = new ComplexMatrix(stateSize, 1);
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
                gateD.tracker.getStateVec().setData(newStateVector.getData());
            }
        }
    }

    private void applyMultiQubitGate(String operator, Integer[] controlQubits, Integer[] targetQubits, int numQubits) {
        int stateSize = 1 << numQubits;
        ComplexMatrix newStateVector = new ComplexMatrix(gateD.tracker.getStateVecSize(), 1);

//        for (int i = 0; i < stateSize; i++) {
//            int controlBit = (i >> controlQubits) & 1;
//
//            if (controlBit == 1) {
//                // Flip the target bit
//                int newState = i ^ (1 << targetQubits);
//                newStateVector.set(newState, 0, gateD.tracker.get(i, 0));
//            } else {
//                // Keep the state as is
//                newStateVector.set(i, 0, gateD.tracker.get(i, 0));
//            }
//        }
        gateD.tracker.setStateVec(newStateVector);
    }
    //TODO: Remove all this if it turns out everything can be down without it.
//
//    private void directControlGate(int controlQubit, int targetQubit, int numQubits, Integer[] targets,
//                                   ComplexMatrix[] operatorSequence, ComplexMatrix singleOperator) {
//
//        // Identify the value of the control and target qubits as 0, 1, or neither
//        boolean controlNotZeroOrOne = false;        //used for probabilistic amplitudes
//        boolean targetNotZeroOrOne = false;         //used for probabilistic amplitudes
//        boolean controlIsOne = false;               //use for definite flip the target condition
//        boolean targetIsOne = false;                //use for definite flip the target condition
//        boolean controlIsZero = false;              //if control is zero, don't do anything besides identity gate
//        boolean targetIsZero = false;               //if control is zero, don't do anything besides identity gate
//        int controlBetaIndex = (1 << controlQubit); //The beta index in the state vector of the control qubit
//        int targetBetaIndex = (1 << targetQubit);   //The beta index in the state vector of the target qubit
//
//        //determine the type of state vector to detect superpositions in the control or target values
//        for (int i = 0; i < gateD.tracker.getStateVecSize(); i++) {
//            if ((i & controlBetaIndex) != 0) {
//                ComplexNumber controlValue = gateD.tracker.getStateVec().get(i, 0);
//                double controlReal = controlValue.getReal();        // for debugging purposes
//                double controlImag = controlValue.getImag();        // for debugging purposes
//                if (Math.abs(controlReal) == 1.0 || Math.abs(controlImag) == 1.0) {             //absolute value of 1 to account for out of phase conditions where it is -1.0 real or imaginary
//                    controlIsOne = true;
//                } else if (controlReal == 0.0 && controlImag == 0.0) {
//                    controlIsZero = true;
//                } else {
//                    controlNotZeroOrOne = true;
//                }
//            }
//            if ((i & targetBetaIndex) != 0) {
//                ComplexNumber targetValue = gateD.tracker.getStateVec().get(i, 0);
//                double targetReal = targetValue.getReal();        // for debugging purposes
//                double targetImag = targetValue.getImag();        // for debugging purposes
//                if (Math.abs(targetReal) == 1.0 || Math.abs(targetImag) == 1.0) {             //absolute value of 1 to account for out of phase conditions where it is -1.0 real or imaginary
//                    targetIsOne = true;
//                } else if (targetReal == 0.0 && targetImag == 0.0) {
//                    targetIsZero = true;
//                } else {
//                    targetNotZeroOrOne = true;
//                }
//            }
//        }
//
//        //if the value is not 0 or 1 be sure to turn those flags off because it needs to be resolved in the probabilistic approach
//        if (controlNotZeroOrOne) {
//            controlIsOne = false;
//            controlIsZero = false;
//        }
//        if (targetNotZeroOrOne) {
//            targetIsOne = false;
//            targetIsZero = false;
//        }
//
//        for (Integer target : targets) {
//            targetQubit = target;
//            if (controlIsOne && (targetIsOne || targetIsZero)) {
//                operatorSequence[target] = singleOperator;
////                if (controlQubit < target) { //TODO Fix to account for flipped states, currently this just doesn't perform the operation if flipped found.
////                    operatorSequence[target] = PAULI_X.getMatrix();
////                }
//            } else if ((controlIsZero && (targetIsOne || targetIsZero)) || (controlIsZero && targetNotZeroOrOne)) {
//                operatorSequence[target] = IDENTITY.getMatrix();
//                //think on if the control is zero and target is not 0 or 1, do we just return identity? I think it should, for now at least...
//            } else if (controlIsOne && targetNotZeroOrOne) {
//                System.out.println("Control one target not 0 or 1");
//                operatorSequence[target] = singleOperator;
////                gateD.tracker.setStateVec(resolveProbTarget(controlQubit, targetQubit, numQubits, singleOperator));
//            } else if (controlNotZeroOrOne && (targetIsOne || targetIsZero)) {
//                System.out.println("Control not 0 or 1, target is 1 or 0");
//                gateD.tracker.setStateVec(resolveProbControl(controlQubit, targetQubit, numQubits, singleOperator, operatorSequence));
//            } else {
//                System.out.println("'ELSE' Control not 0 or 1 and  target not 0 or 1");
//                operatorSequence[target] = singleOperator;
////                gateD.tracker.setStateVec(resolveProbBoth(controlQubit, targetQubit, numQubits, singleOperator));
//            }
//            gateD.executeOperatorSequence(operatorSequence);
//        }
//    }
//
//    private ComplexMatrix resolveProbBoth(int controlQubit, int targetQubit, int numQubits, ComplexMatrix singleOperator) {
//        ComplexMatrix result = new ComplexMatrix((int) Math.pow(2, numQubits), 1);
//        return result;
//    }
//
//    private ComplexMatrix resolveProbControl(int controlQubit, int targetQubit, int numQubits,
//                                             ComplexMatrix singleOperator, ComplexMatrix[] operatorSequence) {
//        //Apply the control case
//        ComplexMatrix result = new ComplexMatrix((int) Math.pow(2, numQubits), 1);
//        operatorSequence[targetQubit] = singleOperator;
//        //Don't apply the control case
//        ComplexMatrix interimApplied = gateD.executeInterimStep(operatorSequence);
//        operatorSequence[targetQubit] = IDENTITY.getMatrix();
//        ComplexMatrix interimNotApplied = gateD.executeInterimStep(operatorSequence);
//
//        //get valid states
//        //to be valid applied, control beta has value, target beta has value
//        //to be valid not applied, control beta has no value, target beta has no value
////        int testValue = 0 << controlQubit;
//        int targetQubitStep = (int) Math.pow(2, targetQubit);
//        int controlQubitStep = (int) Math.pow(2, controlQubit);
//        System.out.println("Target Step: " + targetQubitStep + ", Control Step: "+controlQubitStep+"\n");
//
//        System.out.println("Checking index for applied: " + (targetQubitStep + controlQubitStep));
//        System.out.println("Turned on value should be at index "+
//                (targetQubitStep + controlQubitStep)+
//                " found: \n" +
//                interimApplied.get((targetQubitStep + controlQubitStep),
//                        0));
////        System.out.println("Checking index: " +
////                (targetQubitStep + targetQubitStep - controlQubitStep));
////        System.out.println("Turned on value 2 should be "+
////                (targetQubitStep + targetQubitStep-controlQubitStep)+
////                " found: \n" +
////                interimApplied.get((targetQubitStep + targetQubitStep - controlQubitStep),
////                        0));
////        System.out.println("Turned off value should at index " + (targetQubitStep-controlQubitStep) + " found: \n" + interimNotApplied.get((targetQubitStep - controlQubitStep), 0));
//
//        System.out.println();
//        System.out.println("Interim applied: " + ComplexMath.complexMatrixToDiracNotation(interimApplied));
//        System.out.println();
//
//        System.out.println("Applied State Vector: \n" + interimApplied);
//
//
//        System.out.println("Checking index for not applied: " + (controlQubitStep - controlQubitStep));
//        System.out.println("Turned off value should be at index "+
//                (controlQubitStep - controlQubitStep)+
//                " found: \n" +
//                interimNotApplied.get((controlQubitStep - controlQubitStep),
//                        0));
//
//        System.out.println("\nInterim not Applied: " + ComplexMath.complexMatrixToDiracNotation(interimNotApplied));
//
//        System.out.println("Not Applied State: \n" + interimNotApplied);
//
//        result.set(controlQubitStep-controlQubitStep, 0, interimNotApplied.get(controlQubitStep-controlQubitStep, 0));
//        result.set(targetQubitStep + controlQubitStep, 0, interimApplied.get(targetQubitStep + controlQubitStep, 0));
//        System.out.println("Resulting state vector is: \n" + result);
//
//        return result;
//    }
//
//    private ComplexMatrix resolveProbTarget(int controlQubit, int targetQubit, int numQubits, ComplexMatrix singleOperator) {
//        ComplexMatrix result = new ComplexMatrix((int) Math.pow(2, numQubits), 1);
//        return result;
//    }
//
//    private ComplexMatrix resolveControlBranch(int controlQubit, int targetQubit, int numQubits,
//                                               ComplexMatrix singleOperator) {
//        ComplexMatrix newSystemState = new ComplexMatrix(gateD.tracker.getStateVecSize(), 1);
//        ComplexMatrix systemStateDivergent = new ComplexMatrix(gateD.tracker.getStateVecSize(), 1);
//        ComplexMatrix[] operatorSequenceDivergent = new ComplexMatrix[numQubits];
//
//        double targetReal = gateD.tracker.getStateVec().get(targetQubit, 0).getReal();
//        double targetImag = gateD.tracker.getStateVec().get(targetQubit, 0).getImag();
//        if ((targetReal != 0.0 || targetReal != Math.abs(1.0)) ||
//                targetImag != 0.0 || targetImag != Math.abs(1.0)) {
//            return gateD.tracker.getStateVec();
//        }
//
//
//        //Copy the current system state vector into the divergent state vector for processing
//        for (int i = 0; i < ((int) Math.pow(2, numQubits)); i++) {
//            systemStateDivergent.set(i, 0, new ComplexNumber(gateD.tracker.getStateVec().get(i, 0).getReal(), gateD.tracker.getStateVec().get(i, 0).getImag()));
//        }
//        //new jqs to run the divergent state vector through the Control ON possibilities
//        jqs jqs2 = new jqs(numQubits);
//        //set jqs current state vector to the divergent state vector
//        jqs2.getStateVec().setData(systemStateDivergent.getData());
//        //build the operator sequence using the single operator on the target qubit
//        for (int j = 0; j < numQubits; j++) {
//            if (j == targetQubit) {
//                operatorSequenceDivergent[targetQubit] = singleOperator;
//            } else {
//                operatorSequenceDivergent[j] = IDENTITY.getMatrix();
//            }
//        }
//        //build the divergent counterpart to finalGate
//        ComplexMatrix divergentOperatorGate = new ComplexMatrix(gateD.tracker.getStateVecSize(), gateD.tracker.getStateVecSize());
//        //run the divergent system state vector through the execution pipeline to get the final divergent operator matrix
//        for (int k = operatorSequenceDivergent.length - 1; k >= 0; k--) {
//            if (k == operatorSequenceDivergent.length - 1) {
//                divergentOperatorGate = ComplexMath.tensorMultiply(operatorSequenceDivergent[k], operatorSequenceDivergent[k - 1]);
//                k--;
//            } else {
//                divergentOperatorGate = ComplexMath.tensorMultiply(divergentOperatorGate, operatorSequenceDivergent[k]);
//            }
//        }
//        // ensures no accidental mutation across a reference
//        ComplexMatrix result = new ComplexMatrix((int) Math.pow(2, numQubits), 1);
//        // set result equal to the divergent operator multiplied against the divergent system state vector
//        result = ComplexMath.multiplyMatrix(divergentOperatorGate, systemStateDivergent);
//
//
//        ////////////////////////////////////////////////////////////////////////////////
//        //checking states, this should turn into state vector direct checks in the end//
//        ////////////////////////////////////////////////////////////////////////////////
//
//        // Here I need something to check the states as valid or not
//        StringBuilder binaryStringOFF = new StringBuilder();
//        StringBuilder binaryStringON = new StringBuilder();
//        StringBuilder currentStateValues = new StringBuilder();
//
//        for (int i = 0; i < systemStateDivergent.getHeight(); i++) {
//            ComplexNumber amplitude = systemStateDivergent.get(i, 0);
//            if (amplitude.magnitudeSquared() > 1e-10) {  // Threshold for considering non-zero amplitudes
//                binaryStringOFF.append(String.format("%" + numQubits + "s", Integer.toBinaryString(i)).replace(' ', '0')).append(" ");
//            }
//            ComplexNumber amplitudeTwo = result.get(i, 0);
//            if (amplitudeTwo.magnitudeSquared() > 1e-10) {  // Threshold for considering non-zero amplitudes
//                binaryStringON.append(String.format("%" + numQubits + "s", Integer.toBinaryString(i)).replace(' ', '0')).append(" ");
//            }
//            ComplexNumber amplitudeThree = gateD.tracker.getStateVec().get(i, 0);
//            if (amplitudeThree.magnitudeSquared() > 1e-10) {
//                currentStateValues.append(String.format("%" + numQubits + "s", Integer.toBinaryString(i)).replace(' ', '0')).append(" ");
//            }
//        }
//        String[] binaryStatesOFF = binaryStringOFF.toString().split(" ");
//        String[] binaryStatesON = binaryStringON.toString().split(" ");
//        String[] currentValidStates = currentStateValues.toString().split(" ");
//        String[] binaryStatesPossible = new String[binaryStatesOFF.length + binaryStatesON.length];
//        int combinedIndex = 0;
//        for (String value : binaryStatesOFF) {
//            binaryStatesPossible[combinedIndex] = value;
//            combinedIndex++;
//        }
//        for (String value : binaryStatesON) {
//            binaryStatesPossible[combinedIndex] = value;
//            combinedIndex++;
//        }
//
//        System.out.println("Current state: " + Arrays.deepToString(currentValidStates));
//        System.out.println("All states Possible: " + Arrays.deepToString(binaryStatesPossible));
//        System.out.println();
//
//        char targetIndexStartValue = '0';
//        for (String value : currentValidStates) {
//            if (value.charAt(controlQubit) <= 1e-10) {
//                targetIndexStartValue = value.charAt(targetQubit);
//            }
//        }
//
//        Set<String> validatedPossibleStates = new HashSet<>();
//        int validatedIndex = 0;
//        for (String value : binaryStatesPossible) {
//            if (value.charAt(value.length() - 1 - controlQubit) == '0') {
//                if (value.charAt(value.length() - 1 - targetQubit) == targetIndexStartValue) {
//                    validatedPossibleStates.add(value);
//                    validatedIndex++;
//                }
//            } else if (value.charAt(value.length() - 1 - controlQubit) == '1') {
//                if (value.charAt(value.length() - 1 - targetQubit) != targetIndexStartValue) {
//                    validatedPossibleStates.add(value);
//                    validatedIndex++;
//                }
//            }
//        }
//        System.out.println("Validated Possibilities: " + validatedPossibleStates);
//        newSystemState.setData(result.getData());
//        return newSystemState;
//    }
//
//    /**
//     * Builds the operator matrix for a gate with multiple control qubits and a single target qubit.
//     *
//     * @param work The WorkItem containing the gate information.
//     */
//    void multiControlSingleTarget(WorkItem work) {
//        // Implementation to be added
//    }
//
//    /**
//     * Builds the operator matrix for a gate with multiple control qubits and multiple target qubits.
//     *
//     * @param work The WorkItem containing the gate information.
//     */
//    void multiControlMultiTarget(WorkItem work) {
//        // Implementation to be added
//    }
}