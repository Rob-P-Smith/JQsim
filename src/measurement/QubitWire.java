package measurement;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Represents a quantum qubit wire that stores quantum gates in a queue.
 *
 * @author Robert Smith
 * @version 0.1
 * @since 27 Jun 2024
 */
public class QubitWire {
    private int wireNumber;
    private Queue<String[]> workQueue = new LinkedList<>();

    /**
     * Constructs a qubit wire with a specified wire number to match the qubit it will apply to.
     *
     * @param wireNumber The number assigned to this qubit wire.
     */
    public QubitWire(int wireNumber) {
        this.wireNumber = wireNumber;
    }

    /**
     * Adds a quantum gate represented as a string array to the qubit wire's work queue.
     *
     * @param gate The quantum gate to be added, represented as a string array.
     */
    public void addGate(String[] gate) {
        workQueue.add(gate);
    }

    /**
     * Retrieves the work queue of quantum gates stored in this qubit wire.
     *
     * @return The work queue containing quantum gates.
     */
    public Queue<String[]> getWorkQueue() {
        return workQueue;
    }

    /**
     * Displays the content of the qubit wire, showing each quantum gate and its parameters.
     * Gates with two elements are displayed as single-qubit gates,
     * and gates with three elements are displayed as controlled gates.
     */
    public void viewLine() {
        System.out.println("QubitWire #" + this.wireNumber + ":");
        for (String[] gate : workQueue) {
            if (gate.length == 2 && "0".equals(gate[1])) {
                System.out.print(gate[0] + " -> ");
            } else if (gate.length == 3) {
                System.out.print(gate[0] + " {control: " + gate[1] + ", target: " + gate[2] + "} -> ");
            }
        }
    }
}
