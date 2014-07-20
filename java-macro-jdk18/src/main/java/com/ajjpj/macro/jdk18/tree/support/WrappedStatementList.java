package com.ajjpj.macro.jdk18.tree.support;

import com.ajjpj.macro.tree.MStatementTree;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.List;

import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;


/**
 * @author arno
 */
public class WrappedStatementList implements java.util.List<MStatementTree> {
    private final JCTree.JCCompilationUnit compilationUnit;
    private List<JCTree.JCStatement> inner;

    public WrappedStatementList (JCTree.JCCompilationUnit compilationUnit, List<JCTree.JCStatement> inner) {
        this.compilationUnit = compilationUnit;
        this.inner = inner;
    }

    public List<JCTree.JCStatement> getInner () {
        return inner;
    }

    @Override public int size () {
        return inner.size ();
    }

    @Override public boolean isEmpty () {
        return inner.isEmpty ();
    }

    @Override public boolean contains (Object o) {
        return o instanceof MStatementTree && inner.contains (((MStatementTree) o).getInternalRepresentation ());
    }

    @Override public Iterator<MStatementTree> iterator () {
        return new Iterator<MStatementTree> () {
            final Iterator<JCTree.JCStatement> innerIter = inner.iterator ();

            @Override public boolean hasNext () {
                return innerIter.hasNext ();
            }

            @Override public MStatementTree next () {
                return WrapperFactory.wrap (compilationUnit, innerIter.next ());
            }
        };
    }

    @Override public Object[] toArray () {
        throw new UnsupportedOperationException ();
    }

    @Override public <T> T[] toArray (T[] a) {
        throw new UnsupportedOperationException ();
    }

    @Override public boolean add (MStatementTree MStatementTree) {
        return inner.add ((JCTree.JCStatement) MStatementTree.getInternalRepresentation ());
    }

    @Override public boolean remove (Object o) {
        return o instanceof MStatementTree && inner.remove (((MStatementTree) o).getInternalRepresentation ());
    }

    @Override public boolean containsAll (Collection<?> c) {
        for (Object o : c) {
            if (!contains (o)) {
                return false;
            }
        }
        return true;
    }

    @Override public boolean addAll (Collection<? extends MStatementTree> c) {
        boolean result = false;
        for (MStatementTree o : c) {
            result = add (o) || result;
        }
        return result;
    }

    @Override public boolean addAll (int index, Collection<? extends MStatementTree> c) {
        throw new UnsupportedOperationException ();
    }

    @Override public boolean removeAll (Collection<?> c) {
        boolean result = false;
        for (Object o : c) {
            result = remove (o) || result;
        }
        return result;
    }

    @Override public boolean retainAll (Collection<?> c) {
        throw new UnsupportedOperationException ();
    }

    @Override public void clear () {
        inner = List.nil ();
    }

    @Override public MStatementTree get (int index) {
        return WrapperFactory.wrap (compilationUnit, inner.get (index));
    }

    @Override public MStatementTree set (int index, MStatementTree element) {
        throw new UnsupportedOperationException ();
    }

    @Override public void add (int index, MStatementTree element) {
        throw new UnsupportedOperationException ();
    }

    @Override public MStatementTree remove (int index) {
        return WrapperFactory.wrap (compilationUnit, inner.remove (index));
    }

    @Override public int indexOf (Object o) {
        if (o instanceof MStatementTree) {
            return inner.indexOf (((MStatementTree) o).getInternalRepresentation ());
        }

        return -1;
    }

    @Override public int lastIndexOf (Object o) {
        if (o instanceof MStatementTree) {
            return inner.lastIndexOf (((MStatementTree) o).getInternalRepresentation ());
        }

        return -1;
    }

    @Override public ListIterator<MStatementTree> listIterator () {
        throw new UnsupportedOperationException ();
    }

    @Override public ListIterator<MStatementTree> listIterator (int index) {
        throw new UnsupportedOperationException ();
    }

    @Override public List<MStatementTree> subList (int fromIndex, int toIndex) {
        throw new UnsupportedOperationException ();
    }
}
