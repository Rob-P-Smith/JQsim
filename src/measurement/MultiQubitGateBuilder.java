//TODO: Profile this class using
// https://bell-sw.com/announcements/2020/07/22/Hunting-down-code-hotspots-with-JDK-Flight-Recorder/
// and try to reduce memory and cpu usage.
package measurement;

import complex_classes.ComplexMath;
import complex_classes.ComplexMatrix;
import complex_classes.ComplexNumber;
import interpreter.jqs;
import state.WorkItem;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static complex_classes.ComplexGateEnums.IDENTITY;

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
    void oneControlOneOrManyTargetsSetup(WorkItem work, ComplexMatrix singleOperator) {
        int numQubits = (int) (Math.log(gateD.tracker.getStateVecSize()) / Math.log(2));
        int controlQubit = work.getControls()[0];
        int targetQubit = work.getTargets()[0]; // TODO Once single control single target is 100%, remove this and use targets instead.
        boolean flipped = false;
        Integer[] targets = work.getTargets();
        ComplexMatrix[] operatorSequence = new ComplexMatrix[numQubits];

        // Initialize all qubits with identity operators
        for (int i = 0; i < numQubits; i++) {
            operatorSequence[i] = IDENTITY.getMatrix();
        }

        // Check if the control and target qubit ids are flipped, if so set flipped = true to handle later
        // in the execution of gates to ensure the proper form of control operator is applied
        for (Integer target : targets) {
            if (target < controlQubit) {
                System.out.println("Flipped state identified.");
                flipped = true;
            }
        }

        directControlGate(controlQubit, targetQubit, numQubits, targets, operatorSequence, singleOperator);
    }

    private void directControlGate(int controlQubit, int targetQubit, int numQubits, Integer[] targets,
                                   ComplexMatrix[] operatorSequence, ComplexMatrix singleOperator) {

        // Identify the value of the control and target qubits as 0, 1, or neither
        boolean controlNotZeroOrOne = false;        //used for probabilistic amplitudes
        boolean targetNotZeroOrOne = false;         //used for probabilistic amplitudes
        boolean controlIsOne = false;               //use for definite flip the target condition
        boolean targetIsOne = false;                //use for definite flip the target condition
        boolean controlIsZero = false;              //if control is zero, don't do anything besides identity gate
        boolean targetIsZero = false;               //if control is zero, don't do anything besides identity gate
        int controlBetaIndex = (1 << controlQubit); //The beta index in the state vector of the control qubit
        int targetBetaIndex = (1 << targetQubit);   //The beta index in the state vector of the target qubit

        //determine the type of state vector to detect superpositions in the control or target values
        for (int i = 0; i < gateD.tracker.getStateVecSize(); i++) {
            if ((i & controlBetaIndex) != 0) {
                ComplexNumber controlValue = gateD.tracker.getStateVec().get(i, 0);
                double controlReal = controlValue.getReal();        // for debugging purposes
                double controlImag = controlValue.getImag();        // for debugging purposes
                if (Math.abs(controlReal) == 1.0 || Math.abs(controlImag) == 1.0) {             //absolute value of 1 to account for out of phase conditions where it is -1.0 real or imaginary
                    controlIsOne = true;
                    break;
                } else if (controlReal == 0.0 && controlImag == 0.0) {
                    controlIsZero = true;
                } else {
                    controlNotZeroOrOne = true;
                }
            }
            if ((i & targetBetaIndex) != 0) {
                ComplexNumber targetValue = gateD.tracker.getStateVec().get(i, 0);
                double targetReal = targetValue.getReal();        // for debugging purposes
                double targetImag = targetValue.getImag();        // for debugging purposes
                if (Math.abs(targetReal) == 1.0 || Math.abs(targetImag) == 1.0) {             //absolute value of 1 to account for out of phase conditions where it is -1.0 real or imaginary
                    targetIsOne = true;
                    break;
                } else if (targetReal == 0.0 && targetImag == 0.0) {
                    targetIsZero = true;
                } else {
                    targetNotZeroOrOne = true;
                }
            }
        }

        //if the value is not 0 or 1 be sure to turn those flags off because it needs to be resolved in the probabilistic approach
        if (controlNotZeroOrOne) {
            controlIsOne = false;
            controlIsZero = false;
        }
        if (targetNotZeroOrOne) {
            targetIsOne = false;
            targetIsZero = false;
        }

        for (Integer target : targets) {
            if (controlIsOne && (targetIsOne || targetIsZero)) {
                operatorSequence[target] = singleOperator;
//                if (controlQubit < target) { //TODO Fix to account for flipped states, currently this just doesn't perform the operation if flipped found.
//                    operatorSequence[target] = PAULI_X.getMatrix();
//                }
            } else if ((controlIsZero && (targetIsOne || targetIsZero))||(controlIsZero && targetNotZeroOrOne)) {
                operatorSequence[target] = IDENTITY.getMatrix();
                //think on if the control is zero and target is not 0 or 1, do we just return identity? I think it should, for now at least...
            } else if (controlIsOne && targetNotZeroOrOne) {
                System.out.println("Control one target not 0 or 1");
                gateD.tracker.setStateVec(resolveProbTarget(controlQubit, targetQubit, numQubits, singleOperator));
            } else if (controlNotZeroOrOne && (targetIsOne || targetIsZero)) {
                System.out.println("Control not 0 or 1, target is 1 or 0");
                gateD.tracker.setStateVec(resolveProbControl(controlQubit, targetQubit, numQubits, singleOperator));
            } else {
                System.out.println("Control not 0 or 1 and  target not 0 or 1");
                gateD.tracker.setStateVec(resolveProbBoth(controlQubit, targetQubit, numQubits, singleOperator));
            }
            gateD.executeOperatorSequence(operatorSequence);
        }
    }

    private ComplexMatrix resolveProbBoth(int controlQubit, int targetQubit, int numQubits, ComplexMatrix singleOperator) {
        ComplexMatrix result = new ComplexMatrix((int) Math.pow(2, numQubits), 1);
        return result;
    }

    private ComplexMatrix resolveProbControl(int controlQubit, int targetQubit, int numQubits, ComplexMatrix singleOperator) {
        ComplexMatrix result = new ComplexMatrix((int) Math.pow(2, numQubits), 1);
        return result;
    }

    private ComplexMatrix resolveProbTarget(int controlQubit, int targetQubit, int numQubits, ComplexMatrix singleOperator) {
        ComplexMatrix result = new ComplexMatrix((int) Math.pow(2, numQubits), 1);
        return result;
    }

    private ComplexMatrix resolveControlBranch(int controlQubit, int targetQubit, int numQubits,
                                               ComplexMatrix singleOperator) {
        ComplexMatrix newSystemState = new ComplexMatrix(gateD.tracker.getStateVecSize(), 1);
        ComplexMatrix systemStateDivergent = new ComplexMatrix(gateD.tracker.getStateVecSize(), 1);
        ComplexMatrix[] operatorSequenceDivergent = new ComplexMatrix[numQubits];

        double targetReal = gateD.tracker.getStateVec().get(targetQubit, 0).getReal();
        double targetImag = gateD.tracker.getStateVec().get(targetQubit, 0).getImag();
        if ((targetReal != 0.0 || targetReal != Math.abs(1.0)) ||
                targetImag != 0.0 || targetImag != Math.abs(1.0)) {
            return gateD.tracker.getStateVec();
        }
        //Get the possible initial states where the control is set to 0, 1, -1, 1i, -1i values accordingly for testing
//        ComplexMatrix[] possibleBasisStates = getViableStates(numQubits);
//        System.out.println("Initial System state: \n" + tracker.getStateVec());
//        System.out.println("Resolving States: " + ComplexMath.complexMatrixToDiracNotation(tracker.getStateVec()));
//        for (ComplexMatrix possibility : possibleBasisStates) {
//            if (possibility != null) {
//                System.out.println(possibility);
//            }
//        }

//        int controlIndex = (int) Math.pow(2, controlQubit);
//        int targetIndex = (int) Math.pow(2, targetQubit);
//        System.out.println("Control index: " + controlIndex);
//        System.out.println("Target index: " + targetIndex);
//        for (int i = 0; i < tracker.getStateVecSize(); i++) {
//            ComplexNumber onTest = tracker.getStateVec().get(i, 0);
////            System.out.println("\nState Vector Entry Index: " + i);
//
//        }

        // copy the actual state vector into a complex matrix and use new objects to avoid passing a reference instead of a value
//        ComplexMatrix[] interimState = new ComplexMatrix[tracker.getStateVecSize()];
//        int interimIndex = 0;
//        for (ComplexMatrix possibility : possibleBasisStates) {
//            if (possibility != null) {
//                for (int i = 0; i < ((int) Math.pow(2, numQubits)); i++) {
//                    systemStateDivergent.setData(possibility.getData());
//
//                    jqs jqs2 = new jqs(numQubits);
//                    jqs2.getStateVec().setData(systemStateDivergent.getData());
//
//                    for (int j = 0; j < numQubits; j++) {
//                        if (j == targetQubit) {
//                            operatorSequenceDivergent[targetQubit] = singleOperator;
//                        } else {
//                            operatorSequenceDivergent[j] = IDENTITY.getMatrix();
//                        }
//                    }
//
//                    interimState[interimIndex] = new ComplexMatrix(tracker.getStateVecSize(), tracker.getStateVecSize());
//                    for (int k = operatorSequenceDivergent.length - 1; k >= 0; k--) {
//                        if (k == operatorSequenceDivergent.length - 1) {
//                            interimState[interimIndex] = ComplexMath.tensorMultiply(operatorSequenceDivergent[k], operatorSequenceDivergent[k - 1]);
//                            k--;
//                        } else {
//                            interimState[interimIndex] = ComplexMath.tensorMultiply(interimState[interimIndex], operatorSequenceDivergent[k]);
//                        }
//                    }
//                }
//                interimIndex++;
//            }
//        }

        //Copy the current system state vector into the divergent state vector for processing
        for (int i = 0; i < ((int) Math.pow(2, numQubits)); i++) {
            systemStateDivergent.set(i, 0, new ComplexNumber(gateD.tracker.getStateVec().get(i, 0).getReal(), gateD.tracker.getStateVec().get(i, 0).getImag()));
        }
        //new jqs to run the divergent state vector through the Control ON possibilities
        jqs jqs2 = new jqs(numQubits);
        //set jqs current state vector to the divergent state vector
        jqs2.getStateVec().setData(systemStateDivergent.getData());
        //build the operator sequence using the single operator on the target qubit
        for (int j = 0; j < numQubits; j++) {
            if (j == targetQubit) {
                operatorSequenceDivergent[targetQubit] = singleOperator;
            } else {
                operatorSequenceDivergent[j] = IDENTITY.getMatrix();
            }
        }
        //build the divergent counterpart to finalGate
        ComplexMatrix divergentOperatorGate = new ComplexMatrix(gateD.tracker.getStateVecSize(), gateD.tracker.getStateVecSize());
        //run the divergent system state vector through the execution pipeline to get the final divergent operator matrix
        for (int k = operatorSequenceDivergent.length - 1; k >= 0; k--) {
            if (k == operatorSequenceDivergent.length - 1) {
                divergentOperatorGate = ComplexMath.tensorMultiply(operatorSequenceDivergent[k], operatorSequenceDivergent[k - 1]);
                k--;
            } else {
                divergentOperatorGate = ComplexMath.tensorMultiply(divergentOperatorGate, operatorSequenceDivergent[k]);
            }
        }
        // ensures no accidental mutation across a reference
        ComplexMatrix result = new ComplexMatrix((int) Math.pow(2, numQubits), 1);
        // set result equal to the divergent operator multiplied against the divergent system state vector
        result = ComplexMath.multiplyMatrix(divergentOperatorGate, systemStateDivergent);


        ////////////////////////////////////////////////////////////////////////////////
        //checking states, this should turn into state vector direct checks in the end//
        ////////////////////////////////////////////////////////////////////////////////

        // Here I need something to check the states as valid or not
        StringBuilder binaryStringOFF = new StringBuilder();
        StringBuilder binaryStringON = new StringBuilder();
        StringBuilder currentStateValues = new StringBuilder();

        for (int i = 0; i < systemStateDivergent.getHeight(); i++) {
            ComplexNumber amplitude = systemStateDivergent.get(i, 0);
            if (amplitude.magnitudeSquared() > 1e-10) {  // Threshold for considering non-zero amplitudes
                binaryStringOFF.append(String.format("%" + numQubits + "s", Integer.toBinaryString(i)).replace(' ', '0')).append(" ");
            }
            ComplexNumber amplitudeTwo = result.get(i, 0);
            if (amplitudeTwo.magnitudeSquared() > 1e-10) {  // Threshold for considering non-zero amplitudes
                binaryStringON.append(String.format("%" + numQubits + "s", Integer.toBinaryString(i)).replace(' ', '0')).append(" ");
            }
            ComplexNumber amplitudeThree = gateD.tracker.getStateVec().get(i, 0);
            if (amplitudeThree.magnitudeSquared() > 1e-10) {
                currentStateValues.append(String.format("%" + numQubits + "s", Integer.toBinaryString(i)).replace(' ', '0')).append(" ");
            }
        }
        String[] binaryStatesOFF = binaryStringOFF.toString().split(" ");
        String[] binaryStatesON = binaryStringON.toString().split(" ");
        String[] currentValidStates = currentStateValues.toString().split(" ");
        String[] binaryStatesPossible = new String[binaryStatesOFF.length + binaryStatesON.length];
        int combinedIndex = 0;
        for (String value : binaryStatesOFF) {
            binaryStatesPossible[combinedIndex] = value;
            combinedIndex++;
        }
        for (String value : binaryStatesON) {
            binaryStatesPossible[combinedIndex] = value;
            combinedIndex++;
        }

        System.out.println("Current state: " + Arrays.deepToString(currentValidStates));
        System.out.println("All states Possible: " + Arrays.deepToString(binaryStatesPossible));
        System.out.println();

        char targetIndexStartValue = '0';
        for (String value : currentValidStates) {
            if (value.charAt(controlQubit) <= 1e-10) {
                targetIndexStartValue = value.charAt(targetQubit);
            }
        }

        Set<String> validatedPossibleStates = new HashSet<>();
        int validatedIndex = 0;
        for (String value : binaryStatesPossible) {
            if (value.charAt(value.length() - 1 - controlQubit) == '0') {
                if (value.charAt(value.length() - 1 - targetQubit) == targetIndexStartValue) {
                    validatedPossibleStates.add(value);
                    validatedIndex++;
                }
            } else if (value.charAt(value.length() - 1 - controlQubit) == '1') {
                if (value.charAt(value.length() - 1 - targetQubit) != targetIndexStartValue) {
                    validatedPossibleStates.add(value);
                    validatedIndex++;
                }
            }
        }
        System.out.println("Validated Possibilities: " + validatedPossibleStates);
        newSystemState.setData(result.getData());
        return newSystemState;
    }

    /**
     * Builds the operator matrix for a gate with multiple control qubits and a single target qubit.
     *
     * @param work The WorkItem containing the gate information.
     */
    void multiControlSingleTarget(WorkItem work) {
        // Implementation to be added
    }

    /**
     * Builds the operator matrix for a gate with multiple control qubits and multiple target qubits.
     *
     * @param work The WorkItem containing the gate information.
     */
    void multiControlMultiTarget(WorkItem work) {
        // Implementation to be added
    }

//    public void printIt(ComplexMatrix result) {
//        System.out.println("Initial System state: \n" + gateD.tracker.getStateVec());
//        System.out.println("Original Dirac state: \n" + ComplexMath.complexMatrixToDiracNotation(gateD.tracker.getStateVec()) + "\n");
//        System.out.println("Resulting Dirac: \n" + ComplexMath.complexMatrixToDiracNotation(result) + "\n");
//        System.out.println("Resulting interim state vector: \n" + result);
//
//
//        System.out.println("NOW MERGE THEM!");
//    }

//    //TODO broken AF, remove or rewrite
//    public ComplexMatrix[] getNonViableStates(int numQubits) {
//        int numRows = gateD.tracker.getStateVec().getHeight();
//
//        ComplexMatrix[] possibleStates = new ComplexMatrix[numRows];
//
//        //The magic loop!
//        // This needs to build state vectors that represent both cases of the control qubit, 0 and 1.
//        // Beta probability is found by loop at 'row' when not 0.0 and Alpha is found by 'row-qubitStep'.
//        // Doesnt work, is shit.
//        for (int row = 0; row < numRows; row++) {
////            System.out.println("\nRow"+row);  //shows which row is currently being checked
//            for (int qubit = 0; qubit < numQubits; qubit++) {
//                int qubitStep = (int) Math.pow(2, qubit);
//                if ((row & qubitStep) != 0 || row == 0) {
//                    double realValue = gateD.tracker.getStateVec().get(row, 0).getReal();
//                    double imagValue = gateD.tracker.getStateVec().get(row, 0).getImag();
////                    System.out.println("Qubits: "+qubit); // shows which qubits are checked in a given row for their beta value
//                    if (realValue < 0.0 || imagValue < 0.0) {
//                        if ((gateD.tracker.getStateVec().get(row, 0).getReal()) != 0.0) {
//                            possibleStates[row] = new ComplexMatrix(numRows, 1);
//                            possibleStates[row].set(row, 0, new ComplexNumber(-1));
//
//                        }
//                        if (gateD.tracker.getStateVec().get(row, 0).getImag() != 0.0) {
//                            possibleStates[row] = new ComplexMatrix(numRows, 1);
//                            possibleStates[row].set(row, 0, new ComplexNumber(0, -1));
//                        }
//                    }
//                    if (realValue > 0.0 || imagValue > 0.0) {
//                        if ((gateD.tracker.getStateVec().get(row, 0).getReal()) != 0.0) {
//                            possibleStates[row] = new ComplexMatrix(numRows, 1);
//                            possibleStates[row].set(row, 0, new ComplexNumber(1));
//
//                        }
//                        if (gateD.tracker.getStateVec().get(row, 0).getImag() != 0.0) {
//                            possibleStates[row] = new ComplexMatrix(numRows, 1);
//                            possibleStates[row].set(row, 0, new ComplexNumber(0, 1));
//                        }
//                    }
//                }
//            }
//        }
//        return possibleStates;
//    }
}