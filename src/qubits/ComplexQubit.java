package qubits;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a single qubit. It uses ComplexNumbers so that 'i' can be used for qubit alpha and beta values
 * to track probabilities.
 * @author Robert Smith
 * @version 0.1
 * @since 25 June 2024
 */
public class ComplexQubit {
    private final double ERROR_MARGIN = 0.00001;
    private ComplexMatrix state;
    private Map<ComplexQubit, ComplexQubit> entangledQubits;

    public ComplexQubit() {
        // Initializing in the |0> state as a column vector of [1,0]
        this.state = new ComplexMatrix(new ComplexNumber[][]{{new ComplexNumber(1)}, {new ComplexNumber()}});
        this.entangledQubits = new HashMap<>();
    }
    public ComplexQubit(ComplexNumber a, ComplexNumber b) {
        if (Math.abs(a.magnitudeSquared() + b.magnitudeSquared() - 1.0) < ERROR_MARGIN) {
            this.state = new ComplexMatrix(new ComplexNumber[][]{{a}, {b}});
        } else {
            throw new IllegalArgumentException("Violates Born's Rule for provided values");
        }
    }

    /**
     * Add the provided parameter qubit to this qubit's entangledQubits HashMap and then add this qubit to the parameter
     * qubit's entangledQubits HashMap, so they remain synchronized as entangled with each other.
     * @param otherQubit A valid Qubit to entangle with.
     */
    public void entangleQubit(ComplexQubit otherQubit) {
        // Add the other qubit to this qubit's list, and this qubit to the other qubit's list
        if (!entangledQubits.containsKey(otherQubit)) {
            entangledQubits.put(otherQubit, otherQubit);
            otherQubit.entangledQubits.put(this, this);
        }
    }

    public ComplexMatrix getState() {
        return state;
    }

    @Override
    public String toString() {
        char aa = '\u03B1';
        char bb = '\u03B2';
        StringBuilder sb = new StringBuilder();
        sb.append(aa).append("\n");
        sb.append('[').append(state.get(0,0)).append("]\n");
        sb.append(bb).append("\n");
        sb.append('[').append(state.get(1,0)).append("]\n");
        return sb.toString();
    }
}
