package complexClasses;

import java.util.HashMap;
import java.util.Map;

//TODO clean up obsolete entangled qubit hashmap if it ends up being superfluous
/**
 * This class represents a single qubit. It uses ComplexNumbers so that 'i' can be used for qubit alpha and beta values
 * to track probabilities.
 *
 * @author Robert Smith
 * @version 0.1
 * @since 25 June 2024
 */
public class ComplexQubit extends ComplexObject{
    public static final double ERROR_MARGIN = 0.00001;
    private double phase = 0.0;
    private static int numQubits = 0;
    private int qubitID;
    private ComplexMatrix state;
//    private Map<ComplexQubit, ComplexQubit> entangledQubits;//TODO This can probably go away, marked for future removal

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

    //TODO This can probably go away, marked for future removal
//    /**
//     * Add the provided parameter qubit to this qubit's entangledQubits HashMap and then add this qubit to the parameter
//     * qubit's entangledQubits HashMap, so they remain synchronized as entangled with each other.
//     *
//     * @param otherQubit A valid Qubit to entangle with.
//     */
//    public void setEntangledQubit(ComplexQubit otherQubit) {
//        // Add the other qubit to this qubit's list, and this qubit to the other qubit's list
//        if (!entangledQubits.containsKey(otherQubit)) {
//            entangledQubits.put(otherQubit, otherQubit);
//            otherQubit.entangledQubits.put(this, this);
//        }
//    }

    //TODO This can probably go away, marked for future removal
//    /**
//     * Getter for the current entanglement map of entangled qubits with this qubit
//     *
//     * @return map of all qubit entangled with this qubit
//     */
//    public Map<ComplexQubit, ComplexQubit> getEntangledQubit() {
//        return entangledQubits;
//    }

    public ComplexQubit getZeroQubit(){
        ComplexQubit zeroQubit = new ComplexQubit();
        zeroQubit.setState(new ComplexMatrix(new ComplexNumber[][]{{new ComplexNumber(1)}, {new ComplexNumber()}}));
        return zeroQubit;
    }

    public ComplexQubit getOneQubit(){
        ComplexQubit zeroQubit = new ComplexQubit();
        zeroQubit.setState(new ComplexMatrix(new ComplexNumber[][]{{new ComplexNumber()}, {new ComplexNumber(1)}}));
        return zeroQubit;
    }

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
        sBuild.append("Qubit #"+this.qubitID+"\n");
        sBuild.append('α').append("\n");
        sBuild.append('[').append(state.get(0, 0)).append("]\n");
        sBuild.append('β').append("\n");
        sBuild.append('[').append(state.get(1, 0)).append("]");
        //TODO This can probably go away, marked for future removal
//        if (entangledQubits != null) {
//            if (entangledQubits.size() == 1) {
//                sBuild.append("\nQubit #").append(this.qubitID).append(" entangled with Qubit #");
//                for (ComplexQubit value : entangledQubits.values()) {
//                    sBuild.append(value.getQubitID());
//                }
//            } else if (entangledQubits.size() > 1) {
//                sBuild.append("\nQubit #").append(this.qubitID).append(" entangled with Qubit #s: ");
//                ComplexQubit[] values = entangledQubits.values().toArray(new ComplexQubit[0]);
//                for (int i = 0; i < values.length; i++) {
//                    if (i != entangledQubits.size() - 1) {
//                        sBuild.append(values[i].getQubitID()).append(", ");
//                    } else {
//                        sBuild.append(values[i].getQubitID());
//                    }
//                }
//            }
//        }
        return sBuild.toString();
    }
}
