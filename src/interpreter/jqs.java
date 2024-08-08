package interpreter;

import complex_classes.ComplexMath;
import complex_classes.ComplexSparse;
import measurement.Backend;
import measurement.GateDirector;
import measurement.QFTBuilder;
import state.StateTracker;
import state.WorkItem;
import state.WorkQueue;

import java.util.*;

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
 *
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
    private String label;
    private int numQubits;
    private WorkQueue workQueue;
    private StateTracker tracker;
    private GateDirector gd;
    private Backend backend;
    private static final String[] GATES = {
            "M", "X", "Z", "Y",
            "RX", "RZ", "RY", "R1",
            "S", "Si", "T", "Ti",
            "ID", "H",
            "CZ", "CY", "CX", "CH",
            "TOF", "CXX", "CGate", "CCGate",
            "SWAP", "ISWAP", "CSWAP", };

    ////////////////////////
    //   Class Required   //
    ////////////////////////

    /**
     * Default constructor, it's just here, so I don't get fined. Defaults to system with 2 qubits.
     */
    public jqs() {
        this.label = "Default";
        device(2);
        this.numQubits = 2;
    }

    /**
     * Constructor for the jqs class that takes and prepares a system using the provided number of qubits.
     *
     * @param numQubits the number of qubits you want to initialize to 0.0 real and 0.0 imaginary
     */
    public jqs(int numQubits){
        this.label = "Default";
        this.numQubits = numQubits;
        device(numQubits);
    }

    /**
     * Constructor for the jqs class that takes and prepares a system using the provided number of qubits.
     *
     * @param numQubits the number of qubits you want to initialize to 0.0 real and 0.0 imaginary
     * @param label the label for this jqs object, helpful in creating separate registers
     */
    public jqs(int numQubits, String label) {
        this.label = label;
        this.numQubits = numQubits;
        device(numQubits);
    }

    /**
     * Constructor for the jqs class that takes and prepares a system using the provided number of qubits.
     *
     * @param numQubits the number of qubits you want to initialize to 0.0 real and 0.0 imaginary
     * @param label the label for this jqs object, helpful in creating separate registers
     */
    public jqs(int numQubits, int shots, String label) {
        this.shots = shots;
        this.label = label;
        this.numQubits = numQubits;
        device(numQubits);
    }

    /**
     * Constructor for the jqs class that takes and prepares a system using the provided number of qubits.
     *
     * @param numQubits the number of qubits you want to initialize to 0.0 real and 0.0 imaginary
     */
    public jqs(int numQubits, int shots) {
        this.shots = shots;
        this.numQubits = numQubits;
        device(numQubits);
    }

    /**
     * Returns the number of qubits in the system.
     * @return int as value for number of qubits.
     */
    public int size(){
        return numQubits;
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
     * Reset the system state to zeroed out column vector of the same size as initially declared, replace gate builder
     * with new empty gate builder, and reset the workQueue to a new empty work queue.
     */
    public void reset(int numQubits) {
        device(numQubits);
    }

    /**
     * Initializes the jqs device with the specified number of qubits and default number of shots.
     *
     * @param numQubits The number of qubits for the device.
     */
    public void device(int numQubits) {
        this.numQubits = numQubits;
        this.tracker = new StateTracker(numQubits);
        this.gd = new GateDirector(tracker);
        this.workQueue = new WorkQueue();
        this.backend = new Backend(gd, tracker, workQueue, shots);
    }

    /**
     * Initializes the jqs device with the specified number of qubits and number of shots.
     *
     * @param numberOfQubits The number of qubits for the device.
     * @param numShots       The number of shots to run the simulation.
     */
    public void device(int numberOfQubits, int numShots) {
        this.shots = numShots;
        this.workQueue = new WorkQueue();
        this.tracker = new StateTracker(numberOfQubits);
        this.backend = new Backend(gd, tracker, workQueue, shots);
    }

    /**
     * Returns the state vector from StateTracker.
     *
     * @return tracker's state vector which is a 2^nqubits x 1 ComplexMatrix.
     */
    public ComplexSparse getStateVec() {
        return tracker.getStateVec();
    }

    public void listGates() {
        for (String gate : GATES) {
            System.out.println(gate);
        }
    }

    /**
     * Setter for the state vector to make setting it shorter from outside jqs.
     * @param newState the state to set the tracker system vector to.
     */
    public void setState(ComplexSparse newState) {
        tracker.setStateVec(newState);
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
     * Applies the RZ gate to the specified target qubit with default PI/2 as theta.
     *
     * @param target The target qubit.
     */
    public void RZ(int target) {
        workQueue.addGate(new WorkItem("RZ", target, Math.PI));
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
     * @param bitOne The first qubit.
     * @param bitTwo  The second qubit.
     */
    public void SWAP(int bitOne, int bitTwo) {
        workQueue.addGate("SWAP", bitOne, bitTwo);
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
     * @param targetOne  The target qubit.
     * @param targetTwo  The second target qubit.
     */
    public void CSWAP(int control, int targetOne, int targetTwo) {
        workQueue.addGate("CSWAP", control, targetOne, targetTwo);
    }

    /**
     * Applies the CSWAP (controlled SWAP) gate with the specified control and target qubits.
     *
     * @param control The control qubit.
     * @param targetOne  The target qubit.
     * @param targetTwo  The second target qubit.
     */
    public void Fredkin(int control, int targetOne, int targetTwo) {
        workQueue.addGate("CSWAP", control, targetOne, targetTwo);
    }

    /**
     * Applies a controlled gate with the specified gate name, control qubit, and target qubit.
     * Single control and single target gate
     * Accepts any single basis state changing qubit gate as the type to apply as controlled gate, e.g. cS, cT etc.
     *
     * @param gate    The name of the controlled gate.
     * @param control The control qubit.
     * @param target  The target qubit.
     */
    public void CGate(String gate, int control, int target) {
        workQueue.addGate(new WorkItem("C"+gate, control, target));
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
     * Applies a controlled phase gate with the specified gate name, control qubit, target qubit, and theta.
     * Single control and single target gate
     * Accepts any single qubit phase rotation gate as the type to apply as controlled gate, e.g. CRZ, CRY etc.
     *
     * @param gate    The name of the controlled gate.
     * @param control The control qubit.
     * @param target  The target qubit.
     * @param theta the rotation to apply as a double.
     */
    public void CGate(String gate, int control, int target, double theta) {
        workQueue.addGate(new WorkItem("C"+gate, control, target, theta));
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
    public void XCGate(String gate, int[] controls, int[] targets) {
        Integer[] controlQubits = Arrays.stream(controls).boxed().toArray(Integer[]::new);
        Integer[] targetQubits = Arrays.stream(targets).boxed().toArray(Integer[]::new);
        workQueue.addGate(new WorkItem(gate, controlQubits, targetQubits));
    }

    ///////////////////////
    //  System Actions   //
    ///////////////////////

    /**
     * Returns the computational basis states + the amplitude corresponding to that state and the phase corresponding to the amplitudes of the state.
     */
    public void buildCircuit() {
        backend.getComputationalState();
    }

    /**
     * Should measure a given qubit state and give the result as 0 or 1.
     * TODO re-look this process, it is suspect
     * @param target the qubit to measure
     * @return the found result.
     */
    public int measureQubit(int target){
        return backend.measureQubit(target);
    }

    /**
     * Simulates the quantum circuit running shots times and collects the results and prints them out.
     */
    public Map<String, String> simulate(){
        return backend.simulate();
    }

    /**
     * Simulates the quantum circuit running shots times and collects the results.
     */
    public Map<String, String> silentSimulate(){
        return backend.silentSimulate();
    }

    /**
     * Performs Quantum Phase Estimation (QPE) with a specified gate type and phase angle.
     * This method does not print the probabilistic results, only the two most lilely
     * phases and their percentage.
     *
     * @param gateType      The type of quantum gate to use in the estimation.
     * @param theta         The phase angle to estimate.
     * @param lowEstimator  The lower bound of the estimation register.
     * @param highEstimator The upper bound of the estimation register.
     * @param target        The target qubit for the controlled operations.
     *
     * @see #QPE(String, int, int, int) QPE without theta parameter
     * @see #QFTi(int, int) Inverse Quantum Fourier Transform
     * @see #silentSimulate() Simulation method
     * @see #extractAndPrintQPEResults(Map, int) Result extraction method
     * @see <a href="https://en.wikipedia.org/wiki/Quantum_phase_estimation_algorithm">Quantum Phase Estimation Algorithm</a>
     */
    public void QPE(String gateType, double theta, int lowEstimator, int highEstimator, int target){
        this.X(target);
        for(int i = 0; i <= highEstimator; i++){
            this.H(i);
        }
        for(int i = 0; i <= highEstimator; i++){
            for(int j = 0; j < (int)Math.pow(2,i); j++) {
                this.CGate(gateType, i, target, theta);
            }
        }
        this.buildCircuit();
        this.QFTi(lowEstimator,highEstimator);
        Map<String, String> result = this.silentSimulate();
        extractAndPrintQPEResults(result, (highEstimator-lowEstimator)+1);
    }

    /**
     * Performs Quantum Phase Estimation (QPE) with a specified gate type.
     * This overload does not require a theta parameter.
     * This method does not print the probabilistic results, only the two most lilely
     * phases and their percentage.
     *
     * @param gateType      The type of quantum gate to use in the estimation.
     * @param lowEstimator  The lower bound of the estimation register.
     * @param highEstimator The upper bound of the estimation register.
     * @param target        The target qubit for the controlled operations.
     *
     * @see #QPE(String, double, int, int, int) QPE with theta parameter
     * @see #QFTi(int, int) Inverse Quantum Fourier Transform
     * @see #silentSimulate() Simulation method
     * @see #extractAndPrintQPEResults(Map, int) Result extraction method
     * @see <a href="https://en.wikipedia.org/wiki/Quantum_phase_estimation_algorithm">Quantum Phase Estimation Algorithm</a>
     */
    public void QPE(String gateType,int lowEstimator, int highEstimator, int target){
        this.X(target);
        for(int i = 0; i <= highEstimator; i++){
            this.H(i);
        }
        for(int i = 0; i <= highEstimator; i++){
            for(int j = 0; j < (int)Math.pow(2,i); j++) {
                this.CGate(gateType, i, target);
            }
        }
        this.buildCircuit();
        this.QFTi(lowEstimator,highEstimator);
        Map<String, String> result = this.silentSimulate();
        extractAndPrintQPEResults(result, (highEstimator-lowEstimator)+1);
    }

    /**
     * Extracts and prints the results of the Quantum Phase Estimation.
     *
     * @param result          A map containing the simulation results.
     * @param estimationCount The number of qubits used for estimation.
     *
     * @see #QPE(String, double, int, int, int) QPE method
     * @see #QPE(String, int, int, int) QPE method without theta
     */
    private void extractAndPrintQPEResults(Map<String, String> result, int estimationCount){
        System.out.println("\nTwo primary phases detected:");
        for(String key : result.keySet()) {
            int decimalValue = Integer.parseInt(key,2);
            double phase = (double) decimalValue /Math.pow(2, estimationCount);
            String value = String.valueOf(phase);
            System.out.println(value + ": " + result.get(key)+"%");
        }
    }

    /**
     * Conduct QFT on the system
     */
    public void QFT(){
        QFTBuilder qft = new QFTBuilder(gd);
        qft.applyQFT();
    }

    /**
     * Conduct QFTi on the system
     */
    public void QFTi(){
        QFTBuilder qft = new QFTBuilder(gd);
        qft.applyQFTi();
    }

    /**
     * Conduct QFTi on the system in a range of startQubit to endQubit, where start and end are inclusive.
     */
    public void QFTi(int startQubit, int endQubit){
        QFTBuilder qft = new QFTBuilder(gd);
        qft.applyQFTi(startQubit, endQubit);
    }
}