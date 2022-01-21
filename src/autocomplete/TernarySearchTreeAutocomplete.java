package autocomplete;

import java.util.*;

/**
 * Ternary search tree (TST) implementation of the {@link Autocomplete} interface.
 *
 * @see Autocomplete
 */
public class TernarySearchTreeAutocomplete implements Autocomplete {
    /**
     * The overall root of the tree: the first character of the first autocompletion term added to this tree.
     */
    private Node overallRoot;
    /**
     * Constructs an empty instance.
     */
    public TernarySearchTreeAutocomplete() {
        overallRoot = null;
    }


    /**
     * Returns the value associated with the given key.
     * @param key the key
     * @return the value associated with the given key if the key is in the symbol table
     *     and {@code null} if the key is not in the symbol table
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public Boolean get(CharSequence key) {
        if (key == null) {
            throw new IllegalArgumentException("calls get() with null argument");
        }
        if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
        Node x = get(overallRoot, key, 0);
        if (x == null) return null;
        return x.isTerm;
    }

    // return subtrie corresponding to given key
    // T(N) = 1 + 1 + 1 + T(N/3)
    // Worst case - N
    private Node get(Node x, CharSequence key, int d) {
        if (x == null) return null;
        if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
        char c = key.charAt(d);
        if      (c < x.data)              return get(x.left,  key, d);
        else if (c > x.data)              return get(x.right, key, d);
        else if (d < key.length() - 1) return get(x.mid,   key, d+1);
        else                           return x;
    }

    /**
     * Does this symbol table contain the given key?
     * @param key the key
     * @return {@code true} if this symbol table contains {@code key} and
     *     {@code false} otherwise
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public boolean contains(CharSequence key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to contains() is null");
        }
        //return get(key) != true;
        // Need to check if is term
        return get(key) != null && get(key) == true;
        //Node x = get(overallRoot, key, 0);
        //return x != null && x.isTerm;
    }

    /**
     * Inserts the key-value pair into the symbol table, overwriting the old value
     * with the new value if the key is already in the symbol table.
     * If the value is {@code null}, this effectively deletes the key from the symbol table.
     * @param key the key
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void put(CharSequence key) {
        if (key == null) {
            throw new IllegalArgumentException("calls put() with null key");
        }
        if (!contains(key)) {
            overallRoot = put(overallRoot, key, 0);
        }
    }

    // put helper
    private Node put(Node x, CharSequence key, int d) {
        char c = key.charAt(d);
        if (x == null) {
            x = new Node(c);
        }
        if      (c < x.data)               x.left  = put(x.left,  key, d);
        else if (c > x.data)               x.right = put(x.right, key, d);
        else if (d < key.length() - 1)  x.mid   = put(x.mid,   key, d+1);
        else                            x.isTerm   = true;
        return x;
    }


    /**
     * Returns all of the keys in the set that start with {@code prefix}.
     * @param prefix the prefix
     * @return all of the keys in the set that start with {@code prefix},
     *     as an iterable
     * @throws IllegalArgumentException if {@code prefix} is {@code null}
     */
    public List<CharSequence> keysWithPrefix(CharSequence prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException("calls keysWithPrefix() with null argument");
        } else if (prefix.length() == 0) {
            throw new IllegalArgumentException("prefix must have length >= 1");
        }
        List<CharSequence> list = new LinkedList<CharSequence>();
        Node x = get(overallRoot, prefix, 0);
        if (x == null) return list;
        if (x.isTerm) list.add(prefix);
        collect(x.mid, new StringBuilder(prefix), list);
        return list;
    }
    // T(N) = 3T(N/3) + 1 --> theta logN
    // all keys in subtrie rooted at x with given prefix
    private void collect(Node x, StringBuilder prefix, List<CharSequence> list) {
        if (x == null) return;
        collect(x.left,  prefix, list);
        if (x.isTerm) list.add(prefix.toString() + x.data);
        prefix.append(x.data);
        collect(x.mid, prefix, list);
        //remove the char at the root after done traversing through mid to go right
        prefix.deleteCharAt(prefix.length() - 1);
        collect(x.right, prefix, list);
    }

    @Override
    /**
     * N * logN
     *
     */
    public void addAll(Collection<? extends CharSequence> terms) {
        for (CharSequence term : terms) {
            put(term);
        }
    }

    @Override
    /**
     * Worst case theta(N + logN)
     */
    public List<CharSequence> allMatches(CharSequence prefix) {
        return keysWithPrefix(prefix);
    }

    /**
     * A search tree node representing a single character in an autocompletion term.
     */
    private static class Node {
        private final char data;
        private boolean isTerm;
        private Node left;
        private Node mid;
        private Node right;

        public Node(char data) {
            this.data = data;
            this.isTerm = false;
            this.left = null;
            this.mid = null;
            this.right = null;
        }
    }
}



