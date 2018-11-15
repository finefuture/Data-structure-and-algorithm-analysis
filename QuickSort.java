package org.gra4j.trochilus;

public class QuickSort {

    public static <T extends Comparable<? super T>> void quickSort (T[] array) {
        if (array == null || array.length < 2)
            return;

        quickSort(array, 0, array.length-1);
    }

    private static <T extends Comparable<? super T>> void quickSort(T[] array, int left, int right) {
        if (left + 10 <= right) {
            T pivot = medianSplit(array, left, right);
            int i = left, j = right - 1;
            for (;;) {
                while (array[++i].compareTo(pivot) < 0) {}
                while (array[--j].compareTo(pivot) > 0) {}
                if (i < j)
                    swapReferences(array, i, j);
                else
                    break;
            }
            swapReferences(array, i, right - 1);
            quickSort(array, left, i - 1);
            quickSort(array, i + 1, right);
        } else
            insertSort(array);
    }

    public static <T extends Comparable<? super T>> void quickSort2 (T[] array, int left, int right) {
        if (left >= right)
            return;

        T pivot = medianSplit(array, left, right);
        int i = left, j = right - 1;
        for (;i != j;) {
            while (array[++i].compareTo(pivot) < 0) {}
            while (array[--j].compareTo(pivot) > 0) {}
            if (i < j)
                swapReferences(array, i, j);
            else
                break;
        }
        swapReferences(array, i, right - 1);
        quickSort2(array, left, i - 1);
        quickSort2(array, i + 1, right);
    }

    private static <T extends Comparable<? super T>> T medianSplit(T[] array, int left, int right) {
        int center = (left + right) / 2;
        if (array[center].compareTo(array[left]) < 0)
            swapReferences(array, left, center);
        if (array[right].compareTo(array[left]) < 0)
            swapReferences(array, left, right);
        if (array[right].compareTo(array[center]) < 0)
            swapReferences(array, center, right);

        swapReferences(array, center, right - 1);
        return array[right - 1];
    }

    private final static <T extends Comparable<? super T>> void swapReferences(T[] array, int left, int center) {
        if (left == center)
            return;

        T temp = array[left];
        array[left] = array[center];
        array[center] = temp;
    }

    private static <T extends Comparable<? super T>> void insertSort (T[] array) {
        if (array == null || array.length < 2)
            return;

        int j;
        for (int i = 1; i < array.length; i++) {
            T temp = array[i];
            for (j = i; j > 0 && temp.compareTo(array[j-1]) < 0; j--)
                array[j] = array[j-1];
            array[j] = temp;
        }
    }
}
