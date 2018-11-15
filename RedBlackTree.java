package org.gra4j.trochilus;

import java.util.LinkedList;
import java.util.Queue;

public class RedBlackTree<E extends Comparable<? super E>> {

    private RedBlackNode<E> header;

    private RedBlackNode<E> nullNode;

    private RedBlackNode<E> current;

    private RedBlackNode<E> parent;

    private RedBlackNode<E> grand;

    private RedBlackNode<E> great;

    private RedBlackNode<E> brother;

    private static final int BLACK = 1;

    private static final int RED = 0;

    public RedBlackTree () {
        nullNode = new RedBlackNode<>(null);
        nullNode.left = nullNode.right = nullNode;
        header = new RedBlackNode<>(null);
        header.left = header.right = nullNode;
    }

    private static class RedBlackNode<E> {
        E element;
        RedBlackNode<E> left;
        RedBlackNode<E> right;
        int color;

        RedBlackNode (E element) {
            this(element, null, null);
        }

        RedBlackNode (E element, RedBlackNode<E> left, RedBlackNode<E> right) {
            this.element = element;
            this.left = left;
            this.right = right;
            this.color = BLACK;
        }
    }

    public boolean isEmpty () {
        return header.right == nullNode;
    }

    public void makeEmpty () {
        header.right = nullNode;
    }

    private RedBlackNode<E> rotate (E element, RedBlackNode<E> parent) {
        if (compare(element, parent) < 0)
            return parent.left = compare(element, parent.left) < 0 ?
                   rotateWithLeftChild(parent.left) : rotateWithRightChild(parent.left);
        else
            return parent.right = compare(element, parent.right) < 0 ?
                   rotateWithLeftChild(parent.right) : rotateWithRightChild(parent.right);
    }

    private RedBlackNode<E> rotateWithLeftChild (RedBlackNode<E> t) {
        RedBlackNode<E> left = t.left;
        t.left = left.right;
        left.right = t;
        return left;
    }

    private RedBlackNode<E> rotateWithRightChild (RedBlackNode<E> t) {
        RedBlackNode<E> right = t.right;
        t.right = right.left;
        right.left = t;
        return right;
    }

    private final int compare (E element, RedBlackNode<E> t) {
        if (t == header)
            return 1;
        else
            return element.compareTo(t.element);
    }

    private void handleReorient (E element) {
        current.color = RED;
        current.left.color = BLACK;
        current.right.color = BLACK;
        if (parent.color == RED) {
            grand.color = RED;
            if ((compare(element, grand) < 0) != (compare(element, parent) < 0))
                parent = rotate(element, grand);
            current = rotate(element, great);
            current.color = BLACK;
        }
        header.right.color = BLACK;
    }

    public void insert (E element) {
        current = parent = grand = header;
        nullNode.element = element;

        while (compare(element, current) != 0) {
            great = grand; grand = parent; parent = current;
            current = compare(element, current) < 0 ? current.left : current.right;
            if (current.left.color == RED && current.right.color == RED)
                handleReorient(element);
        }

        if (current != nullNode)
            return;

        current = new RedBlackNode<>(element, nullNode, nullNode);
        if (compare(element, parent) < 0)
            parent.left = current;
        else
            parent.right = current;
        handleReorient(element);
    }

    public RedBlackNode<E> remove (E element, RedBlackNode<E> t) {
        current = t.right;
        brother = t.left;
        grand = great = parent = t;

        while (current != nullNode) {
            if (current.left.color == BLACK && current.right.color == BLACK) {
                if (brother.left.color == BLACK && brother.right.color == BLACK) {
                    parent.color = BLACK;
                    current.color = RED;
                    if (brother != nullNode)
                        brother.color = RED;
                } else
                    solveStep2A23();

                if (current.element.compareTo(element) == 0)
                    element = findElement(element);
                else
                    normalDown(element);
            } else {
                if (current.element.compareTo(element) != 0)
                    normalDown(element);
                else
                    element = findElement(element);

                if (current == nullNode)
                    break;

                if (current.color == BLACK)
                    solve2B();
                else if (current.element.compareTo(element) != 0)
                    normalDown(element);
                else
                    element = findElement(element);
            }
        }

        t.color = BLACK;
        t.right.color = BLACK;
        return t;
    }

    private void solve2B() {
        brother.color = BLACK;
        parent.color = RED;
        if (parent.left == current) {
            if (grand.left == parent)
                grand.left = rotateWithLeftChild(parent);
            else
                grand.right = rotateWithLeftChild(parent);

            brother = parent.right;
        } else {
            if (grand.left == parent)
                grand.left = rotateWithRightChild(parent);
            else
                grand.right = rotateWithRightChild(parent);

            brother = parent.left;
        }
    }

    private void normalDown(E element) {
        if (element.compareTo(current.element) < 0) {
            grand = parent; parent = current; brother = parent.right;
            current = current.left;
        } else {
            grand = parent; parent = current; brother = parent.left;
            current = current.right;
        }
    }

    private E findElement(E element) {
        E temp;
        RedBlackNode<E> toDelete;
        if (current.left == nullNode && current.right == nullNode) {
            if (parent.right == current)
                parent.right = nullNode;
            else
                parent.left = nullNode;

            current = nullNode;
            temp = element;
        } else {
            if (current.right != nullNode) {
                toDelete = findMin(current.right);
                current.element = toDelete.element;
                temp = toDelete.element;
                if (toDelete.color == RED) {
                    current.right = deleteNode(toDelete, current.right);
                    current = nullNode;
                } else {
                    grand = parent; parent = current; brother = parent.left;
                    current = current.right;
                }
            } else {
                toDelete = findMax(current.left);
                current.element = toDelete.element;
                temp = toDelete.element;
                if (toDelete.color == RED) {
                    current.left = deleteNode(toDelete, current.left);
                    current = nullNode;
                } else {
                    grand = parent; parent = current; brother = parent.right;
                    current = current.left;
                }
            }
        }
        return temp;
    }

    private RedBlackNode<E> deleteNode(RedBlackNode<E> toDelete, RedBlackNode<E> t) {
        RedBlackNode<E> origin = t;
        RedBlackNode<E> parent = null;
        while (t != toDelete) {
            parent = t;
            if (toDelete.element.compareTo(t.element) < 0)
                t = t.left;
            else
                t = t.right;
        }

        if (t == origin) {
            RedBlackNode<E> temp;
            if (t.right != nullNode)
                temp = t.right;
            else
                temp = t.left;

            return temp;
        }

        if (parent.right == t) {
            if (t.right != nullNode)
                parent.right = t.right;
            else
                parent.right = t.left;
        } else {
            if (t.right != nullNode)
                parent.left = t.right;
            else
                parent.left = t.left;
        }
        
        return origin;
    }

    private void solveStep2A23() {
        if (parent.left == current) {
            if (brother.left.color == RED) {
                parent.color = BLACK;
                current.color = RED;
                parent.right = rotateWithRightChild(brother);

                if (grand.left == parent)
                    grand.left = rotateWithLeftChild(parent);
                else
                    grand.right = rotateWithLeftChild(parent);
            } else {
                current.color = RED;
                parent.color = BLACK;
                brother.color = RED;
                brother.right.color = BLACK;

                if (grand.right == parent)
                    grand.right = rotateWithLeftChild(parent);
                else
                    grand.left = rotateWithLeftChild(parent);
            }
        } else {
            if (brother.right.color == RED) {
                current.color = RED;
                parent.color = BLACK;
                parent.left = rotateWithLeftChild(brother);

                if (grand.left == parent)
                    grand.left = rotateWithRightChild(parent);
                else
                    grand.right = rotateWithRightChild(parent);
            } else {
                current.color = RED;
                parent.color = BLACK;
                brother.color = RED;
                brother.left.color = BLACK;

                if (grand.right == parent)
                    grand.right = rotateWithRightChild(parent);
                else
                    grand.left = rotateWithRightChild(parent);
            }
        }
    }

    public boolean contains (E element) {
        return contains(header.right, element);
    }

    private boolean contains (RedBlackNode<E> t, E element) {
        if (t == nullNode)
            return false;

        int compareResult = element.compareTo(t.element);
        if (compareResult < 0)
            return contains(t.left, element);
        else if (compareResult > 0)
            return contains(t.right, element);
        else
            return true;
    }

    public E findMax () {
        RedBlackNode<E> node = findMax(header.right);
        return node == nullNode ? null : node.element;
    }

    private RedBlackNode<E> findMax (RedBlackNode<E> t) {
        if (t == nullNode || t.right == nullNode)
            return t;

        return findMax(t.right);
    }

    public E findMin () {
        RedBlackNode<E> node = findMin(header.right);
        return node == nullNode ? null : node.element;
    }

    private RedBlackNode<E> findMin (RedBlackNode<E> t) {
        if (t == nullNode || t.left == nullNode)
            return t;

        return findMin(t.left);
    }

    public void printTree () {
        if (isEmpty())
            System.out.println("Empty tree");
        else
            printTree(header.right);
    }

    private void printTree (RedBlackNode<E> t) {
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
            levelOrder(header.right);
    }

    private void levelOrder (RedBlackNode<E> t) {
        Queue<RedBlackNode<E>> queue = new LinkedList<>();
        queue.add(t);
        while (!queue.isEmpty()) {
            RedBlackNode<E> node = queue.poll();
            if (node != nullNode) {
                System.out.println(node.element);
                queue.add(node.left);
                queue.add(node.right);
            }
        }
    }

    public static void main (String[] a) {
        RedBlackTree<Integer> redBlackTree = new RedBlackTree<>();
        redBlackTree.insert(10);
        redBlackTree.insert(85);
        redBlackTree.insert(15);
        redBlackTree.insert(70);
        redBlackTree.insert(20);
        redBlackTree.insert(60);
        redBlackTree.insert(30);
        redBlackTree.insert(50);
        redBlackTree.insert(65);
        redBlackTree.insert(80);
        redBlackTree.insert(90);
        redBlackTree.insert(40);
        redBlackTree.insert(5);
        redBlackTree.insert(55);
        redBlackTree.insert(68);
    }

}
