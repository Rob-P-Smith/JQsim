package state;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Represents a queue of {@link WorkItem} objects with thread-safe operations.
 * This class provides methods to add and retrieve work items, as well as
 * an iterator that removes items as they are processed.
 */
public class WorkQueue implements Iterable<WorkItem> {
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
     * Returns the internal queue of {@link WorkItem} objects.
     * Note: This method does not provide thread-safe access to the queue.
     *
     * @return The {@link Queue} of {@link WorkItem} objects.
     */
    public Queue<WorkItem> getGates() {
        return gates;
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
     * Returns an iterator over the {@link WorkItem} objects in this queue.
     * The iterator removes items from the queue as they are processed.
     *
     * @return An {@link Iterator} that removes and returns {@link WorkItem} objects.
     * @see WorkQueueIterator
     */
    @Override
    public Iterator<WorkItem> iterator() {
        return new WorkQueueIterator();
    }

    /**
     * An iterator that removes and returns {@link WorkItem} objects from the queue.
     * This iterator is designed to prevent concurrent modification of the queue.
     */
    private class WorkQueueIterator implements Iterator<WorkItem> {
        /**
         * Checks if there are more {@link WorkItem} objects in the queue.
         *
         * @return true if the queue is not empty, false otherwise.
         */
        @Override
        public boolean hasNext() {
            lock.lock();
            try {
                return !gates.isEmpty();
            } finally {
                lock.unlock();
            }
        }

        /**
         * Removes and returns the next {@link WorkItem} from the queue.
         *
         * @return The next {@link WorkItem} in the queue.
         * @throws NoSuchElementException if the queue is empty.
         */
        @Override
        public WorkItem next() {
            lock.lock();
            try {
                if (gates.isEmpty()) {
                    throw new NoSuchElementException("No more elements in the WorkQueue");
                }
                return gates.poll();
            } finally {
                lock.unlock();
            }
        }
    }
}