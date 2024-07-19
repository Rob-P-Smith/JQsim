package measurement;

import complex_classes.ComplexMath;
import complex_classes.ComplexMatrix;
import complex_classes.ComplexNumber;
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

        if (thisGate.isSingleTarget()) {
            SingleQubitGateBuilder sqgb = new SingleQubitGateBuilder(this);
            sqgb.buildSingleQubitOperator(thisGate, singleOperator);
        } else if (thisGate.isDualTarget() || thisGate.isMultiTarget()){
            MultiQubitGateBuilder mqgb = new MultiQubitGateBuilder(this);
            mqgb.dualAndMultiGateSetup(thisGate);
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
            case ("X"), ("CX"), ("CXX"), ("TOFFOLI") -> singleOperator = PAULI_X.getMatrix();
            case ("Y"), ("CY") -> singleOperator = PAULI_Y.getMatrix();
            case ("Z"), ("CZ") -> singleOperator = PAULI_Z.getMatrix();
            case ("H"), ("CH") -> singleOperator = HADAMARD.getMatrix();
            case ("S") -> singleOperator = S_GATE.getMatrix();
            case ("Si") -> singleOperator = SI_GATE.getMatrix();
            case ("T") -> singleOperator = T_GATE.getMatrix();
            case ("Ti") -> singleOperator = TI_GATE.getMatrix();
            case ("ID") -> singleOperator = IDENTITY.getMatrix();
            case ("RX") -> singleOperator = buildRXGate(work);
            case ("RY") -> singleOperator = buildRYGate(work);
            case ("RZ") -> singleOperator = buildRZGate(work);
            case ("R1") -> singleOperator = buildR1Gate(work);
            case ("SWAP"), ("CSWAP") -> singleOperator = SWAP.getMatrix();
            case ("ISWAP") -> singleOperator = ISWAP.getMatrix();
        }
        return singleOperator;
    }

    /**
     * Construct the matrix for the R1 gate based on user provided theta value.
     * @param work the work item that contains the RX gate in the workQueue
     * @return the complex matrix that is the RX gate operator
     */
    private static ComplexMatrix buildR1Gate(WorkItem work) {
        ComplexMatrix builtGate = new ComplexMatrix(2,2);
        builtGate.set(0,0, new ComplexNumber(1,0) );
        builtGate.set(0,1, new ComplexNumber(0,0));
        builtGate.set(1,0, new ComplexNumber(0,0));
        builtGate.set(1,1, new ComplexNumber(Math.cos(work.getTheta()), Math.sin(work.getTheta())));
        return builtGate;
    }
    /**
     * Construct the matrix for the RX gate based on user provided theta value.
     * @param work the work item that contains the RX gate in the workQueue
     * @return the complex matrix that is the RX gate operator
     */
    private static ComplexMatrix buildRXGate(WorkItem work){
        ComplexMatrix builtGate = new ComplexMatrix(2,2);
        double cosTheta = Math.cos(work.getTheta()/2);
        double sinTheta = Math.sin(work.getTheta()/2);
        builtGate.set(0,0,new ComplexNumber(cosTheta, 0));
        builtGate.set(0,1, new ComplexNumber(0, -sinTheta));
        builtGate.set(1,0,new ComplexNumber(0, -sinTheta));
        builtGate.set(1,1,new ComplexNumber(cosTheta, 0));
        return builtGate;
    }
    /**
     * Construct the matrix for the RY gate based on user provided theta value.
     * @param work the work item that contains the RX gate in the workQueue
     * @return the complex matrix that is the RY gate operator
     */
    private static ComplexMatrix buildRYGate(WorkItem work){
        ComplexMatrix builtGate = new ComplexMatrix(2,2);
        double cosTheta = Math.cos(work.getTheta()/2);
        double sinTheta = Math.sin(work.getTheta()/2);
        builtGate.set(0,0,new ComplexNumber(cosTheta, 0));
        builtGate.set(0,1, new ComplexNumber(-sinTheta, 0));
        builtGate.set(1,0,new ComplexNumber(sinTheta,0));
        builtGate.set(1,1,new ComplexNumber(cosTheta, 0));
        return builtGate;
    }
    /**
     * Construct the matrix for the RZ gate based on user provided theta value.
     * @param work the work item that contains the RX gate in the workQueue
     * @return the complex matrix that is the RZ gate operator
     */
    private static ComplexMatrix buildRZGate(WorkItem work){
        ComplexMatrix builtGate = new ComplexMatrix(2,2);
        builtGate.set(0,0,new ComplexNumber(Math.cos(-work.getTheta()/2), Math.sin(-work.getTheta()/2)));
        builtGate.set(0,1, new ComplexNumber(0, 0));
        builtGate.set(1,0,new ComplexNumber(0, 0));
        builtGate.set(1,1,new ComplexNumber(Math.cos(-work.getTheta()/2), -Math.sin(-work.getTheta()/2)));
        return builtGate;
    }

    /**
     * Overridden toString for this class.
     * @return A string of the final operator calculated and the system state as a column vector in a ComplexMatrix of 2^n x 1 dimensions.
     */
    @Override
    public String toString() {
        return "GateDirector{" +
                "finalGate=" + finalGate +
                ", tracker=" + tracker +
                '}';
    }
}
