package obsolete_Classes;

import complex_classes.ComplexQubit;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
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
    private List<String[]> workQueueLog = new ArrayList<>();

    /**
     * Constructs a qubit wire with a specified wire number to match the qubit it will apply to.
     *
     * @param wireNumber The number assigned to this qubit wire.
     */
    public QubitWire(int wireNumber) {
        this.wireNumber = wireNumber;
    }

    /**
     * Get the wire number for the current wire
     * @return wireNumber as integer
     */
    public int getWireNumber() {
        return wireNumber;
    }

    /**
     * Retrieves the next gate from the queue, inserts in into the work log, and returns the next gate as a string[]
     * @return a String[] array with element 0 as the gate type, element 1 is 0 for Pauli and Hadamard gates. CNOT
     * has element 0 as CNOT, element 1 as control qubit, and element 2 as target qubit.
     */
    public String[] getNextGate(ComplexQubit activeQubit){
        String[] gateExecuted = new String[workQueue.peek().length+1];
        gateExecuted[0] = "Gate Applied: "+workQueue.peek()[0];
        if(gateExecuted[0].equals("CNOT")){
            gateExecuted[1] = "Control Qubit #"+workQueue.peek()[1];
            gateExecuted[2] = "Target Qubit #"+workQueue.peek()[2];
        }
        gateExecuted[gateExecuted.length-1] = "On Qubit #"+String.valueOf(activeQubit.getQubitID());
        workQueueLog.add(gateExecuted);
        return workQueue.poll();
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

    public List<String[]> getWorkLog(){
        return workQueueLog;
    }

    /**
     * Displays the content of the qubit wire, showing each quantum gate and its parameters.
     * Gates with two elements are displayed as single-qubit gates,
     * and gates with three elements are displayed as controlled gates.
     */
    public void viewLine() {
        System.out.println("QubitWire #" + this.wireNumber + ":");
        for (String[] gate : workQueue) {
            if (gate.length == 2 && gate[1].equals("0")) {
                System.out.print(gate[0] + " -> ");
            } else if (gate.length == 3) {
                System.out.print(gate[0] + " {control: " + gate[1] + ", target: " + gate[2] + "} -> ");
            }
        }
    }
}
