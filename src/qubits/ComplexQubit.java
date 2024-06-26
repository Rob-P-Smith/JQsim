package qubits;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a single qubit. It uses ComplexNumbers so that 'i' can be used for qubit alpha and beta values
 * to track probabilities.
 *
 * @author Robert Smith
 * @version 0.1
 * @since 25 June 2024
 */
public class ComplexQubit {
    public static final double ERROR_MARGIN = 0.00001;
    private static int numQubits = 0;
    private int qubitID;
    private ComplexMatrix state;
    private Map<ComplexQubit, ComplexQubit> entangledQubits;

    /**
     * Default constructor for the qubit class
     */
    public ComplexQubit() {
        // Initializing in the |0> state as a column vector of [1,0]
        this.state = new ComplexMatrix(new ComplexNumber[][]{{new ComplexNumber(1)}, {new ComplexNumber()}});
        this.qubitID = numQubits;
        numQubits++;
        this.entangledQubits = new HashMap<>();
    }

    /**
     * Parameterized constructor for the qubit class
     *
     * @param aVec the |0> vector
     * @param bVec the |1> vector
     */
    public ComplexQubit(ComplexNumber aVec, ComplexNumber bVec) {
        if (Math.abs(aVec.magnitudeSquared() + bVec.magnitudeSquared() - 1.0) < ERROR_MARGIN) {
            this.state = new ComplexMatrix(new ComplexNumber[][]{{aVec}, {bVec}});
            this.qubitID = numQubits;
            numQubits++;
        } else {
            throw new IllegalArgumentException("Violates Born's Rule for provided values");
        }
    }

    /**
     * Add the provided parameter qubit to this qubit's entangledQubits HashMap and then add this qubit to the parameter
     * qubit's entangledQubits HashMap, so they remain synchronized as entangled with each other.
     *
     * @param otherQubit A valid Qubit to entangle with.
     */
    public void setEntangledQubit(ComplexQubit otherQubit) {
        // Add the other qubit to this qubit's list, and this qubit to the other qubit's list
        if (!entangledQubits.containsKey(otherQubit)) {
            entangledQubits.put(otherQubit, otherQubit);
            otherQubit.entangledQubits.put(this, this);
        }
    }

    /**
     * Getter for the current entanglement map of entangled qubits with this qubit
     *
     * @return map of all qubit entangled with this qubit
     */
    public Map<ComplexQubit, ComplexQubit> getEntangledQubit() {
        return entangledQubits;
    }

    /**
     * Getter for the current state
     *
     * @return the state as a ComplexMatrix |0> as 'a' and |1> as 'B'
     */
    public ComplexMatrix getState() {
        return state;
    }

    /**
     * Getter for qubitID
     *
     * @return the Qubit unique ID as int
     */
    public int getQubitID() {
        return qubitID;
    }

    /**
     * Custom to string to display the current qubit and its entanglement map
     *
     * @return string
     */
    @Override
    public String toString() {
        StringBuilder sBuild = new StringBuilder();
        sBuild.append('α').append("\n");
        sBuild.append('[').append(state.get(0, 0)).append("]\n");
        sBuild.append('β').append("\n");
        sBuild.append('[').append(state.get(1, 0)).append("]");
        if (entangledQubits != null) {
            if (entangledQubits.size() == 1) {
                sBuild.append("\nQubit #").append(this.qubitID).append(" entangled with Qubit #");
                for (ComplexQubit value : entangledQubits.values()) {
                    sBuild.append(value.getQubitID());
                }
            } else if (entangledQubits.size() > 1) {
                sBuild.append("\nQubit #").append(this.qubitID).append(" entangled with Qubit #s: ");
                ComplexQubit[] values = entangledQubits.values().toArray(new ComplexQubit[0]);
                for (int i = 0; i < values.length; i++) {
                    if (i != entangledQubits.size() - 1) {
                        sBuild.append(values[i].getQubitID()).append(", ");
                    } else {
                        sBuild.append(values[i].getQubitID());
                    }
                }
            }
        }
        return sBuild.toString();
    }
}
