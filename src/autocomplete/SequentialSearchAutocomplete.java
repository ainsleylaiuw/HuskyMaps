package autocomplete;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Sequential search implementation of the {@link Autocomplete} interface.
 *
 * @see Autocomplete
 */
public class SequentialSearchAutocomplete implements Autocomplete {
    /**
     * {@link List} of added autocompletion terms.
     */
    private final List<CharSequence> terms;

    /**
     * Constructs an empty instance.
     */
    public SequentialSearchAutocomplete() {
        this.terms = new ArrayList<>();
    }

    @Override
    /**
     * Adds 'terms' parameter
     * https://www.baeldung.com/java-collections-complexity
     * ArrayList addAll - O(N)
     */
    public void addAll(Collection<? extends CharSequence> terms) {
        this.terms.addAll(terms);
    }

    @Override
    /**
     * Adds 'terms' parameter
     * https://www.baeldung.com/java-collections-complexity
     * ArrayList addAll - O(N)
     */
    public List<CharSequence> allMatches(CharSequence prefix) {
        List<CharSequence> matches = new ArrayList<>();
        for (CharSequence term : this.terms) {
            if (term.length() >= prefix.length()
                    && CharSequence.compare(term.subSequence(0, prefix.length()), prefix) == 0) {
                matches.add(term);
            }
        }
        return matches;
    }
}
