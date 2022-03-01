package minpq;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;

/**
 * My part for the presentation
 */

/**
 * {@link PriorityQueue} implementation of the {@link ExtrinsicMinPQ} interface.
 *
 * @param <T> the type of elements in this priority queue.
 * @see ExtrinsicMinPQ
 */
public class HeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    /**
     * {@link PriorityQueue} storing {@link PriorityNode} objects representing each item-priority pair.
     */
    private final PriorityQueue<PriorityNode<T>> pq;

    /**
     * Constructs an empty instance.
     */
    public HeapMinPQ() {
        pq = new PriorityQueue<>(Comparator.comparingDouble(PriorityNode::priority));
    }

    @Override
    public void add(T item, double priority) {
        if (contains(item)) {
            throw new IllegalArgumentException("Already contains " + item);
        }
        pq.add(new PriorityNode<>(item, priority));
    }

    @Override
    public boolean contains(T item) {
        return pq.contains(new PriorityNode<>(item, 0));
    }

    @Override
    public T peekMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("PQ is empty");
        }
        return pq.peek().item();
    }

    /**
     * O(logN) https://docs.oracle.com/javase/7/docs/api/java/util/PriorityQueue.html
     *
     * @return The item of the minimum priority PriorityNode
     */
    @Override
    public T removeMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("PQ is empty");
        }
        return pq.poll().item();
    }

    /**
     * O(N) https://docs.oracle.com/javase/7/docs/api/java/util/PriorityQueue.html
     * remove(Object) takes O(N)
     * add() takes O(logN)
     * Use highest term
     *
     * @param item     the element whose associated priority value should be modified.
     * @param priority the updated priority value.
     */
    @Override
    public void changePriority(T item, double priority) {
        if (!contains(item)) {
            throw new NoSuchElementException("PQ does not contain " + item);
        }
        pq.remove(new PriorityNode<>(item, 0)); // does not use priority
        pq.add(new PriorityNode<>(item, priority));
    }

    @Override
    public int size() {
        return pq.size();
    }
}
