package com.tinkerpop.pipes.pgm;

import com.tinkerpop.blueprints.pgm.Element;
import com.tinkerpop.pipes.filter.AbstractComparisonFilterPipe;
import com.tinkerpop.pipes.filter.ComparisonFilterPipe;

import java.util.Collection;
import java.util.NoSuchElementException;

/**
 * An IdCollectionFilterPipe will take a collection of object ids and a Filter.NOT_EQUAL or Filter.EQUAL argument.
 * If an incoming object's id is contained (or not contained) in the provided collection, then it is emitted (or not emitted).
 *
 * @author Darrick Wiebe (ofallpossibleworlds.wordpress.com)
 */
public class IdCollectionFilterPipe extends AbstractComparisonFilterPipe<Element, Object> {

    private final Collection idCollection;
    private final ComparisonFilterPipe.Filter filter;

    public IdCollectionFilterPipe(final Collection idCollection, final ComparisonFilterPipe.Filter filter) {
        super(filter);
        this.idCollection = idCollection;
        if (filter == ComparisonFilterPipe.Filter.NOT_EQUAL || filter == ComparisonFilterPipe.Filter.EQUAL) {
            this.filter = filter;
        } else {
            throw new IllegalArgumentException("The only legal filters are equals and not equals");
        }
    }


    public boolean compareObjects(Object ignored, Object elementId) {
        if (this.idCollection.contains(elementId))
            return this.filter == ComparisonFilterPipe.Filter.NOT_EQUAL;
        return this.filter == ComparisonFilterPipe.Filter.EQUAL;
    }


    protected Element processNextStart() {
        while (true) {
            Element element = this.starts.next();
            if (this.compareObjects(null, element.getId())) {
                return element;
            }
        }
    }
}
