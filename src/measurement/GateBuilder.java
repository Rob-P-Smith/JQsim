package measurement;

import complexClasses.ComplexMatrix;
import state.StateTracker;
import state.WorkItem;
import state.WorkQueue;

public class GateBuilder {
    //needs to identify the gate to apply
    //needs to build the operator matrix by tensoring the provided operation for each qubit
    //needs to store the final resulting matrix as a ComplexMatrix object
    //needs to return the operator matrix
    private ComplexMatrix finalGate;

    public GateBuilder() {
        this.finalGate = new ComplexMatrix(StateTracker.getStateVec().getHeight(),
                StateTracker.getStateVec().getHeight());
        calculateGate();
    }

    private void calculateGate() {
        WorkItem work = WorkQueue.getNextGate();
        int stateLength = StateTracker.getStateVec().getHeight();
        if (work.isSingleQubit()) {
            for (int i = 0; i < stateLength; i++) {
                if (i != work.getTarget()) {
                    //multiply |0x0| or |1x1| by identity
                } else {
                    //multiply |0x0| or |1x1| by gateMatrix
                }
            }
        }
        if (!work.isSingleQubit()) {
            int controlSize = work.getControls().length;
            int targetsSize = work.getTargets().length;
            for(int i = 0; i < stateLength; i++){

            }
        }
    }

    public ComplexMatrix getGate() {
        return finalGate;
    }

    @Override
    public String toString() {
        return finalGate.toString();
    }
}
