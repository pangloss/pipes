package com.tinkerpop.pipes.filter;

import com.tinkerpop.pipes.AbstractPipe;

import java.util.Collection;

/**
 * A CollectionFilterPipe will take a collection of objects and a Filter.NOT_EQUAL or Filter.EQUAL argument.
 * If an incoming object is contained (or not contained) in the provided collection, then it is emitted (or not emitted).
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class CollectionFilterPipe<S> extends AbstractPipe<S, S> implements FilterPipe<S>, ComparisonFilterPipe<S, S> {

    private final Collection<S> storedCollection;
    private final ComparisonFilterPipe.Filter filter;

    public CollectionFilterPipe(final Collection<S> storedCollection, final ComparisonFilterPipe.Filter filter) {
        this.storedCollection = storedCollection;
        if (filter == ComparisonFilterPipe.Filter.NOT_EQUAL || filter == ComparisonFilterPipe.Filter.EQUAL) {
            this.filter = filter;
        } else {
            throw new IllegalArgumentException("The only legal filters are equals and not equals");
        }
    }


    public boolean compareObjects(S leftObject, S rightObject) {
        if (this.filter == ComparisonFilterPipe.Filter.NOT_EQUAL) {
            if (this.storedCollection.contains(rightObject))
                return true;
        } else {
            if (!this.storedCollection.contains(rightObject))
                return true;
        }
        return false;
    }


    protected S processNextStart() {
        while (true) {
            S s = this.starts.next();
            if (this.compareObjects(null, s)) {
                return s;
            }
        }
    }
}
