package org.gra4j.trochilus;

public class LeftistHeap <E extends Comparable<? super E>> {

    private Node<E> root;

    public LeftistHeap () {
        root = null;
    }

    public LeftistHeap (E element) {
        root = new Node<>(element);
    }

    private static class Node<E> {
        E element;
        Node<E> left;
        Node<E> right;
        int npl;

        Node(E element) {
            this(element, null, null);
        }

        Node(E element, Node<E> lt, Node<E> rt) {
            this.element = element;
            this.left = lt;
            this.right = rt;
            this.npl = 0;
        }
    }

    public void merge (LeftistHeap<E> rhs) {
        if (this == rhs || rhs == null)
            return;

        root = merge(root, rhs.root);
    }

    public void insert (E element) {
        if (root == null) {
            root = new Node<>(element);
            return;
        }

        root = merge(new Node<>(element), root);
    }

    public E findMin () {
        return root == null ? null : root.element;
    }

    public E deleteMin () {
        if (isEmpty())
            throw new UnsupportedOperationException("heap is empty");

        E min = findMin();
        root = merge(root.left, root.right);
        return min;
    }

    public boolean isEmpty () {
        return root == null;
    }

    public void makeEmpty () {
        root = null;
    }

    private Node<E> merge (Node<E> h1, Node<E> h2) {
        if (h1 == null)
            return h2;
        if (h2 == null)
            return h1;
        if (h1.element.compareTo(h2.element) < 0)
            return merge1(h1, h2);
        else
            return merge1(h2, h1);
    }

    private Node<E> merge1 (Node<E> h1, Node<E> h2) {
        if (h1.left == null)
            h1.left = h2;
        else {
            h1.right = merge(h1.right, h2);
            if (h1.left.npl < h1.right.npl)
                swapChildren(h1);
            h1.npl = h1.right.npl + 1;
        }
        return h1;
    }

    private void swapChildren (Node<E> t) {
        Node<E> left = t.left;
        t.left = t.right;
        t.right = left;
    }
}
