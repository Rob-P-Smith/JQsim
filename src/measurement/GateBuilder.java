package measurement;

import complexClasses.*;
import state.StateTracker;
import state.WorkItem;
import state.WorkQueue;


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
    public static ComplexMatrix getGate() {
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
     * Calculates and applies the quantum gate based on the next WorkItem in the queue.
     *
     * @param queue The WorkQueue containing the gate operations to be applied.
     */
    private static void calculateGate(WorkQueue queue) {
        ComplexObject coj = new ComplexObject();
        WorkItem work = queue.getNextGate();

        int stateLength = StateTracker.getStateVecSize();
        int numQubits = (int) (Math.log(stateLength) / Math.log(2));

        ComplexMatrix singleOperator = null;
        switch (work.getOperator()) {
            case ("PAULI_X") -> singleOperator = ComplexGates.getPauliX();
            case ("PAULI_Y") -> singleOperator = ComplexGates.getPauliY();
            case ("PAULI_Z") -> singleOperator = ComplexGates.getPauliZ();
            case ("HADAMARD") -> singleOperator = ComplexGates.getHadamard();
            case ("SGate") -> singleOperator = ComplexGates.getSGate();
            case ("TGate") -> singleOperator = ComplexGates.getTGate();
            case ("IDENTITY") -> singleOperator = ComplexGates.getIdentity();
        }

        ComplexMatrix calculatedOperator = null;

        if (work.isSingleQubit()) {
            ComplexMatrix[] constructOperator = new ComplexMatrix[numQubits];
            // construct an order of individual gates to apply to each qubit location for building the calculated
            // operator to multiply against the system state vector
            for (int i = 0; i < numQubits; i++) {
                if (i != work.getTarget()) {
                    constructOperator[i] = ComplexGates.getIdentity();
                } else {
                    constructOperator[i] = singleOperator;
                }
            }

            if (numQubits == 1) {
                calculatedOperator = singleOperator;

            } else {
                for (int i = constructOperator.length - 1; i >= 0; i--) {
                    if (i == constructOperator.length - 1) {
                        calculatedOperator = coj.tensorMultiply(constructOperator[i], constructOperator[i - 1]);
                        i--;
                    } else {
                        assert calculatedOperator != null;
                        calculatedOperator = coj.tensorMultiply(calculatedOperator, constructOperator[i]);
                    }
                }
            }
            if (DEBUG) {
                System.out.println("Applying " + work.getOperator() +
                        " to qubit " + work.getTarget() + "\n" + calculatedOperator);
            }
        }


        //for multi qubit gate construction
        if (!work.isSingleQubit()) {
            int controlSize = work.getControls().length;
            int targetsSize = work.getTargets().length;

            if (controlSize == 1 && targetsSize > 1) {
                System.out.println("Single Control Multi Target hit");
            } else if (controlSize > 1 && targetsSize == 1) {
                System.out.println("Multi Control Single Target hit");
            } else if (controlSize > 1 && targetsSize > 1) {
                System.out.println("Multi Control Multi Target hit");
            }

            if (DEBUG) {
                System.out.println("Applying " + work.getOperator() +
                        " to qubit " + work.getTarget() + "\n" + calculatedOperator);
            }
        }

        assert calculatedOperator != null;
        finalGate = calculatedOperator;
        StateTracker.setStateVec(coj.multiplyMatrix(finalGate, StateTracker.getStateVec()));
    }

    /**
     * Main method for testing the GateBuilder functionality.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        StateTracker systemState = new StateTracker(10);
        WorkQueue workQueue = new WorkQueue();
        workQueue.addGate(new WorkItem("PAULI_X", 1));
        workQueue.addGate(new WorkItem("TGate", 1));

        System.out.println(workQueue.getGates().size());

        while (workQueue.hasWork()) {
            System.out.println(workQueue.peek());
            calculateGate(workQueue);
            System.out.println(ComplexObject.complexMatrixToDiracNotation(StateTracker.getStateVec()));
        }

    }
}
