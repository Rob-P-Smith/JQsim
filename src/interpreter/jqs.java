package interpreter;

import complexClasses.ComplexMath;
import complexClasses.ComplexMatrix;
import measurement.GateBuilder;
import state.StateTracker;
import state.WorkItem;
import state.WorkQueue;

/**
 * This class provides the user interface for jqsim. The commands are similar to other established
 * quantum simulator libraries.
 * <br>
 * The jqs constructor is overloaded, you can provide the number of shots as the 2nd parameter if you
 * desire a different number of shots than the default of 1024.
 * <br><ul>
 * Phi values: as a double.
 * Control values: as an integer of which qubit to use as control.
 * Target values: as an integer of which qubit to use as the target.
 * <br>
 * For multi-qubit gates, provide an array of int for controls and an array of int[] as targets.
 * </ul>
 * <br>This class instantiates a StateTracker which tracks the system vector as the system state as a column vector
 * of the qubit amplitudes. It also instantiates the WorkQueue which processes the gates applied to each qubit as the
 * user defines.
 *
 * @author Robert Smith
 * @version 0.1
 * @since 7 July 2024
 */
public class jqs {
    private int shots = 1024;
    private int numQubits;
    private WorkQueue workQueue;
    private StateTracker tracker;
    private GateBuilder gb;
    private final boolean DEBUG = false;

    /**
     * Default constructor, it's just here, so I don't get fined.
     */
    public jqs() {
    }

    /**
     * Constructor for the jqs class that takes and prepares a system using the provided number of qubits.
     *
     * @param numQubits the number of qubits you want to initialize to 0.0 real and 0.0 imaginary
     */
    public jqs(int numQubits) {
        device(numQubits);
    }

    /**
     * Reset the system state to zeroed out column vector of the same size as initially declared, replace gate builder
     * with new empty gate builder, and reset the workQueue to a new empty work queue.
     */
    public void reset() {
        tracker = new StateTracker(numQubits);
        gb = new GateBuilder(tracker);
        workQueue = new WorkQueue();
    }

    /**
     * Initializes the jqs device with the specified number of qubits and default number of shots.
     *
     * @param numberOfQubits The number of qubits for the device.
     */
    public void device(int numberOfQubits) {
        numQubits = numberOfQubits;
        tracker = new StateTracker(numQubits);
        gb = new GateBuilder(tracker);
        workQueue = new WorkQueue();
    }

    /**
     * Initializes the jqs device with the specified number of qubits and number of shots.
     *
     * @param numberOfQubits The number of qubits for the device.
     * @param numShots       The number of shots to run the simulation.
     */
    public void device(int numberOfQubits, int numShots) {
        shots = numShots;
        workQueue = new WorkQueue();
        StateTracker tracker = new StateTracker(numberOfQubits);
    }

    /**
     * Returns the StateTracker as tracker, for use in tests only.
     * TODO: Remove this at some point.
     *
     * @return tracker, the StateTracker of this instance of jqs
     */
    public StateTracker getStateTracker() {
        return tracker;
    }

    /**
     * Returns the state vector from StateTracker.
     *
     * @return tracker's state vector which is a 2^nqubits x 1 ComplexMatrix.
     */
    public ComplexMatrix getStateVec() {
        return tracker.getStateVec();
    }

    ////////////////////////
    // Single Qubit Gates //
    ////////////////////////

    /**
     * Applies the X gate to the specified target qubit.
     *
     * @param target The target qubit.
     */
    public void X(int target) {
        workQueue.addGate(new WorkItem("X", target));
    }

    /**
     * Applies the Z gate to the specified target qubit.
     *
     * @param target The target qubit.
     */
    public void Z(int target) {
        workQueue.addGate(new WorkItem("Z", target));
    }

    /**
     * Applies the Y gate to the specified target qubit.
     *
     * @param target The target qubit.
     */
    public void Y(int target) {
        workQueue.addGate(new WorkItem("Y", target));
    }

    /**
     * Applies the RZ gate to the specified target qubit.
     *
     * @param phi    The rotation angle in radians.
     * @param target The target qubit.
     */
    public void RZ(double phi, int target) {
        workQueue.addGate(new WorkItem("RZ", target));
    }

    /**
     * Applies the RX gate to the specified target qubit.
     *
     * @param phi    The rotation angle in radians.
     * @param target The target qubit.
     */
    public void RX(double phi, int target) {
        workQueue.addGate(new WorkItem("RX", target));
    }

    /**
     * Applies the RY gate to the specified target qubit.
     *
     * @param phi    The rotation angle in radians.
     * @param target The target qubit.
     */
    public void RY(double phi, int target) {
        workQueue.addGate(new WorkItem("RY", target));
    }

    /**
     * Applies the S gate to the specified target qubit.
     *
     * @param target The target qubit.
     */
    public void S(int target) {
        workQueue.addGate(new WorkItem("S", target));
    }

    /**
     * Applies the T gate to the specified target qubit.
     *
     * @param target The target qubit.
     */
    public void T(int target) {
        workQueue.addGate(new WorkItem("T", target));
    }

    /**
     * Applies the Ti gate to the specified target qubit.
     *
     * @param target The target qubit.
     */
    public void Ti(int target) {
        workQueue.addGate(new WorkItem("Ti", target));
    }

    /**
     * Applies the IDENTITY gate to the specified target qubit.
     *
     * @param target The target qubit.
     */
    public void ID(int target) {
        workQueue.addGate(new WorkItem("ID", target));
    }

    /**
     * Applies the HADAMARD gate to the specified target qubit.
     *
     * @param target The target qubit.
     */
    public void H(int target) {
        workQueue.addGate(new WorkItem("H", target));
    }

    /**
     * Applies the Si gate to the specified target qubit (S INVERSE).
     *
     * @param target The target qubit.
     */
    public void Si(int target) {
        workQueue.addGate(new WorkItem("Si", target));
    }

    //////////////////////
    // Dual Qubit Gates //
    //////////////////////

    /**
     * Applies the CZ (controlled Z) gate with the specified control and target qubits.
     *
     * @param control The control qubit.
     * @param target  The target qubit.
     */
    public void CZ(int control, int target) {
        Integer[] controls = {control};
        Integer[] targets = {target};
        workQueue.addGate(new WorkItem("CZ", controls, targets));
    }

    /**
     * Applies the CY (controlled Y) gate with the specified control and target qubits.
     *
     * @param control The control qubit.
     * @param target  The target qubit.
     */
    public void CY(int control, int target) {
        Integer[] controls = {control};
        Integer[] targets = {target};
        workQueue.addGate(new WorkItem("CY", controls, targets));
    }

    /**
     * Applies the CNOT (controlled X) gate with the specified control and target qubits.
     *
     * @param control The control qubit.
     * @param target  The target qubit.
     */
    public void CX(int control, int target) {
        Integer[] controls = {control};
        Integer[] targets = {target};
        workQueue.addGate(new WorkItem("CX", controls, targets));
    }

    /**
     * Applies the CH (controlled HADAMARD) gate with the specified control and target qubits.
     *
     * @param control The control qubit.
     * @param target  The target qubit.
     */
    public void CH(int control, int target) {
        Integer[] controls = {control};
        Integer[] targets = {target};
        workQueue.addGate(new WorkItem("CH", controls, targets));
    }

    /**
     * Applies the SWAP gate with the specified control and target qubits.
     *
     * @param control The control qubit.
     * @param target  The target qubit.
     */
    public void SWAP(int control, int target) {
        Integer[] controls = {control};
        Integer[] targets = {target};
        workQueue.addGate(new WorkItem("SWAP", controls, targets));
    }

    /**
     * Applies the ISWAP (inverse SWAP) gate with the specified control and target qubits.
     *
     * @param control The control qubit.
     * @param target  The target qubit.
     */
    public void ISWAP(int control, int target) {
        Integer[] controls = {control};
        Integer[] targets = {target};
        workQueue.addGate(new WorkItem("ISWAP", controls, targets));
    }

    /**
     * Applies the CSWAP (controlled SWAP) gate with the specified control and target qubits.
     *
     * @param control The control qubit.
     * @param target  The target qubit.
     */
    public void CSWAP(int control, int target) {
        Integer[] controls = {control};
        Integer[] targets = {target};
        workQueue.addGate(new WorkItem("CSWAP", controls, targets));
    }

    ///////////////////////
    // Multi Qubit Gates //
    ///////////////////////

    /**
     * Applies the Toffoli (controlled, controlled X) gate with the specified control qubits and target qubit.
     *
     * @param controlOne The first control qubit.
     * @param controlTwo The second control qubit.
     * @param target     The target qubit.
     */
    public void Toffoli(int controlOne, int controlTwo, int target) {
        Integer[] controls = {controlOne, controlTwo};
        Integer[] targets = {target};
        workQueue.addGate(new WorkItem("TOFFOLI", controls, targets));
    }

    /**
     * Applies a controlled gate with the specified gate name, control qubit, and target qubit.
     * TODO: Implement this in the gatebuilder
     * Single control and single target gate
     * Accepts any single qubit gate as the type to apply as controlled gate, e.g. cS, cT etc.
     *
     * @param gate    The name of the controlled gate.
     * @param control The control qubit.
     * @param target  The target qubit.
     */
    public void CGate(String gate, int control, int target) {
        Integer[] controls = {control};
        Integer[] targets = {target};
        workQueue.addGate(new WorkItem(gate, controls, targets));
    }

    /**
     * Applies a controlled gate with the specified gate name, control qubits, and target qubits.
     * TODO: Implement this in the gatebuilder
     * Multi control and multi target, can be a single control or many, and a single target or many.
     * Accepts any single qubit gate as the type to apply as controlled gate, e.g. cS, cT etc.
     *
     * @param gate     The name of the controlled gate.
     * @param controls The array of control qubits.
     * @param targets  The array of target qubits.
     */
    public void CGate(String gate, int[] controls, int[] targets) {
        Integer[] controlsInteger = new Integer[controls.length];
        for (int i = 0; i < controls.length; i++) {
            controlsInteger[i] = controls[i];
        }
        Integer[] targetsInteger = new Integer[targets.length];
        for (int i = 0; i < targets.length; i++) {
            targetsInteger[i] = targets[i];
        }
        workQueue.addGate(new WorkItem(gate, controlsInteger, targetsInteger));
    }

    /**
     * Applies a controlled-controlled gate with the specified gate name, control qubits, and target qubit.
     * TODO: Implement this in the gatebuilder
     * Dual control single target gate.
     * Accepts any single qubit gate as the type to apply as controlled gate, e.g. cS, cT etc.
     *
     * @param gate       The name of the controlled-controlled gate.
     * @param controlOne The first control qubit.
     * @param controlTwo The second control qubit.
     * @param target     The target qubit.
     */
    public void CCGate(String gate, int controlOne, int controlTwo, int target) {
        Integer[] controls = {controlOne, controlTwo};
        Integer[] targets = {target};
        workQueue.addGate(new WorkItem(gate, controls, targets));
    }

    ////////////////////
    // System Results //
    ////////////////////

    public void Measure(int i) {

    }

    /**
     * Calculates and returns the expected value of the quantum system.
     */
    public void getState() {
        if (DEBUG) System.out.println("Initial State: " + ComplexMath.complexMatrixToDiracNotation(tracker.getStateVec()));
        while (workQueue.hasWork()) {
            if (DEBUG) System.out.println("Adding " + workQueue.peek().getOperator());
            if (DEBUG) if (workQueue.peek().isSingleQubit()) {
                System.out.println("Target:" + workQueue.peek().getTarget());
            } else {
                for (Integer control : workQueue.peek().getControls()) {
                    System.out.println("Control: " + control);
                }
                for (Integer target : workQueue.peek().getTargets()) {
                    System.out.println("Target: " + target);
                }
            }
            if (!workQueue.peek().isSingleQubit()) {
                System.out.println(workQueue.peek());
            }
            ComplexMatrix matrix = gb.getGate(workQueue.getNextGate());
            tracker.setStateVec(ComplexMath.multiplyMatrix(matrix, tracker.getStateVec()));
            if (DEBUG) System.out.println("After Gate: " + ComplexMath.complexMatrixToDiracNotation(tracker.getStateVec()) + "\n");
        }
    }
}