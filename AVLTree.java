package org.gra4j.trochilus;

import java.util.LinkedList;
import java.util.Queue;

public class AVLTree<E extends Comparable<? super E>> {

    private AVLNode<E> root;

    private static final int ALLOWED_IMBALANCE = 1;

    private static class AVLNode<E extends Comparable<? super E>> {
        E element;
        AVLNode<E> left;
        AVLNode<E> right;
        AVLNode<E> parent;
        int height;

        AVLNode (E e) {
            this(e, null, null, null);
        }

        AVLNode (E e, AVLNode<E> parent) {
            this(e, null, null, parent);
        }

        AVLNode (E e, AVLNode<E> lt, AVLNode<E> rt, AVLNode<E> pt) {
            element = e;
            left = lt;
            right = rt;
            parent = pt;
            height = 0;
        }
    }

    public AVLTree (E element) {
        root = new AVLNode<>(element);
    }

    public AVLNode<E> getRoot() {
        return root;
    }

    public void setRoot(AVLNode<E> root) {
        this.root = root;
    }

    public void makeEmpty () {
        root = null;
    }

    public boolean isEmpty () {
        return root == null;
    }

    public int height () {
        return height(root);
    }


    public int height (AVLNode<E> t) {
        return t == null ? -1 : t.height;
    }

    public AVLNode<E> insert (E element) {
        root = insert(element, root);
        return root;
    }

    private AVLNode<E> insert (E element, AVLNode<E> t) {
        if (t == null)
            return new AVLNode<>(element);

        int compareResult = element.compareTo(t.element);

        if (compareResult < 0) {
            t.left = insert(element, t.left);
            t.left.parent = t;
        } else if (compareResult > 0) {
            t.right = insert(element, t.right);
            t.right.parent = t;
        } else
            ;
        return balance(t);
    }

    public AVLNode<E> insertN (E element) {
        if (root == null)
            return root = new AVLNode<>(element);

        AVLNode<E> node = root;
        while (node != null) {
            int compareResult = element.compareTo(node.element);
            if (compareResult < 0) {
                if (node.left == null) {
                    AVLNode<E> left = node.left = new AVLNode<>(element, node);
                    root = balanceN(left);
                    break;
                } else
                    node = node.left;
            } else if (compareResult > 0) {
                if (node.right == null) {
                    AVLNode<E> right = node.right = new AVLNode<>(element, node);
                    root = balanceN(right);
                    break;
                } else
                    node = node.right;
            } else
                ;
        }
        return node;
    }

    private AVLNode<E> balanceN (AVLNode<E> node) {
        AVLNode<E> eavlNode = null, preNode = null;
        while (node != null) {
            if (eavlNode != null && preNode != eavlNode) {
                if (node.left == preNode)
                    node.left = eavlNode;
                else if (node.right == preNode)
                    node.right = eavlNode;
            }
            AVLNode<E> parent = node.parent;
            preNode = node;
            eavlNode = balance(node);
            node = parent;
        }
        return eavlNode;
    }

    public AVLNode<E> remove (E element) {
        root = remove(element, root);
        return root;
    }

    private AVLNode<E> remove (E element, AVLNode<E> t) {
        if (t == null)
            return t;

        int compareResult = element.compareTo(t.element);

        if (compareResult < 0)
            t.left = remove(element, t.left);
        else if (compareResult > 0)
            t.right = remove(element, t.right);
        else if (t.left != null && t.right != null){
            t.element = findMin(t.right).element;
            t.right = remove(t.element, t.right);
        } else {
            AVLNode<E> parent = t.parent;
            t = (t.left != null) ? t.left : t.right;
            if (t != null)
                t.parent = parent;
        }
        return balance(t);
    }

    public AVLNode<E> removeN (E element) {
        if (root == null)
            return root;

        AVLNode<E> node = root;
        while (node != null) {
            int compareResult = element.compareTo(node.element);
            if (compareResult < 0)
                node = node.left;
            else if (compareResult > 0)
                node = node.right;
            else if (node.left != null && node.right != null) {
                element = node.element = findMin(node.right).element;
                node = node.right;
            } else {
                AVLNode<E> parent = node.parent;
                if (parent.left == node)
                    parent.left = node = (node.left != null) ? node.left : node.right;
                else if (parent.right == node)
                    parent.right = node = (node.left != null) ? node.left : node.right;
                if (node != null)
                    node.parent = parent;
                balanceN(parent);
                break;
            }
        }
        return node;
    }

    public E findMin () {
        AVLNode<E> min = findMin(root);
        return min == null ? null : min.element;
    }

    public AVLNode<E> findMin (AVLNode<E> t) {
        if (t == null || t.left == null)
            return t;

        return findMin(t.left);
    }

    public E findMax () {
        AVLNode<E> max = findMax(root);
        return max == null ? null : max.element;
    }

    public AVLNode<E> findMax (AVLNode<E> t) {
        if (t == null || t.right == null)
            return t;

        return findMax(t.right);
    }

    public boolean contains (E element) {
        return contains(element, root);
    }

    public boolean contains (E element, AVLNode<E> t) {
        if (t == null)
            return false;

        int compareResult = element.compareTo(t.element);

        if (compareResult < 0)
            return contains(element, t.left);
        else if (compareResult > 0)
            return contains(element, t.right);
        else
            return true;
    }

    public void printTree () {
        if (isEmpty())
            System.out.println("Empty tree");
        else
            printTree(root);
    }

    private void printTree (AVLNode<E> t) {
        if (t == null)
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

    private void levelOrder (AVLNode<E> t) {
        if (t == null)
            return;

        Queue<AVLNode<E>> q = new LinkedList<>();
        q.add(t);
        while (!q.isEmpty()) {
            AVLNode<E> node = q.poll();
            if (node != null) {
                System.out.println(node.element);
                q.add(node.left);
                q.add(node.right);
            }
        }
    }

    private AVLNode<E> balance (AVLNode<E> t) {
        if (t == null)
            return t;

        if (height(t.left) - height(t.right) > ALLOWED_IMBALANCE)
            if (height(t.left.left) >= height(t.left.right))
                t = rotateWithLeftChild(t);
            else
                t = doubleWithLeftChild(t);
        else if (height(t.right) - height(t.left) > ALLOWED_IMBALANCE)
            if (height(t.right.right) >= height(t.right.left))
                t = rotateWithRightChild(t);
            else
                t = doubleWithRightChild(t);

        t.height = Math.max(height(t.left), height(t.right)) + 1;
        return t;
    }

    private AVLNode<E> rotateWithLeftChild (AVLNode<E> t) {
        AVLNode<E> lt = t.left;
        t.left = lt.right;
        if (t.left != null)
            t.left.parent = t;
        lt.right = t;
        lt.parent = t.parent;
        t.parent = lt;
        t.height = Math.max(height(t.left), height(t.right)) + 1;
        lt.height = Math.max(height(lt.left), height(lt.right)) + 1;
        return lt;
    }

    private AVLNode<E> rotateWithRightChild (AVLNode<E> t) {
        AVLNode<E> rt = t.right;
        t.right = rt.left;
        if (t.right != null)
            t.right.parent = t;
        rt.left = t;
        rt.parent = t.parent;
        t.parent = rt;
        t.height = Math.max(height(t.left), height(t.right)) + 1;
        rt.height = Math.max(height(rt.left), height(rt.right)) + 1;
        return rt;
    }

    private AVLNode<E> doubleWithLeftChild(AVLNode<E> t) {
        t.left = rotateWithRightChild(t.left);
        return rotateWithLeftChild(t);
    }

    private AVLNode<E> doubleWithRightChild(AVLNode<E> t) {
        t.right = rotateWithLeftChild(t.right);
        return rotateWithRightChild(t);
    }

    public AVLNode<E> changeLeftRight (AVLNode<E> node) {
        if (node == null)
            return null;

        AVLNode<E> left = node.left;
        node.left = changeLeftRight(node.right);
        node.right = changeLeftRight(left);
        return node;
    }

}
