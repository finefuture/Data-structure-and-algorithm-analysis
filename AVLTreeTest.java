package tree;

import org.gra4j.trochilus.AVLTree;
import org.gra4j.trochilus.BinaryHeap;
import org.gra4j.trochilus.DisjSets;
import org.gra4j.trochilus.HuffmanTree;
import org.gra4j.trochilus.LeftistHeap;
import org.gra4j.trochilus.MaxHeap;
import org.gra4j.trochilus.MergeSort;
import org.gra4j.trochilus.QuickSort;
import org.gra4j.trochilus.SplayTree;
import org.gra4j.trochilus.TreapTree;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class AVLTreeTest {

    @Test
    public void avlTree () {
        AVLTree<Integer> tree = new AVLTree<>(2);
        tree.insertN(1);
        tree.insertN(3);
        tree.insertN(9);
        tree.insertN(4);
        tree.insertN(5);
        tree.insertN(19);
        tree.insertN(7);
//        tree.removeN(4);
        tree.printTree();
//        tree.levelOrder();
        tree.changeLeftRight(tree.getRoot());
        System.out.println("--------------------------");
        tree.printTree();
        System.out.println(tree.height());
    }

    @Test
    public void splayTree () {
        SplayTree<Integer> splayTree = new SplayTree<>();
        splayTree.insert(25);
        splayTree.insert(30);
        splayTree.insert(13);
        splayTree.insert(20);
        splayTree.insert(18);
        splayTree.insert(15);
        splayTree.insert(24);
        splayTree.insert(12);
        splayTree.insert(16);
        splayTree.insert(5);
        splayTree.levelOrder();
        splayTree.remove(2);
        splayTree.remove(1);
    }

    @Test
    public void binaryHeap () {
        BinaryHeap<Integer> binaryHeap = new BinaryHeap<>();
        binaryHeap.insert(1);
//        binaryHeap.insert(10);
//        binaryHeap.insert(9);
//        binaryHeap.insert(4);
        binaryHeap.deleteMin();
    }

    @Test
    public void maxHeap () {
        MaxHeap<Integer> maxHeap = new MaxHeap<>();
        maxHeap.insert(31);
        maxHeap.insert(59);
        maxHeap.insert(41);
        maxHeap.insert(26);
        maxHeap.insert(58);
        maxHeap.insert(97);
        maxHeap.insert(53);
        maxHeap.deleteMax();
        maxHeap.deleteMax();
        maxHeap.deleteMax();
        maxHeap.deleteMax();
        maxHeap.deleteMax();
        maxHeap.deleteMax();
        maxHeap.deleteMax();
        System.out.println(1);
    }

    @Test
    public void treapTree () {
        TreapTree<Integer> treapTree = new TreapTree();
//        treapTree.insert(1);
//        treapTree.insert(2);
//        treapTree.insert(3);
//        treapTree.insert(4);
//        treapTree.insert(5);
//        treapTree.insert(6);
//        treapTree.insert(7);
//        treapTree.insert(8);
//        treapTree.removeN(3);
        TreapTree<Integer> treapTree2 = new TreapTree();
        treapTree2.insertN(1);
        treapTree2.insertN(2);
        treapTree2.insertN(3);
        treapTree2.insertN(4);
        treapTree2.insertN(5);
        treapTree2.insertN(6);
        treapTree2.insertN(7);
        treapTree2.insertN(8);
    }

    @Test
    public void leftistHeap () {
        LeftistHeap<Integer> leftistHeap = new LeftistHeap<>();
        leftistHeap.insert(1);
        leftistHeap.insert(2);
        leftistHeap.insert(3);
        leftistHeap.insert(4);
        leftistHeap.insert(5);
        leftistHeap.insert(6);
        leftistHeap.insert(7);
        leftistHeap.insert(8);
        leftistHeap.insert(9);
        leftistHeap.insert(10);
        leftistHeap.insert(11);
        leftistHeap.insert(12);
        leftistHeap.insert(13);
        leftistHeap.insert(14);
        leftistHeap.insert(15);
        leftistHeap.deleteMin();
        System.out.println(1/2);
    }

    @Test
    public void huffmanTree () {
        List<HuffmanTree.Node> nodes = new ArrayList<>();
        nodes.add(new HuffmanTree.Node("a", 10d));
        nodes.add(new HuffmanTree.Node("e", 15d));
        nodes.add(new HuffmanTree.Node("i", 12d));
        nodes.add(new HuffmanTree.Node("s", 3d));
        nodes.add(new HuffmanTree.Node("t", 4d));
        nodes.add(new HuffmanTree.Node("sp", 13d));
        nodes.add(new HuffmanTree.Node("nl", 1d));
        HuffmanTree<String> huffmanTree = new HuffmanTree<String>(nodes);
        huffmanTree.picTree();
    }

    @Test
    public void quickSort () {
        Integer[] array = new Integer[] {4,1,5,6,9,0,3,7,2,1,90,8};
        Integer[] array2 = new Integer[] {3,1,5,4,9,2,6,11,7};
        QuickSort.quickSort2(array2, 0, array2.length-1);
    }

    @Test
    public void disjSets() {
        DisjSets disjSets = new DisjSets(8);
        disjSets.deepUnion(3, 4);
        disjSets.compressFind(4);
    }

    @Test
    public void mergeSort () {
        Integer[] array = new Integer[]{3, 2};
        MergeSort.mergeSort(array);
    }

    private static double eval (int n) {
        double[] c = new double[n + 1];
        c[0] = 1.0;
        for (int i = 1; i <= n; i++) {
            double sum = 0.0;
            for (int j = 0; j < i; j++)
                sum += c[j];
            c[i] = 2.0 * sum / i + i;
        }
        return c[n];
    }

    private static double eval2 (int n) {
        double[] c = new double[n + 1];
        c[0] = 1.0;
        for (int i = 1; i <= n; i++) {
            double sum = (c[i-1] - (i-1)) * (i-1) / 2 + c[i-1];
            c[i] = 2.0 * sum / i + i;
        }
        return c[n];
    }

    @Test
    public void eval () {
        System.out.println(eval(1));
        System.out.println(eval2(1));
        System.out.println(eval(2));
        System.out.println(eval2(2));
        System.out.println(eval(3));
        System.out.println(eval2(3));
    }

    public int fibonacci (int n) {
        if (n <= 1)
            return 1;

        int last = 1;
        int nextToLast = 1;
        int answer = 1;
        for (int i = 2; i <= n; i++) {
            answer = last + nextToLast;
            nextToLast = last;
            last = answer;
        }
        return answer;
    }

    private void insertSort (Comparable[] array) {
        if (array == null || array.length < 2)
            return;

        int j;
        for (int i = 1; i < array.length; i++) {
            Comparable temp = array[i];
            for (j = i; j > 0 && temp.compareTo(array[j-1]) < 0; j--)
                array[j] = array[j-1];
            array[j] = temp;
        }
    }

    private void shellSort (Comparable[] array) {
        if (array == null || array.length < 2)
            return;

        int j;
        for (int gap = array.length/2; gap > 0; gap /= 2) {
            for (int i = gap; i < array.length; i++) {
                Comparable temp = array[i];
                for (j = i; j >= gap && temp.compareTo(array[j-gap]) < 0; j -= gap)
                    array[j] = array[j-gap];
                array[j] = temp;
            }
        }
    }

    private void countingRadixSort (String[] array, int length) {
        final int BUCKETS = 256;
        int N = array.length;
        String[] buffer = new String[N];
        String[] in = array;
        String[] out = buffer;
        for (int pos = length - 1; pos >= 0; pos--) {
            int[] count = new int[BUCKETS + 1];
            for (int i = 0; i < N; i++)
                count[in[i].charAt(pos) + 1] ++;


        }
    }

}
