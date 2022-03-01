package minpq;

import java.util.*;

/**
 * Optimized binary heap implementation of the {@link ExtrinsicMinPQ} interface.
 *
 * @param <T> the type of elements in this priority queue.
 * @see ExtrinsicMinPQ
 */
public class OptimizedHeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    /**
     * {@link List} of {@link PriorityNode} objects representing the heap of item-priority pairs.
     */
    private final List<PriorityNode<T>> items;
    /**
     * {@link Map} of each item to its associated index in the {@code items} heap.
     */
    private final Map<T, Integer> itemToIndex;

    /**
     * Constructs an empty instance.
     */
    public OptimizedHeapMinPQ() {
        items = new ArrayList<>();
        itemToIndex = new HashMap<>();
        items.add(null);
    }

    @Override
    public void add(T item, double priority) {
        if (contains(item)) {
            throw new IllegalArgumentException("Already contains " + item);
        }
        PriorityNode node = new PriorityNode<>(item, priority);
        items.add(node);
        itemToIndex.put(item, size());
        swim(size());
    }

    @Override
    public boolean contains(T item) {
        return itemToIndex.containsKey(item);
    }

    @Override
    public T peekMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("PQ is empty");
        }
        return items.get(1).item();
    }

    @Override
    public T removeMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("PQ is empty");
        }
        T min = peekMin();
        swap(1, size());
        itemToIndex.remove(min);
        items.remove(size());
        sink(1);
        return min;
    }

    @Override
    public void changePriority(T item, double priority) {
        if (!contains(item)) {
            throw new NoSuchElementException("PQ does not contain " + item);
        }
        int index = itemToIndex.get(item);
        swap(1, index);
        removeMin();
        swim(index);
        add(item, priority);
    }

    @Override
    public int size() {
        return items.size()-1;
    }

    /** Returns the index of the given index's parent node. */
    private static int parent(int index) {
        return index / 2;
    }

    /** Returns the index of the given index's left child. */
    private static int left(int index) {
        return index * 2;
    }

    /** Returns the index of the given index's right child. */
    private static int right(int index) {
        return left(index) + 1;
    }

    /** Returns true if and only if the index is accessible. */
    private boolean accessible(int index) {
        return 1 <= index && index <= size();
    }

    /** Returns the index with the lower priority, or 0 if neither is accessible. */
    private int min(int index1, int index2) {
        if (!accessible(index1) && !accessible(index2)) {
            return 0;
        } else if (accessible(index1) && (!accessible(index2)
                || items.get(index1).compareTo(items.get(index2)) < 0)) {
            return index1;
        } else {
            return index2;
        }
    }

    /** Swap the nodes at the two indices. */
    private void swap(int index1, int index2) {
        PriorityNode<T> temp1 = items.get(index1);
        PriorityNode<T> temp2 = items.get(index2);
        items.set(index1, temp2);
        items.set(index2, temp1);
        itemToIndex.put(temp1.item(), index2);
        itemToIndex.put(temp2.item(), index1);
    }

    /** Bubbles up the node currently at the given index. */
    private void swim(int index) {
        int parent = parent(index);
        while (accessible(parent) && items.get(index).compareTo(items.get(parent)) < 0) {
            swap(index, parent);
            index = parent;
            parent = parent(index);
        }
    }

    /** Bubbles down the node currently at the given index. */
    private void sink(int index) {
        int child = min(left(index), right(index));
        while (accessible(child) && items.get(index).compareTo(items.get(child)) > 0) {
            swap(index, child);
            index = child;
            child = min(left(index), right(index));
        }
    }


}
