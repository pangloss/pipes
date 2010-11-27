package com.tinkerpop.pipes;

import java.util.Iterator;
import java.util.List;

/**
 * The generic interface for any Pipe implementation.
 * A Pipe takes/consumes objects of type S and returns/emits objects of type E.
 * S refers to <i>starts</i> and the E refers to <i>ends</i>.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 * @author Darrick Wiebe (darrick@innatesoftware.com)
 */
public interface Pipe<S, E> extends Iterator<E>, Iterable<E> {

    /**
     * Set an iterator of S objects to the head (start) of the pipe.
     *
     * @param starts the iterator of incoming objects
     */
    public void setStarts(Iterator<S> starts);

    /**
     * Set an iterable of S objects to the head (start) of the pipe.
     *
     * @param starts the iterable of incoming objects
     */
    public void setStarts(Iterable<S> starts);

    /**
     * Returns an iterable object that lazily evaluates the path traversed for each
     * iteration of the result of the pipe.
     * <p/>
     * When implementing this method you must call this method against all
     * input pipes to ensure that the entire chain of pipes is correctly set up
     * to perform the additional caching sometimes required to calculate the
     * path.
     *
     * @return an iterable of lists that represent each object traversed
     * including the original source element, the result of the current pipe.
     */
    public Iterable<List> getPaths();

    /**
     * Returns the path traversed to arrive at the current result of
     * the pipe.
     * <p/>
     * This method will raise an UnsupportedOperationException exception if
     * getPaths() has not been called before iteration has begun.
     *
     * @return an ArrayList of all of the objects of various types traversed
     * for the current iterator position of the pipe.
     */
    public List getPathForCurrentEnd();
}
