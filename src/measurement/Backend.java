package measurement;

import complex_classes.ComplexMath;
import complex_classes.ComplexMatrix;
import complex_classes.ComplexNumber;
import state.StateTracker;
import state.WorkItem;
import state.WorkQueue;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * The default engine that will execute the WorkQueue for a given jqs quantum circuit.
 *
 * @author Robert Smith
 * @version 0.1
 * @since 27 July 2024
 */
public class Backend {
    private int shots;
    private GateDirector gateD;
    private StateTracker tracker;
    private WorkQueue workQueue;
    private int numQubits;


    public Backend(GateDirector gateD, StateTracker tracker, WorkQueue workQueue, int shots) {
        this.numQubits = tracker.getQubitCount();
        this.gateD = gateD;
        this.tracker = tracker;
        this.workQueue = workQueue;
        this.shots = shots;
    }

    /**
     * Calculates the computational basis states of the quantum system, mutates the StateTracker state vector directly.
     */
    public void getComputationalState() {
        while (workQueue.hasWork()) {
            WorkItem nextItem = workQueue.peek();
            ComplexMatrix matrix = gateD.getGate(workQueue.getNextGate());
            if (nextItem.isSingleTarget()) { // TODO: temporary, adjust this for non Control multi-qubit gates
                tracker.setStateVec(ComplexMath.multiplyMatrix(matrix, tracker.getStateVec()));
            }
        }
    }

    /**
     * Will provide a measurement for the system on the specified qubit, not complete yet.
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

    private String[] getViableStates() {
        String states = ComplexMath.complexMatrixToBasisStates(this.tracker.getStateVec());
        return states.split("\\$");
    }

    public void simulate() {
        WorkQueue workCopy = workQueue.makeClone();
        StateTracker stateClone = tracker.makeClone();
        Map<String, Double> resultsMap = new HashMap<>();


        for (int i = 0; i < shots; i++) {
            while (workQueue.hasWork()) {
                GateDirector gdd = new GateDirector(this.tracker);
                WorkItem nextItem = workQueue.peek();
                ComplexMatrix matrix = gdd.getGate(workQueue.getNextGate());
                if (nextItem.isSingleTarget()) {
                    tracker.setStateVec(ComplexMath.multiplyMatrix(matrix, tracker.getStateVec()));
                }
            }
            // Need to insert the determined state resulting from the collapse into the resultsMap not the long form dirac or
            // short form dirac possible results, but a concrete result.
            for (int j = 0; j < numQubits; j++) {
                measureQubit(j);
            }
            String[] viableStates = getViableStates();
            for (String state : viableStates) {
                resultsMap.put(state, resultsMap.getOrDefault(state, 0.0) + 1.0);
            }
            workQueue = workCopy.makeClone();
            tracker.setStateVec(stateClone.getStateVec());
        }
        aggregateResults(resultsMap);
    }

    //enabled commented out code to check the sum values of all probabilities while the method runs and display in console.
    //used for verifying the sum of all probabilities is ~1.0
    private void aggregateResults(Map<String, Double> resultsMap) {
        String[] probabilitiesArray = new String[tracker.getStateVecSize() + 2];  //sized for all possibilities + the 2 comments
        int probArrayIdx = 1;                                                   //tracker position in for each loop
        probabilitiesArray[0] = "Probabilities over " + shots + " shots:";
        double perIncident = 1.0 / shots;                                       //weighted value per occurance of a result
        double total = 0.0;

        for (String key : resultsMap.keySet()) {
            double chance = (resultsMap.get(key) / (shots)) * perIncident * shots;
            String chanceString = String.format("%.3f", chance);
            total += chance;
            probabilitiesArray[probArrayIdx++] = key + ": " + chanceString;
        }

        probabilitiesArray[probArrayIdx] = "\nSum of raw probability values: " + total; //enable if sum of probabilities == 1.0 is in doubt
        for (String value : probabilitiesArray) {
            if (value != null) {
                System.out.println(value);
            }
        }
    }

}
