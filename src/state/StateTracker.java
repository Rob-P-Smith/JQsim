package state;

import complex_classes.ComplexMatrix;
import complex_classes.ComplexNumber;
import complex_classes.ComplexQubit;

/**
 * The class tracks the state of the quantum system {@link ComplexQubit}s as a column vector stored in a
 * {@link ComplexMatrix} of nx1 dimensions where n is 2 to the power of the number of {@link ComplexQubit}s.
 * This is used to provide the state vector to multiply by gate matrix operators to determine the system state
 * after applying gates.
 *
 * @author Robert Smith
 * @version 0.1
 * @since 4 July 2024
 */
public class StateTracker {
    private ComplexMatrix stateVector = new ComplexMatrix(0, 0);

    /**
     * Default constructor initializes the system state using one qubit set to a 0 real and 0 imag value.
     */
    public StateTracker() {
        stateVector = new ComplexMatrix(1, 1);
        stateVector.set(0, 0, new ComplexNumber(0, 0));
    }

    /**
     * Parameterized constructor initializes the system state using the number of qubits provided as the
     * parameter and sets all qubits to 0 real and 0 imag values.
     *
     * @param numQubits the number of qubits to set up the system with
     */
    public StateTracker(int numQubits) {
        stateVector = new ComplexMatrix((int) Math.pow(2, numQubits), 1);
        stateVector.set(0, 0, new ComplexNumber(1, 0));
        for (int i = 1; i < numQubits; i++) {
            stateVector.set(i, 0, new ComplexNumber(0, 0));
        }
    }

    /**
     * Getter for the current stateVector as a column vector in a {@link ComplexMatrix}  of n,1 where n is
     * 2 to the power of N, where is N is the number of {@link ComplexQubit} in the system.
     * <p>
     * Example:
     * 2 {@link ComplexQubit} as N means the state vector will be 2 to the 2nd power, resulting in a 4x1
     * {@link ComplexMatrix}state vector.
     * </p>
     * <p>
     * 3 {@link ComplexQubit} as N means the state vector will be 2 to the 3rd power, resulting in a 8x1
     * {@link ComplexMatrix} state vector.
     * </p>
     *
     * @return the current stateVector as a {@link ComplexMatrix} in column vector format.
     */
    public ComplexMatrix getStateVec() {
        return stateVector;
    }

    public int getStateVecSize() {
        return stateVector.getHeight();
    }

    /**
     * Setter for the state vector.
     *
     * @param newState the new column vector to assign to the system state
     * @return The boolean true if setting the new stateVector was successful or false if it fails.
     */
    public boolean setStateVec(ComplexMatrix newState) {
        try {
            stateVector.setData(newState.getData());
            return true;
        } catch (Exception e) {
            System.out.println("Setting new state failed.");
            return false;
        }
    }

    /**
     * Converts a quantum state vector represented as a ComplexMatrix to individual qubit probabilities.
     *
     * <p>This method uses the class's state vector represented as a ComplexMatrix (which is composed of and calculates\
     * the probability of each qubit being in the |0⟩ or |1⟩ state. It can handle superposition states and complex
     * amplitudes.</p>
     *
     * <p>The returned ComplexMatrix contains the probabilities for each qubit's |0⟩ and |1⟩ states.
     * The probabilities are stored in the real part of the ComplexNumber objects, as
     * probabilities are always real numbers.</p>
     *
     * @return A ComplexMatrix as Math.pow(2, numQubits) x 1 of ComplexNumber objects. The first dimension represents
     * the qubits (indexed from 0 to n-1), and the second dimension represents the |0⟩ and |1⟩ states (indexed 0 and 1
     * respectively). The real part of each ComplexNumber contains the probability of the qubit being in that state.
     * @throws IllegalArgumentException If the dimensions of the stateVector are not Nx1, or if N is not a power of 2.
     */
    public ComplexMatrix stateVectorToQubits() {
        int rows = stateVector.getHeight();
        int cols = stateVector.getWidth();

        if (rows < 1 || cols != 1) {
            throw new IllegalArgumentException("Incorrect format for state vector. It should be Math.pow(2, numQubits) x 1");
        }

        int numQubits = (int) (Math.log(rows) / Math.log(2));
        ComplexMatrix qubitStates = new ComplexMatrix(numQubits, 2);

        for (int i = 0; i < numQubits; i++) {
            ComplexNumber zeroState = new ComplexNumber(0, 0);
            ComplexNumber oneState = new ComplexNumber(0, 0);

            for (int j = 0; j < rows; j++) {
                if ((j & (1 << i)) == 0) {
                    qubitStates.set(i,j,new ComplexNumber(stateVector.get(i,j).getReal(), stateVector.get(i,j).getImag()));
                } else {
                    qubitStates.set(i,j,new ComplexNumber(stateVector.get(i,j).getReal(), stateVector.get(i,j).getImag()));
                }
            }

            qubitStates.set(i, 0, zeroState);
            qubitStates.set(i, 1, oneState);
        }

        return qubitStates;
    }
}
