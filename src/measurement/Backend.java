package measurement;

import complex_classes.ComplexMath;
import complex_classes.ComplexNumber;
import complex_classes.ComplexSparse;
import state.StateTracker;
import state.WorkItem;
import state.WorkQueue;

import java.util.*;

/**
 * The default engine that executes the WorkQueue for a given jqs quantum circuit.
 * This class handles quantum state calculations, measurements, and simulation.
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

    /**
     * Constructor for the Backend class.
     * @param gateD The GateDirector object for managing quantum gates
     * @param tracker The StateTracker object that tracks the state of the quantum system
     * @param workQueue The WorkQueue containing all the gates to apply
     * @param shots The number of shots (iterations) to use for getting expectation values
     */
    public Backend(GateDirector gateD, StateTracker tracker, WorkQueue workQueue, int shots) {
        this.numQubits = tracker.getQubitCount();
        this.gateD = gateD;
        this.tracker = tracker;
        this.workQueue = workQueue;
        this.shots = shots;
    }

    /**
     * Calculates the computational basis states of the quantum system by applying gates from the WorkQueue.
     * This method mutates the StateTracker's state vector directly.
     */
    public void getComputationalState() {
        while (workQueue.hasWork()) {
            WorkItem nextItem = workQueue.peek();
            ComplexSparse matrix = gateD.getGate(workQueue.getNextGate());

            if (nextItem.isSingleTarget()) { // TODO: temporary, adjust this for non Control multi-qubit gates
                ComplexSparse tempVec = ComplexMath.multiplyMatrix(matrix, tracker.getStateVec());
                tracker.setStateVec(tempVec);
            }
        }
    }

    /**
     * Performs a measurement on the specified qubit of the quantum system.
     *
     * @param target The index of the qubit to measure
     * @return The measured value of the qubit (0 or 1)
     * @throws IllegalArgumentException if the target qubit index is invalid
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
                tracker.getStateVec().put(i, 0, new ComplexNumber());
            }
        }

        // Normalize the remaining states
        normalizationFactor = Math.sqrt(normalizationFactor);
        for (int i = 0; i < stateSize; i++) {
            if (tracker.get(i, 0).getReal() != 0.0 || tracker.get(i, 0).getImag() != 0.0) {
                double denominator = normalizationFactor * normalizationFactor;
                double newReal = (tracker.get(i, 0).getReal() * normalizationFactor) / denominator;
                double newImag = (tracker.get(i, 0).getImag() * normalizationFactor) / denominator;
                tracker.getStateVec().put(i, 0, new ComplexNumber(newReal, newImag));
            }
        }
        return result;
    }

    /**
     * Retrieves the viable states of the quantum system as an array of strings.
     *
     * @return An array of strings representing the viable states
     */
    private String[] getViableStates() {
        String states = ComplexMath.complexMatrixToBasisStates(this.tracker.getStateVec());
        return states.split("\\$");
    }

    /**
     * Performs a simulation of the quantum circuit for the specified number of shots.
     * This method applies gates, performs measurements, and aggregates results.
     */
    public void simulate() {
        WorkQueue workCopy = workQueue.makeClone();
        StateTracker stateClone = tracker.makeClone();
        Map<String, Double> resultsMap = new HashMap<>();

        for (int i = 0; i < shots; i++) {
            while (workQueue.hasWork()) {
                GateDirector gdd = new GateDirector(this.tracker);
                WorkItem nextItem = workQueue.peek();
                ComplexSparse matrix = gdd.getGate(workQueue.getNextGate());
                if (nextItem.isSingleTarget()) {
                    tracker.setStateVec(ComplexMath.multiplyMatrix(matrix, tracker.getStateVec()));
                }
            }

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

    /**
     * Aggregates and displays the results of the quantum simulation.
     * This method sorts the results by the quantum state (key of the resultsMap),
     * calculates probabilities, and prints them along with a total probability check.
     *
     * @param resultsMap A map containing the quantum states as keys and their occurrence counts as values.
     *                   The keys are typically binary strings representing quantum states (e.g., "000", "001", etc.),
     *                   and the values are the number of times each state was observed during the simulation.
     */
    private void aggregateResults(Map<String, Double> resultsMap) {
        System.out.println("Probabilities over " + shots + " shots:");

        // Convert the map to a list and sort it by the quantum state (key)
        List<Map.Entry<String, Double>> sortedResults = new ArrayList<>(resultsMap.entrySet());
        sortedResults.sort(Map.Entry.comparingByKey());

        double totalProbability = 0.0;

        // Iterate through the sorted results, calculate and print probabilities
        for (Map.Entry<String, Double> entry : sortedResults) {
            // Calculate the probability by dividing the count by the total number of shots
            double probability = entry.getValue() / shots;

            // Print the quantum state and its probability
            System.out.printf("%s: %.2f%n", entry.getKey(), probability*100);

            // Add to the total probability (should sum to approximately 1)
            totalProbability += probability;
        }

        // Print the sum of all probabilities as a check (should be very close to 1.0)
        System.out.printf("%nSum of raw probability values: %.4f%n", totalProbability);
    }
}