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
     * @param gate The initial {@link WorkItem} to be added to the queue.
     */
    public WorkQueue(WorkItem gate) {
        gates = new LinkedList<>();
        gates.add(gate);
    }

    /**
     * Returns a copy of the internal queue of {@link WorkItem} objects.
     * Note: This method provides a thread-safe copy of the queue.
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
     * Removes and returns the next {@link WorkItem} from the queue in a thread-safe manner.
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
     * @return true if the queue is not empty, false otherwise.
     */
    public boolean hasWork() {
        return gates.size() > 0;
    }

    /**
     * Provides peek functionality into the {@link WorkQueue}. It is primarily used for iterating through the work queue
     * while executing the work queue to ensure all work items are executed and execution stops when {@link WorkQueue}
     * is empty.
     *<br>
     * Example Use:
     * <p>
 *     while(WorkItem.peek() !=null){
     *     //business logic here
     * }
     * </p>
     * @return a {@link WorkItem} copy of the next {@link WorkItem} in the {@link WorkQueue}. A copy is returned to
     * avoid user attempting to mutate the existing {@link WorkItem} in the {@link WorkQueue} directly.
     */
    public WorkItem peek() {
        WorkItem nextWork = gates.peek();
        return nextWork;
    }
}