package org.gra4j.trochilus;

public class PairHeap<E extends Comparable<? super E>> {

    private PairNode<E> root;

    private int size;

    private PairNode<E>[] treeArray = new PairNode[5];

    public interface Position<E> {
        E getValue();
    }

    private static class PairNode<E> implements Position<E> {

        public E element;
        public PairNode<E> leftChild;
        public PairNode<E> nextSibling;
        public PairNode<E> prev;

        public PairNode(E element) {
            this.element = element;
            leftChild = nextSibling = prev = null;
        }

        @Override
        public E getValue() {
            return element;
        }
    }

    private PairNode<E> compareAndLink (PairNode<E> first, PairNode<E> second) {
        if (second == null)
            return first;

        if (first == null)
            return second;

        if (second.element.compareTo(first.element) < 0) {
            second.prev = first.prev;
            first.prev = second;
            first.nextSibling = second.leftChild;
            if (first.nextSibling != null)
                first.nextSibling.prev = first;
            second.leftChild = first;
            return second;
        } else {
            second.prev = first;
            first.nextSibling = second.nextSibling;
            if (first.nextSibling != null)
                first.nextSibling.prev = first;
            second.nextSibling = first.leftChild;
            if (second.nextSibling != null)
                second.nextSibling.prev = second;
            first.leftChild = second;
            return first;
        }
    }

    public Position<E> insert (E element) {
        PairNode<E> newNode = new PairNode<>(element);

        if (root == null)
            root = newNode;
        else
            root = compareAndLink(root, newNode);

        size++;
        return newNode;
    }

    public void decreaseKey (Position<E> position, E element) {
        PairNode<E> p = (PairNode<E>) position;

        if (p == null || p.element == null || p.element.compareTo(element) < 0)
            throw new IllegalArgumentException();

        p.element = element;
        if (p != root) {
            if (p.nextSibling != null)
                p.nextSibling.prev = p.prev;

            if (p.prev.leftChild == p)
                p.prev.leftChild = p.nextSibling;
            else
                p.prev.nextSibling = p.nextSibling;

            p.nextSibling = null;
            root = compareAndLink(root, p);
        }
    }

    public E deleteMin () {
        if (isEmpty())
            throw new UnsupportedOperationException();

        E element = findMin();
        root.element = null;
        if (root.leftChild == null)
            root = null;
        else
            root = combineSiblings(root.leftChild);

        size--;
        return element;
    }

    private PairNode<E> combineSiblings(PairNode<E> firstSibling) {
        if (firstSibling.nextSibling == null)
            return firstSibling;

        int numSiblings = 0;
        for (; firstSibling != null; numSiblings++) {
            treeArray = doubleIfFull(treeArray, numSiblings);
            treeArray[numSiblings] = firstSibling;
            firstSibling.prev.nextSibling = null;
            firstSibling = firstSibling.nextSibling;
        }
        treeArray = doubleIfFull(treeArray, numSiblings);
        treeArray[numSiblings] = null;

        int i = 0;
        for (; i + 1 < numSiblings; i += 2)
            treeArray[i] = compareAndLink(treeArray[i], treeArray[i + 1]);

        int j = i - 2;
        if (j == numSiblings - 3)
            treeArray[j] = compareAndLink(treeArray[j], treeArray[j + 2]);

        for (; j >= 2; j -= 2)
            treeArray[j - 2] = compareAndLink(treeArray[j - 2], treeArray[j]);

        return  treeArray[0];
    }

    private PairNode<E>[] doubleIfFull(PairNode<E>[] treeArray, int index) {
        if (index == treeArray.length) {
            PairNode<E>[] oldArray = treeArray;

            treeArray = new PairNode[index * 2];
            for (int i = 0; i < index; i++)
                treeArray[i] = oldArray[i];
        }
        return treeArray;
    }

    public E findMin() {
        return root != null ? root.element : null;
    }

    public boolean isEmpty() {
        return root == null || size <= 0;
    }

}
