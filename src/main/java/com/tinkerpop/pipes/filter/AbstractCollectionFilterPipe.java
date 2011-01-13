package com.tinkerpop.pipes.filter;

import com.tinkerpop.blueprints.pgm.Element;
import com.tinkerpop.pipes.filter.AbstractComparisonFilterPipe;
import com.tinkerpop.pipes.filter.ComparisonFilterPipe;

import java.util.Collection;
import java.util.HashMap;
import java.util.NoSuchElementException;

/**
 * An AbstractCollectionFilterPipe provides the functionality to compare an
 * incoming element against a collection of possible values with either a
 * Filter.NOT_EQUAL or Filter.EQUAL comparision.
 *
 * @author Darrick Wiebe (ofallpossibleworlds.wordpress.com)
 */
public abstract class AbstractCollectionFilterPipe<S, T> extends AbstractComparisonFilterPipe<S, T> {

    private final HashMap<T, Object> collection;
    private final ComparisonFilterPipe.Filter filter;

    public AbstractCollectionFilterPipe(final Collection<T> collection, final ComparisonFilterPipe.Filter filter) {
        super(filter);
        if (filter == ComparisonFilterPipe.Filter.NOT_EQUAL || filter == ComparisonFilterPipe.Filter.EQUAL) {
            this.filter = filter;
        } else {
            throw new IllegalArgumentException("The only legal filters are equals and not equals");
        }

        // TODO: is there a benefit to moving items into the right collection type? Is HashMap the right one?
        HashMap<T, Object> hs = new HashMap<T, Object>();
        for (T obj : collection) {
          hs.put(obj, null);
        }
        this.collection = hs;
    }


    public boolean compareToCollection(T object) {
        if (this.collection.containsKey(object))
            return this.filter == ComparisonFilterPipe.Filter.NOT_EQUAL;
        return this.filter == ComparisonFilterPipe.Filter.EQUAL;
    }
}
