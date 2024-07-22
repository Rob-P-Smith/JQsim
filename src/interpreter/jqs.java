package interpreter;

import complex_classes.ComplexMath;
import complex_classes.ComplexMatrix;
import complex_classes.ComplexNumber;
import measurement.GateDirector;
import state.StateTracker;
import state.WorkItem;
import state.WorkQueue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * This class provides the user interface for jqsim. The commands are similar to other established
 * quantum simulator libraries.
 * <br>
 * The jqs constructor is overloaded, you can provide the number of shots as the 2nd parameter if you
 * desire a different number of shots than the default of 1024.
 * <br><ul>
 * Theta values: as a double. e.g Math.PI/2 or 1.57079632679 ; both will work as the parameter when calling R gates.
 * Control values: as an integer of which qubit to use as control.
 * Target values: as an integer of which qubit to use as the target.
 * <br>
 * For dual-qubit gates, just provide the int value for control and target.
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
    private int shots = 1000;
    private int numQubits;
    private WorkQueue workQueue;
    private StateTracker tracker;
    private GateDirector gd;
    private static final String[] GATES = {
            "M", "X", "Z", "Y",
            "RX", "RZ", "RY", "R1",
            "S", "Si", "T", "Ti",
            "ID", "H",
            "CZ", "CY", "CX", "CH",
            "TOF", "CXX", "CGate", "CCGate",
            "SWAP", "ISWAP", "CSWAP", };

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
        gd = new GateDirector(tracker);
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
        gd = new GateDirector(tracker);
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
     * Returns the state vector from StateTracker.
     *
     * @return tracker's state vector which is a 2^nqubits x 1 ComplexMatrix.
     */
    public ComplexMatrix getStateVec() {
        return tracker.getStateVec();
    }

    public void listGates() {
        for (String gate : GATES) {
            System.out.println(gate);
        }
    }

    ////////////////////////
    // Single Qubit Gates //
    ////////////////////////

    /** Applies the measureQubit function to the target qubit by adding it to the workQueue instead of measuring is immediately.
     *
     * @param target the target qubit to measure
     */
    public void M(int target){
        measureQubit(target);
    }

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
     * @param theta  The rotation angle in radians.
     * @param target The target qubit.
     */
    public void RZ(double theta, int target) {
        workQueue.addGate(new WorkItem("RZ", target, theta));
    }

    /**
     * Applies the RX gate to the specified target qubit.
     *
     * @param theta  The rotation angle in radians.
     * @param target The target qubit.
     */
    public void RX(double theta, int target) {
        workQueue.addGate(new WorkItem("RX", target, theta));
    }

    /**
     * Applies the RY gate to the specified target qubit.
     *
     * @param theta  The rotation angle in radians.
     * @param target The target qubit.
     */
    public void RY(double theta, int target) {
        workQueue.addGate(new WorkItem("RY", target, theta));
    }

    /**
     * Applies the R1 gate to the specified target qubit.
     *
     * @param theta  The rotation angle in radians.
     * @param target The target qubit.
     */
    public void R1(double theta, int target) {
        workQueue.addGate(new WorkItem("R1", target, theta));
    }

    /**
     * Applies the RZ gate to the specified target qubit with default PI/4 as theta.
     *
     * @param target The target qubit.
     */
    public void RZ(int target) {
        workQueue.addGate(new WorkItem("RZ", target, Math.PI / 4));
    }

    /**
     * Applies the RY gate to the specified target qubit with default PI/4 for theta.
     *
     * @param target The target qubit.
     */
    public void RY(int target) {
        workQueue.addGate(new WorkItem("RY", target, Math.PI / 4));
    }

    /**
     * Applies the RX gate to the specified target qubit with default PI/4 as theta.
     *
     * @param target The target qubit.
     */
    public void RX(int target) {
        workQueue.addGate(new WorkItem("RX", target, Math.PI / 4));
    }

    /**
     * Applies the R1 gate to the specified target qubit with default PI as theta for full phase flip.
     *
     * @param target The target qubit.
     */
    public void R1(int target) {
        workQueue.addGate(new WorkItem("R1", target, Math.PI));
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
     * Applies the Si gate to the specified target qubit (S INVERSE).
     *
     * @param target The target qubit.
     */
    public void Si(int target) {
        workQueue.addGate(new WorkItem("Si", target));
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
        workQueue.addGate(new WorkItem("CZ", control, target));
    }

    /**
     * Applies the CY (controlled Y) gate with the specified control and target qubits.
     *
     * @param control The control qubit.
     * @param target  The target qubit.
     */
    public void CY(int control, int target) {
        workQueue.addGate(new WorkItem("CY", control, target));
    }

    /**
     * Applies the CNOT (controlled X) gate with the specified control and target qubits.
     *
     * @param control The control qubit.
     * @param target  The target qubit.
     */
    public void CX(int control, int target) {
        workQueue.addGate(new WorkItem("CX", control, target));
    }

    /**
     * Applies the CH (controlled HADAMARD) gate with the specified control and target qubits.
     *
     * @param control The control qubit.
     * @param target  The target qubit.
     */
    public void CH(int control, int target) {
        workQueue.addGate(new WorkItem("CH", control, target));
    }

    /**
     * Applies the SWAP gate with the specified control and target qubits.
     *
     * @param control The control qubit.
     * @param target  The target qubit.
     */
    public void SWAP(int control, int target) {
        workQueue.addGate("SWAP", control, target);
    }

    /**
     * Applies the ISWAP (inverse SWAP) gate with the specified control and target qubits.
     *
     * @param control The control qubit.
     * @param target  The target qubit.
     */
    public void ISWAP(int control, int target) {
        workQueue.addGate("ISWAP", control, target);
    }

    /**
     * Applies the CSWAP (controlled SWAP) gate with the specified control and target qubits.
     *
     * @param control The control qubit.
     * @param target  The target qubit.
     */
    public void CSWAP(int control, int target) {
        workQueue.addGate("CSWAP", control, target);
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
    public void TOF(int controlOne, int controlTwo, int target) {
        Integer[] controls = {controlOne, controlTwo};
        Integer[] targets = {target};
        workQueue.addGate(new WorkItem("TOFFOLI", controls, targets));
    }

    /**
     * Applies the CXX gate where a single control determines the state of two target bits if control is 1.
     *
     * @param control   The control qubit.
     * @param targetOne The first target qubit.
     * @param targetTwo The second target qubit.
     */
    public void CXX(int control, int targetOne, int targetTwo) {
        Integer[] controls = {control};
        Integer[] targets = {targetOne, targetTwo};
        workQueue.addGate(new WorkItem("CXX", controls, targets));
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

    /**
     * Applies a controlled-controlled gate with the specified gate name, control qubits, and target qubits.
     * TODO: Implement this in the gatebuilder
     * Multi control Multi target gate.
     * Accepts any single qubit gate as the type to apply as controlled gate, e.g. cS, cT etc.
     *
     * @param gate     The name of the controlled-controlled gate.
     * @param controls The control qubits.
     * @param targets  The target qubits.
     */
    public void CCGate(String gate, int[] controls, int[] targets) {
        Integer[] controlQubits = Arrays.stream(controls).boxed().toArray(Integer[]::new);
        Integer[] targetQubits = Arrays.stream(targets).boxed().toArray(Integer[]::new);
        workQueue.addGate(new WorkItem(gate, controlQubits, targetQubits));
    }

    /**
     * Will provide a measurement for the system on the specified qubit, not done yet.
     *
     * @param target the qubit to measure
     */
    public int measureQubit(int target) {
        Random random = new Random();
        if (target < 0 || target >= numQubits) {
            throw new IllegalArgumentException("Invalid qubit index");
        }

        int stateSize = tracker.getStateVecSize();
        double probability0 = 0;

        // Calculate probability of measuring |0>
        for (int i = 0; i < stateSize; i++) {
            if ((i & (1 << target)) == 0) {
                probability0 += tracker.get(i, 0).magnitudeSquared();
            }
        }

        // Perform measurement
        int result = (random.nextDouble(0.0, 1.00000000001) < probability0) ? 0 : 1;

        // Update state vector based on measurement result
        double normalizationFactor = 0;
        for (int i = 0; i < stateSize; i++) {
            boolean keepState = (result == 0 && (i & (1 << target)) == 0) ||
                    (result == 1 && (i & (1 << target)) != 0);
            if (keepState) {
                normalizationFactor += tracker.get(i, 0).magnitudeSquared();
            } else {
                tracker.getStateVec().set(i, 0, new ComplexNumber());
            }
        }

        // Normalize the remaining states
        normalizationFactor = Math.sqrt(normalizationFactor);
        for (int i = 0; i < stateSize; i++) {
            if (tracker.get(i, 0).getReal() != 0.0 || tracker.get(i, 0).getImag() != 0.0) {
                double denominator = normalizationFactor * normalizationFactor;
                double newReal = (tracker.get(i, 0).getReal() * normalizationFactor) / denominator;
                double newImag = (tracker.get(i, 0).getImag() * normalizationFactor) / denominator;
                tracker.getStateVec().set(i, 0, new ComplexNumber(newReal, newImag));
            }
        }
        return result;
    }

    /**
     * Calculates the computational basis states of the quantum system, mutates the StateTracker state vector directly.
     */
    public void getComputationalState() {
        while (workQueue.hasWork()) {
            WorkItem nextItem = workQueue.peek();
            ComplexMatrix matrix = gd.getGate(workQueue.getNextGate());
            if (nextItem.isSingleTarget()) { // TODO: temporary, fix this for non Control multi-qubit gates
                tracker.setStateVec(ComplexMath.multiplyMatrix(matrix, tracker.getStateVec()));
            }
        }
    }

    /**
     * Overriden toString() to print the Dirac notation of the current state vector.
     *
     * @return the String representation of the current system state vector in Dirac notation.
     */
    @Override
    public String toString() {
        return ComplexMath.complexMatrixToDiracNotation(this.getStateVec());
    }

    /**
     * Runs the simulation 'shots' number of times ands returns the probabilities of each possible resulting state.
     *
     * @return the probabilities of each state in a String
     */
    public void simulate() {
        WorkQueue workCopy = workQueue.makeClone();
        StateTracker stateClone = tracker.makeClone();
        Map<String, Integer> resultsMap = new HashMap<>();
        String probabilities = "";

        for (int i = 0; i < shots; i++) {
            while (workQueue.hasWork()) {
                GateDirector gdd = new GateDirector(this.tracker);
                WorkItem nextItem = workQueue.peek();
                ComplexMatrix matrix = gdd.getGate(workQueue.getNextGate());
                if (nextItem.isSingleTarget()) { // TODO: temporary, fix this for non Control multi-qubit gates
                    tracker.setStateVec(ComplexMath.multiplyMatrix(matrix, tracker.getStateVec()));
                }
            }
            resultsMap.put(this.toString(), resultsMap.getOrDefault(this.toString(), 0) + 1);
            if(i < shots) {
                this.tracker = stateClone.makeClone();
                this.workQueue = workCopy.makeClone();
            }
        }
        Map<String, Double> incidentMap = new HashMap<>();
        for (String key : resultsMap.keySet()) {
            double value = resultsMap.get(key)/(shots*1.0);
            incidentMap.put(key, value);
        }
        double perIncident = 1.0 / shots;
        double total = 0.0;
        for (String key : incidentMap.keySet()) {
            double chance = incidentMap.get(key)*perIncident * shots;
            String chanceString = String.format("%.3f", chance);
            total+= chance;
            probabilities += "\n" + key + ": " + chanceString + "%";
        }
        probabilities+="\nSum of raw probability values: "+total;
        System.out.println(probabilities);
    }
    public void setState(ComplexMatrix startingState) {
        tracker.setStateVec(startingState);
    }
}