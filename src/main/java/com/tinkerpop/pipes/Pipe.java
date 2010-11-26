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
     *
     * If you wish to do normal iteration but also use the getPath method, you
     * will need to call this method before iteration.
     *
     * When implementing getPaths you must call this method on the pipe(s) that
     * feed into your pipe. The return in that case may be discarded.
     *
     * Note that although under most conditions this method will not cause the
     * pipe to use substantially more memory than normal, using it with (for
     * example) a CopySplitPipe together with an ExhaustiveMergePipe will cause
     * it to store all iterated paths leading up to the split pipe in memory
     * before releasing those objects as it emits the results of the last split
     * branch.
     *
     * @return an iterable of lists that represent each object traversed
     * including the original source element, the result of the current pipe.
     */
    public Iterable<List> getPaths();

    /**
     * Returns the path traversed to arrive at the current result of
     * the pipe.
     *
     * This method will raise an UnsupportedOperationException exception if
     * getPaths has not been called before iteration has begun.
     *
     * @return an ArrayList of all of the objects of various types
     *         traversed for the current iterator position of the pipe.
     */
    public List getPath();
}
