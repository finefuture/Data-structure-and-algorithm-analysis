package org.gra4j.trochilus;

public class HeapSort {

    public static <T extends Comparable<? super T>> void heapSort (T[] array) {
        if (array == null || array.length < 2)
            return;

        for (int i = array.length/2 - 1; i >= 0; i--)
            percDown(array, i, array.length);
        for (int i = array.length - 1; i > 0; i--) {
            swapReferences(array, 0, i);
            percDown(array, 0, i);
        }
    }

    private static <T extends Comparable<? super T>> void swapReferences(T[] array, int i, int i1) {
        if (i == i1)
            return;

        T temp = array[i];
        array[i] = array[i1];
        array[i1] = temp;
    }

    private static <T extends Comparable<? super T>> void percDown(T[] array, int i, int length) {
        int child;
        T temp;
        for (temp = array[i]; leftChild(i) < length; i = child) {
            child = leftChild(i);
            if (child != length-1 && array[child].compareTo(array[child + 1]) < 0)
                child++;
            if (temp.compareTo(array[child]) < 0)
                array[i] = array[child];
            else
                break;
        }
        array[i] = temp;
    }

    private static int leftChild (int i) {
        return 2 * i + 1;
    }
}
