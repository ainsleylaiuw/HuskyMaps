package autocomplete;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Binary search implementation of the {@link Autocomplete} interface.
 *
 * @see Autocomplete
 */
public class BinarySearchAutocomplete implements Autocomplete {
    /**
     * {@link List} of added autocompletion terms.
     */
    private final List<CharSequence> terms;

    /**
     * Constructs an empty instance.
     */
    public BinarySearchAutocomplete() {
        this.terms = new ArrayList<>();
    }

    @Override
    /**
     * Adds 'terms' parameter
     * https://www.baeldung.com/java-collections-complexity
     * ArrayList addAll - O(N)
     * Collections.sort - O(N*log(N))
     * https://stackoverflow.com/q/25492648
     */
    public void addAll(Collection<? extends CharSequence> terms) {
        this.terms.addAll(terms);
        Collections.sort(this.terms, CharSequence::compare);
    }

    @Override
    /**
     *
     */
    public List<CharSequence> allMatches(CharSequence prefix) {
        List<CharSequence> matches = new ArrayList<>();
        int i = Collections.binarySearch(terms, prefix, CharSequence::compare);
        /**if (i < 0) {
            // i < 0 when the prefix does not exactly match any of the terms
            i = terms.size() - 1;
        } else {

        }
        matches = terms.subList(i, terms.size());
        */
        while (CharSequence.compare(terms.get(i).subSequence(0, prefix.length()), prefix) == 0) {

            if (i + 1 < terms.size()) {
                matches.add(terms.get(i));
                i++;
            } else {
                break;
            }
        }
        return matches;
    }
}
