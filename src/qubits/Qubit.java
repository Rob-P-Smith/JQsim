package qubits;

import java.util.HashMap;
import java.util.Map;

public class Qubit {
    private final double ERROR_MARGIN = 0.00001;
    private Matrix state;
    private Map<Qubit, Qubit> entangledQubits;

    public Qubit() {
        // Initializing in the |0> state as a column vector
        this.state = new Matrix(new double[][]{{1.0}, {0.0}});
        this.entangledQubits = new HashMap<>();
    }

    public Qubit(double a, double b) {
        if (Math.abs((a * a) + (b * b)) - 1.0 < ERROR_MARGIN) {
            this.state = new Matrix(new double[][]{{a}, {b}});
        } else {
            throw new IllegalArgumentException("Violates Born's Rule for provided values");
        }
    }

    /**
     * Add the provided parameter qubit to this qubit's entangledQubits HashMap and then add this qubit to the parameter
     * qubit's entangledQubits HashMap, so they remain synchronized as entangled with each other.
     * @param otherQubit A valid Qubit to entangle with.
     */
    public void entangleQubit(Qubit otherQubit) {
        //add the qubit to this Qubit's list and this qubit to the other Qubit's list
        if(!entangledQubits.containsKey(otherQubit)){
            entangledQubits.put(this,otherQubit);
            otherQubit.entangleQubit(this);
        }
    }

    public Matrix getState() {
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
