package org.gra4j.trochilus;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class HuffmanTree<E> {

    private Node<E> node;

    public Node<E> getNode() {
        return node;
    }

    public void setNode(Node<E> node) {
        this.node = node;
    }

    public static class Node<E> implements Comparable<Node<E>> {
        E element;
        double weight;
        Node left;
        Node right;

        public Node (E element, double weight) {
            this(element, weight, null, null);
        }

        Node (E element, double weight, Node left, Node right) {
            this.element = element;
            this.weight = weight;
            this.left = left;
            this.right = right;
        }

        @Override
        public int compareTo(Node<E> o) {
            if (o == null)
                return 1;

            return this.weight > o.weight ? 1 : (this.weight == o.weight ? 0 : -1);
        }
    }

    public HuffmanTree (List<Node> nodes) {
        while (nodes.size() > 1) {
            quickSort(nodes);

            Node left = nodes.remove(0);
            Node right = nodes.remove(0);
            Node<E> node = new Node<>(null, left.weight + right.weight, left, right);
            nodes.add(node);
        }
        node = nodes.get(0);
    }

    private void quickSort (List<Node> nodes) {
        if (nodes == null || nodes.size() < 2)
            return;

        quickSort(nodes, 0, nodes.size() -1);
    }

    private void quickSort(List<Node> nodes, int left, int right) {
        if (left + 10 < right) {
            Node pivot = medianSplit(nodes, left, right);
            int i = left, j = right - 1;
            while (true) {
                while (nodes.get(++i).compareTo(pivot) < 0) {}
                while (nodes.get(--j).compareTo(pivot) > 0) {}
                if (i < j)
                    swapReferences(nodes, i ,j);
                else
                    break;
            }
            swapReferences(nodes, i, right - 1);
            quickSort(nodes, left, i - 1);
            quickSort(nodes, i + 1, right);
        } else
            insertSort(nodes);

    }

    private Node medianSplit (List<Node> nodes, int left, int right) {
        int center = (left + right) / 2;
        if (nodes.get(center).compareTo(nodes.get(left)) < 0)
            swapReferences(nodes, left, center);
        if (nodes.get(right).compareTo(nodes.get(left)) < 0)
            swapReferences(nodes, left, right);
        if (nodes.get(right).compareTo(nodes.get(center)) < 0)
            swapReferences(nodes, center, right);

        swapReferences(nodes, center, right - 1);
        return nodes.get(right - 1);
    }

    private void swapReferences (List<Node> nodes, int t1, int t2) {
        if (t1 == t2)
            return;

        Node node = nodes.get(t1);
        nodes.set(t1, nodes.get(t2));
        nodes.set(t2, node);
    }

    private void insertSort (List<Node> nodes) {
        if (nodes == null || nodes.size() < 2)
            return;

        int j;
        for (int i = 1; i < nodes.size(); i++) {
            Node node = nodes.get(i);
            for (j = i; j > 0 && node.compareTo(nodes.get(j - 1)) < 0; j--)
                nodes.set(j, nodes.get(j - 1));
            nodes.set(j, node);
        }
    }

    public void picTree () {
        if (node == null)
            System.out.println("Empty tree");

        Queue<Node<E>> queue = new LinkedList<>();
        queue.add(node);
        while (!queue.isEmpty()) {
            Node<E> node = queue.poll();
            if (node != null) {
                System.out.println(node.element);
                System.out.print("/");
                System.out.print("\\");
                queue.add(node.left);
                queue.add(node.right);
            }
        }
    }

}
