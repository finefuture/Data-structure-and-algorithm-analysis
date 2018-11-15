package org.gra4j.trochilus;

public class MergeSort {

    public static void mergeSort (Comparable[] array) {
        if (array == null || array.length < 2)
            return;

        Comparable[] temp = new Comparable[array.length];
        mergeSort(array, temp, 0, array.length-1);
    }

    private static void mergeSort (Comparable[] array, Comparable[] temp, int left, int right) {
        if (left < right) {
            int center = (left + right) / 2;
            mergeSort(array, temp, left, center);
            mergeSort(array, temp, center + 1, right);
            merge(array, temp, left, center + 1, right);
        }
    }

    private static void merge(Comparable[] array, Comparable[] temp, int leftPos, int rightPos, int rightEnd) {
        int leftEnd = rightPos - 1;
        int tempPos = leftPos;
        int numElements = rightEnd - leftPos + 1;
        while (leftPos <= leftEnd && rightPos <= rightEnd)
            if (array[leftPos].compareTo(array[rightPos]) <= 0)
                temp[tempPos++] = array[leftPos++];
            else
                temp[tempPos++] = array[rightPos++];

        while (leftPos <= leftEnd)
            temp[tempPos++] = array[leftPos++];

        while (rightPos <= rightEnd)
            temp[tempPos++] = array[rightPos++];

        for (int i = 0; i < numElements; i++, rightEnd--)
            array[rightEnd] = temp[rightEnd];

    }

}
