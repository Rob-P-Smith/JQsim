package measurement;

import complex_classes.ComplexMath;
import complex_classes.ComplexMatrix;
import state.StateTracker;
import state.WorkItem;

import static complex_classes.ComplexGateEnums.*;

/**
 * The GateDirector class does the decoding and direction of applying gates to the correct classes that build the operator matrix
 * to return and multiply against the current state vector for calculating gates.
 *
 * @author Robert Smith
 * @version 0.1
 * @since 16 July 2024
 */
public class GateDirector {
    ComplexMatrix finalGate;
    StateTracker tracker;

    /**
     * Constructor for the GateDirector, takes in the StateTracker tracker from jqs to keep the tracker state
     * consistent from jqs to GateDirector as gates are built.
     * @param tracker the system state tracker from the active jqs class
     */
    public GateDirector(StateTracker tracker){
        this.tracker = tracker;
        finalGate = new ComplexMatrix(tracker.getStateVec().getHeight(),
                tracker.getStateVec().getHeight());
    }

    /**
     * Returns the final gate matrix.
     *
     * @param thisGate is a WorkItem from the WorkQueue
     * @return The ComplexMatrix representing the final gate.
     */
    public ComplexMatrix getGate(WorkItem thisGate) {
        calculateGate(thisGate);
        return finalGate;
    }

    /**
     * Executes the sequence of operators to build the final gate matrix.
     *
     * @param operatorSequence An array of ComplexMatrix operators to be applied.
     */
    public void executeOperatorSequence(ComplexMatrix[] operatorSequence) {
        for (int i = operatorSequence.length - 1; i >= 0; i--) {
            if (i == operatorSequence.length - 1) {
                this.finalGate = ComplexMath.tensorMultiply(operatorSequence[i], operatorSequence[i - 1]);
                i--;
            } else {
                this.finalGate = ComplexMath.tensorMultiply(finalGate, operatorSequence[i]);
            }
        }
    }

    /**
     * Calculates and applies the quantum gate based on the next WorkItem in the queue.
     *
     * @param thisGate The WorkItem containing the gate operation to be applied.
     */
    private void calculateGate(WorkItem thisGate) {
        ComplexMatrix singleOperator = decodeOperator(thisGate);
        this.finalGate = new ComplexMatrix(tracker.getStateVec().getHeight(),
                tracker.getStateVec().getHeight());

        if (thisGate.isSingleQubit()) {
            SingleQubitGateBuilder sqgb = new SingleQubitGateBuilder(this);
            sqgb.buildSingleQubitOperator(thisGate, singleOperator);
        }

        if (!thisGate.isSingleQubit()) {
            MultiQubitGateBuilder mqgb = new MultiQubitGateBuilder(this);
            int controlSize = thisGate.getControls().length;
            int targetsSize = thisGate.getTargets().length;

            if (controlSize == 1 && targetsSize >= 1) {
                mqgb.oneControlOneOrManyTargetsSetup(thisGate, singleOperator);
            } else if (controlSize > 1 && targetsSize == 1) {
                System.out.println("Multi Control Single Target hit");
                mqgb.multiControlSingleTarget(thisGate);
            } else if (controlSize > 1 && targetsSize > 1) {
                System.out.println("Multi Control Multi Target hit");
                mqgb.multiControlMultiTarget(thisGate);
            }
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
        String worker = work.getOperator();
        switch (worker) {
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

    @Override
    public String toString() {
        return "GateDirector{" +
                "finalGate=" + finalGate +
                ", tracker=" + tracker +
                '}';
    }
}
