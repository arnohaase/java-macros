package com.ajjpj.macro.jdk18.tree.support;

import com.ajjpj.macro.tree.MExpressionTree;
import com.ajjpj.macro.tree.MStatementTree;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.List;

import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;


/**
 * @author arno
 */
public class WrappedExpressionList implements java.util.List<MExpressionTree> {
    private final JCTree.JCCompilationUnit compilationUnit;
    private List<JCTree.JCExpression> inner;

    public WrappedExpressionList (JCTree.JCCompilationUnit compilationUnit, List<JCTree.JCExpression> inner) {
        this.compilationUnit = compilationUnit;
        this.inner = inner;
    }

    public List<JCTree.JCExpression> getInner () {
        return inner;
    }

    @Override public int size () {
        return inner.size ();
    }

    @Override public boolean isEmpty () {
        return inner.isEmpty ();
    }

    @Override public boolean contains (Object o) {
        return o instanceof MExpressionTree && inner.contains (((MExpressionTree) o).getInternalRepresentation ());
    }

    @Override public Iterator<MExpressionTree> iterator () {
        return new Iterator<MExpressionTree> () {
            final Iterator<JCTree.JCExpression> innerIter = inner.iterator ();

            @Override public boolean hasNext () {
                return innerIter.hasNext ();
            }

            @Override public MExpressionTree next () {
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

    @Override public boolean add (MExpressionTree mExpressionTree) {
        return inner.add ((JCTree.JCExpression) mExpressionTree.getInternalRepresentation ());
    }

    @Override public boolean remove (Object o) {
        return o instanceof MExpressionTree && inner.remove (((MExpressionTree) o).getInternalRepresentation ());
    }

    @Override public boolean containsAll (Collection<?> c) {
        for (Object o : c) {
            if (!contains (o)) {
                return false;
            }
        }
        return true;
    }

    @Override public boolean addAll (Collection<? extends MExpressionTree> c) {
        boolean result = false;
        for (MExpressionTree o : c) {
            result = add (o) || result;
        }
        return result;
    }

    @Override public boolean addAll (int index, Collection<? extends MExpressionTree> c) {
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

    @Override public MExpressionTree get (int index) {
        return WrapperFactory.wrap (compilationUnit, inner.get (index));
    }

    @Override public MExpressionTree set (int index, MExpressionTree element) {
        throw new UnsupportedOperationException ();
    }

    @Override public void add (int index, MExpressionTree element) {
        throw new UnsupportedOperationException ();
    }

    @Override public MExpressionTree remove (int index) {
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

    @Override public ListIterator<MExpressionTree> listIterator () {
        throw new UnsupportedOperationException ();
    }

    @Override public ListIterator<MExpressionTree> listIterator (int index) {
        throw new UnsupportedOperationException ();
    }

    @Override public List<MExpressionTree> subList (int fromIndex, int toIndex) {
        throw new UnsupportedOperationException ();
    }
}
