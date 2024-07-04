package state;

import complexClasses.ComplexMatrix;
import complexClasses.ComplexNumber;
import complexClasses.ComplexQubit;

/**
 * The class tracks the state of the quantum system {@link ComplexQubit}s as a column vector stored in a
 * {@link ComplexMatrix} of nx1 dimensions where n is 2 to the power of the number of {@link ComplexQubit}s.
 *
 * This is used to provide the state vector to multiply by gate matrix operators to determine the system state
 * after applying gates.
 *
 * @author Robert Smith
 * @version 0.1
 * @since 4 July 2024
 */
public class StateTracker {
    private static ComplexMatrix stateVector = new ComplexMatrix(0, 0);

    /**
     * Default constructor initializes the system state using one qubit set to a 0 real and 0 imag value.
     */
    public StateTracker() {
        stateVector = new ComplexMatrix(1, 1);
        stateVector.set(0, 0, new ComplexNumber(0,0));
    }

    /**
     * Parameterized constructor initializes the system state using the number of qubits provided as the
     * parameter and sets all qubits to 0 real and 0 imag values.
     * @param numQubits the number of qubits to setup the system with
     */
    public StateTracker(int numQubits){
        stateVector = new ComplexMatrix(numQubits, 1);
        for(int i = 0; i < numQubits; i++){
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
     *</p>
     * @return the current stateVector as a {@link ComplexMatrix} in column vector format.
     */
    public static ComplexMatrix getStateVec() {
        return stateVector;
    }

    /**
     * Setter for the state vector.
     *
     * @param newState the new column vector to assign to the system state
     * @return The boolean true if setting the new stateVector was successful or false if it fails.
     */
    public static boolean setStateVec(ComplexMatrix newState) {
        try {
            stateVector.setData(newState.getData());
            return true;
        } catch (Exception e) {
            System.out.println("Setting new state failed.");
            return false;
        }
    }
}
