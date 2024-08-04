package measurement;

import complex_classes.ComplexMatrix;
import complex_classes.ComplexSparse;
import state.WorkItem;

import static complex_classes.ComplexGateEnums.IDENTITY;

/**
 * The SingleQubitGateBuilder constructs the operator matrix for applying single qubit gates. It mutates the GateDirector finalGate field.
 *
 * @author Robert Smith
 * @version 0.1
 * @since 16 July 2024
 */
public class SingleQubitGateBuilder{
    private GateDirector gateD;

    /**
     * Constructor for the SingleQubitGateBuilder class.
     * @param gateD the active GateDirector
     */
    public SingleQubitGateBuilder(GateDirector gateD){
        this.gateD = gateD;
    }

    /**
     * Builds the operator matrix for a single-qubit gate.
     *
     * @param work           The WorkItem containing the gate information.
     * @param singleOperator The single-qubit operator matrix.
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

    @Override
    public String toString() {
        return "SingleQubitGateBuilder{" +
                "gd=" + gateD +
                '}';
    }
}
