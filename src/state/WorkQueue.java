package state;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Represents a queue of {@link WorkItem} objects with thread-safe operations.
 * This class provides methods to add and retrieve work items in a thread-safe manner.
 *
 * @author Robert Smith
 * @version 0.1
 * @since 5 July 2024
 */
public class WorkQueue {
    private Queue<WorkItem> gates;
    private final Lock lock = new ReentrantLock();

    /**
     * Constructs an empty WorkQueue.
     */
    public WorkQueue() {
        gates = new LinkedList<>();
    }

    /**
     * Constructs a WorkQueue with an initial {@link WorkItem}.
     *
     * @param gate The initial {@link WorkItem} to be added to the queue.
     */
    public WorkQueue(WorkItem gate) {
        gates = new LinkedList<>();
        gates.add(gate);
    }

    /**
     * Constructor for copying the WorkQueue to a new WorkQueue
     * @param gates the list of gates to copy into the new WorkQueue
     */
    public WorkQueue(Queue<WorkItem> gates){
        this.gates = gates;
    }

    /**
     * Create and return a new WorkQueue that is a copy of the original WorkQueue
     * @return WorkQueue copy of this
     */
    public WorkQueue makeClone(){
        WorkQueue copy = new WorkQueue();
        Queue<WorkItem> workItems = this.getGates();
        for(WorkItem gate: workItems){
            if(gate.isSingleTarget()) {
                copy.addGate(gate.getOperator(), gate.getTarget());
            } else if (gate.isDualTarget()){
                copy.addGate(gate.getOperator(), gate.getControl(), gate.getTarget());
            }
            else {
                copy.addGate(gate.getOperator(), gate.getControls(), gate.getTargets());
            }
        }
        return copy;
    }

    /**
     * Returns a copy of the internal queue of {@link WorkItem} objects.
     * Note: This method provides a thread-safe copy of the queue.
     *
     * @return A new {@link Queue} containing the {@link WorkItem} objects.
     */
    public Queue<WorkItem> getGates() {
        lock.lock();
        try {
            return new LinkedList<>(gates);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Adds a new {@link WorkItem} to the queue in a thread-safe manner.
     *
     * @param gate The {@link WorkItem} to be added to the queue.
     * @see WorkItem
     */
    public void addGate(WorkItem gate) {
        lock.lock();
        try {
            gates.offer(gate);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Adds a new {@link WorkItem} to the queue in a thread-safe manner, for single qubit gates.
     *
     * @param type   The type of gate to use as a string.
     * @param target The target qubit for the gate.
     * @see WorkItem
     */
    public void addGate(String type, int target) {
        WorkItem gate = new WorkItem(type, target);
        lock.lock();
        try {
            gates.offer(gate);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Adds a new {@link WorkItem} to the queue in a thread-safe manner, for single control, single target gates.
     *
     * @param type    The type of gate to use as a string.
     * @param control The control qubit for the gate.
     * @param target  The target qubit for the gate.
     * @see WorkItem
     */
    public void addGate(String type, int control, int target) {
        WorkItem gate = new WorkItem(type, control, target);
        lock.lock();
        try {
            gates.offer(gate);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Adds a new {@link WorkItem} to the queue in a thread-safe manner, for single control, dual target gates.
     *
     * @param type    The type of gate to use as a string.
     * @param control The control qubit for the gate.
     * @param target  The first target qubit for the gate.
     * @param target2 The second qubit target for this gate.
     * @see WorkItem
     */
    public void addGate(String type, int control, int target, int target2) {
        WorkItem gate = new WorkItem(type, new Integer[]{control}, new Integer[]{target, target2});
        lock.lock();
        try {
            gates.offer(gate);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Adds a new {@link WorkItem} to the queue in a thread-safe manner, for multi control, multi or single target gate.
     *
     * @param type     The type of gate to use as a string.
     * @param controls The control array of qubits for the gate.
     * @param targets  The target array of qubits for the gate.
     * @see WorkItem
     */
    public void addGate(String type, Integer[] controls, Integer[] targets) {
        WorkItem gate = new WorkItem(type, controls, targets);
        lock.lock();
        try {
            gates.offer(gate);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Removes and returns the next {@link WorkItem} from the queue in a thread-safe manner.
     *
     * @return The next {@link WorkItem} in the queue, or null if the queue is empty.
     */
    public WorkItem getNextGate() {
        lock.lock();
        try {
            return gates.poll();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Checks if there are any {@link WorkItem} objects in the queue.
     *
     * @return true if the queue is not empty, false otherwise.
     */
    public boolean hasWork() {
        return gates.size() > 0;
    }

    /**
     * Provides peek functionality into the {@link WorkQueue}. It is primarily used for iterating through the work queue
     * while executing the work queue to ensure all work items are executed and execution stops when {@link WorkQueue}
     * is empty.
     * <br>
     * Example Use:
     * <p>
     * while(WorkItem.peek() !=null){
     * //business logic here
     * }
     * </p>
     *
     * @return a {@link WorkItem} copy of the next {@link WorkItem} in the {@link WorkQueue}. A copy is returned to
     * avoid user attempting to mutate the existing {@link WorkItem} in the {@link WorkQueue} directly.
     */
    public WorkItem peek() {
        return gates.peek();
    }

    @Override
    public String toString() {
        return "WorkQueue{" +
                "gates= " + gates +
                "}";
    }
}