//TODO: Profile this class using
// https://bell-sw.com/announcements/2020/07/22/Hunting-down-code-hotspots-with-JDK-Flight-Recorder/
// and try to reduce memory and cpu usage.
package measurement;

import complexClasses.ComplexMatrix;
import complexClasses.ComplexMath;
import state.StateTracker;
import state.WorkItem;
import state.WorkQueue;

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
    private static ComplexMatrix finalGate;
    private static final boolean DEBUG = false;

    /**
     * Constructs a new GateBuilder and initializes the finalGate matrix.
     */
    public GateBuilder() {
        finalGate = new ComplexMatrix(StateTracker.getStateVec().getHeight(),
                StateTracker.getStateVec().getHeight());
    }

    /**
     * Returns the final gate matrix.
     *
     * @return The ComplexMatrix representing the final gate.
     */
    public static ComplexMatrix getGate(WorkItem thisGate) {
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


    //TODO: Remove this when using the front end interface.
    /**
     * Calculates and applies the quantum gate based on the next WorkItem in the queue.
     *
     * @param queue The WorkQueue containing the gate operations to be applied.
     */
    private static void calculateGate(WorkQueue queue) {
        WorkItem work = queue.getNextGate();
        ComplexMatrix singleOperator = decodeOperator(work);

        if (work.isSingleQubit()) {
            buildSingleQubitOperator(work, singleOperator);
        }

        if (!work.isSingleQubit()) {
            int controlSize = work.getControls().length;
            int targetsSize = work.getTargets().length;

            if (controlSize == 1 && targetsSize >= 1) {
                oneControlOneOrManyTargets(work, singleOperator);
            } else if (controlSize > 1 && targetsSize == 1) {
                System.out.println("Multi Control Single Target hit");
                multiControlSingleTarget(work);
            } else if (controlSize > 1 && targetsSize > 1) {
                System.out.println("Multi Control Multi Target hit");
                multiControlMultiTarget(work);
            }

            if (DEBUG) {
                System.out.println("Applying " + work.getOperator() +
                        " to qubit " + work.getTarget() + "\n" + finalGate);
            }
        }
        //TODO: Remove this and place in the actual measurement when ready, only here for calculation and testing
        // of this class. Eventually measurement will call GateBuilder.getGate() to get the gate to execute.
        StateTracker.setStateVec(ComplexMath.multiplyMatrix(finalGate, StateTracker.getStateVec()));
    }

    /**
     * Calculates and applies the quantum gate based on the next WorkItem in the queue.
     *
     * @param thisGate The WorkItem containing the gate operation to be applied.
     */
    private static void calculateGate(WorkItem thisGate) {
        ComplexMatrix singleOperator = decodeOperator(thisGate);

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
    private static void buildSingleQubitOperator(WorkItem work, ComplexMatrix singleOperator) {
        if (DEBUG) System.out.println("buildSingleQubitOperator hit");
        int stateLength = StateTracker.getStateVecSize();
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
     * Decodes the operator from the WorkItem and returns the corresponding ComplexMatrix.
     *
     * @param work The WorkItem containing the operator information.
     * @return The ComplexMatrix representing the operator.
     */
    private static ComplexMatrix decodeOperator(WorkItem work) {
        ComplexMatrix singleOperator = null;
        switch (work.getOperator()) {
            case ("PAULI_X"), ("CNOT") -> singleOperator = PAULI_X.getMatrix();
            case ("PAULI_Y"), ("CYNOT") -> singleOperator = PAULI_Y.getMatrix();
            case ("PAULI_Z"), ("CZNOT") -> singleOperator = PAULI_Z.getMatrix();
            case ("HADAMARD"), ("CHNOT")-> singleOperator = HADAMARD.getMatrix();
            case ("SGATE") -> singleOperator = S_GATE.getMatrix();
            case ("TGATE") -> singleOperator = T_GATE.getMatrix();
            case ("IDENTITY") -> singleOperator = IDENTITY.getMatrix();
        }
        return singleOperator;
    }

    /**
     * Executes the sequence of operators to build the final gate matrix.
     *
     * @param operatorSequence An array of ComplexMatrix operators to be applied.
     */
    private static void executeOperatorSequence(ComplexMatrix[] operatorSequence) {
        for (int i = operatorSequence.length - 1; i >= 0; i--) {
            if (i == operatorSequence.length - 1) {
                finalGate = ComplexMath.tensorMultiply(operatorSequence[i], operatorSequence[i - 1]);
                i--;
            } else {
                finalGate = ComplexMath.tensorMultiply(finalGate, operatorSequence[i]);
            }
        }
    }

    /**
     * Builds the operator matrix for a gate with one control qubit and one or many target qubits.
     * This method applies the single-qubit operator to each target qubit when the control qubit is |1⟩.
     *
     * @param work The WorkItem containing the gate information.
     * @param singleOperator The single-qubit operator matrix to be applied to target qubits.
     */
    private static void oneControlOneOrManyTargets(WorkItem work, ComplexMatrix singleOperator) {
        if (DEBUG) System.out.println("oneControlOneOrManyTargets hit");
        int numQubits = (int) (Math.log(StateTracker.getStateVecSize()) / Math.log(2));
        int controlQubit = work.getControls()[0];
        Integer[] targets = work.getTargets();

        ComplexMatrix[] operatorSequence = new ComplexMatrix[numQubits];

        // Initialize all qubits with identity operators
        for (int i = 0; i < numQubits; i++) {
            operatorSequence[i] = IDENTITY.getMatrix();
        }

        // Create the controlled operation
        int controlMask = 1 << controlQubit;
        for (int i = 0; i < (1 << numQubits); i++) {
            if ((i & controlMask) != 0) {  // If control qubit is |1⟩
                for (int target : targets) {
                    int targetMask = 1 << target;
                    if ((i & targetMask) != 0) {  // If target qubit is |1⟩
                        operatorSequence[target] = singleOperator;
                    } else {
                        operatorSequence[target] = IDENTITY.getMatrix();
                    }
                }
            }
        }

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
     * Main method for testing the GateBuilder functionality.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        StateTracker systemState = new StateTracker(4);
        WorkQueue workQueue = new WorkQueue();
        workQueue.addGate("PAULI_X", 0);
        workQueue.addGate("PAULI_X", 1);
        workQueue.addGate("TGATE", 1);
        workQueue.addGate("CZNOT", 0, 1);
        workQueue.addGate("HADAMARD", 1);
        workQueue.addGate("CNOT", 0);
//        workQueue.addGate(new WorkItem("HADAMARD", 0));
//        workQueue.addGate(new WorkItem("PAULI_Z", 0));
//        workQueue.addGate(new WorkItem("SGate", 0));
//        workQueue.addGate(new WorkItem("SGate", 0));
//        workQueue.addGate(new WorkItem("HADAMARD", 0));
//        workQueue.addGate(new WorkItem("TGate", 0));
//        workQueue.addGate(new WorkItem("CNOT", new Integer[]{0}, new Integer[]{2}));
//        workQueue.addGate(new WorkItem("CNOT", new Integer[]{2}, new Integer[]{1}));
//        workQueue.addGate(new WorkItem("CNOT", new Integer[]{0}, new Integer[]{7}));
//        workQueue.addGate(new WorkItem("CNOT", new Integer[]{7}, new Integer[]{2}));
//        workQueue.addGate(new WorkItem("CNOT", new Integer[]{2}, new Integer[]{0}));

        System.out.println("System Size: " + (int) (Math.log(StateTracker.getStateVecSize()) / Math.log(2)));
        System.out.println("Work Queue length: " + workQueue.getGates().size());
        System.out.print("Before gate application: ");
        System.out.println(ComplexMath.complexMatrixToDiracNotation(StateTracker.getStateVec()));
        System.out.println();
        while (workQueue.hasWork()) {
            System.out.print(workQueue.peek() + " \nResults is: \n");
            calculateGate(workQueue);
            System.out.println(StateTracker.getStateVec());
            System.out.println(ComplexMath.complexMatrixToDiracNotation(StateTracker.getStateVec())+"\n");
        }
    }
}