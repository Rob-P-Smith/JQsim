package measurement;

import complex_classes.ComplexGateEnums;
import complex_classes.ComplexSparse;
import state.WorkItem;

import static complex_classes.ComplexGateEnums.IDENTITY;

/**
 * The SingleQubitGateBuilder class is responsible for constructing operator matrices
 * for single-qubit gates and applying them to the quantum state vector. This class
 * works in conjunction with the GateDirector to manage the application of quantum operations.
 *
 * This class directly mutates the finalGate field of the associated GateDirector.
 *
 * @author Robert Smith
 * @version 1.0
 * @since 11 August 2024
 * @see GateDirector
 * @see WorkItem
 * @see ComplexSparse
 * @see ComplexGateEnums
 */
public class SingleQubitGateBuilder{
    private GateDirector gateD;

    /**
     * Constructor for the SingleQubitGateBuilder class.
     *
     * @param gateD the active GateDirector used to manage gate operations and state tracking
     */
    public SingleQubitGateBuilder(GateDirector gateD){
        this.gateD = gateD;
    }

    /**
     * Builds the operator matrix for a single-qubit gate and applies it to the quantum state.
     * This method directly mutates the finalGate field of the associated GateDirector.
     *
     * Internal functionality:
     * 1. Determines the number of qubits in the system.
     * 2. Creates an array of operator matrices, one for each qubit.
     * 3. Assigns the identity matrix to all qubits except the target.
     * 4. Assigns the single-qubit operator to the target qubit.
     * 5. If the system has only one qubit, directly assigns the operator.
     * 6. For multi-qubit systems, calls executeOperatorSequence on the GateDirector.
     *
     * @param work The WorkItem containing the gate information, including the target qubit.
     * @param singleOperator The single-qubit operator matrix to be applied.
     * @see WorkItem
     * @see ComplexSparse
     * @see GateDirector#finalGate
     * @see GateDirector#executeOperatorSequence(ComplexSparse[])
     */
    void buildSingleQubitOperator(WorkItem work, ComplexSparse singleOperator) {

        int stateLength = gateD.tracker.getStateVecSize();
        int numQubits = (int) (Math.log(stateLength) / Math.log(2));
        ComplexSparse[] operatorSequence = new ComplexSparse[numQubits];
        for (int i = 0; i < numQubits; i++) {
            if (i != work.getTarget()) {
                operatorSequence[i] = IDENTITY.getMatrix();
            } else {
                operatorSequence[i] = singleOperator;
            }
        }

        if (numQubits == 1) {
            gateD.finalGate = singleOperator;
        } else {
            gateD.executeOperatorSequence(operatorSequence);
        }
    }

    /**
     * Returns a string representation of the SingleQubitGateBuilder.
     *
     * @return A string representation of the object, including the associated GateDirector.
     */
    @Override
    public String toString() {
        return "SingleQubitGateBuilder{" +
                "gd=" + gateD +
                '}';
    }
}
