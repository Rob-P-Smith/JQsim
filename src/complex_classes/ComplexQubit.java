package complex_classes;

//TODO clean up obsolete entangled qubit hashmap if it ends up being superfluous

import static supportClasses.GreekEnums.*;

/**
 * TODO Delete when confirmed obsolete.
 * This class represents a single qubit. It uses ComplexNumbers so that 'i' can be used for qubit alpha and beta values
 * to track probabilities.
 *
 * @author Robert Smith
 * @version 0.1
 * @since 25 June 2024
 */
public final class ComplexQubit {
    public static final double ERROR_MARGIN = 0.00001;
    private static int numQubits = 0;
    private int qubitID;
    private ComplexMatrix state;

    /**
     * Default constructor for the qubit class
     */
    public ComplexQubit() {
        // Initializing in the |0> state as a column vector of [1,0]
        this.state = new ComplexMatrix(new ComplexNumber[][]{{new ComplexNumber(1)}, {new ComplexNumber()}});
        this.qubitID = numQubits;
        numQubits++;
//        this.entangledQubits = new HashMap<>();
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
//            this.entangledQubits = new HashMap<>();
        } else {
            throw new IllegalArgumentException("Violates Born's Rule for provided values");
        }
    }

    /**
     * Parameterized constructor for the qubit class when loading from a file, but not used elsewhere
     *
     * @param aVec the |0> vector
     * @param bVec the |1> vector
     * @param qubitID the ID for this qubit specified when loading a file from the saved qubit record
     */
    public ComplexQubit(ComplexNumber aVec, ComplexNumber bVec, int qubitID) {
        if (Math.abs(aVec.magnitudeSquared() + bVec.magnitudeSquared() - 1.0) < ERROR_MARGIN) {
            this.state = new ComplexMatrix(new ComplexNumber[][]{{aVec}, {bVec}});
            this.qubitID = qubitID;
            numQubits = this.qubitID+1;
//            this.entangledQubits = new HashMap<>();
        } else {
            throw new IllegalArgumentException("Violates Born's Rule for provided values");
        }
    }

    /**
     * Setter for the state of the qubit
     * @param newState the new state of this
     */
    public void setState(ComplexMatrix newState) {
        this.state = newState;
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
     * Setter for qubitID
     *
     * @param qubitID the Qubit unique ID
     */
    public void setQubitID(int qubitID) {
        this.qubitID = qubitID;
    }

    /**
     * Custom to string to display the current qubit and its entanglement map
     *
     * @return string
     */
    @Override
    public String toString() {
        StringBuilder sBuild = new StringBuilder();
        sBuild.append("Qubit #").append(this.qubitID).append("\n");
        sBuild.append(ALPHA.lower()).append("\n");
        sBuild.append('[').append(state.get(0, 0)).append("]\n");
        sBuild.append(BETA.lower()).append("\n");
        sBuild.append('[').append(state.get(1, 0)).append("]");
        return sBuild.toString();
    }

    /**
     * Gets the real value of the complex number
     * @return double of the real value
     */
    public double getReal() {
        return state.get(0,0).getReal();
    }

    /**
     * Gets the imaginary value of the complex number
     * @return double of the imaginary value
     */
    public double getImag() {
        return state.get(0,0).getImag();
    }

    /**
     * Sets the qubit specified to the values provided as parameters.
     * @param row the row index in the state vector
     * @param real real component of the ComplexNumber
     * @param imag imaginary component of the ComplexNumber
     */
    public void setQubit(int row, double real, double imag) {
        state.set(row,0, new ComplexNumber(real, imag));
    }
}
