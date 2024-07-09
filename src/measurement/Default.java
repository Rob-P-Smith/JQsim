package measurement;

import complexClasses.ComplexMath;
import state.StateTracker;
import state.WorkItem;
import state.WorkQueue;

/**
 * The default engine that will execute the WorkQueue for a given jqs quantum circuit.
 *
 * @author Rober Smith
 * @version 0.1
 * @since 7 July 2024
 */
public class Default {

//    /**
//     * The loop controller that executes each WorkItem in the WorkQueue, requires future expansion to include error
//     * handling from all processing options which will bubble to here when complete.
//     * <p>
//     * TODO: Add the error handing layer here. It should crash the program, return a stack trace, and provide the
//     *  the bubbled up error values.
//     *
//     * @param workQueue The queue of gates being applied to the quantum system.
//     */
//    public static void execute(WorkQueue workQueue) {
//        while (workQueue.hasWork()) {
//            executeGate(workQueue.getNextGate());
//        }
//    }

//    /**
//     * Performs the gate execution of each WorkItem to pass the GateBuilder, then does the multiplication of the
//     * generated gate against the state vector and sets the system state to the resulting column vector.
//     *
//     * @param nextGate the nextGate being applied to the system.
//     */
//    private static void executeGate(WorkItem nextGate) {
//        StateTracker.setStateVec(ComplexMath.multiplyMatrix(GateBuilder.getGate(nextGate), StateTracker.getStateVec()));
//    }
}
