package state;

import complex_classes.ComplexNumber;
import complex_classes.ComplexSparse;

/**
 * Tracks the state of a quantum system's qubits as a column vector stored in a
 * {@link ComplexSparse} of nx1 dimensions, where n is 2 to the power of the number of qubits.
 * This state vector is used to determine the system state after applying quantum gates.
 *
 * @author Robert Smith
 * @version 0.3
 * @since 6 August 2024
 */
public class StateTracker {
    private ComplexSparse stateVector;

    /**
     * Default constructor initializes the system state with one qubit set to |0⟩ state (1+0i).
     */
    public StateTracker() {
        stateVector = new ComplexSparse(2, 1);
        stateVector.put(0, 0, new ComplexNumber(1, 0));
        stateVector.put(1, 0, new ComplexNumber(0, 0));
    }

    /**
     * Parameterized constructor initializes the system state with the specified number of qubits,
     * setting all qubits to the |0⟩ state.
     *
     * @param numQubits the number of qubits to set up the system with
     */
    public StateTracker(int numQubits) {
        stateVector = new ComplexSparse((int) Math.pow(2, numQubits), 1);
        stateVector.put(0, 0, new ComplexNumber(1, 0));
        for (int i = 1; i < Math.pow(2, numQubits); i++) {
            stateVector.put(i, 0, new ComplexNumber(0, 0));
        }
    }

    /**
     * Constructor that initializes the state tracker with a given state vector.
     *
     * @param matrix the {@link ComplexSparse} matrix representing the initial state vector
     */
    public StateTracker(ComplexSparse matrix) {
        this.stateVector = matrix;
    }

    /**
     * Creates and returns a deep copy of the current StateTracker.
     *
     * @return a new StateTracker instance with the same state as the current one
     */
    public StateTracker makeClone() {
        ComplexSparse copy = new ComplexSparse(this.getStateVecSize(), 1);
        for (int i = 0; i < this.getStateVecSize(); i++) {
            ComplexNumber rowCopy = new ComplexNumber(this.get(i, 0).getReal(), this.get(i, 0).getImag());
            copy.put(i, 0, rowCopy);
        }
        return new StateTracker(copy);
    }

    /**
     * Retrieves the current state vector as a column vector in a {@link ComplexSparse} of n x 1,
     * where n is 2 to the power of N, and N is the number of qubits in the system.
     *
     * @return the current state vector as a {@link ComplexSparse} in column vector format
     */
    public ComplexSparse getStateVec() {
        return stateVector;
    }

    /**
     * Returns the size of the state vector, which is 2^n where n is the number of qubits.
     *
     * @return the number of rows in the state vector
     */
    public int getStateVecSize() {
        return stateVector.getHeight();
    }

    /**
     * Sets a new state vector for the quantum system.
     *
     * @param newState the new column vector to assign to the system state
     * @return true if setting the new state vector was successful, false otherwise
     */
    public boolean setStateVec(ComplexSparse newState) {
        try {
            stateVector.setData(newState);
            return true;
        } catch (Exception e) {
            System.out.println("Setting new state failed.");
            return false;
        }
    }

    /**
     * Calculates and returns the number of qubits in the system based on the size of the state vector.
     *
     * @return the number of qubits in the quantum system
     */
    public int getQubitCount() {
        return (int) (Math.log(stateVector.getHeight()) / Math.log(2));
    }

    /**
     * Converts the quantum state vector to individual qubit probabilities.
     *
     * <p>This method calculates the probability of each qubit being in the |0⟩ or |1⟩ state,
     * handling superposition states and complex amplitudes.</p>
     *
     * @return A ComplexSparse of size (numQubits x 2) containing the probabilities for each qubit's |0⟩ and |1⟩ states
     * @throws IllegalArgumentException if the state vector dimensions are invalid
     */
    public ComplexSparse stateVectorToQubits() {
        int rows = stateVector.getHeight();
        int cols = stateVector.getWidth();

        if (rows < 1 || cols != 1) {
            throw new IllegalArgumentException("Incorrect format for state vector. It should be Math.pow(2, numQubits) x 1");
        }

        int numQubits = (int) (Math.log(rows) / Math.log(2));
        ComplexSparse qubitStates = new ComplexSparse(numQubits, 2);

        for (int i = 0; i < numQubits; i++) {
            ComplexNumber zeroState = new ComplexNumber(0, 0);
            ComplexNumber oneState = new ComplexNumber(0, 0);

            for (int j = 0; j < rows; j++) {
                if ((j & (1 << i)) == 0) {
                    qubitStates.put(i, 0, new ComplexNumber(stateVector.get(j, 0).getReal(), stateVector.get(j, 0).getImag()));
                } else {
                    qubitStates.put(i, 1, new ComplexNumber(stateVector.get(j, 0).getReal(), stateVector.get(j, 0).getImag()));
                }
            }
            qubitStates.put(i, 0, zeroState);
            qubitStates.put(i, 1, oneState);
        }
        return qubitStates;
    }

    /**
     * Retrieves the ComplexNumber at the specified position in the state vector.
     *
     * @param row the row index (0-based) in the state vector
     * @param column the column index (usually 0 for state vectors)
     * @return the ComplexNumber at the specified position
     */
    public ComplexNumber get(int row, int column) {
        return stateVector.get(row, column);
    }
}