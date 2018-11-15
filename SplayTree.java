package org.gra4j.trochilus;

import java.util.LinkedList;
import java.util.Queue;

public class SplayTree<E extends Comparable<? super E>> {

    private BinaryNode<E> root;

    private BinaryNode<E> nullNode;

    private BinaryNode<E> header = new BinaryNode<>(null);

    private BinaryNode<E> newNode = null;

    public SplayTree () {
        nullNode = new BinaryNode<>(null);
        nullNode.left = nullNode.right = nullNode;
        root = nullNode;
    }

    private static class BinaryNode<E> {
        E element;
        BinaryNode<E> left;
        BinaryNode<E> right;

        BinaryNode(E element) {
            this(element, null, null);
        }

        BinaryNode(E element, BinaryNode<E> left, BinaryNode<E> right) {
            this.element = element;
            this.left = left;
            this.right = right;
        }
    }

    public boolean isEmpty () {
        return root == nullNode;
    }

    public void makeEmpty () {
        root = nullNode;
    }

    public E findMin () {
        return findMin(root);
    }

    private E findMin (BinaryNode<E> t) {
        if (t == nullNode)
            return null;

        if (t.left == nullNode)
            return t.element;
        return findMin(t.left);
    }

    public E findMax () {
        return findMax(root);
    }

    private E findMax (BinaryNode<E> t) {
        if (t == nullNode)
            return null;

        if (t.right == nullNode)
            return t.element;
        return findMax(t.right);
    }

    private BinaryNode<E> splay (E element, BinaryNode<E> t) {
        BinaryNode<E> leftTreeMax, rightTreeMin;
        header.left = header.right = nullNode;
        leftTreeMax = rightTreeMin = header;
        nullNode.element = element;

        for (;;)
            if (element.compareTo(t.element) < 0) {
                if (element.compareTo(t.left.element) < 0)
                    t = rotateWithLeftChild(t);
                if (t.left == nullNode)
                    break;
                rightTreeMin.left = t;
                rightTreeMin = t;
                t = t.left;
            } else if (element.compareTo(t.element) > 0) {
                if (element.compareTo(t.right.element) > 0)
                    t = rotateWithRightChild(t);
                if (t.right == nullNode)
                    break;
                leftTreeMax.right = t;
                leftTreeMax = t;
                t = t.right;
            } else
                break;

        leftTreeMax.right = t.left;
        rightTreeMin.left = t.right;
        t.left = header.right;
        t.right = header.left;
        return t;
    }

    public void insert (E element) {
        if (newNode == null)
            newNode = new BinaryNode<>(null);
        newNode.element = element;

        if (root == nullNode) {
            newNode.left = newNode.right = nullNode;
            root = newNode;
        } else {
            root = splay(element, root);
            if (element.compareTo(root.element) < 0) {
                newNode.left = root.left;
                newNode.right = root;
                root.left = nullNode;
                root = newNode;
            } else if (element.compareTo(root.element) > 0) {
                newNode.right = root.right;
                newNode.left = root;
                root.right = nullNode;
                root = newNode;
            } else
                return;
        }
        newNode = null;
    }

    public void remove (E element) {
        BinaryNode<E> newTree;

        if (!contains(element))
            return;

        root = splay(element, root);
        if (root.left == nullNode)
            newTree = root.right;
        else {
            newTree = root.left;
            newTree = splay(element, newTree);
            newTree.right = root.right;
        }
        root = newTree;
    }

    public boolean contains (E element) {
        return contains(element, root);
    }

    private boolean contains (E element, BinaryNode<E> t) {
        if (t == nullNode)
            return false;

        int compareResult = element.compareTo(t.element);
        if (compareResult > 0)
            return contains(element, t.right);
        else if (compareResult < 0)
            return contains(element, t.left);
        else
            return true;
    }

    public void printTree () {
        if (isEmpty())
            System.out.println("Empty tree");
        else
            printTree(root);
    }

    private void printTree (BinaryNode<E> t) {
        if (t == nullNode)
            return;

        printTree(t.left);
        System.out.println(t.element);
        printTree(t.right);
    }

    public void levelOrder () {
        if (isEmpty())
            System.out.println("Empty tree");
        else
            levelOrder(root);
    }

    private void levelOrder (BinaryNode<E> t) {
        Queue<BinaryNode> queue = new LinkedList<>();
        queue.add(t);
        while (!queue.isEmpty()) {
            BinaryNode node = queue.poll();
            if (node != nullNode) {
                System.out.println(node.element);
                queue.add(node.left);
                queue.add(node.right);
            }
        }
    }

    private BinaryNode<E> rotateWithRightChild(BinaryNode<E> t) {
        BinaryNode<E> right = t.right;
        t.right = right.left;
        right.left = t;
        return right;
    }

    private BinaryNode<E> rotateWithLeftChild(BinaryNode<E> t) {
        BinaryNode<E> left = t.left;
        t.left = left.right;
        left.right = t;
        return left;
    }

}
