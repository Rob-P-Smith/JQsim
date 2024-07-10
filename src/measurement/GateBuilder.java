//TODO: Profile this class using
// https://bell-sw.com/announcements/2020/07/22/Hunting-down-code-hotspots-with-JDK-Flight-Recorder/
// and try to reduce memory and cpu usage.
package measurement;

import complexClasses.ComplexMath;
import complexClasses.ComplexMatrix;
import complexClasses.ComplexNumber;
import state.StateTracker;
import state.WorkItem;

import java.util.Arrays;

import static complexClasses.ComplexGateEnums.*;

/**
 * The GateBuilder class is responsible for constructing and applying quantum gates
 * to a quantum state vector.
 *
 * @author Robert Smith
 * @version 0.1
 * @since 5 July 2024
 */
public class GateBuilder {
    private ComplexMatrix finalGate;
    private static final boolean DEBUG = false;
    private StateTracker tracker;

    /**
     * Constructs a new GateBuilder and initializes the finalGate matrix.
     */
    public GateBuilder(StateTracker tracker) {
        this.tracker = tracker;
        finalGate = new ComplexMatrix(tracker.getStateVec().getHeight(),
                tracker.getStateVec().getHeight());
    }

    /**
     * Returns the final gate matrix.
     *
     * @return The ComplexMatrix representing the final gate.
     */
    public ComplexMatrix getGate(WorkItem thisGate) {
        calculateGate(thisGate);
        return finalGate;
    }

    /**
     * Returns a string representation of the final gate matrix.
     *
     * @return A string representation of the final gate.
     */
    @Override
    public String toString() {
        return finalGate.toString();
    }

    /**
     * Decodes the operator from the WorkItem and returns the corresponding ComplexMatrix.
     *
     * @param work The WorkItem containing the operator information.
     * @return The ComplexMatrix representing the operator.
     */
    private static ComplexMatrix decodeOperator(WorkItem work) {
        ComplexMatrix singleOperator = null;
        switch (work.getOperator()) {
            case ("X"), ("CX"), ("TOFFOLI") -> singleOperator = PAULI_X.getMatrix();
            case ("Y"), ("CY") -> singleOperator = PAULI_Y.getMatrix();
            case ("Z"), ("CZ") -> singleOperator = PAULI_Z.getMatrix();
            case ("H"), ("CH") -> singleOperator = HADAMARD.getMatrix();
            case ("S") -> singleOperator = S_GATE.getMatrix();
            case ("Si") -> singleOperator = SI_GATE.getMatrix();
            case ("T") -> singleOperator = T_GATE.getMatrix();
            case ("Ti") -> singleOperator = TI_GATE.getMatrix();
            case ("ID") -> singleOperator = IDENTITY.getMatrix();
            case ("RX") -> singleOperator = RX_GATE.getMatrix();
            case ("RY") -> singleOperator = RY_GATE.getMatrix();
            case ("RZ") -> singleOperator = RZ_GATE.getMatrix();
            case ("SWAP"), ("CSWAP") -> singleOperator = SWAP.getMatrix();
            case ("ISWAP") -> singleOperator = ISWAP.getMatrix();
        }
        return singleOperator;
    }

    /**
     * Calculates and applies the quantum gate based on the next WorkItem in the queue.
     *
     * @param thisGate The WorkItem containing the gate operation to be applied.
     */
    private void calculateGate(WorkItem thisGate) {
        ComplexMatrix singleOperator = decodeOperator(thisGate);
        finalGate = new ComplexMatrix(tracker.getStateVec().getHeight(),
                tracker.getStateVec().getHeight());

        if (thisGate.isSingleQubit()) {
            buildSingleQubitOperator(thisGate, singleOperator);
        }

        if (!thisGate.isSingleQubit()) {
            int controlSize = thisGate.getControls().length;
            int targetsSize = thisGate.getTargets().length;

            if (controlSize == 1 && targetsSize >= 1) {
                oneControlOneOrManyTargets(thisGate, singleOperator);
            } else if (controlSize > 1 && targetsSize == 1) {
                System.out.println("Multi Control Single Target hit");
                multiControlSingleTarget(thisGate);
            } else if (controlSize > 1 && targetsSize > 1) {
                System.out.println("Multi Control Multi Target hit");
                multiControlMultiTarget(thisGate);
            }

            if (DEBUG) {
                System.out.println("Applying " + thisGate.getOperator() +
                        " to qubit " + thisGate.getTarget() + "\n" + finalGate);
            }
        }
    }

    /**
     * Builds the operator matrix for a single-qubit gate.
     *
     * @param work           The WorkItem containing the gate information.
     * @param singleOperator The single-qubit operator matrix.
     */
    private void buildSingleQubitOperator(WorkItem work, ComplexMatrix singleOperator) {
        if (DEBUG) System.out.println("buildSingleQubitOperator hit");
        int stateLength = tracker.getStateVecSize();
        int numQubits = (int) (Math.log(stateLength) / Math.log(2));
        ComplexMatrix[] operatorSequence = new ComplexMatrix[numQubits];
        for (int i = 0; i < numQubits; i++) {
            if (i != work.getTarget()) {
                operatorSequence[i] = IDENTITY.getMatrix();
            } else {
                operatorSequence[i] = singleOperator;
            }
        }

        if (numQubits == 1) {
            finalGate = singleOperator;
        } else {
            executeOperatorSequence(operatorSequence);
        }
        if (DEBUG) {
            System.out.println("Applying " + work.getOperator() +
                    " to qubit " + work.getTarget() + "\n" + finalGate);
        }
    }

    /**
     * Builds the operator matrix for a gate with one control qubit and one or many target qubits.
     * This method applies the single-qubit operator to each target qubit when the control qubit is not |0⟩.
     *
     * @param work           The WorkItem containing the gate information.
     * @param singleOperator The single-qubit operator matrix to be applied to target qubits.
     */
    private void oneControlOneOrManyTargets(WorkItem work, ComplexMatrix singleOperator) {
        ////////////////////
        // Initialization //
        ////////////////////

        if (DEBUG) System.out.println("oneControlOneOrManyTargets hit");
        int numQubits = (int) (Math.log(tracker.getStateVecSize()) / Math.log(2));
        int controlQubit = work.getControls()[0];
        boolean flipped = false;
        Integer[] targets = work.getTargets();
        ComplexMatrix[] operatorSequence = new ComplexMatrix[numQubits];
        // Initialize all qubits with identity operators
        for (int i = 0; i < numQubits; i++) {
            operatorSequence[i] = IDENTITY.getMatrix();
        }
        for (Integer target : targets) {
            if (target < controlQubit) {
                System.out.println("Flipped state identified.");
                flipped = true;
            }
        }

        ////////////////////
        //  Construction  //
        ////////////////////

        // Identify the value of the control qubit
        boolean controlNotZeroOrOne = false;
        boolean controlIsOne = false;
        boolean controlIsZero = false;
        int controlIndex = (1 << controlQubit);

        for (int i = 0; i < tracker.getStateVecSize(); i++) {
            if ((i & controlIndex) != 0) {
                ComplexNumber controlValue = tracker.getStateVec().get(i, 0);
                double controlReal = controlValue.getReal();
                double controlImag = controlValue.getImag();
                if (controlReal == 1.0 || controlImag == 1.0) {
                    controlIsOne = true;
                    break;
                } else if (controlReal == 0.0 && controlImag == 0.0) {
                    controlIsZero = true;
                } else {
                    controlNotZeroOrOne = true;
                }
            }
        }

        // implement basis states for the system instead of using matrices to transfer the probabilistic values instead of a matrix which requires a collapse of the superposition but only when
        // required for the correct outcome of the simulation

        //when building the gate:


        for (Integer target : targets) {
            if (controlIsOne) {
                operatorSequence[target] = PAULI_X.getMatrix();
//                if (controlQubit < target) { //TODO Fix to account for flipped states, currently this just doesn't perform the operation if flipped found.
//                    operatorSequence[target] = PAULI_X.getMatrix();
//                }
            } else if (controlIsZero) {
                operatorSequence[target] = IDENTITY.getMatrix();
            } else if (controlNotZeroOrOne) {

                //identify valid basis states for each state vector value that is not real/imag == 0.0
                //create a complexmatrix state vector temp object with collapsed states
                //run operation on each created temp state vector
                //collect the possible outcomes
                //construct new state based on possible outcomes that represents their composite
                //replace tracker.stateVector with constructed statevector

                for(int i = 0; i < tracker.getStateVecSize(); i++) {
                    if(tracker.getStateVec().get(i,0).getReal() !=0.0 ||
                       tracker.getStateVec().get(i,0).getImag() !=0.0) {

                    }
                }

                ComplexMatrix[] validBasisStates = new ComplexMatrix[2];
                ComplexMatrix validStates = new ComplexMatrix((int) Math.pow(2, numQubits), 1);
                ComplexMatrix controlAsZero = new ComplexMatrix((int) Math.pow(2, numQubits), 1);
                ComplexMatrix controlAsOne = new ComplexMatrix((int) Math.pow(2, numQubits), 1);
                for(int i = 0; i < tracker.getStateVecSize(); i++) {
                    if(i == controlQubit) {
                        controlAsOne.set(controlQubit, 0, new ComplexNumber(1.0, 0.0));
                        controlAsZero.set(controlQubit, 0, new ComplexNumber(0.0, 0.0));
                    }
                }
                //Identify valid basis states if control is 0
                //Identify valid basis states if control is 1

                //mutate the StateVector to represent the valid possible results without multiplying

                tracker.setStateVec(validStates);
            }
            }

            ////////////////////
            //    Execution   //
            ////////////////////

            executeOperatorSequence(operatorSequence);
            if (DEBUG) {
                System.out.println(Arrays.deepToString(operatorSequence));
                System.out.println("Applying " + work.getOperator() +
                        " with control " + controlQubit +
                        " to targets " + Arrays.toString(targets) + "\n" + finalGate);
            }

        }

    /**
     * Builds the operator matrix for a gate with multiple control qubits and a single target qubit.
     *
     * @param work The WorkItem containing the gate information.
     */
    private static void multiControlSingleTarget(WorkItem work) {
        // Implementation to be added
    }

    /**
     * Builds the operator matrix for a gate with multiple control qubits and multiple target qubits.
     *
     * @param work The WorkItem containing the gate information.
     */
    private static void multiControlMultiTarget(WorkItem work) {
        // Implementation to be added
    }

    /**
     * Executes the sequence of operators to build the final gate matrix.
     *
     * @param operatorSequence An array of ComplexMatrix operators to be applied.
     */
    private void executeOperatorSequence(ComplexMatrix[] operatorSequence) {
        for (int i = operatorSequence.length - 1; i >= 0; i--) {
            if (i == operatorSequence.length - 1) {
                finalGate = ComplexMath.tensorMultiply(operatorSequence[i], operatorSequence[i - 1]);
                i--;
            } else {
                finalGate = ComplexMath.tensorMultiply(finalGate, operatorSequence[i]);
            }
        }
    }
}