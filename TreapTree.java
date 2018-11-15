package org.gra4j.trochilus;

import java.util.Random;
import java.util.Stack;

public class TreapTree<E extends Comparable<? super E>> {

    private TreapNode<E> nullNode;

    private TreapNode<E> header;

    private static class TreapNode<E> {
        E element;
        public TreapNode<E> left;
        public TreapNode<E> right;
        int priority;

        private static Random random = new Random();

        public TreapNode(E element) {
            this(element, null, null);
        }

        TreapNode(E element, TreapNode<E> left, TreapNode<E> right) {
            this.element = element;
            this.left = left;
            this.right = right;
            priority = random.nextInt();
        }

    }

    public TreapTree () {
        nullNode = new TreapNode<>(null);
        nullNode.left = nullNode.right = nullNode;
        header = new TreapNode<>(null);
        header.left = header.right = nullNode;
    }

    public boolean isEmpty () {
        return header.right == nullNode;
    }

    public void makeEmpty () {
        header.right = nullNode;
    }

    public void insert (E element) {
        header.right = insert(element, header.right);
    }

    private TreapNode<E> insert (E element, TreapNode<E> t) {
        if (t == nullNode)
            return new TreapNode<>(element, nullNode, nullNode);

        int compareResult = element.compareTo(t.element);

        if (compareResult < 0) {
            t.left = insert(element, t.left);
            if (t.left.priority < t.priority)
                t = rotateWithLeftChild(t);
        } else if (compareResult > 0) {
            t.right = insert(element, t.right);
            if (t.right.priority < t.priority)
                t = rotateWithRightChild(t);
        } else
            ;

        return t;
    }

    public void insertN (E element) {
        header = insertN(element, header);
    }

    private TreapNode<E> insertN (E element, TreapNode<E> t) {
        TreapNode<E> node = t, parent = t;
        Stack<TreapNode<E>> stack = new Stack<>();
        for (;;) {
            int compareResult = compare(element, t);

            if (compareResult < 0)
                if (t.left == nullNode) {
                    t.left = new TreapNode<>(element, nullNode, nullNode);
                    if (comparePriority(t.left, t) < 0)
                        rotate(element, parent);
                    bottomUp(stack, element);
                    break;
                } else {
                    stack.push(t);
                    parent = t;
                    t = t.left;
                }
            else if (compareResult > 0)
                if (t.right == nullNode) {
                    t.right = new TreapNode<>(element, nullNode, nullNode);
                    if (comparePriority(t.right, t) < 0)
                        rotate(element, parent);
                    bottomUp(stack, element);
                    break;
                } else {
                    stack.push(t);
                    parent = t;
                    t = t.right;
                }
            else
                break;
        }
        return node;
    }

    private void bottomUp (Stack<TreapNode<E>> stack, E element) {
        boolean changed = false;
        while (stack.size() > 1) {
            TreapNode<E> parent = stack.pop();
            TreapNode<E> grand = stack.pop();
            if (compare(element, parent) > 0) {
                if (comparePriority(parent.right, parent) < 0) {
                    rotate(element, grand);
                    changed = true;
                }
            } else {
                if (comparePriority(parent.left, parent) < 0) {
                    rotate(element, grand);
                    changed = true;
                }
            }
            if (!changed)
                break;
            stack.push(grand);
        }
    }

    private final int compare (E element, TreapNode<E> t) {
        if (t == header)
            return 1;
        else
            return element.compareTo(t.element);
    }

    private final int comparePriority (TreapNode<E> t1, TreapNode<E> t2) {
        if (t2 == header || t1 == nullNode)
            return 1;
        else if (t2 == nullNode)
            return -1;
        else
            return t1.priority > t2.priority ? 1 : (t1.priority == t2.priority ? 0 : -1);
    }

    private TreapNode<E> rotate(E element, TreapNode<E> parent) {
        if (compare(element, parent) < 0)
            return parent.left = compare(element, parent.left) < 0 ?
                    rotateWithLeftChild(parent.left) : rotateWithRightChild(parent.left);
        else
            return parent.right = compare(element, parent.right) < 0 ?
                    rotateWithLeftChild(parent.right) : rotateWithRightChild(parent.right);
    }

    public void remove (E element) {
        header.right = remove(element, header.right);
    }

    private TreapNode<E> remove (E element, TreapNode<E> t) {
        if (t != nullNode) {
            int compareResult = element.compareTo(t.element);

            if (compareResult < 0)
                t.left = remove(element, t.left);
            else if (compareResult > 0)
                t.right = remove(element, t.right);
            else {
                if (comparePriority(t.left, t.right) < 0)
                    t = rotateWithLeftChild(t);
                else
                    t = rotateWithRightChild(t);

                if (t != nullNode)
                    t = remove(element, t);
                else
                    t.left = nullNode;
            }
        }

        return t;
    }

    public void removeN (E element) {
        header = removeN(element, header);
    }

    private TreapNode<E> removeN (E element, TreapNode<E> t) {
        TreapNode<E> node = t, parent = t;
        for (;;) {
            int compareResult = compare(element, t);

            if (compareResult < 0) {
                parent = t;
                t = t.left;
            } else if (compareResult > 0) {
                parent = t;
                t = t.right;
            } else {
                if (comparePriority(t.left, t.right) < 0)
                    if (parent.left == t)
                        t = parent.left = rotateWithLeftChild(t);
                    else
                        t = parent.right = rotateWithLeftChild(t);
                else
                    if (parent.left == t)
                        t = parent.left = rotateWithRightChild(t);
                    else
                        t = parent.right = rotateWithRightChild(t);

                if (t == nullNode) {
                    t.left = nullNode;
                    break;
                }
            }
        }
        return node;
    }

    private TreapNode<E> rotateWithLeftChild (TreapNode<E> t) {
        TreapNode<E> left = t.left;
        t.left = left.right;
        left.right = t;
        return left;
    }

    private TreapNode<E> rotateWithRightChild (TreapNode<E> t) {
        TreapNode<E> right = t.right;
        t.right = right.left;
        right.left = t;
        return right;
    }
}
